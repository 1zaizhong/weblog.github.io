package com.zifengliu.weblog.admin.model.vo.article;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/4/20 下午4:05
 * @description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindArticlePageListRspVO {
   /*
   * 文章ID
   * */
    private Long id;

    /*
    * 文章标题
    * */
    private String title;

    /*
    * 文章封面
    * */
private  String cover;
    /*创建时间
    * */
    private LocalDateTime createTime;



    /**
     * 是否置顶
     */
    private Boolean isTop;
    // 关联的用户ID ---
    private Integer userId;
    //文章是否公布/私人  1:私人 @ 2: 公开
    private Integer status;

}
