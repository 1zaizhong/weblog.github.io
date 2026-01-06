package com.zifengliu.weblog.admin.model.vo.category;

import com.zifengliu.weblog.common.model.BasePageQuery;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2026/1/6 下午8:37
 * @description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindCategoryArticlePageListReqVO extends BasePageQuery {
    /**
     * 专栏 ID
     */
    @NotNull(message = "专栏 ID 不能为空")
    private Long categoryId;
}