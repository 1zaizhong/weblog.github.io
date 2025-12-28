package com.zifengliu.weblog.web.service;

import com.zifengliu.weblog.common.utils.Response;
import com.zifengliu.weblog.web.model.vo.article.CheckArticleLikedReqVO;
import com.zifengliu.weblog.web.model.vo.article.LikeArticleReqVO;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/28 下午3:19
 * @description
 **/
public interface ArticleLikeService {
    /**
     * 点赞或取消点赞
     */
    Response likeOrUnlikeArticle(LikeArticleReqVO reqVO);

    /**
     * 检查用户是否已点赞该文章
     */
    Response checkIsLiked(CheckArticleLikedReqVO reqVO);
}
