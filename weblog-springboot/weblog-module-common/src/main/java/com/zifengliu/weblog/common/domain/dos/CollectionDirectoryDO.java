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
 * @date 2025/12/28 下午6:44
 * @description 收藏还是实体类
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_collection_directory")
public class CollectionDirectoryDO {
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 收藏夹名称 */
    private String name;
    /** 所属用户ID */
    private Long userId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}