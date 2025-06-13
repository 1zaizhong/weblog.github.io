package com.zifengliu.weblog.admin.controller;


import com.zifengliu.weblog.admin.model.vo.blogSettings.UpdateBlogSettingsReqVO;
import com.zifengliu.weblog.admin.service.AdminBlogSettingsService;
import com.zifengliu.weblog.common.aspect.ApiOperationLog;
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
 * @date 2025/5/12 下午4:23
 * @description
 **/
@RestController
@RequestMapping("/admin/blog/settings")
@Api(tags = "Admin博客设置")
public class AdminBlogSettingsController {
    @Autowired
    private AdminBlogSettingsService adminBlogSettingsService;

    @PostMapping("/update")
    @ApiOperation(value = "博客信息修改")
    @ApiOperationLog(description = "博客信息修改")
    public Response updateBlogSettings(@RequestBody @Validated UpdateBlogSettingsReqVO updateBlogSettingsReqVO) {
        return adminBlogSettingsService.updateBlogSettings(updateBlogSettingsReqVO);
    }
    @PostMapping("/detail")
    @ApiOperation(value = "获取博客信息详情")
    @ApiOperationLog(description = "获取博客信息详情")
    public Response findDetail() {
        return adminBlogSettingsService.findDetail();
    }
}
