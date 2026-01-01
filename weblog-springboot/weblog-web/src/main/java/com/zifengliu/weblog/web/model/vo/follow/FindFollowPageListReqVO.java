package com.zifengliu.weblog.web.model.vo.follow;

import com.zifengliu.weblog.common.model.BasePageQuery;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Long userId;
}