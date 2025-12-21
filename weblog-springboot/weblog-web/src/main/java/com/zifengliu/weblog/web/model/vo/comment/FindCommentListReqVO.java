package com.zifengliu.weblog.web.model.vo.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/21 下午10:42
 * @description
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindCommentListReqVO {

    @NotBlank(message = "路由地址不能为空")
    private String routerUrl;

}