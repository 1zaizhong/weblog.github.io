package com.zifengliu.weblog.admin.controller;

import com.zifengliu.weblog.admin.service.AiChatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2026/1/1 下午5:12
 * @description
 **/
@RestController
@RequestMapping("/admin/ai")
@Api(tags = "AI 智能助手接口")
public class AiChatController {

    @Autowired
    private AiChatService aiChatService;

    /**
     * AI 流式对话
     *
     */
    @GetMapping(value = "/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ApiOperation(value = "AI 智能流式对话")
    public SseEmitter chat(@RequestParam String question) {
        // 设置超时时间为 10 分钟
        SseEmitter emitter = new SseEmitter(600_000L);

        // 注册回调处理
        emitter.onCompletion(() -> System.out.println("AI 对话完成"));
        emitter.onTimeout(() -> System.err.println("AI 对话超时"));

        aiChatService.streamChat(question, emitter);

        return emitter;
    }
}