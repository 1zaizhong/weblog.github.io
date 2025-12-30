package com.zifengliu.weblog.admin.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zifengliu.weblog.admin.model.vo.user.*;
import com.zifengliu.weblog.admin.service.AdminUserService;
import com.zifengliu.weblog.common.domain.dos.*;
import com.zifengliu.weblog.common.domain.mapper.*;

import com.zifengliu.weblog.common.enums.ResponseCodeEnum;
import com.zifengliu.weblog.common.exception.BizException;
import com.zifengliu.weblog.common.utils.PageResponse;
import com.zifengliu.weblog.common.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/3/27 下午7:13
 * @description
 **/
@Slf4j
@Service
public class AdminUserServiceImpl implements AdminUserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private WikiMapper wikiMapper;
    @Autowired
    private CollectionDirectoryMapper directoryMapper;
    @Autowired
    private CollectionArticleRelMapper relMapper;
    @Autowired
    private  ArticleLikeMapper articleLikeMapper;
    @Autowired
    private ArticleContentMapper articleContentMapper;

    //nado当前登录的用户id
    private Long getLoginUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDO userDO = userMapper.selectOne(Wrappers.<UserDO>lambdaQuery()
                .eq(UserDO::getUsername, authentication.getName()));
        if (userDO == null) {
            throw new BizException(ResponseCodeEnum.USER_NOT_FOUND);
        }
        return userDO.getUserId();
    }


    /**
     * 获取除管理员（ID=1）以外的所有用户
     * 用于后台用户管理列表的初始渲染
     */
    @Override
    public Response findAllUsersExceptAdmin() {
        // 查询条件：userId 不等于 1，且未被逻辑删除
        List<UserDO> userDOS = userMapper.selectList(Wrappers.<UserDO>lambdaQuery()
                .ne(UserDO::getUserId, 1L)
                .eq(UserDO::getIsDeleted, false)
                .orderByDesc(UserDO::getCreateTime)); // 按注册时间倒序

        // 转换为 VO，屏蔽敏感信息（如密码）
        List<FindUserPageListRspVO> vos = userDOS.stream().map(userDO ->
                FindUserPageListRspVO.builder()
                        .userId(userDO.getUserId())
                        .username(userDO.getUsername())
                        .createTime(userDO.getCreateTime())
                        .build()
        ).collect(Collectors.toList());

        return Response.success(vos);
    }

    /**
     * 根据用户名模糊查询
     * @param username 前端传来的搜索关键字
     */
    @Override
    public Response findUsersByUsername(String username) {
        // 1. 构造查询条件
        LambdaQueryWrapper<UserDO> wrapper = new LambdaQueryWrapper<>();

        // 排除管理员 (ID=1)
        wrapper.ne(UserDO::getUserId, 1L);
        // 逻辑未删除
        wrapper.eq(UserDO::getIsDeleted, false);

        //则进行模糊匹配 (username LIKE '%关键字%')
        if (StringUtils.isNotBlank(username)) {
            wrapper.like(UserDO::getUsername, username);
        }

        // 按创建时间倒序排
        wrapper.orderByDesc(UserDO::getCreateTime);

        // 2. 执行查询
        List<UserDO> userDOS = userMapper.selectList(wrapper);

        // 3. 转换为 VO 列表返回（复用你之前的 FindUserPageListRspVO）
        List<FindUserPageListRspVO> vos = userDOS.stream().map(userDO ->
                FindUserPageListRspVO.builder()
                        .userId(userDO.getUserId())
                        .username(userDO.getUsername())
                        .createTime(userDO.getCreateTime())
                        .build()
        ).collect(Collectors.toList());

        return Response.success(vos);
    }

    /**
     * 修改密码
     * @param updateAdminUserPasswordReqVO
     * @return
     */
    @Override
    public Response updatePassword(UpdateAdminUserPasswordReqVO updateAdminUserPasswordReqVO) {
        // 拿到用户名、密码
        String username = updateAdminUserPasswordReqVO.getUsername();
        String password = updateAdminUserPasswordReqVO.getPassword();

        // 加密密码
        String encodePassword = passwordEncoder.encode(password);

        // 更新到数据库
        int count = userMapper.updatePasswordByUsername(username, encodePassword);

        return count == 1 ? Response.success() : Response.fail(ResponseCodeEnum.USER_NOT_FOUND);
    }


    /*
    * 获取当前用户信息
    * @return*/
    @Override
    public Response findUserInfo() {
        //获取存储在 ThreadLocal 中的用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //拿到用户名
        String username = authentication.getName();

        return Response.success(FindUserInfoRspVO.builder().username(username).build());
    }


    @Override
    public Response addUser(AddUserReqVO addUserReqVO) {
        String username = addUserReqVO.getUsername();
        String password = addUserReqVO.getPassword();

        // 1. 校验用户名是否已存在
        UserDO userDO = userMapper.findByUsername(username);
        if (Objects.nonNull(userDO)) {
            // 用户已存在
            return Response.fail(ResponseCodeEnum.USERNAME_HAS_EXISTED);
        }

        // 2. 加密密码
        String encodePassword = passwordEncoder.encode(password);

        // 3. 构建实体类并保存
        UserDO newUser = UserDO.builder()
                .username(username)
                .password(encodePassword)
                .createTime(new Date())
                .updateTime(new Date())
                .isDeleted(false)
                .build();

        int count = userMapper.insert(newUser);

        return count == 1 ? Response.success() : Response.fail(ResponseCodeEnum.SYSTEM_ERROR);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response deleteUser(DeleteUserReqVO deleteUserReqVO) {
        Long loginUserId = getLoginUserId();
        Long targetUserId = deleteUserReqVO.getUserId();

        // 1. 权限校验
        if (!Long.valueOf(1).equals(loginUserId)) {
           throw  new BizException( ResponseCodeEnum.UNAUTHORIZED);
        }
        if (Long.valueOf(1).equals(targetUserId)) {
            throw  new BizException(ResponseCodeEnum.SYSTEM_USER_CANNOT_BE_DELETED);
        }

        log.info("==> 开始级联删除用户 ID: {} 的全部数据（含点赞记录）...", targetUserId);

        // 2. 级联清理

        // A. 清理点赞表 (t_article_like)
        articleLikeMapper.delete(Wrappers.<ArticleLikeDO>lambdaQuery()
                .eq(ArticleLikeDO::getUserId, targetUserId));

        // B. 处理文章及内容 (获取 ID 后删除内容，再删主表)
        List<ArticleDO> articles = articleMapper.selectList(Wrappers.<ArticleDO>lambdaQuery()
                .eq(ArticleDO::getUserId, targetUserId).select(ArticleDO::getId));
        if (!articles.isEmpty()) {
            List<Long> articleIds = articles.stream().map(ArticleDO::getId).collect(Collectors.toList());
            articleContentMapper.delete(Wrappers.<ArticleContentDO>lambdaQuery()
                    .in(ArticleContentDO::getArticleId, articleIds));
            articleMapper.deleteBatchIds(articleIds);
        }

        // C. 清理分类、标签、知识库
        categoryMapper.delete(Wrappers.<CategoryDO>lambdaQuery().eq(CategoryDO::getUserId, targetUserId));
        tagMapper.delete(Wrappers.<TagDO>lambdaQuery().eq(TagDO::getUserId, targetUserId));
        wikiMapper.delete(Wrappers.<WikiDO>lambdaQuery().eq(WikiDO::getUserId, targetUserId));

        // D. 清理收藏夹目录及关联关系
        relMapper.delete(Wrappers.<CollectionArticleRelDO>lambdaQuery().eq(CollectionArticleRelDO::getUserId, targetUserId));
        directoryMapper.delete(Wrappers.<CollectionDirectoryDO>lambdaQuery().eq(CollectionDirectoryDO::getUserId, targetUserId));

        // E. 最后删除用户核心表
        int result = userMapper.deleteById(targetUserId);

        log.info("==> 用户 ID: {} 数据级联删除完毕", targetUserId);
        return result > 0 ? Response.success() : Response.fail("删除失败，用户不存在");
    }
}



