package com.zifengliu.weblog.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zifengliu.weblog.admin.convert.BlogSettingsConvert;
import com.zifengliu.weblog.admin.model.vo.blogSettings.FindBlogSettingsRspVO;
import com.zifengliu.weblog.admin.model.vo.blogSettings.UpdateBlogSettingsReqVO;
import com.zifengliu.weblog.admin.service.AdminBlogSettingsService;
import com.zifengliu.weblog.admin.utils.SecurityUtils;
import com.zifengliu.weblog.common.domain.dos.BlogSettingsDO;
import com.zifengliu.weblog.common.domain.dos.UserDO;
import com.zifengliu.weblog.common.domain.mapper.BlogSettingsMapper;
import com.zifengliu.weblog.common.domain.mapper.UserMapper;
import com.zifengliu.weblog.common.enums.ResponseCodeEnum;
import com.zifengliu.weblog.common.exception.BizException;
import com.zifengliu.weblog.common.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/5/12 下午4:14
 * @description 博客设置 实现类
 **/
@Service
public class AdminBlogSettingsServiceImpl extends ServiceImpl<BlogSettingsMapper, BlogSettingsDO> implements AdminBlogSettingsService {

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
            throw new BizException(ResponseCodeEnum.USERNAME_NOT_FOUND);
        }
        return userDO.getUserId();
    }
    @Override
    public Response updateBlogSettings(UpdateBlogSettingsReqVO updateBlogSettingsReqVO) {
        //拿到id
         Long userId = getLoginUserId();
        // VO 转 DO
        BlogSettingsDO blogSettingsDO = BlogSettingsConvert.INSTANCE.convertVO2DO(updateBlogSettingsReqVO);
        blogSettingsDO.setUserId(userId);
        //修改敏感词
        blogSettingsDO.setIsCommentSensiWordOpen(updateBlogSettingsReqVO.getIsCommentSensiWordOpen());
        blogSettingsDO.setIsCommentExamineOpen(updateBlogSettingsReqVO.getIsCommentExamineOpen());

        // 保存或更新（当数据库中存在 ID 为 userId 的记录时，则执行更新操作，否则执行插入操作）
        BlogSettingsDO exist = blogSettingsMapper.selectOne(Wrappers.<BlogSettingsDO>lambdaQuery()
                .eq(BlogSettingsDO::getUserId, userId));

        if (Objects.nonNull(exist)) {
            blogSettingsDO.setId(exist.getId());
            blogSettingsMapper.updateById(blogSettingsDO);
        } else {
            blogSettingsMapper.insert(blogSettingsDO);
        }
        return Response.success();
    }

    /**
     * 获取博客设置详情
     *
     * @return
     */
    @Override
    public Response findDetail() {
        // 拿到登录用户id
        Long userId = getLoginUserId();
        // 查询 ID 为 1 的记录
        BlogSettingsDO blogSettingsDO = blogSettingsMapper.selectOne(Wrappers.<BlogSettingsDO>lambdaQuery()
                .eq(BlogSettingsDO::getUserId, userId));
        if (Objects.isNull(blogSettingsDO)) {
            log.warn("博客设置记录不存在，初始化一条默认记录");

            // 返回一个初始化的 VO，防止前端报错
            return Response.success(FindBlogSettingsRspVO.builder()
                    .author("新博主")
                    .avatar("http://127.0.0.1:9000/weblog/c46379070c544e09a7868387077a2ed3.png") // 可以设置一个默认头像地址
                    .name("博客名字")
                    .introduction("介绍语")
                    .logo("http://127.0.0.1:9000/weblog/c46379070c544e09a7868387077a2ed3.png")
                    .isCommentExamineOpen(false) // 默认关闭审核
                    .isCommentSensiWordOpen(true) // 默认开启敏感词过滤
                    .build());
        }

        // 4. DO 转 VO 并返回
        FindBlogSettingsRspVO vo = BlogSettingsConvert.INSTANCE.convertDO2VO(blogSettingsDO);
        return Response.success(vo);
    }
}
