package com.zifengliu.weblog.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zifengliu.weblog.common.domain.dos.ArticleLikeDO;
import com.zifengliu.weblog.common.domain.mapper.ArticleLikeMapper;
import com.zifengliu.weblog.common.domain.mapper.ArticleMapper;
import com.zifengliu.weblog.common.utils.Response;
import com.zifengliu.weblog.web.model.vo.like.CheckArticleLikedReqVO;
import com.zifengliu.weblog.web.model.vo.like.LikeArticleReqVO;
import com.zifengliu.weblog.web.service.ArticleLikeService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

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
}
