package com.zifengliu.weblog.admin.model.vo.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/22 上午12:09
 * @description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindCommentPageListRspVO {

    private Long id;

    private String routerUrl;

    private String avatar;

    private String nickname;

    private String mail;
    private String website;

    private Long fromUserId;

    private LocalDateTime createTime;

    private String content;

    private Integer status;

    private String reason;
}

