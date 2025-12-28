package com.zifengliu.weblog.admin.model.vo.article;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/28 下午3:17
 * @description  查询文章点赞列表响应
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FindLikeArticlePageListRspVO {
    private Long articleId;
    private String title;     // 文章标题
    private String cover;     // 文章封面
    private LocalDateTime createTime; // 点赞时间
}
