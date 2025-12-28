package com.zifengliu.weblog.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zifengliu.weblog.admin.model.vo.category.AddCategoryReqVO;
import com.zifengliu.weblog.admin.model.vo.category.DeleteCategoryReqVO;
import com.zifengliu.weblog.admin.model.vo.category.FindCategoryPageListReqVO;
import com.zifengliu.weblog.admin.model.vo.category.FindCategoryPageListRspVO;
import com.zifengliu.weblog.admin.service.AdminCategoryService;
import com.zifengliu.weblog.common.domain.dos.ArticleCategoryRelDO;
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

        // 校验：同一个用户下不能有同名的分类
        CategoryDO categoryDO = categoryMapper.selectOne(Wrappers.<CategoryDO>lambdaQuery()
                .eq(CategoryDO::getName, name)
                .eq(CategoryDO::getUserId, userId)); // 加上用户 ID 校验

        if (Objects.nonNull(categoryDO)) {
            log.warn("==> 该分类已存在: {}, userId: {}", name, userId);
            throw new BizException(ResponseCodeEnum.CATEGORY_NAME_IS_EXISTED);
        }

        // 构建 DO 并保存
        CategoryDO insertCategoryDO = CategoryDO.builder()
                .name(name.trim())
                .userId(userId) // 设置所属用户
                .build();

        categoryMapper.insert(insertCategoryDO);
        return Response.success();

    }

    /*
     * 分类分页数据查询
     * @param findCategoryPageListReqVO
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
                selectPageList(current, size,  name, startDate, endDate,filterUserId);

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
                .eq(CategoryDO::getUserId, userId) // 关键过滤
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

}
