package com.zifengliu.weblog.common.domain.dos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/13 下午8:17
 * @description
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticlePublishCountDO {
    /**
     * 日期
     */
    private LocalDate date;

    /**
     * 文章发布数量
     */
    private Long count;
}
