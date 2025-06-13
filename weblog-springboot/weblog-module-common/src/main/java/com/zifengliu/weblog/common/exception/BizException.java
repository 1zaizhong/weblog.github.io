package com.zifengliu.weblog.common.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * @author 自风流
 * @version 0.0.3
 * @description BizException
 * @date 2025/2/20 下午4:56
 * @description Business Exception 业务异常类,构造器通过BaseExceptionInterface实现业务异常
 **/
@Getter
@Setter
public class BizException  extends RuntimeException{
 //异常码
    private String errorCode;
    //错误信息
    private String errorMessage;

    public  BizException(BaseExceptionInterface baseExceptionInterface){
        this.errorCode = baseExceptionInterface.getErrorCode();
        this.errorMessage = baseExceptionInterface.getErrorMessage();
    }
}
