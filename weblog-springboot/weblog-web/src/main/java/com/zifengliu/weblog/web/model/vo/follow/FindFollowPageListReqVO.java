package com.zifengliu.weblog.web.model.vo.follow;

import com.zifengliu.weblog.common.model.BasePageQuery;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2026/1/2 上午2:58
 * @description
 **/
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class FindFollowPageListReqVO extends BasePageQuery {
    @NotNull(message = "用户 ID 不能为空")
    private Long userId;
}