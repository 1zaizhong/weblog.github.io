package com.zifengliu.weblog.admin.model.vo.collection;

import com.zifengliu.weblog.common.model.BasePageQuery;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/28 下午6:52
 * @description
 **/
@Data
@ApiModel(value = "查询收藏夹内文章列表入参 VO")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FindCollectionArticlePageListReqVO extends BasePageQuery {
    @NotNull(message = "收藏夹 ID 不能为空")
    private Long directoryId;
}