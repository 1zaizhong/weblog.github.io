package com.zifengliu.weblog.web.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zifengliu.weblog.common.domain.dos.ArticleDO;
import com.zifengliu.weblog.common.domain.dos.CategoryDO;
import com.zifengliu.weblog.common.domain.dos.TagDO;
import com.zifengliu.weblog.common.domain.mapper.ArticleMapper;
import com.zifengliu.weblog.common.domain.mapper.CategoryMapper;
import com.zifengliu.weblog.common.domain.mapper.TagMapper;
import com.zifengliu.weblog.common.utils.Response;
import com.zifengliu.weblog.web.model.vo.statistics.FindStatisticsInfoRspVO;
import com.zifengliu.weblog.web.service.StatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/16 下午2:42
 * @description
 **/

@Service
@Slf4j
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private TagMapper tagMapper;

    /**
     * 获取文章总数、分类总数、标签总数、总访问量统计信息
     *
     * @return
     */
    @Override
    public Response findInfo(Long userId) {
        // 是否为管理员 (ID 为 1)
        boolean isAdmin = Objects.equals(userId, 1L);

        // 查询文章总数
        // 如果是管理员，userId 传 null，MyBatis Plus 的 .eq(Objects.nonNull(userId),
        Long articleCount = articleMapper.selectCount(Wrappers.<ArticleDO>lambdaQuery()
                .eq(!isAdmin, ArticleDO::getUserId, userId));

        // 查询分类总数
        Long categoryCount = categoryMapper.selectCount(Wrappers.<CategoryDO>lambdaQuery()
                .eq(!isAdmin, CategoryDO::getUserId, userId));

        // 查询标签总数
        Long tagCount = tagMapper.selectCount(Wrappers.<TagDO>lambdaQuery()
                .eq(!isAdmin, TagDO::getUserId, userId));

        // 总浏览量
        Long pvTotalCount = articleMapper.selectSumPvByUserId(isAdmin ? null : userId);




        // 组装 VO 类
        FindStatisticsInfoRspVO vo = FindStatisticsInfoRspVO.builder()
                .articleTotalCount(articleCount)
                .categoryTotalCount(categoryCount)
                .tagTotalCount(tagCount)
                .pvTotalCount(pvTotalCount)
                .build();;

        return Response.success(vo);
    }
}
