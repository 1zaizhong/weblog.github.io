package com.zifengliu.weblog.web.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zifengliu.weblog.common.domain.dos.ArticleDO;
import com.zifengliu.weblog.common.domain.dos.ArticleTagRelDO;
import com.zifengliu.weblog.common.domain.dos.TagDO;
import com.zifengliu.weblog.common.domain.dos.UserDO;
import com.zifengliu.weblog.common.domain.mapper.ArticleMapper;
import com.zifengliu.weblog.common.domain.mapper.ArticleTagRelMapper;
import com.zifengliu.weblog.common.domain.mapper.TagMapper;
import com.zifengliu.weblog.common.domain.mapper.UserMapper;
import com.zifengliu.weblog.common.enums.ResponseCodeEnum;
import com.zifengliu.weblog.common.exception.BizException;
import com.zifengliu.weblog.common.utils.PageResponse;
import com.zifengliu.weblog.common.utils.Response;
import com.zifengliu.weblog.web.model.vo.tag.FindTagArticlePageListReqVO;
import com.zifengliu.weblog.web.model.vo.tag.FindTagArticlePageListRspVO;
import com.zifengliu.weblog.web.model.vo.tag.FindTagListReqVO;
import com.zifengliu.weblog.web.model.vo.tag.FindTagListRspVO;
import com.zifengliu.weblog.web.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private ArticleTagRelMapper articleTagRelMapper;
    @Autowired
    private ArticleMapper articleMapper;

    /**
     * 获取标签列表 (支持按用户 ID 过滤)
     */
    @Override
    public Response findTagList(FindTagListReqVO reqVO) {

        // 执行 SQL 过滤
        List<TagDO> tagDOS = tagMapper.selectList(Wrappers.<TagDO>lambdaQuery()
                .orderByDesc(TagDO::getArticlesTotal));

        // DO 转 VO
        List<FindTagListRspVO> vos = null;
        if (!CollectionUtils.isEmpty(tagDOS)) {
            vos = tagDOS.stream()
                    .map(tagDO -> FindTagListRspVO.builder()
                            .id(tagDO.getId())
                            .name(tagDO.getName())
                            .articlesTotal(tagDO.getArticlesTotal())
                            .build())
                    .collect(Collectors.toList());
        }

        return Response.success(vos);
    }
    @Override
    public PageResponse findTagPageList(FindTagArticlePageListReqVO findTagArticlePageListReqVO) {
        Long current = findTagArticlePageListReqVO.getCurrent();
        Long size = findTagArticlePageListReqVO.getSize();
        Long tagId = findTagArticlePageListReqVO.getId();

        // 1. 判断标签是否存在
        TagDO tagDO = tagMapper.selectById(tagId);
        if (Objects.isNull(tagDO)) {
            log.warn("==> 该标签不存在, tagId: {}", tagId);
            throw new BizException(ResponseCodeEnum.TAG_NOT_EXISTED);
        }

        // 2. 获取关联关系 (完全保留你原本的逻辑)
        List<ArticleTagRelDO> articleTagRelDOS = articleTagRelMapper.selectByTagId(tagId);

        if (CollectionUtils.isEmpty(articleTagRelDOS)) {
            log.info("==> 该标签下还未发布任何文章, tagId: {}", tagId);
            return PageResponse.success(new Page<>(current, size), null);
        }

        // 3. 提取文章 ID
        List<Long> articleIds = articleTagRelDOS.stream().map(ArticleTagRelDO::getArticleId).collect(Collectors.toList());

        // 4. 查询文章分页数据 (完全保留你原本的逻辑)
        Page<ArticleDO> page = articleMapper.selectPageListByArticleIds(current, size, articleIds);
        List<ArticleDO> articleDOS = page.getRecords();

        // 5. DO 转 VO (确保字段与 FindTagArticlePageListRspVO 匹配)
        List<FindTagArticlePageListRspVO> vos = null;
        if (!CollectionUtils.isEmpty(articleDOS)) {
            vos = articleDOS.stream().map(articleDO -> FindTagArticlePageListRspVO.builder()
                            .id(articleDO.getId())
                            .title(articleDO.getTitle())
                            .cover(articleDO.getCover())
                            .createDate(articleDO.getCreateTime().toLocalDate())
                            .build())
                    .collect(Collectors.toList());
        }

        return PageResponse.success(page, vos);
    }
}