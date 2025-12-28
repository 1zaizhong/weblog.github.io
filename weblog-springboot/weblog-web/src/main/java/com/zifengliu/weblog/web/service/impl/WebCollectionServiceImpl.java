package com.zifengliu.weblog.web.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zifengliu.weblog.common.domain.dos.CollectionArticleRelDO;
import com.zifengliu.weblog.common.domain.dos.CollectionDirectoryDO;
import com.zifengliu.weblog.common.domain.mapper.CollectionArticleRelMapper;
import com.zifengliu.weblog.common.domain.mapper.CollectionDirectoryMapper;
import com.zifengliu.weblog.common.utils.Response;
import com.zifengliu.weblog.web.model.vo.collection.CollectArticleReqVO;
import com.zifengliu.weblog.web.model.vo.collection.FindCollectionDirectoryReqVO;
import com.zifengliu.weblog.web.service.WebCollectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/28 下午8:46
 * @description  收藏夹服务实现类
 **/
@Service
@Slf4j
public class WebCollectionServiceImpl implements WebCollectionService {

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
}