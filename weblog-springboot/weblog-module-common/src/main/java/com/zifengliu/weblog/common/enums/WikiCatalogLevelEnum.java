package com.zifengliu.weblog.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/21 下午5:04
 * @description
 **/

@Getter
@AllArgsConstructor
public enum WikiCatalogLevelEnum {

    // 一级目录
    ONE(1),
    // 二级目录
    TWO(2);

    private Integer value;

}