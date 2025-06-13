package com.zifengliu.weblog.jwt.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/3/4 下午7:01
 * @description
 **/
public class UsernameOrPasswordNullException extends AuthenticationException {
    public UsernameOrPasswordNullException(String msg, Throwable cause) {
        super(msg, cause);

    }

    public UsernameOrPasswordNullException(String msg) {
        super(msg);
    }
}
