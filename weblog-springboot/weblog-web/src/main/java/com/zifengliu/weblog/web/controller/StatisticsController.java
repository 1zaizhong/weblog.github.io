package com.zifengliu.weblog.web.controller;

import com.zifengliu.weblog.common.aspect.ApiOperationLog;
import com.zifengliu.weblog.common.utils.Response;
import com.zifengliu.weblog.web.model.vo.author.FindAuthorDashboardReqVO;
import com.zifengliu.weblog.web.model.vo.statistics.FindAuthorInfoByArticleIdReqVO;
import com.zifengliu.weblog.web.model.vo.statistics.FindStatisticsInfoReqVO;
import com.zifengliu.weblog.web.service.StatisticsService;
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
 * @date 2025/12/16 下午2:43
 * @description
 **/

@RestController
@RequestMapping("/statistics")
@Api(tags = "统计信息")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @PostMapping("/info")
    @ApiOperation(value = "前台获取统计信息")
    @ApiOperationLog(description = "前台获取统计信息")
    public Response findInfo(@RequestBody @Validated FindStatisticsInfoReqVO reqVO) {
        return statisticsService.findInfo(reqVO.getUserId());
    }
    @PostMapping("/author/info")
    @ApiOperation(value = "根据文章 ID 获取作者详细统计信息")
    @ApiOperationLog(description = "根据文章 ID 获取作者详细统计信息")
    public Response findAuthorInfoByArticleId(@RequestBody @Validated FindAuthorInfoByArticleIdReqVO reqVO) {
        return statisticsService.findAuthorInfoByArticleId(reqVO.getArticleId());
    }

    @PostMapping("/author/dashboard")
    @ApiOperation(value = "获取博主主页")
    @ApiOperationLog(description = "获取博主主页")
    public Response findAuthorDashboardData(@RequestBody @Validated FindAuthorDashboardReqVO reqVO) {
        return statisticsService.findAuthorDashboardData(reqVO);
    }
}