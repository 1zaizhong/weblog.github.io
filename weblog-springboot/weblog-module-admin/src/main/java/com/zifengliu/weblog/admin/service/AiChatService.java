package com.zifengliu.weblog.admin.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2026/1/1 下午5:11
 * @description
 **/
public interface AiChatService {
    /**
     * 流式对话服务
     * @param question 用户的问题
     * @param emitter 用于推送数据的 SSE 发射器
     */
    void streamChat(String question, SseEmitter emitter);
}