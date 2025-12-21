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
public enum ArticleTypeEnum {

    NORMAL(1, "普通"),
    WIKI(2, "收录于知识库");

    private Integer value;
    private String description;

}