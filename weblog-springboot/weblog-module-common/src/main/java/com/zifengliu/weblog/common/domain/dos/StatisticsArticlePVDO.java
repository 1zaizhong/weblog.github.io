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
 * @date 2025/12/13 下午9:26
 * @description
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_statistics_article_pv")
public class StatisticsArticlePVDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private LocalDate pvDate;

    private Long pvCount;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}