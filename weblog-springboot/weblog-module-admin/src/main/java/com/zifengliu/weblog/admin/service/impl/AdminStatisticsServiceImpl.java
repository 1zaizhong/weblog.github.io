package com.zifengliu.weblog.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Maps;
import com.zifengliu.weblog.admin.service.AdminStatisticsService;
import com.zifengliu.weblog.common.domain.dos.ArticleCategoryRelDO;
import com.zifengliu.weblog.common.domain.dos.CategoryDO;
import com.zifengliu.weblog.common.domain.mapper.ArticleCategoryRelMapper;
import com.zifengliu.weblog.common.domain.mapper.CategoryMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/16 下午8:14
 * @description
 **/

@Service
@Slf4j
public class AdminStatisticsServiceImpl implements AdminStatisticsService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ArticleCategoryRelMapper articleCategoryRelMapper;

    @Override
    public void statisticsCategoryArticleTotal() {
        // 查询所有分类
        List<CategoryDO> categoryDOS = categoryMapper.selectList(Wrappers.emptyWrapper());

        // 查询所有文章-分类映射记录
        List<ArticleCategoryRelDO> articleCategoryRelDOS = articleCategoryRelMapper.selectList(Wrappers.emptyWrapper());

        // 按所属分类 ID 进行分组
        Map<Long, List<ArticleCategoryRelDO>> categoryIdAndArticleCategoryRelDOMap = Maps.newHashMap();

        // 如果不为空
        if (!CollectionUtils.isEmpty(articleCategoryRelDOS)) {
            categoryIdAndArticleCategoryRelDOMap = articleCategoryRelDOS.stream()
                    .collect(Collectors.groupingBy(ArticleCategoryRelDO::getCategoryId));
        }

        if (!CollectionUtils.isEmpty(categoryDOS)) {
            // 循环统计各分类下的文章总数
            for (CategoryDO categoryDO : categoryDOS) {
                Long categoryId = categoryDO.getId();
                // 获取此分类下所有映射记录
                List<ArticleCategoryRelDO> articleCategoryRelDOList = categoryIdAndArticleCategoryRelDOMap.get(categoryId);

                // 获取文章总数
                int articlesTotal = CollectionUtils.isEmpty(articleCategoryRelDOList) ? 0 : articleCategoryRelDOList.size();

                // 更新该分类的文章总数
                CategoryDO categoryDO1 = CategoryDO.builder()
                        .id(categoryId)
                        .articlesTotal(articlesTotal)
                        .build();
                categoryMapper.updateById(categoryDO1);
            }
        }
    }
}

