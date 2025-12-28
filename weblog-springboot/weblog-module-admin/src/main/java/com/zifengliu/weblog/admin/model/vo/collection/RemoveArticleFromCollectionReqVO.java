package com.zifengliu.weblog.admin.model.vo.collection;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/28 下午7:56
 * @description
 **/
@Data
@ApiModel(value = "移出收藏夹文章 VO")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RemoveArticleFromCollectionReqVO {
    @NotNull(message = "收藏夹 ID 不能为空")
    private Long directoryId;

    @NotNull(message = "文章 ID 不能为空")
    private Long articleId;
}