package com.zifengliu.weblog.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zifengliu.weblog.admin.model.vo.follow.FindFollowingUserRspVO;
import com.zifengliu.weblog.admin.service.UserFollowService;
import com.zifengliu.weblog.common.domain.dos.BlogSettingsDO;
import com.zifengliu.weblog.common.domain.dos.UserDO;
import com.zifengliu.weblog.common.domain.dos.UserFollowDO;
import com.zifengliu.weblog.common.domain.mapper.BlogSettingsMapper;
import com.zifengliu.weblog.common.domain.mapper.UserFollowMapper;
import com.zifengliu.weblog.common.domain.mapper.UserMapper;
import com.zifengliu.weblog.common.enums.ResponseCodeEnum;
import com.zifengliu.weblog.common.exception.BizException;
import com.zifengliu.weblog.common.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2026/1/2 上午2:27
 * @description
 **/
@Service
public class AdminUserFollowServiceImpl implements UserFollowService {

    @Autowired
    private UserFollowMapper userFollowMapper;

    @Autowired
    private BlogSettingsMapper blogSettingsMapper;
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
    public Response findFollowingList() {
        // 1. 从 Token 中获取当前登录用户 ID
        Long loginUserId = getLoginUserId();

        // 2. 查询关注表，拿到所有关注的博主 ID
        LambdaQueryWrapper<UserFollowDO> followWrapper = Wrappers.lambdaQuery();
        followWrapper.eq(UserFollowDO::getFollowerUserId, loginUserId)
                .orderByDesc(UserFollowDO::getCreateTime);

        List<UserFollowDO> followDOs = userFollowMapper.selectList(followWrapper);

        if (followDOs.isEmpty()) {
            throw  new BizException(ResponseCodeEnum.FOLLOW_NOT_FOUND);
        }

        // 3. 提取博主 ID 集合
        List<Long> followingUserIds = followDOs.stream()
                .map(UserFollowDO::getFollowingUserId)
                .collect(Collectors.toList());

        //批量查询博客设置表
        LambdaQueryWrapper<BlogSettingsDO> settingsWrapper = Wrappers.lambdaQuery();
        settingsWrapper.in(BlogSettingsDO::getUserId, followingUserIds);
        List<BlogSettingsDO> settingsDOs = blogSettingsMapper.selectList(settingsWrapper);

        // 将设置信息转为 Map 方便匹配
        Map<Long, BlogSettingsDO> settingsMap = settingsDOs.stream()
                .collect(Collectors.toMap(BlogSettingsDO::getUserId, s -> s));

        // 5. 组装 VO
        List<FindFollowingUserRspVO> vos = followDOs.stream().map(followDO -> {
            BlogSettingsDO settings = settingsMap.get(followDO.getFollowingUserId());
            return FindFollowingUserRspVO.builder()
                    .userId(followDO.getFollowingUserId())
                    .author(settings != null ? settings.getAuthor() : "未知作者")
                    .avatar(settings != null ? settings.getAvatar() : "")
                    .introduction(settings != null ? settings.getIntroduction() : "")
                    .createTime(followDO.getCreateTime())
                    .build();
        }).collect(Collectors.toList());

        return Response.success(vos);
    }

    @Override
    public Response deleteFollow(Long targetUserId) {
        // 1. 获取当前登录用户 ID
        Long loginUserId = getLoginUserId();

        // 2. 构建删除条件
        LambdaQueryWrapper<UserFollowDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UserFollowDO::getFollowerUserId, loginUserId)
                .eq(UserFollowDO::getFollowingUserId, targetUserId);

        // 3. 执行删除
        int rows = userFollowMapper.delete(wrapper);

        return rows > 0 ? Response.success("取消关注成功") : Response.fail("取消关注失败或未关注该用户");
    }
}