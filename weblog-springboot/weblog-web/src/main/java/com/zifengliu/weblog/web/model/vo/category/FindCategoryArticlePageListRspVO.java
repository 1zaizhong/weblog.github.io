package com.zifengliu.weblog.web.model.vo.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/10 下午4:17
 * @description 首页获取分类文章分页数据 出参
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindCategoryArticlePageListRspVO {
    private Long id;
    private String cover;
    private String title;
    /**
     * 发布日期
     */
    private LocalDate createDate;

}
