package com.zifengliu.weblog.web.controller;

import com.zifengliu.weblog.common.aspect.ApiOperationLog;
import com.zifengliu.weblog.common.utils.Response;
import com.zifengliu.weblog.web.model.vo.like.CheckArticleLikedReqVO;
import com.zifengliu.weblog.web.model.vo.like.LikeArticleReqVO;
import com.zifengliu.weblog.web.service.ArticleLikeService;
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
 * @date 2025/12/28 下午3:33
 * @description
 **/
@RestController
@RequestMapping("/article")
@Api(tags = "文章点赞")
public class ArticleLikeController {

    @Autowired
    private ArticleLikeService articleLikeService;

    @PostMapping("/like")
    @ApiOperationLog(description = "点赞/取消点赞文章")
    @ApiOperation(value = "文章点赞")
    public Response likeArticle(@RequestBody @Validated LikeArticleReqVO reqVO) {
        return articleLikeService.likeOrUnlikeArticle(reqVO);
    }

    @PostMapping("/isLiked")
    @ApiOperationLog(description = "检查是否已点赞")
    @ApiOperation(value = "检查是否点赞")
    public Response isLiked(@RequestBody @Validated CheckArticleLikedReqVO reqVO) {
        return articleLikeService.checkIsLiked(reqVO);
    }
}