package com.zifengliu.weblog.common.domain.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/28 下午6:45
 * @description 收藏-文章关系DO
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_collection_article_rel")
public class CollectionArticleRelDO {
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 收藏夹ID */
    private Long directoryId;
    /** 文章ID */
    private Long articleId;
    /** 用户ID */
    private Long userId;
    private LocalDateTime createTime;
}