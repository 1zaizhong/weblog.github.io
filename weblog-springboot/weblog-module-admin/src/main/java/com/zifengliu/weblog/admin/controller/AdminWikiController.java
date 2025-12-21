package com.zifengliu.weblog.admin.controller;

import com.zifengliu.weblog.admin.model.vo.wiki.*;
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

    @PostMapping("/delete")
    @ApiOperation(value = "知识库删除")
    @ApiOperationLog(description = "知识库删除")
    public Response deleteWiki(@RequestBody @Validated DeleteWikiReqVO deleteWikiReqVO) {
        return wikiService.deleteWiki(deleteWikiReqVO);
    }
    @PostMapping("/list")
    @ApiOperation(value = "查询知识库分页数据")
    @ApiOperationLog(description = "查询知识库分页数据")
    public Response findWikiPageList(@RequestBody @Validated FindWikiPageListReqVO findWikiPageListReqVO) {
        return wikiService.findWikiPageList(findWikiPageListReqVO);
    }

    @PostMapping("/isTop/update")
    @ApiOperation(value = "更新知识库置顶状态")
    @ApiOperationLog(description = "更新知识库置顶状态")
    public Response updateWikiIsTop(@RequestBody @Validated UpdateWikiIsTopReqVO updateWikiIsTopReqVO) {
        return wikiService.updateWikiIsTop(updateWikiIsTopReqVO);
    }

    @PostMapping("/isPublish/update")
    @ApiOperation(value = "更新知识库发布状态")
    @ApiOperationLog(description = "更新知识库发布状态")
    public Response updateWikiIsPublish(@RequestBody @Validated UpdateWikiIsPublishReqVO updateWikiIsPublishReqVO) {
        return wikiService.updateWikiIsPublish(updateWikiIsPublishReqVO);
    }
    @PostMapping("/update")
    @ApiOperation(value = "更新知识库")
    @ApiOperationLog(description = "更新知识库")
    public Response updateWiki(@RequestBody @Validated UpdateWikiReqVO updateWikiReqVO) {
        return wikiService.updateWiki(updateWikiReqVO);
    }

    @PostMapping("/catalog/list")
    @ApiOperation(value = "查询知识库目录数据")
    @ApiOperationLog(description = "查询知识库目录数据")
    public Response findWikiCatalogList(@RequestBody @Validated FindWikiCatalogListReqVO findWikiCatalogListReqVO) {
        return wikiService.findWikiCatalogList(findWikiCatalogListReqVO);
    }
    @PostMapping("/catalog/update")
    @ApiOperation(value = "更新知识库目录")
    @ApiOperationLog(description = "更新知识库目录")
    public Response updateWikiCatalogs(@RequestBody @Validated UpdateWikiCatalogReqVO updateWikiCatalogsReqVO) {
        return wikiService.updateWikiCatalogs(updateWikiCatalogsReqVO);
    }
}