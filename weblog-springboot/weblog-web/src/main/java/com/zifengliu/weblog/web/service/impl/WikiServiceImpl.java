package com.zifengliu.weblog.web.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.zifengliu.weblog.common.domain.dos.ArticleDO;
import com.zifengliu.weblog.common.domain.dos.WikiCatalogDO;
import com.zifengliu.weblog.common.domain.dos.WikiDO;
import com.zifengliu.weblog.common.domain.mapper.ArticleMapper;
import com.zifengliu.weblog.common.domain.mapper.WikiCatalogMapper;
import com.zifengliu.weblog.common.domain.mapper.WikiMapper;
import com.zifengliu.weblog.common.enums.WikiCatalogLevelEnum;
import com.zifengliu.weblog.common.utils.Response;
import com.zifengliu.weblog.web.model.vo.article.FindPreNextArticleRspVO;
import com.zifengliu.weblog.web.model.vo.wiki.*;
import com.zifengliu.weblog.web.service.WikiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/21 下午8:04
 * @description
 **/
@Service
@Slf4j
public class WikiServiceImpl implements WikiService {
    @Autowired
    private WikiMapper wikiMapper;
    @Autowired
    private WikiCatalogMapper wikiCatalogMapper;
    @Autowired
   private ArticleMapper articleMapper;
    /**

     * @return
     */
    @Override
    public Response findWikiList(FindWikiListReqVO findWikiListReqVO) {
        //用户id
        Long userId = findWikiListReqVO.getUserId();

        // 管理员逻辑：ID 为 1 则查全量
        if (userId != null && userId == 1L) {
            userId = null;
        }
        // 查询已发布的知识库
        List<WikiDO> wikiDOS = wikiMapper.selectList(Wrappers.<WikiDO>lambdaQuery()
                .eq(WikiDO::getIsPublish, true)
                .eq(Objects.nonNull(userId), WikiDO::getUserId, userId)
                .orderByDesc(WikiDO::getWeight)
                .orderByDesc(WikiDO::getCreateTime));

        if (CollectionUtils.isEmpty(wikiDOS)) {
            return Response.success(Lists.newArrayList());
        }
        // DO 转 VO
        List<FindWikiListRspVO> vos = null;
        if (!CollectionUtils.isEmpty(wikiDOS)) {
            vos = wikiDOS.stream()
                    .map(wikiDO -> FindWikiListRspVO.builder()
                            .id(wikiDO.getId())
                            .title(wikiDO.getTitle())
                            .cover(wikiDO.getCover())
                            .summary(wikiDO.getSummary())
                            .isTop(wikiDO.getWeight() > 0)
                            .build())
                    .collect(Collectors.toList());

            // 设置每个知识库的第一篇文章 ID，方便前端跳转
            vos.forEach(vo -> {
                Long wikiId = vo.getId();
                WikiCatalogDO wikiCatalogDO = wikiCatalogMapper.selectFirstArticleId(wikiId);
                vo.setFirstArticleId(Objects.nonNull(wikiCatalogDO) ? wikiCatalogDO.getArticleId() : null);
            });
        }

        return Response.success(vos);
    }
    /**
     * 获取知识库目录
     *
     * @param findWikiCatalogListReqVO
     * @return
     */

    @Override
    public Response findWikiCatalogList(FindWikiCatalogListReqVO findWikiCatalogListReqVO) {
        Long wikiId = findWikiCatalogListReqVO.getId();
        Long loginUserId = findWikiCatalogListReqVO.getUserId(); // 获取传入的用户ID

        // 1. 查询所有目录
        List<WikiCatalogDO> catalogDOs = wikiCatalogMapper.selectByWikiId(wikiId);
        if (CollectionUtils.isEmpty(catalogDOs)) return Response.success();

        // 2. 获取关联的文章ID
        List<Long> articleIds = catalogDOs.stream()
                .map(WikiCatalogDO::getArticleId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // 3. 核心：查出“可见”的文章ID集合（公开的 或 自己的）
        List<Long> visibleArticleIds = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(articleIds)) {
            visibleArticleIds = articleMapper.selectList(Wrappers.<ArticleDO>lambdaQuery()
                    .in(ArticleDO::getId, articleIds)
                    .and(w -> w.eq(ArticleDO::getStatus, 2)
                            .or(Objects.nonNull(loginUserId), i -> i.eq(ArticleDO::getUserId, loginUserId)))
            ).stream().map(ArticleDO::getId).collect(Collectors.toList());
        }

        // 4. 组装目录树（只过滤掉既不公开也不属于自己的文章目录）
        List<FindWikiCatalogListRspVO> catalogVOS = Lists.newArrayList();

        // 筛选一级目录
        List<WikiCatalogDO> level1Catalogs = catalogDOs.stream()
                .filter(c -> Objects.equals(c.getLevel(), WikiCatalogLevelEnum.ONE.getValue()))
                .sorted(Comparator.comparing(WikiCatalogDO::getSort))
                .collect(Collectors.toList());

        for (WikiCatalogDO c1 : level1Catalogs) {
            // 如果关联了文章且不可见，则跳过
            if (c1.getArticleId() != null && !visibleArticleIds.contains(c1.getArticleId())) continue;

            FindWikiCatalogListRspVO v1 = FindWikiCatalogListRspVO.builder()
                    .id(c1.getId()).articleId(c1.getArticleId()).title(c1.getTitle()).level(c1.getLevel()).build();

            // 筛选二级目录
            List<FindWikiCatalogListRspVO> children = catalogDOs.stream()
                    .filter(c2 -> Objects.equals(c2.getParentId(), c1.getId()))
                    .filter(c2 -> c2.getArticleId() == null || visibleArticleIds.contains(c2.getArticleId()))
                    .map(c2 -> FindWikiCatalogListRspVO.builder()
                            .id(c2.getId()).articleId(c2.getArticleId()).title(c2.getTitle()).level(c2.getLevel()).build())
                    .collect(Collectors.toList());

            v1.setChildren(children);
            catalogVOS.add(v1);
        }

        return Response.success(catalogVOS);
    }
    /**
     * 获取上下篇文章
     *     * 获取知识库
     *      *根据知识库 ID 和文章 ID, 查询出当前文章在所属知识库的目录记录；
     *      * 根据其目录 ID 查询出其上下篇目录记录；
     *      * 分别进行 DO 转 VO 操作；
     * @param findWikiArticlePreNextReqVO
     * @return
     */
    @Override
    public Response findArticlePreNext(FindWikiArticlePreNextReqVO findWikiArticlePreNextReqVO) {
        // 知识库 ID
        Long wikiId = findWikiArticlePreNextReqVO.getId();
        //用户id
        Long userId = findWikiArticlePreNextReqVO.getUserId();
        // 文章 ID
        Long articleId = findWikiArticlePreNextReqVO.getArticleId();

        FindWikiArticlePreNextRspVO vo = new FindWikiArticlePreNextRspVO();
        // 获取当前文章所属知识库的目录
        WikiCatalogDO wikiCatalogDO = wikiCatalogMapper.selectByWikiIdAndArticleId(wikiId, articleId);

        // 构建上一篇文章 VO
        WikiCatalogDO preArticleDO = wikiCatalogMapper.selectPreArticle(wikiId, wikiCatalogDO.getId());
        if (Objects.nonNull(preArticleDO)) {
            FindPreNextArticleRspVO preArticleVO = FindPreNextArticleRspVO.builder()
                    .articleId(preArticleDO.getArticleId())
                    .articleTitle(preArticleDO.getTitle())
                    .build();
            vo.setPreArticle(preArticleVO);
        }

        // 构建下一篇文章 VO
        WikiCatalogDO nextArticleDO = wikiCatalogMapper.selectNextArticle(wikiId, wikiCatalogDO.getId());
        if (Objects.nonNull(nextArticleDO)) {
            FindPreNextArticleRspVO nextArticleVO = FindPreNextArticleRspVO.builder()
                    .articleId(nextArticleDO.getArticleId())
                    .articleTitle(nextArticleDO.getTitle())
                    .build();
            vo.setNextArticle(nextArticleVO);
        }

        return Response.success(vo);
    }

}
