package com.zifengliu.weblog.common.domain.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/5/13 下午9:21
 * @description 文章实体
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_article")
public class ArticleDO {

    @TableId(type = IdType.AUTO)
    private  Long  id;

    private  String title;

    private  String cover;

    private  String summary;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private boolean isDeleted;

    private  Long readNum;
    //置顶权重
    private Integer weight;
    //文章类型 1普通文章 2 知识库
    private Integer type;

    private Long userId;
    private Integer status;
    private Integer likeNum;
}
