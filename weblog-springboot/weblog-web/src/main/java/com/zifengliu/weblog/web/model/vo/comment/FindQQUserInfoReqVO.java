package com.zifengliu.weblog.web.model.vo.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/21 下午9:35
 * @description
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindQQUserInfoReqVO {

    @NotBlank(message = "QQ 号不能为空")
    private String qq;

}