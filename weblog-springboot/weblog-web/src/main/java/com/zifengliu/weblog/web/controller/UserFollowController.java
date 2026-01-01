package com.zifengliu.weblog.web.controller;

import com.zifengliu.weblog.common.utils.Response;
import com.zifengliu.weblog.web.model.vo.follow.CheckFollowReqVO;
import com.zifengliu.weblog.web.model.vo.follow.FindFollowPageListReqVO;
import com.zifengliu.weblog.web.model.vo.follow.FollowUserReqVO;
import com.zifengliu.weblog.web.service.UserFollowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2026/1/2 上午3:04
 * @description
 **/
@RestController
@RequestMapping("/follow")
@Api(tags = "前台-用户关注接口")
public class UserFollowController {

    @Autowired
    private UserFollowService userFollowService;

    @PostMapping("/subscribe")
    @ApiOperation(value = "关注/取消关注博主")
    public Response subscribe(@RequestBody FollowUserReqVO reqVO) {
        return userFollowService.followOrUnfollowUser(reqVO);
    }

    @PostMapping("/check")
    @ApiOperation(value = "检查是否关注该博主")
    public Response check(@RequestBody CheckFollowReqVO reqVO) {
        return userFollowService.checkIsFollowed(reqVO);
    }

    @PostMapping("/list")
    @ApiOperation(value = "获取我的关注列表")
    public Response list(@RequestBody FindFollowPageListReqVO reqVO) {
        return userFollowService.findFollowPageList(reqVO);
    }
}