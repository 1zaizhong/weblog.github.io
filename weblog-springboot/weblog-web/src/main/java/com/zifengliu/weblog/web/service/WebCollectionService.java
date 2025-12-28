package com.zifengliu.weblog.web.service;

import com.zifengliu.weblog.common.utils.Response;
import com.zifengliu.weblog.web.model.vo.collection.CollectArticleReqVO;
import com.zifengliu.weblog.web.model.vo.collection.FindCollectionDirectoryReqVO;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/28 下午8:45
 * @description
 **/
public interface WebCollectionService {
    /**
     * 根据用户ID获取收藏夹列表
     */
    Response findCollectionDirectoryList(FindCollectionDirectoryReqVO reqVO);

    /**
     * 收藏文章（显式传入用户ID）
     */
    Response collectArticle(CollectArticleReqVO reqVO);
}
