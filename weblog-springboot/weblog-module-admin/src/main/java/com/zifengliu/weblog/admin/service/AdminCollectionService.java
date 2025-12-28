package com.zifengliu.weblog.admin.service;

import com.zifengliu.weblog.admin.model.vo.collection.*;
import com.zifengliu.weblog.common.utils.PageResponse;
import com.zifengliu.weblog.common.utils.Response;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/28 下午6:49
 * @description
 **/
public interface AdminCollectionService {

    /**
     * 新增收藏夹
     */
    Response addDirectory(AddCollectionDirectoryReqVO reqVO);

    /**
     * 查询我的收藏夹列表（分页）
     */
    PageResponse findDirectoryPageList(FindCollectionDirectoryPageListReqVO reqVO);

    /**
     * 删除收藏夹
     */
    Response deleteDirectory(DeleteCollectionDirectoryReqVO reqVO);

    /**
     * 查询某个收藏夹内的文章列表
     */
    PageResponse findCollectionArticlePageList(FindCollectionArticlePageListReqVO reqVO);

    /**
     * 将文章移出收藏夹
     */
    Response removeArticleFromCollection(RemoveArticleFromCollectionReqVO reqVO);

    /**
     * 获取所有收藏夹（用于前台收藏时的下拉展示）
     */
    Response findCollectionDirectorySelectList();
}