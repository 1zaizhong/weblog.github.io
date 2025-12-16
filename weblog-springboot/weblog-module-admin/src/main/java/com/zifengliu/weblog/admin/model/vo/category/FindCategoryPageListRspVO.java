package com.zifengliu.weblog.admin.model.vo.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/4/20 下午4:05
 * @description 分类分页数据查询响应 VO
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindCategoryPageListRspVO {
   /*
   * 分类ID
   * */
    private Long id;

    /*
    * 分类名称
    * */
    private String name;

    /*创建时间
    * */
    private LocalDateTime createTime;
 /*
  * 文章总数
  */
 private Integer articlesTotal;



}
