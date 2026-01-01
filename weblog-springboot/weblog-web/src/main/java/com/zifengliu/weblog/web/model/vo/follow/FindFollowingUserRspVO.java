package com.zifengliu.weblog.web.model.vo.follow;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2026/1/2 上午5:12
 * @description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindFollowingUserRspVO {
    /**
     * 被关注的博主用户 ID (用于点击跳转)
     */
    private Long userId;

    /**
     * 博主头像
     */
    private String avatar;

    /**
     * 博主昵称/作者名
     */
    private String author;

    /**
     * 博主简介
     */
    private String introduction;

    /**
     * 关注时间 (可选，用于排序或展示)
     */
    private LocalDateTime createTime;
}