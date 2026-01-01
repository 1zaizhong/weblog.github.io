package com.zifengliu.weblog.admin.service;

import com.zifengliu.weblog.common.utils.Response;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2026/1/2 上午2:26
 * @description
 **/
public interface UserFollowService {
    /**
     * 查询当前登录用户关注的博主列表
     */
    Response findFollowingList();

    /**
     * 取消关注
     * @param targetUserId 被取消关注的博主ID
     */
    Response deleteFollow(Long targetUserId);
}