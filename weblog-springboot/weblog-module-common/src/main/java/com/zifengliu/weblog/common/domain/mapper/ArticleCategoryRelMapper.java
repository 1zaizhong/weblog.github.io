package com.zifengliu.weblog.common.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zifengliu.weblog.common.domain.dos.ArticleCategoryRelDO;
import com.zifengliu.weblog.common.domain.dos.ArticleContentDO;

import java.util.List;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/5/13 下午9:40
 * @description
 **/
public interface ArticleCategoryRelMapper extends BaseMapper<ArticleCategoryRelDO> {

    /*
    * 根据文章ID 来删除关联记录
    * @param articleID
    * @return
    * */
    default int deleteByArticleId(Long articleID) {
        return delete(Wrappers.<ArticleCategoryRelDO>lambdaQuery()
                .eq(ArticleCategoryRelDO:: getArticleId,articleID));
    }

    /*
     * 根据文章ID 查询
     * @param articleID
     * @return
     * */
    default ArticleCategoryRelDO selectByArticleId(Long articleId) {
        return selectOne(Wrappers.<ArticleCategoryRelDO>lambdaQuery()
                .eq(ArticleCategoryRelDO::getArticleId, articleId));
    }

    /**
     * 根据分类 ID 查询
     * @param categoryId
     * @return
     */
    default ArticleCategoryRelDO selectOneByCategoryId(Long categoryId) {
        return selectOne(Wrappers.<ArticleCategoryRelDO>lambdaQuery()
                .eq(ArticleCategoryRelDO::getCategoryId, categoryId)
                .last("LIMIT 1"));
    }
    /**
     * 根据文章 ID 集合批量查询
     * @param articleIds
     * @return
     */
    default List<ArticleCategoryRelDO> selectByArticleIds(List<Long> articleIds) {
        return selectList(Wrappers.<ArticleCategoryRelDO>lambdaQuery()
                .in(ArticleCategoryRelDO::getArticleId, articleIds));
    }
    /**
     * 根据分类 ID 查询所有的关联记录
     * @param categoryId
     * @return
     */
    default List<ArticleCategoryRelDO> selectListByCategoryId(Long categoryId) {
        return selectList(Wrappers.<ArticleCategoryRelDO>lambdaQuery()
                .eq(ArticleCategoryRelDO::getCategoryId, categoryId));
    }
}
