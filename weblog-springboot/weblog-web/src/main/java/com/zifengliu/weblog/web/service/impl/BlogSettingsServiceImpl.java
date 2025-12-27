package com.zifengliu.weblog.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zifengliu.weblog.common.domain.dos.BlogSettingsDO;
import com.zifengliu.weblog.common.domain.dos.UserDO;
import com.zifengliu.weblog.common.domain.mapper.BlogSettingsMapper;
import com.zifengliu.weblog.common.domain.mapper.UserMapper;
import com.zifengliu.weblog.common.utils.Response;
import com.zifengliu.weblog.web.convert.BlogSettingsConvert;
import com.zifengliu.weblog.web.model.vo.blogsettings.FindBlogSettingsDetailRspVO;
import com.zifengliu.weblog.web.service.BlogSettingsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/10/17 下午5:07
 * @description BlogSettingsServiceImpl
 **/
@Service
@Slf4j
public class BlogSettingsServiceImpl implements BlogSettingsService {

    @Autowired
    private BlogSettingsMapper blogSettingsMapper;

    @Override
    public Response findDetail(Long userId) {
        log.info("开始查询用户 ID 为 {} 的博客设置", userId);

        //拿到Id来查
        LambdaQueryWrapper<BlogSettingsDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BlogSettingsDO::getUserId, userId);

        BlogSettingsDO blogSettingsDO = blogSettingsMapper.selectOne(wrapper);

        // 2.如果该用户没有配置过（查不到数据）
        if (Objects.isNull(blogSettingsDO)) {
            log.warn("用户 ID: {} 尚未配置博客详情，返回系统默认设置 (ID=1)", userId);
            // 返回数据库中 ID 为 1 的那条默认记录
            blogSettingsDO = blogSettingsMapper.selectById(1L);
        }

        // 3. 将查询到的 DO 转换为返回给前端的 VO
        FindBlogSettingsDetailRspVO vo = BlogSettingsConvert.BLOG_SETTINGS_CONVERT.convertDo2VO(blogSettingsDO);

        return Response.success(vo);
    }
}