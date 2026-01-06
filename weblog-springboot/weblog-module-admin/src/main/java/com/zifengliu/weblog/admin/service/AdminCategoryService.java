package com.zifengliu.weblog.admin.service;

import com.zifengliu.weblog.admin.model.vo.article.UpdateArticleIsTopReqVO;
import com.zifengliu.weblog.admin.model.vo.category.AddCategoryReqVO;
import com.zifengliu.weblog.admin.model.vo.category.DeleteCategoryReqVO;
import com.zifengliu.weblog.admin.model.vo.category.FindCategoryArticlePageListReqVO;
import com.zifengliu.weblog.admin.model.vo.category.FindCategoryPageListReqVO;

import com.zifengliu.weblog.common.utils.PageResponse;
import com.zifengliu.weblog.common.utils.Response;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/4/10 下午5:09
 * @description 分类接口管理
 **/
public interface AdminCategoryService {

    /*
     * 添加分类
     * @param addCategoryReqVO
     * @return
     * */
    Response  addCategory(AddCategoryReqVO addCategoryReqVO);



    /*
     * 分类分页数据查询
     * @param findCategoryPageListReqVO
     * @return
     */
    PageResponse findCategoryPageList(FindCategoryPageListReqVO findCategoryPageListReqVO);

    /*
    * 删除分类
    * @return
    * */
    Response deleteCategory(DeleteCategoryReqVO deleteCategoryReqVO);

    /*
    * 获取文章分类的 Select 列表数据
    * @return
    * */
    Response findeCategorySelectList();
    /**
     * 查询专栏下的文章分页数据
     */
    PageResponse findCategoryArticlePageList(FindCategoryArticlePageListReqVO findCategoryArticlePageListReqVO);




}
