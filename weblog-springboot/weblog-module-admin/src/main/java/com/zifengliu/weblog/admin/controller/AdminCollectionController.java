package com.zifengliu.weblog.admin.controller;

import com.zifengliu.weblog.admin.model.vo.collection.*;
import com.zifengliu.weblog.admin.service.AdminCollectionService;
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
 * @date 2025/12/28 下午8:01
 * @description
 **/
@RestController
@RequestMapping("/admin/collection")
@Api(tags = "Admin 收藏管理模块")
public class AdminCollectionController {

    @Autowired
    private AdminCollectionService collectionService;

    // ----------------------------------------------------------------
    // 收藏夹基础管理
    // ----------------------------------------------------------------

    @PostMapping("/directory/add")
    @ApiOperation(value = "新增收藏夹")
    @ApiOperationLog(description = "新增收藏夹")
    public Response addDirectory(@RequestBody @Validated AddCollectionDirectoryReqVO addCollectionDirectoryReqVO) {
        return collectionService.addDirectory(addCollectionDirectoryReqVO);
    }

    @PostMapping("/directory/list")
    @ApiOperation(value = "收藏夹分页列表获取")
    @ApiOperationLog(description = "收藏夹分页列表获取")
    public PageResponse findDirectoryPageList(@RequestBody @Validated FindCollectionDirectoryPageListReqVO findCollectionDirectoryPageListReqVO) {
        return collectionService.findDirectoryPageList(findCollectionDirectoryPageListReqVO);
    }

    @PostMapping("/directory/delete")
    @ApiOperation(value = "删除收藏夹")
    @ApiOperationLog(description = "删除收藏夹")
    public Response deleteDirectory(@RequestBody @Validated DeleteCollectionDirectoryReqVO deleteCollectionDirectoryReqVO) {
        return collectionService.deleteDirectory(deleteCollectionDirectoryReqVO);
    }

    @PostMapping("/directory/select")
    @ApiOperation(value = "获取收藏夹下拉列表")
    @ApiOperationLog(description = "获取收藏夹下拉列表")
    public Response findCollectionDirectorySelectList() {
        return collectionService.findCollectionDirectorySelectList();
    }

    // ----------------------------------------------------------------
    // 收藏夹内文章管理
    // ----------------------------------------------------------------

    @PostMapping("/article/list")
    @ApiOperation(value = "收藏夹内文章分页获取")
    @ApiOperationLog(description = "收藏夹内文章分页获取")
    public PageResponse findCollectionArticlePageList(@RequestBody @Validated FindCollectionArticlePageListReqVO findCollectionArticlePageListReqVO) {
        return collectionService.findCollectionArticlePageList(findCollectionArticlePageListReqVO);
    }

    @PostMapping("/article/remove")
    @ApiOperation(value = "将文章移出收藏夹")
    @ApiOperationLog(description = "将文章移出收藏夹")
    public Response removeArticleFromCollection(@RequestBody @Validated RemoveArticleFromCollectionReqVO removeArticleFromCollectionReqVO) {
        return collectionService.removeArticleFromCollection(removeArticleFromCollectionReqVO);
    }
}