package com.zifengliu.weblog.jwt.handler;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zifengliu.weblog.common.domain.dos.UserDO;
import com.zifengliu.weblog.common.domain.mapper.UserMapper;
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

    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @Autowired
    private UserMapper userMapper; // 新增：注入 UserMapper

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 1. 获取用户名
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        // 2. 生成 Token
        String token = jwtTokenHelper.generateToken(username);

        // 3. --- 关键修改：从数据库获取对应的 userID ---
        UserDO userDO = userMapper.selectOne(Wrappers.<UserDO>lambdaQuery()
                .eq(UserDO::getUsername, username));

        Long userID = (userDO != null) ? userDO.getUserId() : null;

        // 4. 将 userID 封装到响应对象中
        LoginRspVO loginRspVo = LoginRspVO.builder()
                .token(token)
                .userID(userID) // 这里的 Key 必须和前端取值的一致
                .build();

        log.info("==> 用户 {} 登录成功，userID 为 {}", username, userID);

        // 返回响应
        ResultUtil.ok(response, Response.success(loginRspVo));
    }
}