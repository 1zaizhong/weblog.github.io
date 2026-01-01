package com.zifengliu.weblog.web.service;

import com.zifengliu.weblog.common.utils.Response;
import com.zifengliu.weblog.web.model.vo.article.FindArticleDetailReqVO;
import com.zifengliu.weblog.web.model.vo.article.FindIndexArticlePageListReqVO;

/**
 * @author: 粟英朝
 * @date:  2025-09-15 14:03
 * @description: 文章
 **/
public interface ArticleService {
    /**
     * 获取首页文章分页数据
     * @param findIndexArticlePageListReqVO
     * @return
     */
    Response findArticlePageList(FindIndexArticlePageListReqVO findIndexArticlePageListReqVO);

    /**
     * 获取文章详情
     * @param findArticleDetailReqVO
     * @return
     */
    Response findArticleDetail(FindArticleDetailReqVO findArticleDetailReqVO);

    /**
     * 获取文章热度信息
     * @return
     */
     Response findHotArticleList();
}
