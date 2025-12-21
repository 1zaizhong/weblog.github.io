package com.zifengliu.weblog.web.service;

import com.zifengliu.weblog.common.utils.Response;
import com.zifengliu.weblog.web.model.vo.comment.FindCommentListReqVO;
import com.zifengliu.weblog.web.model.vo.comment.FindQQUserInfoReqVO;
import com.zifengliu.weblog.web.model.vo.comment.PublishCommentReqVO;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/21 下午9:38
 * @description 评论
 **/
public interface CommentService  {

    /**
     * 根据 QQ 号获取用户信息
     * @param findQQUserInfoReqVO
     * @return
     */
    Response findQQUserInfo(FindQQUserInfoReqVO findQQUserInfoReqVO);
    /**
     * 发布评论
     * @param publishCommentReqVO
     * @return
     */
    Response publishComment(PublishCommentReqVO publishCommentReqVO);

    /**
     * 查询页面所有评论
     * @param findCommentListReqVO
     * @return
     */
    Response findCommentList(FindCommentListReqVO findCommentListReqVO);
}
