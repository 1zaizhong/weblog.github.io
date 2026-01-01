package com.zifengliu.weblog.web.model.vo.collection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2026/1/1 下午9:25
 * @description
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FindCollectionArticlePageListRspVO {
    private Long id;
    private String title;
    private String cover;
    //描述
    private String summary;
    private LocalDateTime createTime;
}
