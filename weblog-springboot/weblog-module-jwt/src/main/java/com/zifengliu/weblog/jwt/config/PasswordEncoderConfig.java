package com.zifengliu.weblog.jwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @description PasswordEncoderConfig
 * @date 2025/3/4 下午6:17
 * @description 密码加密算法
 **/
@Configuration
public class PasswordEncoderConfig {
    @Bean
    public PasswordEncoder passwordEncoder(){
        //BCrypt 加盐
        return new BCryptPasswordEncoder();
    }

    public  static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode("syc"));
    }



}
