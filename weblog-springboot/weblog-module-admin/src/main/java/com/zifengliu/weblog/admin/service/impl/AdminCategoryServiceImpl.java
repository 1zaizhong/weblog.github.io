package com.zifengliu.weblog.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zifengliu.weblog.admin.model.vo.article.FindArticlePageListRspVO;
import com.zifengliu.weblog.admin.model.vo.category.*;
import com.zifengliu.weblog.admin.service.AdminCategoryService;
import com.zifengliu.weblog.common.domain.dos.ArticleCategoryRelDO;
import com.zifengliu.weblog.common.domain.dos.ArticleDO;
import com.zifengliu.weblog.common.domain.dos.CategoryDO;
import com.zifengliu.weblog.common.domain.dos.UserDO;
import com.zifengliu.weblog.common.domain.mapper.*;
import com.zifengliu.weblog.common.enums.ResponseCodeEnum;
import com.zifengliu.weblog.common.exception.BizException;
import com.zifengliu.weblog.common.model.vo.SelectRspVO;
import com.zifengliu.weblog.common.utils.PageResponse;
import com.zifengliu.weblog.common.utils.Response;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/4/10 下午5:13
 * @description 分类的管理 实现对应service
 **/
@Slf4j
@Service
public class AdminCategoryServiceImpl  implements AdminCategoryService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private ArticleContentMapper articleContentMapper;
    @Autowired
    private ArticleCategoryRelMapper articleCategoryRelMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private ArticleTagRelMapper articleTagRelMapper;
    @Autowired
    private ApplicationEventPublisher eventPublisher;
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
    /*
    * 添加分类
    * @param addCategory
    * @return
    * */
    @Override
    public Response addCategory(AddCategoryReqVO addCategoryReqVO) {
        String name = addCategoryReqVO.getName();

        Long userId = getLoginUserId();
        if (!Objects.equals(userId, 1L)) {
            return Response.fail("权限不足：只有管理员可以创建新专栏");
        }
        // 校验：同一个用户下不能有同名的分类
        String categoryName = addCategoryReqVO.getName();
        CategoryDO categoryDO = categoryMapper.selectByName(categoryName);
        if (categoryDO != null) {
            throw new BizException(ResponseCodeEnum.CATEGORY_NAME_IS_EXISTED);
        }

        // 构建 DO 并保存
        CategoryDO insertCategoryDO = CategoryDO.builder()
                .name(categoryName)
                .userId(1L)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();

        categoryMapper.insert(insertCategoryDO);
        return Response.success();

    }

    /*
     * 分类分页数据查询
     * @param
     * @return
     * */
    @Override
    public PageResponse findCategoryPageList(FindCategoryPageListReqVO findCategoryPageListReqVO) {
        // 获取当前页、以及每页需要展示的数据数量
        Long current = findCategoryPageListReqVO.getCurrent();
        Long size = findCategoryPageListReqVO.getSize();
        String name  = findCategoryPageListReqVO.getName();
        LocalDate startDate = findCategoryPageListReqVO.getStartDate();
        LocalDate endDate = findCategoryPageListReqVO.getEndDate();
        Long userId=getLoginUserId();
         //判断是否是管理员
        Long filterUserId = Objects.equals(userId, 1L) ? null : userId;
        // 执行分页查询
        Page<CategoryDO> categoryDOPage = categoryMapper.
                selectPageList(current, size,  name, startDate, endDate,1L);

        List<CategoryDO> categoryDOS = categoryDOPage.getRecords();

        // DO 转 VO
        List<FindCategoryPageListRspVO> vos = null;
        if (!CollectionUtils.isEmpty(categoryDOS)) {
            vos = categoryDOS.stream()
                    .map(categoryDO -> FindCategoryPageListRspVO.builder()
                            .id(categoryDO.getId())
                            .name(categoryDO.getName())
                            .createTime(categoryDO.getCreateTime())
                            .articlesTotal(categoryDO.getArticlesTotal())
                            .build())
                    .collect(Collectors.toList());
        }

        return PageResponse.success(categoryDOPage, vos);
    }

    /*
    *删除文章分类
    * @param deleteCategoryReqVO
    * @return
    */
    @Override
    public Response deleteCategory(DeleteCategoryReqVO deleteCategoryReqVO) {
        // 分类 ID
        Long categoryId = deleteCategoryReqVO.getId();
        //用户id
        Long userId=getLoginUserId();
        // 1. 判断有无
        CategoryDO categoryDO = categoryMapper.selectById(categoryId);
        if (Objects.isNull(categoryDO)) {
            return Response.fail("分类不存在");
        }

        if (!Objects.equals(userId, 1L) && !Objects.equals(categoryDO.getUserId(), userId)) {
            log.warn("==> 用户 {} 尝试越权删除分类 {}", userId, categoryId);
            return Response.fail("无权删除他人创建的分类");
        }

        if (Objects.isNull(categoryDO)) {
            return Response.fail("分类不存在或无权操作");
        }

        // 2. 校验分类下是否有文章
        ArticleCategoryRelDO articleCategoryRelDO = articleCategoryRelMapper.selectOneByCategoryId(categoryId);
        if (Objects.nonNull(articleCategoryRelDO)) {
            throw new BizException(ResponseCodeEnum.CATEGORY_CAN_NOT_DELETE);
        }

        categoryMapper.deleteById(categoryId);
        return Response.success();
    }

    /*
    查询标签列表
    * @return
    * */
    @Override
    public Response findeCategorySelectList() {
        // 1. 获取当前登录用户 ID
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserDO userDO = userMapper.findByUsername(username);
        Long userId = userDO.getUserId();

        // 2. 仅查询属于该用户的分类 (且未删除)
        List<CategoryDO> categoryDOS = categoryMapper.selectList(Wrappers.<CategoryDO>lambdaQuery()
                .eq(CategoryDO::getUserId, 1L) // 关键过滤
                .eq(CategoryDO::getIsDeleted, false)
                .orderByDesc(CategoryDO::getCreateTime));

        //DO 转 VO
        List<SelectRspVO> selectRspVOS = null;
        //如果标签数据不为空
        if(!CollectionUtils.isEmpty(categoryDOS)){
            //将分类ID 作为value 值,将分类名称作为label展示
            selectRspVOS = categoryDOS.stream()
                    .map(categoryDO -> SelectRspVO.builder()
                            .label(categoryDO.getName())
                            .value(categoryDO.getId())
                            .build())
                    .collect(Collectors.toList());
        }
        return Response.success(selectRspVOS);
    }
    /**
     * 查询专栏（分类）下的文章列表
     */
    @Override
    public PageResponse findCategoryArticlePageList(FindCategoryArticlePageListReqVO reqVO) {
        // 1. 获取当前登录用户 ID
        Long loginUserId = getLoginUserId();

        // 2. 调用 ArticleMapper专栏查询方法
        Page<ArticleDO> articleDOPage = articleMapper.selectPageListByCategoryId(
                reqVO.getCurrent(),
                reqVO.getSize(),
                reqVO.getCategoryId(),
                loginUserId,
                articleCategoryRelMapper
        );

        List<ArticleDO> articleDOS = articleDOPage.getRecords();

        // 3. DO 转 VO
        List<FindArticlePageListRspVO> vos = null;
        if (!CollectionUtils.isEmpty(articleDOS)) {
            vos = articleDOS.stream()
                    .map(articleDO -> FindArticlePageListRspVO.builder()
                            .id(articleDO.getId())
                            .title(articleDO.getTitle())
                            .cover(articleDO.getCover())
                            .createTime(articleDO.getCreateTime())
                            .build())
                    .collect(Collectors.toList());
        }

        return PageResponse.success(articleDOPage, vos);
    }

}
