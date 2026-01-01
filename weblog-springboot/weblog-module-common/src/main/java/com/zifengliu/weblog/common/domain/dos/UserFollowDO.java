package com.zifengliu.weblog.common.domain.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2026/1/2 上午1:39
 * @description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_user_follow")
public class UserFollowDO {
    /**
     * 自增ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 关注者用户ID (粉丝)
     */
    private Long followerUserId;

    /**
     * 被关注者用户ID (博主)
     */
    private Long followingUserId;

    /**
     * 关注时间
     */
    private LocalDateTime createTime;
}