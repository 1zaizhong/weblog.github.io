package com.zifengliu.weblog.admin.model.vo.follow;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2026/1/2 上午2:26
 * @description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindFollowingUserRspVO {
    private Long userId;        // 博主用户ID
    private String author;      // 博主作者名（从 t_blog_settings 获取）
    private String avatar;      // 博主头像
    private String introduction;// 博主简介
    private LocalDateTime createTime; // 关注时间
}