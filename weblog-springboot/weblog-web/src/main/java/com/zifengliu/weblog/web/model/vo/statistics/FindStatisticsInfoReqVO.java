package com.zifengliu.weblog.web.model.vo.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/27 下午6:56
 * @description
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindStatisticsInfoReqVO {
    @NotNull(message = "用户 ID 不能为空")
    private Long userId;
}