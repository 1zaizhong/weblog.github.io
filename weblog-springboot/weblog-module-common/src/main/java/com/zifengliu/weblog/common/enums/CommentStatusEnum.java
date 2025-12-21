package com.zifengliu.weblog.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/21 下午10:14
 * @description
 **/
@Getter
@AllArgsConstructor
public enum CommentStatusEnum {

    // ----------- 通用异常状态码 -----------
    WAIT_EXAMINE(1, "等待审核"),
    NORMAL(2, "正常"),
    EXAMINE_FAILED(3, "审核不通过"),
    ;

    private Integer code;
    private String description;

}