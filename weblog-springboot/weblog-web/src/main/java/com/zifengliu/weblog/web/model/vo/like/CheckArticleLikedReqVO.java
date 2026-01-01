package com.zifengliu.weblog.web.model.vo.like;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/28 下午3:30
 * @description 检查是否点赞了
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckArticleLikedReqVO {
    @NotNull(message = "文章ID不能为空")
    private Long articleId;

    private Long userId; // 可为空，未登录即为未点赞
}