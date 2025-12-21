package com.zifengliu.weblog.admin.controller;

import com.zifengliu.weblog.admin.model.vo.wiki.AddWikiReqVO;
import com.zifengliu.weblog.admin.service.AdminWikiService;
import com.zifengliu.weblog.common.aspect.ApiOperationLog;
import com.zifengliu.weblog.common.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/21 下午4:59
 * @description
 **/
@RestController
@RequestMapping("/admin/wiki")
@Api(tags = "Admin 知识库模块")
public class AdminWikiController {

    @Autowired
    private AdminWikiService wikiService;

    @PostMapping("/add")
    @ApiOperation(value = "新增知识库")
    @ApiOperationLog(description = "新增知识库")
    public Response addWiki(@RequestBody @Validated AddWikiReqVO addWikiReqVO) {
        return wikiService.addWiki(addWikiReqVO);
    }

}