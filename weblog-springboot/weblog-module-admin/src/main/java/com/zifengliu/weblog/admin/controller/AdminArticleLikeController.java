package com.zifengliu.weblog.admin.controller;

import com.zifengliu.weblog.admin.model.vo.like.CancelLikeArticleReqVO;
import com.zifengliu.weblog.admin.model.vo.like.FindLikeArticlePageListReqVO;
import com.zifengliu.weblog.admin.service.ArticleLikeService;
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
 * @date 2025/12/28 下午5:27
 * @description
 **/
@RestController
@RequestMapping("/admin/like")
@Api(tags = "Admin 点赞管理模块")
public class AdminArticleLikeController {

    @Autowired
    private ArticleLikeService articleLikeService;

    @PostMapping("/list")
    @ApiOperation(value = "点赞文章分页数据获取")
    @ApiOperationLog(description = "点赞文章分页数据获取")
    public Response findLikePageList(@RequestBody @Validated FindLikeArticlePageListReqVO findLikeArticlePageListReqVO) {
        return articleLikeService.findUserLikePageList(findLikeArticlePageListReqVO);
    }

    @PostMapping("/cancel")
    @ApiOperation(value = "取消点赞")
    @ApiOperationLog(description = "取消点赞")
    public Response cancelLike(@RequestBody @Validated CancelLikeArticleReqVO cancelLikeArticleReqVO) {
        return articleLikeService.deleteLike(cancelLikeArticleReqVO);
    }
}