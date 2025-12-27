package com.zifengliu.weblog.admin.service.impl;


import com.zifengliu.weblog.admin.model.vo.user.AddUserReqVO;
import com.zifengliu.weblog.admin.model.vo.user.FindUserInfoRspVO;
import com.zifengliu.weblog.admin.model.vo.user.UpdateAdminUserPasswordReqVO;
import com.zifengliu.weblog.admin.service.AdminUserService;
import com.zifengliu.weblog.common.domain.dos.UserDO;
import com.zifengliu.weblog.common.domain.mapper.UserMapper;

import com.zifengliu.weblog.common.enums.ResponseCodeEnum;
import com.zifengliu.weblog.common.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Security;
import java.util.Date;
import java.util.Objects;


/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/3/27 下午7:13
 * @description
 **/
@Service
public class AdminUserServiceImpl implements AdminUserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

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

        return count == 1 ? Response.success() : Response.fail(ResponseCodeEnum.USERNAME_NOT_FOUND);
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


}

