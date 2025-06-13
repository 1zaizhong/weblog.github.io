package com.zifengliu.weblog.common.domain.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


/**
 * @author 自风流
 * @version 0.0.3
 * @description UserDO
 * @date 2025/3/3 下午6:58
 * @description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_user")
public class UserDO {
    @TableId(type=IdType.AUTO)
    private Long id;
    private String username;

    private String password;

    private Date createTime;

    private Date updateTime;

    private  Boolean isDeleted;

}
