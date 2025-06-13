package com.zifengliu.weblog.jwt.handler;

import com.zifengliu.weblog.common.utils.Response;
import com.zifengliu.weblog.jwt.JwtTokenHelper;
import com.zifengliu.weblog.jwt.utils.ResultUtil;
import com.zifengliu.weblog.jwt.model.LoginRspVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/3/4 下午9:11
 * @description 处理用户登录成功后的逻辑，生成并返回JWT给客户端
 **/
@Component
@Slf4j
public class RestAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    //jwtTokenHelper用于生成和验证JWT。
    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
     //从authentication 对象中获取用户的UserDetails 实例,这里获取用户名;
        UserDetails userDetails = (UserDetails)authentication.getPrincipal();

        //通过用户名生成调用jwtTokenHelper 来生成ToKen
        String username = userDetails.getUsername();
        String token = jwtTokenHelper.generateToken(username);

        //返回Token给客户端 LoginRspVO响应类
        LoginRspVO loginRspVo = LoginRspVO.builder().token(token).build();
        //返回响应ok
        ResultUtil.ok(response,Response.success(loginRspVo));
    }
}
