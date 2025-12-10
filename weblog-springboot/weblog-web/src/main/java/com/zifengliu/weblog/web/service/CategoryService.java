package com.zifengliu.weblog.web.service;

import com.zifengliu.weblog.common.utils.Response;
import com.zifengliu.weblog.web.model.vo.category.FindCategoryArticlePageListReqVO;

/**
 * @author: 粟英朝
 *
 * @date:  2025-09-15 14:03
 * @description: 分类
 **/
public interface CategoryService {
    /**
     * 获取分类列表
     * @return
     */
    Response findCategoryList();


    /**
     * 获取分类下文章分页数据
     * @param findCategoryArticlePageListReqVO
     * @return
     */
    Response findCategoryArticlePageList(FindCategoryArticlePageListReqVO findCategoryArticlePageListReqVO);

}
