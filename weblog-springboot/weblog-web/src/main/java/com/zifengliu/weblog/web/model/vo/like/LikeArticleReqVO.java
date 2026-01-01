package com.zifengliu.weblog.web.model.vo.like;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/28 下午3:16
 * @description 前台点赞VO判断是不是点赞了的
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LikeArticleReqVO {
    @NotNull(message = "文章ID不能为空")
    private Long articleId;

    @NotNull(message = "用户ID不能为空")
    private Long userId;
}