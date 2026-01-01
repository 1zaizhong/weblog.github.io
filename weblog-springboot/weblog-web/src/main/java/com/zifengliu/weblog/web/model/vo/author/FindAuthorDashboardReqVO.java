package com.zifengliu.weblog.web.model.vo.author;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2026/1/2 上午6:07
 * @description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "查询博主看板数据请求 VO")
public class FindAuthorDashboardReqVO {

    @NotNull(message = "博主 ID 不能为空")
    private Long authorId;
}