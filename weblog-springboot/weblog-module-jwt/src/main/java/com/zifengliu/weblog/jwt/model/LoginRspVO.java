package com.zifengliu.weblog.jwt.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/3/4 下午9:43
 * @description  显示返回客户端的界面
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRspVO {
    /*
    * Token值*/
    private String token;
}
