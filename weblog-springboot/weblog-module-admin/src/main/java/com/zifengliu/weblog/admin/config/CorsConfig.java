package com.zifengliu.weblog.admin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author 粟英朝
 * @description 全局跨域配置
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        // 1. 创建 CORS 配置对象
        CorsConfiguration config = new CorsConfiguration();

        // 允许的域：使用 allowedOriginPatterns 允许你的前端端口 (5173)
        config.addAllowedOriginPattern("*");

        // 是否允许发送 Cookie
        config.setAllowCredentials(true);

        // 允许的请求方式 (GET, POST, OPTIONS 等)
        config.addAllowedMethod("*");

        // 允许的头信息
        config.addAllowedHeader("*");

        // 暴露哪些头部信息 (对于 SSE 流式响应，有时需要暴露 Content-Type)
        config.addExposedHeader("*");

        // 2. 添加映射路径，拦截一切请求
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}