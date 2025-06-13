package com.zifengliu.weblog.common.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zifengliu.weblog.common.config.InsertBatchMapper;
import com.zifengliu.weblog.common.domain.dos.ArticleCategoryRelDO;
import com.zifengliu.weblog.common.domain.dos.ArticleTagRelDO;

import java.util.List;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/5/13 下午9:41
 * @description
 **/
public interface ArticleTagRelMapper extends InsertBatchMapper<ArticleTagRelDO> {


    /*
    * */
    default  int deleteByArticleId(Long articleId) {
        return  delete(Wrappers.<ArticleTagRelDO>lambdaQuery()
                .eq(ArticleTagRelDO::getArticleId, articleId));
    }
    /*
     * 根据文章ID 查询
     * @param articleID
     * @return
     * */
    default List<ArticleTagRelDO> selectByArticleId(Long articleId) {
        return selectList(Wrappers.<ArticleTagRelDO>lambdaQuery()
                .eq(ArticleTagRelDO::getArticleId, articleId));
    }

    /**
     * 根据标签 ID 查询
     * @param tagId
     * @return
     */
    default ArticleTagRelDO selectOneByTagId(Long tagId) {
        return selectOne(Wrappers.<ArticleTagRelDO>lambdaQuery()
                .eq(ArticleTagRelDO::getTagId, tagId)
                .last("LIMIT 1"));
    }

    /**
     * 根据文章 ID 集合批量查询
     * @param articleIds
     * @return
     */
    default List<ArticleTagRelDO> selectByArticleIds(List<Long> articleIds) {
        return selectList(Wrappers.<ArticleTagRelDO>lambdaQuery()
                .in(ArticleTagRelDO::getArticleId, articleIds));
    }

}
