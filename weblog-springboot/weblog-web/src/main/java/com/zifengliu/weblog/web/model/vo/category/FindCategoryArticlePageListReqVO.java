package com.zifengliu.weblog.web.model.vo.category;

import com.zifengliu.weblog.common.model.BasePageQuery;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;


/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/10 下午4:14
 * @description 首页获取分类文章分页数据 入参
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindCategoryArticlePageListReqVO extends BasePageQuery {
    /**
     * 分类 ID
     */
    @NotNull(message = "分类 ID 不能为空")
    private Long id;

}

