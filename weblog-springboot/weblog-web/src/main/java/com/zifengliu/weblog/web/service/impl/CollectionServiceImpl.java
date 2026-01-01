package com.zifengliu.weblog.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zifengliu.weblog.common.domain.dos.ArticleDO;
import com.zifengliu.weblog.common.domain.dos.CollectionArticleRelDO;
import com.zifengliu.weblog.common.domain.dos.CollectionDirectoryDO;
import com.zifengliu.weblog.common.domain.mapper.ArticleMapper;
import com.zifengliu.weblog.common.domain.mapper.CollectionArticleRelMapper;
import com.zifengliu.weblog.common.domain.mapper.CollectionDirectoryMapper;
import com.zifengliu.weblog.common.utils.PageResponse;
import com.zifengliu.weblog.common.utils.Response;
import com.zifengliu.weblog.web.model.vo.collection.*;
import com.zifengliu.weblog.web.service.WebCollectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/28 下午8:46
 * @description  收藏夹服务实现类
 **/
@Service
@Slf4j
public class CollectionServiceImpl implements WebCollectionService {

    @Autowired
    private CollectionDirectoryMapper directoryMapper;
    @Autowired
    private CollectionArticleRelMapper relMapper;

/*
* 查询收藏夹列表*/
    @Override
    public Response findCollectionDirectoryList(FindCollectionDirectoryReqVO reqVO) {
        // 1. 根据用户ID查询所有的收藏夹 (对应 t_collection_directory 表)
        List<CollectionDirectoryDO> directories = directoryMapper.selectList(Wrappers.<CollectionDirectoryDO>lambdaQuery()
                .eq(CollectionDirectoryDO::getUserId, reqVO.getUserId())
                .orderByDesc(CollectionDirectoryDO::getCreateTime));

        // 如果该用户没有收藏夹，直接返回空列表
        if (CollectionUtils.isEmpty(directories)) {
            return Response.success(Collections.emptyList());
        }

        // 2. 将 DO 转换为 VO，并填充统计数据
        List<CollectionDirectoryRspVO> vos = directories.stream().map(directory -> {

            // 3. 统计该收藏夹下的文章数量 (对应 t_collection_article_rel 表)
            // 使用 relMapper 根据 directory_id 分组计数
            Long count = relMapper.selectCount(Wrappers.<CollectionArticleRelDO>lambdaQuery()
                    .eq(CollectionArticleRelDO::getDirectoryId, directory.getId()));

            // 构建返回对象
            return CollectionDirectoryRspVO.builder()
                    .id(directory.getId())
                    .name(directory.getName())
                    .articlesTotal(count.intValue())
                    .build();
        }).collect(Collectors.toList());

        return Response.success(vos);
    }
    /*根据收藏夹列表查文章
    * */
    @Override
    public Response collectArticle(CollectArticleReqVO reqVO) {
        Long userId = reqVO.getUserId();
        Long articleId = reqVO.getArticleId();
        Long directoryId = reqVO.getDirectoryId();

        // 1. 校验该收藏夹是否属于该用户（防止前端篡改 directoryId 存入他人的收藏夹）
        CollectionDirectoryDO directoryDO = directoryMapper.selectOne(Wrappers.<CollectionDirectoryDO>lambdaQuery()
                .eq(CollectionDirectoryDO::getId, directoryId)
                .eq(CollectionDirectoryDO::getUserId, userId));

        if (directoryDO == null) {
            return Response.fail("收藏夹不存在或不属于该用户");
        }

        // 2. 校验是否已经收藏过
        Long count = relMapper.selectCount(Wrappers.<CollectionArticleRelDO>lambdaQuery()
                .eq(CollectionArticleRelDO::getDirectoryId, directoryId)
                .eq(CollectionArticleRelDO::getArticleId, articleId));

        if (count > 0) {
            return Response.fail("该文章已在此收藏夹中");
        }

        // 3. 插入关联数据
        CollectionArticleRelDO relDO = CollectionArticleRelDO.builder()
                .directoryId(directoryId)
                .articleId(articleId)
                .userId(userId) // 使用传来的 userId
                .createTime(LocalDateTime.now())
                .build();

        relMapper.insert(relDO);
        return Response.success();
    }
    @Autowired
    private ArticleMapper articleMapper;
    /*
    * 查找收藏文章分页
    * */
    @Override
    public Response findCollectionArticlePageList(FindCollectionArticlePageListReqVO reqVO) {
        Long directoryId = reqVO.getDirectoryId();
        Long userId = reqVO.getUserId();

        // 1. 分页查询关联表
        Page<CollectionArticleRelDO> page = new Page<>(reqVO.getCurrent(), reqVO.getSize());
        LambdaQueryWrapper<CollectionArticleRelDO> wrapper = Wrappers.<CollectionArticleRelDO>lambdaQuery()
                .eq(CollectionArticleRelDO::getDirectoryId, directoryId)
                .eq(CollectionArticleRelDO::getUserId, userId)
                .orderByDesc(CollectionArticleRelDO::getCreateTime);

        Page<CollectionArticleRelDO> relPage = relMapper.selectPage(page, wrapper);
        List<CollectionArticleRelDO> relDOList = relPage.getRecords();

        if (CollectionUtils.isEmpty(relDOList)) {
            return Response.success(PageResponse.success(relPage, Collections.emptyList()));
        }

        //  提取文章 ID
        List<Long> articleIds = relDOList.stream()
                .map(CollectionArticleRelDO::getArticleId)
                .collect(Collectors.toList());

        // 批量查询文章，并转为 Map 方便后续按关联表顺序提取
        List<ArticleDO> articleDOList = articleMapper.selectList(Wrappers.<ArticleDO>lambdaQuery()
                .in(ArticleDO::getId, articleIds));

        Map<Long, ArticleDO> articleMap = articleDOList.stream()
                .collect(Collectors.toMap(ArticleDO::getId, a -> a));

        // 按照 relDOList 的顺序构建 VO
        List<FindCollectionArticlePageListRspVO> vos = relDOList.stream().map(rel -> {
            ArticleDO articleDO = articleMap.get(rel.getArticleId());
            if (articleDO == null) return null;

            return FindCollectionArticlePageListRspVO.builder()
                    .id(articleDO.getId())
                    .title(articleDO.getTitle())
                    .cover(articleDO.getCover())
                    .summary(articleDO.getSummary())
                    .createTime(articleDO.getCreateTime())
                    .build();
        }).filter(Objects::nonNull).collect(Collectors.toList());

        return Response.success(PageResponse.success(relPage, vos));
    }
}