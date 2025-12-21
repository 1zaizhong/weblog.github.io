package com.zifengliu.weblog.common.domain.mapper;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zifengliu.weblog.common.config.InsertBatchMapper;
import com.zifengliu.weblog.common.domain.dos.WikiCatalogDO;

import java.util.List;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/21 下午4:48
 * @description  知识库目录
 **/
public interface WikiCatalogMapper extends InsertBatchMapper<WikiCatalogDO> {

    /**
     * 根据某个知识库下所有目录
     * @param wikiId
     * @return
     */
    default List<WikiCatalogDO> selectByWikiId(Long wikiId) {
        return selectList(Wrappers.<WikiCatalogDO>lambdaQuery()
                .eq(WikiCatalogDO::getWikiId, wikiId)
        );
    }

    /**
     * 删除知识库
     * @param wikiId
     * @return
     */
    default int deleteByWikiId(Long wikiId) {
        return delete(Wrappers.<WikiCatalogDO>lambdaQuery()
                .eq(WikiCatalogDO::getWikiId, wikiId));
    }
}