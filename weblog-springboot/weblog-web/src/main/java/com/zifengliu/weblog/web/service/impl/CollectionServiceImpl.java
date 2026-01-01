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
import com.zifengliu.weblog.web.model.vo.collection.CollectArticleReqVO;
import com.zifengliu.weblog.web.model.vo.collection.FindCollectionArticlePageListReqVO;
import com.zifengliu.weblog.web.model.vo.collection.FindCollectionArticlePageListRspVO;
import com.zifengliu.weblog.web.model.vo.collection.FindCollectionDirectoryReqVO;
import com.zifengliu.weblog.web.service.WebCollectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
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

    @Override
    public Response findCollectionDirectoryList(FindCollectionDirectoryReqVO reqVO) {
        // 根据前端传来的 userId 查询
        List<CollectionDirectoryDO> list = directoryMapper.selectList(Wrappers.<CollectionDirectoryDO>lambdaQuery()
                .eq(CollectionDirectoryDO::getUserId, reqVO.getUserId())
                .select(CollectionDirectoryDO::getId, CollectionDirectoryDO::getName)
                .orderByDesc(CollectionDirectoryDO::getCreateTime));

        return Response.success(list);
    }

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

        // 1. 分页查询关联表，获取文章 ID 集合
        Page<CollectionArticleRelDO> page = new Page<>(reqVO.getCurrent(), reqVO.getSize());
        LambdaQueryWrapper<CollectionArticleRelDO> wrapper = Wrappers.<CollectionArticleRelDO>lambdaQuery()
                .eq(CollectionArticleRelDO::getDirectoryId, directoryId)
                .eq(CollectionArticleRelDO::getUserId, userId)
                .orderByDesc(CollectionArticleRelDO::getCreateTime);

        Page<CollectionArticleRelDO> relPage = relMapper.selectPage(page, wrapper);
        List<CollectionArticleRelDO> relDOList = relPage.getRecords();

        // 2. 如果收藏夹是空的，直接返回
        if (CollectionUtils.isEmpty(relDOList)) {
            return Response.success(PageResponse.success(relPage, Collections.emptyList()));
        }

        // 3. 提取文章 ID 集合
        List<Long> articleIds = relDOList.stream()
                .map(CollectionArticleRelDO::getArticleId)
                .collect(Collectors.toList());

        // 4. 根据文章 ID 集合查询文章详情
        // 注意：这里要保持关联表的排序，或者根据 ID 集合批量查询
        List<ArticleDO> articleDOList = articleMapper.selectList(Wrappers.<ArticleDO>lambdaQuery()
                .in(ArticleDO::getId, articleIds));

        // 5. DO 转 VO (这里建议使用 Map 转换以保证顺序正确)
        List<FindCollectionArticlePageListRspVO> vos = articleDOList.stream().map(articleDO -> {
            return FindCollectionArticlePageListRspVO.builder()
                    .id(articleDO.getId())
                    .title(articleDO.getTitle())
                    .cover(articleDO.getCover())
                    .summary(articleDO.getSummary())
                    .createTime(articleDO.getCreateTime())
                    .build();
        }).collect(Collectors.toList());

        return Response.success(PageResponse.success(relPage, vos));
    }
}