package com.zifengliu.weblog.admin.model.vo.collection;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/28 下午7:57
 * @description
 **/
@Data
@ApiModel(value = "文章收藏请求 VO")
public class CollectArticleReqVO {
    @NotNull(message = "文章 ID 不能为空")
    private Long articleId;

    @NotNull(message = "请选择收藏夹")
    private Long directoryId;
}