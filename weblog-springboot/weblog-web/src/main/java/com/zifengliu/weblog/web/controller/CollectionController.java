package com.zifengliu.weblog.web.controller;

import com.zifengliu.weblog.common.aspect.ApiOperationLog;
import com.zifengliu.weblog.common.utils.Response;
import com.zifengliu.weblog.web.model.vo.collection.CollectArticleReqVO;
import com.zifengliu.weblog.web.model.vo.collection.FindCollectionArticlePageListReqVO;
import com.zifengliu.weblog.web.model.vo.collection.FindCollectionDirectoryReqVO;
import com.zifengliu.weblog.web.service.WebCollectionService;
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
 * @date 2025/12/28 下午8:47
 * @description
 **/
@RestController
@RequestMapping("/collection")
@Api(tags = "Web 收藏模块")
public class CollectionController {

    @Autowired
    private WebCollectionService collectionService;

    @PostMapping("/directory/list")
    @ApiOperation(value = "获取指定用户的收藏夹列表")
    @ApiOperationLog(description = "获取指定用户的收藏夹列表")
    public Response findCollectionDirectoryList(@RequestBody @Validated FindCollectionDirectoryReqVO reqVO) {
        return collectionService.findCollectionDirectoryList(reqVO);
    }

    @PostMapping("/article/collect")
    @ApiOperation(value = "收藏文章")
    @ApiOperationLog(description = "收藏文章")
    public Response collectArticle(@RequestBody @Validated CollectArticleReqVO collectArticleReqVO) {
        return collectionService.collectArticle(collectArticleReqVO);
    }
    @PostMapping("/article/list")
    @ApiOperation(value = "获取指定收藏夹内的文章列表")
    @ApiOperationLog(description = "获取指定收藏夹内的文章列表")
    public Response findCollectionArticlePageList(@RequestBody @Validated FindCollectionArticlePageListReqVO reqVO) {
        return collectionService.findCollectionArticlePageList(reqVO);
    }
}