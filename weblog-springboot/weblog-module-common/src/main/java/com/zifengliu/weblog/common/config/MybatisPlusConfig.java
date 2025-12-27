package com.zifengliu.weblog.common.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 自风流
 * @version 0.0.3
 * @description MybatisPlusConfig
 * @date 2025/3/3 下午6:54
 * @description MybatisPlus配置类
 **/
@Configuration
@MapperScan("com.zifengliu.weblog.common.domain.mapper")
public class MybatisPlusConfig
{
/*
* 分页插件
* @return */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return interceptor;
    }
    /*
    * 自定义批量注入 SQL 注入器
    * */
    @Bean
    public InsertBatchSqlInjector InsertBatchSqlInjector(){
        return new InsertBatchSqlInjector();
    }
}
