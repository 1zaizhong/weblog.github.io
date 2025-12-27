package com.zifengliu.weblog.admin.model.vo.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/28 上午2:53
 * @description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddUserReqVO {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;
}
