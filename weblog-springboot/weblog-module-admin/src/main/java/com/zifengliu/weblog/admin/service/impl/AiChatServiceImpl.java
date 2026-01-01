package com.zifengliu.weblog.admin.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zifengliu.weblog.admin.service.AiChatService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okio.BufferedSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class AiChatServiceImpl implements AiChatService {

    @Value("${deepseek.api-key}")
    private String apiKey;

    @Value("${deepseek.base-url}")
    private String baseUrl;

    @Value("${deepseek.model:deepseek-chat}")
    private String model;

    // 初始化 OkHttpClient，兼容 JDK 1.8
    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.MINUTES)
            .build();

    private final Gson gson = new Gson();

    @Override
    public void streamChat(String question, SseEmitter emitter) {
        // 1. 构建请求体 (适配 JDK 1.8，不使用 Map.of)
        Map<String, Object> requestBodyMap = new HashMap<>();
        requestBodyMap.put("model", model);
        requestBodyMap.put("stream", true);

        List<Map<String, String>> messages = new ArrayList<>();

        Map<String, String> systemMsg = new HashMap<>();
        systemMsg.put("role", "system");
        systemMsg.put("content", "你是一个专业、友好的博客智能助手。");
        messages.add(systemMsg);

        Map<String, String> userMsg = new HashMap<>();
        userMsg.put("role", "user");
        userMsg.put("content", question);
        messages.add(userMsg);

        requestBodyMap.put("messages", messages);

        // 2. 构建 Request
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                gson.toJson(requestBodyMap)
        );

        Request request = new Request.Builder()
                .url(baseUrl)
                .addHeader("Authorization", "Bearer " + apiKey)
                .post(body)
                .build();

        // 3. 发起异步请求
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                log.error("==> AI 对话请求失败: ", e);
                emitter.completeWithError(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    log.error("==> AI 接口返回异常: {}", response);
                    emitter.completeWithError(new RuntimeException("AI接口返回异常"));
                    return;
                }

                // 显式指定 okhttp3.ResponseBody 避免与 Spring 注解冲突
                try (okhttp3.ResponseBody responseBody = response.body()) {
                    if (responseBody == null) {
                        emitter.complete();
                        return;
                    }

                    BufferedSource source = responseBody.source();
                    while (!source.exhausted()) {
                        String line = source.readUtf8Line();
                        if (line != null && line.startsWith("data: ")) {
                            String data = line.substring(6).trim();

                            if ("[DONE]".equals(data)) {
                                emitter.complete();
                                break;
                            }

                            try {
                                JsonObject jsonObject = JsonParser.parseString(data).getAsJsonObject();
                                JsonObject delta = jsonObject.getAsJsonArray("choices")
                                        .get(0).getAsJsonObject()
                                        .getAsJsonObject("delta");

                                if (delta.has("content")) {
                                    String content = delta.get("content").getAsString();
                                    // 推送给前端 SSE
                                    emitter.send(content);
                                }
                            } catch (Exception ignored) {
                                // 忽略 SSE 协议中的空行或非 JSON 数据
                            }
                        }
                    }
                } catch (Exception e) {
                    log.error("==> 推送流数据异常: ", e);
                    emitter.completeWithError(e);
                }
            }
        });
    }
}