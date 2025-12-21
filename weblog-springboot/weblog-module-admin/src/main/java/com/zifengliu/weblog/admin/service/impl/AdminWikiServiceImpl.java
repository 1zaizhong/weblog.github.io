package com.zifengliu.weblog.admin.service.impl;

import com.zifengliu.weblog.admin.model.vo.wiki.AddWikiReqVO;
import com.zifengliu.weblog.admin.service.AdminWikiService;
import com.zifengliu.weblog.common.domain.dos.WikiCatalogDO;
import com.zifengliu.weblog.common.domain.dos.WikiDO;
import com.zifengliu.weblog.common.domain.mapper.WikiCatalogMapper;
import com.zifengliu.weblog.common.domain.mapper.WikiMapper;
import com.zifengliu.weblog.common.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/21 下午4:56
 * @description  知识库具体实现
 **/

@Service
@Slf4j
public class AdminWikiServiceImpl implements AdminWikiService {

    @Autowired
    private WikiMapper wikiMapper;
    @Autowired
    private WikiCatalogMapper wikiCatalogMapper;

    /**
     * 新增知识库
     *
     * @param addWikiReqVO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response addWiki(AddWikiReqVO addWikiReqVO) {
        // VO 转 DO
        WikiDO wikiDO = WikiDO.builder()
                .cover(addWikiReqVO.getCover())
                .title(addWikiReqVO.getTitle())
                .summary(addWikiReqVO.getSummary())
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();

        // 新增知识库
        wikiMapper.insert(wikiDO);
        // 获取新增记录的主键 ID
        Long wikiId = wikiDO.getId();

        // 初始化默认目录
        // > 概述
        // > 基础
        wikiCatalogMapper.insert(WikiCatalogDO.builder().wikiId(wikiId).title("概述").sort(1).build());
        wikiCatalogMapper.insert(WikiCatalogDO.builder().wikiId(wikiId).title("基础").sort(2).build());
        return Response.success();
    }

}
