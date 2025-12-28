package com.zifengliu.weblog.admin.model.vo.collection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/28 下午7:55
 * @description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindCollectionArticlePageListRspVO {
    private Long articleId;
    private String title;
    private String cover;
    private LocalDateTime collectTime;
}