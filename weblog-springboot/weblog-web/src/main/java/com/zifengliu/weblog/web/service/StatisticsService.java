package com.zifengliu.weblog.web.service;

import com.zifengliu.weblog.common.utils.Response;
import com.zifengliu.weblog.web.model.vo.author.FindAuthorDashboardReqVO;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/16 下午2:42
 * @description
 **/
public interface StatisticsService {
    /**
     * 获取文章总数、分类总数、标签总数、总访问量统计信息
     * @return
     */
    Response findInfo(Long userId);
    /**
     * 通过文章Id获取文章总数、分类总数、标签总数、总访问量统计信息
     * @return
     */
    Response findAuthorInfoByArticleId(Long articleId);

    /**
     * 获取博主主页看板数据
     */
    Response findAuthorDashboardData(FindAuthorDashboardReqVO reqVO);
}