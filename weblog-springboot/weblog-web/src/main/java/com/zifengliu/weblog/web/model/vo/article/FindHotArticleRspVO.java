package com.zifengliu.weblog.web.model.vo.article;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2026/1/1 下午4:36
 * @description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindHotArticleRspVO {
    private Long id;
    private String title;
    private String cover;
    private Long readNum;
    private LocalDateTime createTime;
    private Double score; // 热度分值
}