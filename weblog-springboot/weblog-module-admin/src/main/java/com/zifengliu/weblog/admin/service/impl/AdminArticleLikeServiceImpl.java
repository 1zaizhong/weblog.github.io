package com.zifengliu.weblog.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zifengliu.weblog.admin.model.vo.like.CancelLikeArticleReqVO;
import com.zifengliu.weblog.admin.model.vo.like.FindLikeArticlePageListReqVO;
import com.zifengliu.weblog.admin.model.vo.article.FindLikeArticlePageListRspVO;
import com.zifengliu.weblog.admin.service.ArticleLikeService;
import com.zifengliu.weblog.common.domain.dos.ArticleDO;
import com.zifengliu.weblog.common.domain.dos.ArticleLikeDO;
import com.zifengliu.weblog.common.domain.dos.UserDO;
import com.zifengliu.weblog.common.domain.mapper.ArticleLikeMapper;
import com.zifengliu.weblog.common.domain.mapper.ArticleMapper;
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
    @Autowired
    private UserMapper userMapper;

    //拿到登录信息id,用来后面用
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
    public PageResponse findUserLikePageList(FindLikeArticlePageListReqVO reqVO) {
        // 1. 从 SecurityContext 中获取当前登录用户 ID
        Long userId = getLoginUserId();

        // 2. 分页查询点赞记录
        Page<ArticleLikeDO> page = new Page<>(reqVO.getCurrent(), reqVO.getSize());
        LambdaQueryWrapper<ArticleLikeDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ArticleLikeDO::getUserId, userId)
                .orderByDesc(ArticleLikeDO::getCreateTime);

        articleLikeMapper.selectPage(page, wrapper);
        List<ArticleLikeDO> records = page.getRecords();

        // 3. 组装 VO
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response deleteLike(CancelLikeArticleReqVO reqVO) {
        Long userId = getLoginUserId();
        Long articleId = reqVO.getArticleId();

        // 1. 删除点赞记录（双重条件确保只能删除自己的）
        LambdaQueryWrapper<ArticleLikeDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ArticleLikeDO::getUserId, userId)
                .eq(ArticleLikeDO::getArticleId, articleId);

        int count = articleLikeMapper.delete(wrapper);

        // 2. 更新文章表点赞数
        if (count > 0) {
            articleMapper.updateLikeNum(articleId, -1);
            return Response.success("取消点赞成功");
        }

        return Response.fail("取消点赞失败，记录不存在");
    }
}