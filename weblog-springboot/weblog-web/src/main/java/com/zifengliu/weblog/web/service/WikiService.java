package com.zifengliu.weblog.web.service;

import com.zifengliu.weblog.common.utils.Response;
import com.zifengliu.weblog.web.model.vo.wiki.FindWikiArticlePreNextReqVO;
import com.zifengliu.weblog.web.model.vo.wiki.FindWikiCatalogListReqVO;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/21 下午8:03
 * @description
 **/

public interface WikiService {

    /**
     * 获取知识库
     * @return
     */
    Response findWikiList();

    /**
     * 获取知识库目录
     * @param findWikiCatalogListReqVO
     * @return
     */
    Response findWikiCatalogList(FindWikiCatalogListReqVO findWikiCatalogListReqVO);

    /**
     * 获取上下页
     * @param findWikiArticlePreNextReqVO
     * @return
     */
    Response findArticlePreNext(FindWikiArticlePreNextReqVO findWikiArticlePreNextReqVO);

}
