package com.zifengliu.weblog.web.model.vo.follow;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2026/1/2 上午2:57
 * @description 关注/取关 请求 VO
 **/
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class FollowUserReqVO {
    private Long userId;
    private Long articleId;
}
