package com.zifengliu.weblog.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zifengliu.weblog.admin.model.vo.collection.*;
import com.zifengliu.weblog.admin.service.AdminCollectionService;
import com.zifengliu.weblog.common.domain.dos.ArticleDO;
import com.zifengliu.weblog.common.domain.dos.CollectionArticleRelDO;
import com.zifengliu.weblog.common.domain.dos.CollectionDirectoryDO;
import com.zifengliu.weblog.common.domain.dos.UserDO;
import com.zifengliu.weblog.common.domain.mapper.ArticleMapper;
import com.zifengliu.weblog.common.domain.mapper.CollectionArticleRelMapper;
import com.zifengliu.weblog.common.domain.mapper.CollectionDirectoryMapper;
import com.zifengliu.weblog.common.domain.mapper.UserMapper;
import com.zifengliu.weblog.common.enums.ResponseCodeEnum;
import com.zifengliu.weblog.common.exception.BizException;
import com.zifengliu.weblog.common.utils.PageResponse;
import com.zifengliu.weblog.common.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/28 下午6:49
 * @description
 **/
@Service
@Slf4j
public class AdminCollectionServiceImpl implements AdminCollectionService {

    @Autowired
    private CollectionDirectoryMapper directoryMapper;
    @Autowired
    private CollectionArticleRelMapper relMapper;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private UserMapper userMapper;

    private Long getLoginUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDO userDO = userMapper.selectOne(Wrappers.<UserDO>lambdaQuery()
                .eq(UserDO::getUsername, authentication.getName()));
        if (userDO == null) {
            throw new BizException(ResponseCodeEnum.USER_NOT_FOUND);
        }
        return userDO.getUserId();
    }

    @Override
    public Response addDirectory(AddCollectionDirectoryReqVO reqVO) {
        Long userId = getLoginUserId();

        // 校验同名收藏夹
        Long count = directoryMapper.selectCount(Wrappers.<CollectionDirectoryDO>lambdaQuery()
                .eq(CollectionDirectoryDO::getUserId, userId)
                .eq(CollectionDirectoryDO::getName, reqVO.getName()));
        if (count > 0) {
            return Response.fail("该收藏夹名称已存在");
        }

        CollectionDirectoryDO directoryDO = CollectionDirectoryDO.builder()
                .name(reqVO.getName())
                .userId(userId)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();

        directoryMapper.insert(directoryDO);
        return Response.success();
    }

    @Override
    public PageResponse findDirectoryPageList(FindCollectionDirectoryPageListReqVO reqVO) {
        Long userId = getLoginUserId();
        Page<CollectionDirectoryDO> page = new Page<>(reqVO.getCurrent(), reqVO.getSize());

        directoryMapper.selectPage(page, Wrappers.<CollectionDirectoryDO>lambdaQuery()
                .eq(CollectionDirectoryDO::getUserId, userId)
                .orderByDesc(CollectionDirectoryDO::getCreateTime));

        return PageResponse.success(page, page.getRecords());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response deleteDirectory(DeleteCollectionDirectoryReqVO reqVO) {
        Long userId = getLoginUserId();
        Long dirId = reqVO.getId();

        // 1. 删除收藏夹本身（增加 userId 校验防止越权）
        int count = directoryMapper.delete(Wrappers.<CollectionDirectoryDO>lambdaQuery()
                .eq(CollectionDirectoryDO::getId, dirId)
                .eq(CollectionDirectoryDO::getUserId, userId));

        if (count > 0) {
            // 2. 删除该收藏夹下的所有关联关系
            relMapper.delete(Wrappers.<CollectionArticleRelDO>lambdaQuery()
                    .eq(CollectionArticleRelDO::getDirectoryId, dirId));
            return Response.success();
        }
        return Response.fail("收藏夹不存在或无权限删除");
    }

    @Override
    public PageResponse findCollectionArticlePageList(FindCollectionArticlePageListReqVO reqVO) {
        // 1. 参数校验
        if (reqVO.getDirectoryId() == null) {
           throw  new BizException(ResponseCodeEnum.Directory_NOT_FOUND);
        }
        Long current = reqVO.getCurrent();
        Long size = reqVO.getSize();
        Long userId = getLoginUserId();
        Long directoryId = reqVO.getDirectoryId();

        log.info("查询收藏夹文章，目录ID: {}, 用户ID: {}", directoryId, userId);
        System.out.println();

        // 1. 分页查询关联表
        Page<CollectionArticleRelDO> page = new Page<>(current, size);
        relMapper.selectPage(page, Wrappers.<CollectionArticleRelDO>lambdaQuery()
                .eq(CollectionArticleRelDO::getDirectoryId, directoryId)
                .eq(CollectionArticleRelDO::getUserId, userId)
                .orderByDesc(CollectionArticleRelDO::getCreateTime));
        // 拿到记录条数
        List<CollectionArticleRelDO> records = page.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return PageResponse.success(page, Collections.emptyList());
        }

        // 2. 转换为含有文章详情的 VO
        List<FindCollectionArticlePageListRspVO> vos = records.stream().map(rel -> {
            ArticleDO article = articleMapper.selectById(rel.getArticleId());
            return FindCollectionArticlePageListRspVO.builder()
                    .articleId(rel.getArticleId())
                    .title(article != null ? article.getTitle() : "文章已删除")
                    .cover(article != null ? article.getCover() : "")
                    .collectTime(rel.getCreateTime())
                    .build();
        }).collect(Collectors.toList());

        return PageResponse.success(page, vos);
    }

    @Override
    public Response removeArticleFromCollection(RemoveArticleFromCollectionReqVO reqVO) {
        Long userId = getLoginUserId();

        int count = relMapper.delete(Wrappers.<CollectionArticleRelDO>lambdaQuery()
                .eq(CollectionArticleRelDO::getDirectoryId, reqVO.getDirectoryId())
                .eq(CollectionArticleRelDO::getArticleId, reqVO.getArticleId())
                .eq(CollectionArticleRelDO::getUserId, userId));

        return count > 0 ? Response.success() : Response.fail("移除失败，记录不存在");
    }

    @Override
    public Response findCollectionDirectorySelectList() {
        Long userId = getLoginUserId();
        List<CollectionDirectoryDO> list = directoryMapper.selectList(Wrappers.<CollectionDirectoryDO>lambdaQuery()
                .eq(CollectionDirectoryDO::getUserId, userId)
                .select(CollectionDirectoryDO::getId, CollectionDirectoryDO::getName));
        return Response.success(list);
    }
}