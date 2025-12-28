package com.zifengliu.weblog.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zifengliu.weblog.admin.model.vo.article.FindLikeArticlePageListRspVO;
import com.zifengliu.weblog.admin.service.ArticleLikeService;
import com.zifengliu.weblog.common.domain.dos.ArticleDO;
import com.zifengliu.weblog.common.domain.dos.ArticleLikeDO;
import com.zifengliu.weblog.common.domain.mapper.ArticleLikeMapper;
import com.zifengliu.weblog.common.domain.mapper.ArticleMapper;
import com.zifengliu.weblog.common.utils.PageResponse;
import com.zifengliu.weblog.common.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/28 下午3:21
 * @description
 **/
@Service
@Slf4j
public class AdminArticleLikeServiceImpl implements ArticleLikeService {
    @Autowired
    private ArticleLikeMapper articleLikeMapper;
    @Autowired
    private ArticleMapper articleMapper;
/*
* 查询点赞的文章分页
* */
    @Override
    public Response findUserLikePageList(Long userId, Long pageNo, Long pageSize) {
        // 分页查询该用户的点赞记录
        Page<ArticleLikeDO> page = new Page<>(pageNo, pageSize);
        LambdaQueryWrapper<ArticleLikeDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ArticleLikeDO::getUserId, userId)
                .orderByDesc(ArticleLikeDO::getCreateTime);

        articleLikeMapper.selectPage(page, wrapper);
        List<ArticleLikeDO> records = page.getRecords();

        // 组装 VO，关联查询文章信息
        List<FindLikeArticlePageListRspVO> vos = records.stream().map(like -> {
            ArticleDO article = articleMapper.selectById(like.getArticleId());
            return FindLikeArticlePageListRspVO.builder()
                    .articleId(like.getArticleId())
                    .title(article != null ? article.getTitle() : "文章已删除")
                    .cover(article != null ? article.getCover() : "")
                    .createTime(like.getCreateTime())
                    .build();
        }).collect(Collectors.toList());

        return PageResponse.success(page, vos);
    }
}
