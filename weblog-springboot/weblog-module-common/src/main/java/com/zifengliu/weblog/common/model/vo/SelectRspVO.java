package com.zifengliu.weblog.common.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/4/20 下午5:39
 * @description 公共查询VO 无入参,
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SelectRspVO {
    /*
    * Select 下拉列表的展示文字
    * */
    private String label;
    /*
    * Select 下拉列表的值 比如:ID 等
    * */
    private Object value;

}
