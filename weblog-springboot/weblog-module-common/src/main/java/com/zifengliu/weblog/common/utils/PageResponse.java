package com.zifengliu.weblog.common.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.util.List;
import java.util.Objects;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/4/20 下午3:32
 * @description
 **/
@Data
public class PageResponse <T>  extends  Response<List<T>>{

    /*总记录数
    * */
    private long total = 0L;

    /*每一页显示的记录数,默认为10
    * */
    private long size = 10L;

    /*当前页码
    * */
    private  long current ;

    /*总页数
    * */
    private  long page ;

    /*成功响应
    * @param page Mybatis Plus 提供的接口
    * @param data
    * @return
    * @param<T>
    */
    public static  <T> PageResponse<T> success(IPage page ,List<T> data ){
        PageResponse<T> response = new PageResponse<>();
        response.setSuccess(true);
        response.setCurrent(Objects.isNull(page)? 1L : page.getCurrent());
        response.setSize(Objects.isNull(page)? 10L : page.getSize());
        response.setPage(Objects.isNull(page)? 0L : page.getPages());
        response.setTotal(Objects.isNull(page)? 0L : page.getTotal());
        response.setData(data);

        return response;
    }



}
