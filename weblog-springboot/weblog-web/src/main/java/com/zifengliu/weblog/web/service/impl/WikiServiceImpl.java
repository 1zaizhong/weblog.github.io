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
        Long loginUserId = findWikiCatalogListReqVO.getUserId();
        Long userId = Objects.equals(loginUserId, 1L) ? null : loginUserId;
        // 1.获取该知识库下所有目录数据
        List<WikiCatalogDO> catalogDOs = wikiCatalogMapper.selectByWikiId(wikiId);
        if (CollectionUtils.isEmpty(catalogDOs)) {
            return Response.success();
        }
    // 2. 找到这些目录关联的所有文章 ID
        List<Long> articleIds = catalogDOs.stream()
                .map(WikiCatalogDO::getArticleId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        List<Long> canAccessArticleIds = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(articleIds)) {
            canAccessArticleIds = articleMapper.selectList(Wrappers.<ArticleDO>lambdaQuery()
                            .in(ArticleDO::getId, articleIds)
                            .and(w -> {
                                w.eq(ArticleDO::getStatus, 2); 
                                if (loginUserId != null) {
                                    w.or().eq(ArticleDO::getUserId, loginUserId);
                                }
                            }))
                    .stream().map(ArticleDO::getId).collect(Collectors.toList());
        }
        // DO 转 VO
        List<FindWikiCatalogListRspVO> vos = null;
        if (!CollectionUtils.isEmpty(catalogDOs)) {
            vos = Lists.newArrayList();

            // 过滤出一级目录，并按 sort 值升序排列
            List<WikiCatalogDO> level1Catalogs = catalogDOs.stream()
                    .filter(catalogDO -> Objects.equals(catalogDO.getLevel(), WikiCatalogLevelEnum.ONE.getValue())) // 过滤
                    .sorted(Comparator.comparing(WikiCatalogDO::getSort)) // 升序
                    .collect(Collectors.toList());

            // 构造 VO 对象, 并添加到 vos 集合中
            for (WikiCatalogDO level1Catalog : level1Catalogs) {
                vos.add(FindWikiCatalogListRspVO.builder()
                        .id(level1Catalog.getId())
                        .articleId(level1Catalog.getArticleId())
                        .title(level1Catalog.getTitle())
                        .level(level1Catalog.getLevel())
                        .build());
            }

            // 循环 vos 集合，开始构造二级目录数据
            vos.forEach(level1Catalog -> {
                // 一级目录的 ID
                Long parentId = level1Catalog.getId();

                // 过滤出当前一级目录下的子目录，并按照 sort 值升序排列
                List<WikiCatalogDO> level2CatalogDOS = catalogDOs.stream()
                        .filter(catalogDO -> Objects.equals(catalogDO.getParentId(), parentId)
                                && Objects.equals(catalogDO.getLevel(), WikiCatalogLevelEnum.TWO.getValue()))
                        .sorted(Comparator.comparing(WikiCatalogDO::getSort))
                        .collect(Collectors.toList());

                // 设置子目录数据到 children 字段中
                if (!CollectionUtils.isEmpty(level2CatalogDOS)) {
                    List<FindWikiCatalogListRspVO> level2Catalogs = level2CatalogDOS.stream()
                            .map(catalogDO -> FindWikiCatalogListRspVO.builder()
                                    .id(catalogDO.getId())
                                    .articleId(catalogDO.getArticleId())
                                    .title(catalogDO.getTitle())
                                    .level(catalogDO.getLevel())
                                    .build())
                            .collect(Collectors.toList());
                    level1Catalog.setChildren(level2Catalogs);
                }
            });
        }

        return Response.success(vos);
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
