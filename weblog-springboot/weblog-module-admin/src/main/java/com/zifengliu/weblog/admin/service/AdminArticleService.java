package com.zifengliu.weblog.admin.service;

import com.zifengliu.weblog.admin.model.vo.article.*;
import com.zifengliu.weblog.admin.model.vo.category.FindCategoryPageListReqVO;
import com.zifengliu.weblog.common.utils.PageResponse;
import com.zifengliu.weblog.common.utils.Response;


/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/5/13 下午9:56
 * @description
 **/

public interface AdminArticleService {

/*
* 发布文章
* @Param publishArticleReqVO
* @return
* */
    Response publishArticle(PublishArticleReqVO publishArticleReqVO);

    /*
    * 删除文章
    * @param deleteArticleReqVO
    * @return
    * */
    Response deleteArticle(DeleteArticleReqVO deleteArticleReqVO);


    /*
     * 文章分页数据查询
     * @param
     * @return
     */
    PageResponse findArticlePageList(FindArticlePageListReqVO findCategoryPageListReqVO);

    /*查询文章详情
    @param findArticleDetailReqVO
    @return
    * */
    Response findArticleDetail(FindArticleDetailReqVO findArticleDetailReqVO);

    /*
    * 更新文章
    * @param updateArticleReqVO
    * @return
    * */
    Response updateArticle(UpdateArticleReqVO updateArticleReqVO);



}
