package com.zifengliu.weblog.web.model.vo.article;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2026/1/2 上午6:43
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

private  String summary;
private  Long readNum;


    private Integer userId;
    //文章是否公布/私人  1:私人 @ 2: 公开
    private Integer status;

}
