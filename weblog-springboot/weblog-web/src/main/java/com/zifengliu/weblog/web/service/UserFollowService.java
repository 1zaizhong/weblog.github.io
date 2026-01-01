package com.zifengliu.weblog.web.service;

import com.zifengliu.weblog.common.utils.Response;
import com.zifengliu.weblog.web.model.vo.follow.CheckFollowReqVO;
import com.zifengliu.weblog.web.model.vo.follow.FindFollowPageListReqVO;
import com.zifengliu.weblog.web.model.vo.follow.FollowUserReqVO;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2026/1/2 上午2:59
 * @description 用户关注服务
 **/
public interface UserFollowService {
    // 关注或取关
    Response followOrUnfollowUser(FollowUserReqVO reqVO);

    // 检查是否已关注
    Response checkIsFollowed(CheckFollowReqVO reqVO);

    // 分页查询我的关注列表
    Response findFollowPageList(FindFollowPageListReqVO reqVO);

}