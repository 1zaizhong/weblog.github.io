package com.zifengliu.weblog.admin.controller;

import com.zifengliu.weblog.admin.model.vo.user.AddUserReqVO;
import com.zifengliu.weblog.admin.model.vo.user.DeleteUserReqVO;
import com.zifengliu.weblog.admin.model.vo.user.FindUserPageListReqVO;
import com.zifengliu.weblog.admin.model.vo.user.UpdateAdminUserPasswordReqVO;
import com.zifengliu.weblog.admin.service.AdminUserService;
import com.zifengliu.weblog.common.aspect.ApiOperationLog;
import com.zifengliu.weblog.common.utils.PageResponse;
import com.zifengliu.weblog.common.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/3/27 下午9:18
 * @description
 **/
@RestController
@RequestMapping("/admin")
@Api(tags = "Admin 用户模块")
public class AdminUserController {

    @Autowired
    private AdminUserService userService;

    @PostMapping("/password/update")
    @ApiOperation(value = "修改用户密码")
    @ApiOperationLog(description = "修改用户密码")
    public Response updatePassword(@RequestBody @Validated UpdateAdminUserPasswordReqVO updateAdminUserPasswordReqVO) {
        return userService.updatePassword(updateAdminUserPasswordReqVO);
    }
    //查询用户信息接口
    @PostMapping("/user/info")
    @ApiOperation(value = "获取用户信息")
    @ApiOperationLog(description = "获取用户信息")
    public Response findUserInfo() {
        return userService.findUserInfo();
    }
    //新增用户
    @PostMapping("/user/add")
    @ApiOperation(value = "新增用户")
    @ApiOperationLog(description = "新增用户")
    public Response addUser(@RequestBody @Validated AddUserReqVO addUserReqVO) {
        return userService.addUser(addUserReqVO);
    }
    // 删除用户
    @PostMapping("/user/delete")
    @ApiOperation(value = "删除用户")
    @ApiOperationLog(description = "删除用户及其关联数据")
    public Response deleteUser(@RequestBody @Validated DeleteUserReqVO deleteUserReqVO) {
        return userService.deleteUser(deleteUserReqVO);
    }
    // ：渲染全部用户
    @PostMapping("/user/list/all")
    @ApiOperation(value = "获取用户列表")
    @ApiOperationLog(description = "用于后台用户管理初始化渲染")
    public Response findAllUsers() {
        return userService.findAllUsersExceptAdmin();
    }

    // 按名搜索
    @PostMapping("/user/search")
    @ApiOperation(value = "模糊搜索用户")
    @ApiOperationLog(description = "模糊搜索用户")
    public Response searchUser(@RequestBody FindUserPageListReqVO reqVO) {
        return userService.findUsersByUsername(reqVO.getUsername());
    }

}
