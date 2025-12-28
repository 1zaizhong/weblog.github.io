package com.zifengliu.weblog.web.model.vo.collection;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/28 下午8:44
 * @description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "前台文章收藏请求 VO")
public class CollectArticleReqVO {
    @NotNull(message = "文章 ID 不能为空")
    private Long articleId;

    @NotNull(message = "请选择收藏夹")
    private Long directoryId;

    @NotNull(message = "用户 ID 不能为空")
    private Long userId; // 显式接收用户ID
}