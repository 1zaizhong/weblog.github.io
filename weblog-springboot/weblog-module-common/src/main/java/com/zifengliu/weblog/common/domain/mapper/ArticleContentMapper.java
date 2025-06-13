package com.zifengliu.weblog.common.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zifengliu.weblog.common.domain.dos.ArticleContentDO;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/5/13 下午9:37
 * @description 文章内容Mapper接口
 **/
public interface ArticleContentMapper extends BaseMapper <ArticleContentDO>{

    /*
    * 根据文章ID 删除记录
     * @param articleId
     * @return
     */
    default int deleteByArticleId(Long articleId) {
        return delete(Wrappers.<ArticleContentDO>lambdaQuery()
                .eq(ArticleContentDO::getArticleId, articleId));
    }

    /**
     * 根据文章 ID 查询
     * @param articleId
     * @return
     */
    default ArticleContentDO selectByArticleId(Long articleId) {
        return selectOne(Wrappers.<ArticleContentDO>lambdaQuery()
                .eq(ArticleContentDO::getArticleId, articleId));
    }
    /**
     * 根据文章 ID 更新文章
     * @param articleContentDO
     * @return
     */
    default  int updateByArticleId(ArticleContentDO articleContentDO) {
        return  update(articleContentDO, Wrappers.<ArticleContentDO>lambdaQuery()
                .eq(ArticleContentDO::getArticleId,articleContentDO.getArticleId()));
    }
}
