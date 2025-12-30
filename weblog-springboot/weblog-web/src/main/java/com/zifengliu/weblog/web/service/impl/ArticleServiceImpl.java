package com.zifengliu.weblog.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.zifengliu.weblog.admin.event.ReadArticleEvent;
import com.zifengliu.weblog.common.domain.dos.*;
import com.zifengliu.weblog.common.domain.mapper.*;
import com.zifengliu.weblog.common.exception.BizException;
import com.zifengliu.weblog.common.utils.PageResponse;
import com.zifengliu.weblog.common.utils.Response;
import com.zifengliu.weblog.common.enums.ResponseCodeEnum;
import com.zifengliu.weblog.web.convert.ArticleConvert;
import com.zifengliu.weblog.web.markdown.MarkdownHelper;
import com.zifengliu.weblog.web.model.vo.article.*;
import com.zifengliu.weblog.web.model.vo.category.FindCategoryListRspVO;
import com.zifengliu.weblog.web.model.vo.tag.FindTagListRspVO;
import com.zifengliu.weblog.web.service.ArticleService;
import com.zifengliu.weblog.web.utils.MarkdownStatsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author: 粟英朝
 *  
 * @date:  2025-10-15 14:03
 * @description: 文章
 **/
@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private ArticleContentMapper articleContentMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ArticleCategoryRelMapper articleCategoryRelMapper;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private ArticleTagRelMapper articleTagRelMapper;
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    /**
     * 获取首页文章分页数据
     *
     * @param findIndexArticlePageListReqVO
     * @return
     */
    @Override
    public PageResponse findArticlePageList(FindIndexArticlePageListReqVO findIndexArticlePageListReqVO) {
        // 1. 获取分页参数
        Long current = findIndexArticlePageListReqVO.getCurrent();
        Long size = findIndexArticlePageListReqVO.getSize();
        //y用户id
        Long UserId = findIndexArticlePageListReqVO.getUserId();
        // 显式创建 Page 对象，MyBatis Plus 需要这个对象来承载分页结果和 Total 总数
        Page<ArticleDO> page = new Page<>(findIndexArticlePageListReqVO.getCurrent(), findIndexArticlePageListReqVO.getSize());
        LambdaQueryWrapper<ArticleDO> wrapper = new LambdaQueryWrapper<>();
      //查询条件：根据用户或者公布
        if (Objects.nonNull(UserId)) {
            // 使用 .and(w -> ...) 确保 OR 逻辑被括号包裹，不干扰其他查询条件
            wrapper.and(w -> w.eq(ArticleDO::getStatus, 2) // 2 为已发布
                    .or()
                    .eq(ArticleDO::getUserId, UserId));
        } else {
            // 如果前端没传 userId（用户未登录），则只能看到已发布的文章
            wrapper.eq(ArticleDO::getStatus, 2);
        }
        //倒叙
        wrapper.orderByDesc(ArticleDO::getWeight)
                .orderByDesc(ArticleDO::getCreateTime);

        // 2. 分页查询文章主体记录
      //必须确保第一个参数是 Page 对象
        Page<ArticleDO> articleDOPage =   articleMapper.selectPage(page, wrapper);

        // 获取当前页的记录列表
        List<ArticleDO> articleDOS = articleDOPage.getRecords();

        List<FindIndexArticlePageListRspVO> vos = null;
        if (!CollectionUtils.isEmpty(articleDOS)) {
            // 第一步：文章 DO 转 VO
            vos = articleDOS.stream()
                    .map(articleDO -> {
                        FindIndexArticlePageListRspVO vo = ArticleConvert.INSTANCE.convertDO2VO(articleDO);
                        // 处理置顶逻辑 (weight > 0 则为置顶)
                        vo.setIsTop(articleDO.getWeight() != null && articleDO.getWeight() > 0);
                        return vo;
                    })
                    .collect(Collectors.toList());

            // 拿到所有文章的 ID 集合，用于后续批量查询分类和标签
            List<Long> articleIds = articleDOS.stream().map(ArticleDO::getId).collect(Collectors.toList());

            // 第二步：批量设置文章所属分类
            List<CategoryDO> categoryDOS = categoryMapper.selectList(Wrappers.emptyWrapper());
            Map<Long, String> categoryIdNameMap = categoryDOS.stream()
                    .collect(Collectors.toMap(CategoryDO::getId, CategoryDO::getName));

            List<ArticleCategoryRelDO> articleCategoryRelDOS = articleCategoryRelMapper.selectByArticleIds(articleIds);

            vos.forEach(vo -> {
                Long currArticleId = vo.getId();
                articleCategoryRelDOS.stream()
                        .filter(rel -> Objects.equals(rel.getArticleId(), currArticleId))
                        .findAny()
                        .ifPresent(rel -> {
                            Long categoryId = rel.getCategoryId();
                            vo.setCategory(FindCategoryListRspVO.builder()
                                    .id(categoryId)
                                    .name(categoryIdNameMap.get(categoryId))
                                    .build());
                        });
            });

            // 第三步：批量设置文章标签
            List<TagDO> tagDOS = tagMapper.selectList(Wrappers.emptyWrapper());
            Map<Long, String> tagIdNameMap = tagDOS.stream()
                    .collect(Collectors.toMap(TagDO::getId, TagDO::getName));

            List<ArticleTagRelDO> articleTagRelDOS = articleTagRelMapper.selectByArticleIds(articleIds);
            vos.forEach(vo -> {
                Long currArticleId = vo.getId();
                List<FindTagListRspVO> findTagListRspVOS = articleTagRelDOS.stream()
                        .filter(rel -> Objects.equals(rel.getArticleId(), currArticleId))
                        .map(rel -> FindTagListRspVO.builder()
                                .id(rel.getTagId())
                                .name(tagIdNameMap.get(rel.getTagId()))
                                .build())
                        .collect(Collectors.toList());
                vo.setTags(findTagListRspVOS);
            });
        }


        return PageResponse.success(articleDOPage, vos);
    }

    /**
     * 获取文章详情
     *
     * @param findArticleDetailReqVO
     * @return
     */
    @Override
    public Response findArticleDetail(FindArticleDetailReqVO findArticleDetailReqVO) {
        Long loginUserId = findArticleDetailReqVO.getUserId();
        Long articleId = findArticleDetailReqVO.getArticleId();

        ArticleDO articleDO = articleMapper.selectById(articleId);

        Long currentUserId = findArticleDetailReqVO.getUserId();

        // 判断文章是否存在
        if (articleDO == null) {
            throw new BizException(ResponseCodeEnum.ARTICLE_NOT_EXISTED);
        }
          // 权限校验逻辑
        if (!Objects.equals(articleDO.getStatus(), 2)
                && !Objects.equals(loginUserId, articleDO.getUserId())) {
            log.warn("==> 用户无权查看该私密文章, articleId: {}, loginUserId: {}", articleId, loginUserId);
            throw new BizException(ResponseCodeEnum.UNAUTHORIZED);
        }

        // 查询正文
        ArticleContentDO articleContentDO = articleContentMapper.selectByArticleId(articleId);
        String content = articleContentDO.getContent();

        // 计算 md 正文字数
        Integer totalWords = MarkdownStatsUtil.calculateWordCount(content);

        // DO 转 VO
        FindArticleDetailRspVO vo = FindArticleDetailRspVO.builder()
                .title(articleDO.getTitle())
                .createTime(articleDO.getCreateTime())
                .content(MarkdownHelper.convertMarkdown2Html(content))
                .readNum(articleDO.getReadNum())
                .totalWords(totalWords)
                .readTime(MarkdownStatsUtil.calculateReadingTime(totalWords))
                .updateTime(articleDO.getUpdateTime())
                .likeNum(articleDO.getLikeNum())
                .build();

        // 查询所属分类
        ArticleCategoryRelDO articleCategoryRelDO = articleCategoryRelMapper.selectByArticleId(articleId);
        CategoryDO categoryDO = categoryMapper.selectById(articleCategoryRelDO.getCategoryId());
        vo.setCategoryId(categoryDO.getId());
        vo.setCategoryName(categoryDO.getName());

        // 查询标签
        List<ArticleTagRelDO> articleTagRelDOS = articleTagRelMapper.selectByArticleId(articleId);
        List<Long> tagIds = articleTagRelDOS.stream().map(ArticleTagRelDO::getTagId).collect(Collectors.toList());
        List<TagDO> tagDOS = tagMapper.selectByIds(tagIds);

        // 标签 DO 转 VO
        List<FindTagListRspVO> tagVOS = tagDOS.stream()
                .map(tagDO -> FindTagListRspVO.builder().id(tagDO.getId()).name(tagDO.getName()).build())
                .collect(Collectors.toList());
        vo.setTags(tagVOS);

        Long userId = articleDO.getUserId();
        // 上一篇
        ArticleDO preArticleDO = articleMapper.selectPreArticle(articleId, userId); // 传入 userId
        if (Objects.nonNull(preArticleDO)) {
            FindPreNextArticleRspVO preArticleVO = FindPreNextArticleRspVO.builder()
                    .articleId(preArticleDO.getId())
                    .articleTitle(preArticleDO.getTitle())
                    .build();
            vo.setPreArticle(preArticleVO);
        }

        // 下一篇
        ArticleDO nextArticleDO = articleMapper.selectNextArticle(articleId, userId); // 传入 userId
        if (Objects.nonNull(nextArticleDO)) {
            FindPreNextArticleRspVO nextArticleVO = FindPreNextArticleRspVO.builder()
                    .articleId(nextArticleDO.getId())
                    .articleTitle(nextArticleDO.getTitle())
                    .build();
            vo.setNextArticle(nextArticleVO);
        }

          // 发布文章阅读事件
        eventPublisher.publishEvent(new ReadArticleEvent(this, articleId));
        return Response.success(vo);
    }

}