package com.zifengliu.weblog.common.domain.dos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2026/1/1 下午5:03
 * @description
 **/
// 请求 DTO
@Data
@Builder
public class AiChatRequestDTO {
    private String model; //  "deepseek-chat"
    private List<Message> messages;
    private boolean stream; // 流式输出

    @Data
    @AllArgsConstructor
    public static class Message {
        private String role;    // system, user, assistant
        private String content; // 消息内容
    }
}
