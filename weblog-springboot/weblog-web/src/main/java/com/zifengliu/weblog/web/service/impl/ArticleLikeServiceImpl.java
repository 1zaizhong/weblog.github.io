package com.zifengliu.weblog.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zifengliu.weblog.common.domain.dos.ArticleDO;
import com.zifengliu.weblog.common.domain.dos.ArticleLikeDO;
import com.zifengliu.weblog.common.domain.mapper.ArticleLikeMapper;
import com.zifengliu.weblog.common.domain.mapper.ArticleMapper;
import com.zifengliu.weblog.common.utils.PageResponse;
import com.zifengliu.weblog.common.utils.Response;
import com.zifengliu.weblog.web.model.vo.like.CheckArticleLikedReqVO;
import com.zifengliu.weblog.web.model.vo.like.FindLikeArticlePageListReqVO;
import com.zifengliu.weblog.web.model.vo.like.FindLikeArticlePageListRspVO;
import com.zifengliu.weblog.web.model.vo.like.LikeArticleReqVO;
import com.zifengliu.weblog.web.service.ArticleLikeService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
 * @date 2025/12/28 下午3:31
 * @description
 **/
@Service
@Slf4j
public class ArticleLikeServiceImpl implements ArticleLikeService {
    @Autowired
    private ArticleLikeMapper articleLikeMapper;

    @Autowired
    private ArticleMapper articleMapper;
/**
* 点赞
 * param reqVO
 * */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response likeOrUnlikeArticle(LikeArticleReqVO reqVO) {
        Long userId = reqVO.getUserId();
        Long articleId = reqVO.getArticleId();

        // 检查 t_article_like 表中是否存在该用户对该文章的点赞记录
        LambdaQueryWrapper<ArticleLikeDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ArticleLikeDO::getArticleId, articleId)
                .eq(ArticleLikeDO::getUserId, userId);
        //查询有没有记录
        ArticleLikeDO existLike = articleLikeMapper.selectOne(wrapper);
        if (Objects.isNull(existLike)) {
            ArticleLikeDO likeDO = ArticleLikeDO.builder()
                    .articleId(articleId)
                    .userId(userId)
                    .createTime(LocalDateTime.now()) // 确保设置了创建时间
                    .build();
            articleLikeMapper.insert(likeDO);

            // 文章表点赞数 +1
            articleMapper.updateLikeNum(articleId, 1);
            return Response.success("点赞成功");
        } else {
            // 4已存在 -> 取消点赞
            articleLikeMapper.deleteById(existLike.getId());

            // 文章表点赞数 -1
            articleMapper.updateLikeNum(articleId, -1);
            return Response.success("取消点赞成功");
        }
    }

    /**检查是否点赞
     * @param reqVO
     * */
    @Override
    public Response checkIsLiked(CheckArticleLikedReqVO reqVO) {
        Long userId = reqVO.getUserId();
        // 如果用户未登录，直接返回未点赞
        if (Objects.isNull(userId)) {
            return Response.success(false);
        }

        // 查询数据库中是否存在该用户的点赞记录
        Long count = articleLikeMapper.selectCount(Wrappers.<ArticleLikeDO>lambdaQuery()
                .eq(ArticleLikeDO::getArticleId, reqVO.getArticleId())
                .eq(ArticleLikeDO::getUserId, userId));

        return Response.success(count > 0);
    }
    /**
     * 查找点赞列表
     * param reqVO
    * */
    @Override
    public Response findLikeArticlePageList(FindLikeArticlePageListReqVO reqVO) {
        Long userId = reqVO.getUserId();

        // 1. 分页查询点赞关联表 t_article_like
        Page<ArticleLikeDO> page = new Page<>(reqVO.getCurrent(), reqVO.getSize());
        LambdaQueryWrapper<ArticleLikeDO> wrapper = Wrappers.<ArticleLikeDO>lambdaQuery()
                .eq(ArticleLikeDO::getUserId, userId)
                .orderByDesc(ArticleLikeDO::getCreateTime); // 按点赞时间倒序

        Page<ArticleLikeDO> likePage = articleLikeMapper.selectPage(page, wrapper);
        List<ArticleLikeDO> likeDOList = likePage.getRecords();

        // 2. 判空处理
        if (CollectionUtils.isEmpty(likeDOList)) {
            return Response.success(PageResponse.success(likePage, Collections.emptyList()));
        }

        // 3. 提取文章 ID 集合
        List<Long> articleIds = likeDOList.stream()
                .map(ArticleLikeDO::getArticleId)
                .collect(Collectors.toList());

        // 4. 根据文章 ID 集合查询文章详情
        List<ArticleDO> articleDOList = articleMapper.selectList(Wrappers.<ArticleDO>lambdaQuery()
                .in(ArticleDO::getId, articleIds));

        // 5. 保持点赞顺序（Map转换）
        Map<Long, ArticleDO> articleMap = articleDOList.stream()
                .collect(Collectors.toMap(ArticleDO::getId, a -> a));

        // 6. DO 转 VO
        List<FindLikeArticlePageListRspVO> vos = likeDOList.stream().map(likeDO -> {
            ArticleDO articleDO = articleMap.get(likeDO.getArticleId());
            if (Objects.isNull(articleDO)) return null;

            return FindLikeArticlePageListRspVO.builder()
                    .id(articleDO.getId())
                    .title(articleDO.getTitle())
                    .cover(articleDO.getCover())
                    .summary(articleDO.getSummary())
                    .createTime(articleDO.getCreateTime())
                    .build();
        }).filter(Objects::nonNull).collect(Collectors.toList());

        return Response.success(PageResponse.success(likePage, vos));
    }
}
