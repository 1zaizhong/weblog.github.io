package com.zifengliu.weblog.web.model.vo.comment;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/21 下午10:10
 * @description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PublishCommentReqVO {

    /**
     * 头像不用一点传
     */
    private String avatar;


    private String nickname;


    @NotBlank(message = "路由地址不能为空")
    private String routerUrl;

    @NotBlank(message = "评论内容不能为空")
    @Length(min = 1, max = 120, message = "评论内容需大于 1 小于 120 字符")
    private String content;

    @NotNull(message = "评论人不能为空")
    private Long fromUserId;
    /**
     * 回复的评论 ID
     */
    private Long replyCommentId;

    /**
     * 父评论 ID
     */
    private Long parentCommentId;

}
