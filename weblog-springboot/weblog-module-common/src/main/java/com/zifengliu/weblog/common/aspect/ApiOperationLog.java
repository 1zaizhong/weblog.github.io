package com.zifengliu.weblog.common.aspect;

import java.lang.annotation.*;

/**
 * @author 自风流
 * @version 0.0.3
 * @description ApiOperationLog 日志的AOP切面
 * @date 2025/2/20 上午5:52
 * @Target(ElementType.METHOD) 规定只能用在方法上
 * Documented 被注释的元素及其注解信息也会包含放在文档
 * @description
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface ApiOperationLog {
    /*
    * API功能描述
    * @return
    * */
    String description() default "";
}
