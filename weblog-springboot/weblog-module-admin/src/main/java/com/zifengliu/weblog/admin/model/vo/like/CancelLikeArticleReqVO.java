package com.zifengliu.weblog.admin.model.vo.like;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/28 下午5:40
 * @description  取消点赞请求
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "取消点赞 VO")
public class CancelLikeArticleReqVO {
    @NotNull(message = "文章 ID 不能为空")
    private Long articleId;
}