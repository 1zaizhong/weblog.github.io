package com.zifengliu.weblog.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.zifengliu.weblog.web.model.vo.follow.FindFollowingUserRspVO;
import com.zifengliu.weblog.common.domain.dos.ArticleDO;
import com.zifengliu.weblog.common.domain.dos.BlogSettingsDO;
import com.zifengliu.weblog.common.domain.dos.UserDO;
import com.zifengliu.weblog.common.domain.dos.UserFollowDO;
import com.zifengliu.weblog.common.domain.mapper.ArticleMapper;
import com.zifengliu.weblog.common.domain.mapper.BlogSettingsMapper;
import com.zifengliu.weblog.common.domain.mapper.UserFollowMapper;
import com.zifengliu.weblog.common.domain.mapper.UserMapper;
import com.zifengliu.weblog.common.utils.PageResponse;
import com.zifengliu.weblog.common.utils.Response;
import com.zifengliu.weblog.web.model.vo.follow.CheckFollowReqVO;
import com.zifengliu.weblog.web.model.vo.follow.FindFollowPageListReqVO;
import com.zifengliu.weblog.web.model.vo.follow.FollowUserReqVO;
import com.zifengliu.weblog.web.service.UserFollowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2026/1/2 上午2:59
 * @description
 **/
@Service
@Slf4j
public class UserFollowServiceImpl implements UserFollowService {

    @Autowired
    private UserFollowMapper userFollowMapper;
    @Autowired
    private BlogSettingsMapper blogSettingsMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ArticleMapper articleMapper;

   /**
   * 关注/取消关注
    * */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response followOrUnfollowUser(FollowUserReqVO reqVO) {
        Long userId = reqVO.getUserId();
        Long articleId = reqVO.getArticleId();

        // 根据文章 ID 查询文章信息，获取作者 ID
        ArticleDO articleDO = articleMapper.selectById(articleId);
        if (Objects.isNull(articleDO)) {
            return Response.fail("该文章不存在");
        }
        Long targetUserId = articleDO.getUserId();

        //  校验被关注的用户在数据库中是否存在
        UserDO targetUser = userMapper.selectById(targetUserId);
        if (Objects.isNull(targetUser)) {
            return Response.fail("该作者不存在或已被注销");
        }

        // 3. 不能关注自己
        if (userId.equals(targetUserId)) {
            return Response.fail("不能关注你自己哦");
        }

        //
        LambdaQueryWrapper<UserFollowDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UserFollowDO::getFollowerUserId, userId)
                .eq(UserFollowDO::getFollowingUserId, targetUserId);

        UserFollowDO existFollow = userFollowMapper.selectOne(wrapper);

        if (Objects.isNull(existFollow)) {
            UserFollowDO followDO = UserFollowDO.builder()
                    .followerUserId(userId)
                    .followingUserId(targetUserId)
                    .createTime(LocalDateTime.now())
                    .build();
            userFollowMapper.insert(followDO);
            return Response.success("关注成功");
        } else {
            userFollowMapper.deleteById(existFollow.getId());
            return Response.success("取消关注成功");
        }
    }

    /**
     * 检查是否啊棍子
     * */
    @Override
    public Response checkIsFollowed(CheckFollowReqVO reqVO) {
        if (Objects.isNull(reqVO.getUserId())) return Response.success(false);

        // 同样先通过文章查作者
        ArticleDO articleDO = articleMapper.selectById(reqVO.getArticleId());
        if (Objects.isNull(articleDO)) return Response.success(false);

        Long count = userFollowMapper.selectCount(Wrappers.<UserFollowDO>lambdaQuery()
                .eq(UserFollowDO::getFollowerUserId, reqVO.getUserId())
                .eq(UserFollowDO::getFollowingUserId, articleDO.getUserId()));

        return Response.success(count > 0);
    }

    /**
    * 关注列表
    * */
    @Override
    public Response findFollowPageList(FindFollowPageListReqVO reqVO) {
        // 1. 分页查询关注表
        Page<UserFollowDO> page = new Page<>(reqVO.getCurrent(), reqVO.getSize());
        LambdaQueryWrapper<UserFollowDO> wrapper = Wrappers.<UserFollowDO>lambdaQuery()
                .eq(UserFollowDO::getFollowerUserId, reqVO.getUserId())
                .orderByDesc(UserFollowDO::getCreateTime);

        Page<UserFollowDO> followPage = userFollowMapper.selectPage(page, wrapper);
        List<UserFollowDO> records = followPage.getRecords();

        if (CollectionUtils.isEmpty(records)) {
            return Response.success(PageResponse.success(followPage, Collections.emptyList()));
        }

        // 2. 提取博主 ID 集合
        List<Long> followingIds = records.stream()
                .map(UserFollowDO::getFollowingUserId)
                .collect(Collectors.toList());

        // 3. 批量查询博主的博客设置信息 (头像、作者名)
        List<BlogSettingsDO> settingsDOs = blogSettingsMapper.selectList(Wrappers.<BlogSettingsDO>lambdaQuery()
                .in(BlogSettingsDO::getUserId, followingIds));

        Map<Long, BlogSettingsDO> settingsMap = settingsDOs.stream()
                .collect(Collectors.toMap(BlogSettingsDO::getUserId, s -> s));

        // 4. 组装 RspVO (复用之前定义的 FindFollowingUserRspVO)
        List<FindFollowingUserRspVO> vos = records.stream().map(record -> {
            BlogSettingsDO settings = settingsMap.get(record.getFollowingUserId());
            return FindFollowingUserRspVO.builder()
                    .userId(record.getFollowingUserId())
                    .author(settings != null ? settings.getAuthor() : "未知博主")
                    .avatar(settings != null ? settings.getAvatar() : "")
                    .introduction(settings != null ? settings.getIntroduction() : "")
                    .createTime(record.getCreateTime())
                    .build();
        }).collect(Collectors.toList());

        return Response.success(PageResponse.success(followPage, vos));
    }
}