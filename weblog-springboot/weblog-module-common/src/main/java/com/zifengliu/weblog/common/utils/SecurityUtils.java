package com.zifengliu.weblog.common.utils;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zifengliu.weblog.common.domain.dos.UserDO;
import com.zifengliu.weblog.common.domain.mapper.UserMapper;
import com.zifengliu.weblog.common.enums.ResponseCodeEnum;
import com.zifengliu.weblog.common.exception.BizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/27 上午12:08
 * @description
 **/
public class SecurityUtils {
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
}
