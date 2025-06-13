package com.zifengliu.weblog.common.model;

import lombok.Data;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/4/20 下午3:47
 * @description
 **/
@Data
public class BasePageQuery {
    /*
    * 当前页码,默认第一页
    * */
   private  Long current = 1L;

   /*每页展示的数据数量,默认是10个数据
   */
   private Long size = 10L;





}
