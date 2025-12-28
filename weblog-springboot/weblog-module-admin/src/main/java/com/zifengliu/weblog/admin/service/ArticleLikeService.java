package com.zifengliu.weblog.admin.service;

import com.zifengliu.weblog.admin.model.vo.like.CancelLikeArticleReqVO;
import com.zifengliu.weblog.admin.model.vo.like.FindLikeArticlePageListReqVO;
import com.zifengliu.weblog.common.utils.Response;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/28 下午3:20
 * @description
 **/
public interface ArticleLikeService {
    // 后台：查询当前用户的点赞列表
    Response findUserLikePageList(FindLikeArticlePageListReqVO findLikeArticlePageListReqVO);
    //取消点赞
    Response deleteLike(CancelLikeArticleReqVO cancelLikeArticleReqVO);


}
