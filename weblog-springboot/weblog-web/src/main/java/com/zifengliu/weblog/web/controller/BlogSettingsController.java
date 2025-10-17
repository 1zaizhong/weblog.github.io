package com.zifengliu.weblog.web.controller;

import com.zifengliu.weblog.common.aspect.ApiOperationLog;
import com.zifengliu.weblog.common.utils.Response;
import com.zifengliu.weblog.web.service.BlogSettingsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/10/17 下午5:05
 * @description 博客设置控制器
 **/
@RestController
@Api(tags = "博客设置")
@RequestMapping("/blog/settings")
public class BlogSettingsController {
    @Autowired
    private BlogSettingsService blogSettingsService;

    @PostMapping("/detail")
    @ApiOperation(value = "前台获取博客设置详情")
    @ApiOperationLog(description = "前台获取博客设置详情" )
    public Response findDetail(){
        return blogSettingsService.findDetail();
    }
}
