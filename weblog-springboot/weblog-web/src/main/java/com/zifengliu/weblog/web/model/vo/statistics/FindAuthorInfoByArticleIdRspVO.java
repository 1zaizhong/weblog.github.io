package com.zifengliu.weblog.web.model.vo.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2026/1/2 上午4:07
 * @description 文章对应的作者信息VO
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FindAuthorInfoByArticleIdRspVO {
    // 作者基本信息
    private String author;
    private String avatar;
    private String introduction;

    // 统计数据
    private Long articleTotalCount;
    private Long categoryTotalCount;
    private Long tagTotalCount;
    private Long pvTotalCount;
    private Long fansTotalCount;
}