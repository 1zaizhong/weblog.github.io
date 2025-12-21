package com.zifengliu.weblog.admin.service;

import com.zifengliu.weblog.admin.model.vo.comment.DeleteCommentReqVO;
import com.zifengliu.weblog.admin.model.vo.comment.FindCommentPageListReqVO;
import com.zifengliu.weblog.common.utils.Response;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/22 上午12:12
 * @description
 **/

public interface AdminCommentService {

    /**
     * 查询评论分页数据
     * @param findCommentPageListReqVO
     * @return
     */
    Response findCommentPageList(FindCommentPageListReqVO findCommentPageListReqVO);

    /**
     * 删除评论
     * @param deleteCommentReqVO
     * @return
     */
    Response deleteComment(DeleteCommentReqVO deleteCommentReqVO);

}