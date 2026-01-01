package com.zifengliu.weblog.web.model.vo.like;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2026/1/1 下午10:44
 * @description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindLikeArticlePageListRspVO {
    private Long id;
    private String title;
    private String cover;
    private String summary;
    private LocalDateTime createTime; // 文章发布时间
}