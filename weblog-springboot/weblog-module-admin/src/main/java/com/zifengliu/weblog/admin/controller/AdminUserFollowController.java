package com.zifengliu.weblog.admin.controller;

import com.zifengliu.weblog.admin.service.UserFollowService;
import com.zifengliu.weblog.common.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2026/1/2 上午2:30
 * @description
 **/
@RestController
@RequestMapping("/admin/follow")
@Api(tags = "后台-用户关注管理")
public class AdminUserFollowController {

    @Autowired
    private UserFollowService userFollowService;

    @GetMapping("/list")
    @ApiOperation(value = "获取关注的博主列表")
    public Response findFollowingList() {
        return userFollowService.findFollowingList();
    }

    @PostMapping("/delete")
    @ApiOperation(value = "取消关注博主")
    public Response deleteFollow(@RequestParam Long targetUserId) {
        return userFollowService.deleteFollow(targetUserId);
    }
}
