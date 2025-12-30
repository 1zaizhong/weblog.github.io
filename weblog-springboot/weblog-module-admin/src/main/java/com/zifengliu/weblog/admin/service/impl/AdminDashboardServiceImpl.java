package com.zifengliu.weblog.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zifengliu.weblog.admin.model.vo.dashboard.FindDashboardPVStatisticsInfoRspVO;
import com.zifengliu.weblog.admin.model.vo.dashboard.FindDashboardStatisticsInfoRspVO;
import com.zifengliu.weblog.admin.service.AdminDashboardService;
import com.zifengliu.weblog.common.constant.Constants;
import com.zifengliu.weblog.common.domain.dos.*;
import com.zifengliu.weblog.common.domain.mapper.*;
import com.zifengliu.weblog.common.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Collectors;
/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/13 下午8:13
 * @description
 **/

@Service
@Slf4j
public class AdminDashboardServiceImpl implements AdminDashboardService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private StatisticsArticlePVMapper articlePVMapper;
    @Autowired
    private UserMapper userMapper;
    /**
     * 获取当前登录用户的 ID
     * @return
     */
    private Long getLoginUserId() {
        // 1. 从 Spring Security 上下文中获取当前认证信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 2. 如果未登录或匿名访问，返回 null
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            return null;
        }

        // 3. 根据用户名查询用户信息
        String username = authentication.getName();
        UserDO userDO = userMapper.selectOne(Wrappers.<UserDO>lambdaQuery()
                .eq(UserDO::getUsername, username));

        // 4. 返回用户 ID
        return userDO != null ? userDO.getUserId() : null;
    }

    /**
     * 获取仪表盘基础统计信息
     *
     * @return
     */
    @Override
    public Response findDashboardStatistics() {
        // 获取当前登录用户名
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        // 查询用户信息
        UserDO userDO = userMapper.selectOne(Wrappers.<UserDO>lambdaQuery()
                .eq(UserDO::getUsername, username));
        Long loginUserId = userDO.getUserId();
        //判断用户
        Long userId = Objects.equals(loginUserId, 1L) ? null : loginUserId;

        // 查询文章总数
        Long articleTotalCount = articleMapper.selectCount(Wrappers.<ArticleDO>lambdaQuery()
                .eq(Objects.nonNull(userId), ArticleDO::getUserId, userId));

        // 查询分类总数
        Long categoryTotalCount = categoryMapper.selectCount(Wrappers.<CategoryDO>lambdaQuery()
                .eq(Objects.nonNull(userId), CategoryDO::getUserId, userId));

        // 查询标签总数
        Long tagTotalCount = tagMapper.selectCount(Wrappers.<TagDO>lambdaQuery()
                .eq(Objects.nonNull(userId), TagDO::getUserId, userId));

        // 总浏览量

        Long pvTotalCount = articleMapper.selectSumPvByUserId(userId);



        // 组装 VO 类
        return Response.success(FindDashboardStatisticsInfoRspVO.builder()
                .articleTotalCount(articleTotalCount)
                .categoryTotalCount(categoryTotalCount)
                .tagTotalCount(tagTotalCount)
                .pvTotalCount(pvTotalCount) // 返回绑定用户后的总浏览量
                .build());
    }
    /**
     * 获取文章排行
     *
     * @return
     */
    @Override
    public Response findDashboardPublishArticleStatistics() {
        // 1. 获取 ID，逻辑不变
        Long loginUserId = getLoginUserId();
        Long userId = Objects.equals(loginUserId, 1L) ? null : loginUserId;

        // 2. 查询前5，逻辑不变
        List<ArticleDO> articles = articleMapper.selectList(Wrappers.<ArticleDO>lambdaQuery()
                .eq(userId != null, ArticleDO::getUserId, userId)
                .orderByDesc(ArticleDO::getReadNum)
                .last("LIMIT 5")
                .select(ArticleDO::getTitle, ArticleDO::getCover, ArticleDO::getReadNum));

        // 3. 返回给前端（注意：前端接收的是 res.data，是一个数组）
        return Response.success(articles);
    }
    /**
     * 获取文章最近一周 PV 访问量统计信息
     *
     * @return
     */
    @Override
    public Response findDashboardPVStatistics() {
        Long loginUserId = getLoginUserId();
        Long userId = Objects.equals(loginUserId, 1L) ? null : loginUserId;
        // 查询最近一周的 PV 访问量记录
        List<StatisticsArticlePVDO> articlePVDOS = articlePVMapper.selectLatestWeekRecords(userId);
        Map<LocalDate, Long> pvDateCountMap = Maps.newHashMap();
        if (!CollectionUtils.isEmpty(articlePVDOS)) {
            // 转 Map, 方便后续通过日期获取 PV 访问量
            pvDateCountMap = articlePVDOS.stream()
                    .collect(Collectors.toMap(StatisticsArticlePVDO::getPvDate, StatisticsArticlePVDO::getPvCount));
        }

        FindDashboardPVStatisticsInfoRspVO vo = null;

        // 日期集合
        List<String> pvDates = Lists.newArrayList();
        // PV 集合
        List<Long> pvCounts = Lists.newArrayList();

        // 当前日期
        LocalDate currDate = LocalDate.now();
        // 一周前
        LocalDate tmpDate = currDate.minusWeeks(1);
        // 从一周前开始循环
        for (; tmpDate.isBefore(currDate) || tmpDate.isEqual(currDate); tmpDate = tmpDate.plusDays(1)) {
            // 设置对应日期的 PV 访问量
            pvDates.add(tmpDate.format(Constants.MONTH_DAY_FORMATTER));
            Long pvCount = pvDateCountMap.get(tmpDate);
            pvCounts.add(Objects.isNull(pvCount) ? 0 : pvCount);
        }

        vo = FindDashboardPVStatisticsInfoRspVO.builder()
                .pvDates(pvDates)
                .pvCounts(pvCounts)
                .build();

        return Response.success(vo);
    }

}
