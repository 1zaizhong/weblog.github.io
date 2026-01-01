package com.zifengliu.weblog.web.model.vo.author;

import com.zifengliu.weblog.common.domain.dos.CategoryDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2026/1/2 上午6:04
 * @description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindAuthorDashboardRspVO {

    private String author;
    private String avatar;
    private String introduction;


    private Long articleTotalCount;   // 文章总数
    private Long categoryTotalCount;  // 分类总数
    private Long tagTotalCount;       // 标签总数
    private Long pvTotalCount;        // 总阅读量
    private Long fansTotalCount;      // 粉丝数


   private List<CategoryDO> categories;
}