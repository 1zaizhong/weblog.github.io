package com.zifengliu.weblog.web.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.zifengliu.weblog.common.domain.dos.*;
import com.zifengliu.weblog.common.domain.mapper.*;
import com.zifengliu.weblog.common.utils.PageResponse;
import com.zifengliu.weblog.common.utils.Response;
import com.zifengliu.weblog.web.convert.ArticleConvert;
import com.zifengliu.weblog.web.model.vo.article.FindIndexArticlePageListReqVO;
import com.zifengliu.weblog.web.model.vo.article.FindIndexArticlePageListRspVO;
import com.zifengliu.weblog.web.model.vo.category.FindCategoryListRspVO;
import com.zifengliu.weblog.web.model.vo.tag.FindTagListRspVO;
import com.zifengliu.weblog.web.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @date:  2025-09-15 14:03
 * @description: 文章
 **/
@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ArticleCategoryRelMapper articleCategoryRelMapper;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private ArticleTagRelMapper articleTagRelMapper;

    /**
     * 获取首页文章分页数据
     * @param findIndexArticlePageListReqVO
     * @return
     */
    @Override
    public Response findArticlePageList(FindIndexArticlePageListReqVO findIndexArticlePageListReqVO) {
        Long current = findIndexArticlePageListReqVO.getCurrent();
        Long size = findIndexArticlePageListReqVO.getSize();

        //1; 获取分页查询主体记录
        Page<ArticleDO> articleDOPage = articleMapper.selectPageList(current, size, null, null, null);

        //返回的分页数据
        List<ArticleDO> articleDOS = articleDOPage.getRecords();


        List<FindIndexArticlePageListRspVO>  rspVOS = null;
        if(!CollectionUtils.isEmpty(articleDOS)) {
            //文章DO 转 VO
            rspVOS =  articleDOS.stream()
                    .map(articleDO -> ArticleConvert.INSTANCE.convertDO2VO(articleDO))
                    .collect(Collectors.toList());
        }
        //拿到全部的文章的ID 集合
        List<Long>  articleIds = articleDOS.stream().map(ArticleDO::getId).collect(Collectors.toList());

        //2; 设置文章所属分类
        //查询全部分类
        List<CategoryDO> categoryDOS = categoryMapper.selectList(Wrappers.emptyWrapper());
        //转 map ,方便后续根据分类 ID 拿到对应的分类名称
        Map<Long, String> categoryNameMap = categoryDOS.stream().collect(Collectors.toMap(CategoryDO::getId, CategoryDO::getName));

        //根据文章ID p批量查询全部的分类关联记录
        List<ArticleCategoryRelDO> articleCategoryRelDOS = articleCategoryRelMapper.selectByArticleIds(articleIds);

         rspVOS.forEach( vo -> {
             Long currArticleId  = vo.getId();
             //过滤当前文章对应的关联数据
             Optional<ArticleCategoryRelDO> optional = articleCategoryRelDOS.stream().filter(rel -> Objects.equals(rel.getArticleId(), currArticleId)).findAny();

         });

        return  null;
    }
}
