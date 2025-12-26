package com.zifengliu.weblog.common.domain.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zifengliu.weblog.common.domain.dos.CategoryDO;
import com.zifengliu.weblog.common.domain.dos.TagDO;

import java.time.LocalDate;
import java.util.Objects;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/4/9 下午8:13
 * @description
 **/
public interface CategoryMapper extends BaseMapper<CategoryDO> {
    /*
     * 分页查询
     * @param current,size,name,startDate,endDate
     * @return
     * */
    default Page<CategoryDO> selectPageList (long current, long size, String name, LocalDate startDate, LocalDate endDate,Long userId){

        Page<CategoryDO> page = new Page<>(current, size);
        LambdaQueryWrapper<CategoryDO> wrapper = new LambdaQueryWrapper<>();

        wrapper
                .eq(Objects.nonNull(userId), CategoryDO::getUserId, userId) // 关键：隔离用户数据
                .like(StringUtils.isNotBlank(name), CategoryDO::getName, name.trim())
                .ge(Objects.nonNull(startDate), CategoryDO::getCreateTime, startDate)
                .le(Objects.nonNull(endDate), CategoryDO::getCreateTime, endDate)
                .orderByDesc(CategoryDO::getCreateTime);


        return   selectPage(page, wrapper);
    }

    /*
    * 根据用户名查询
    * @param categoryName
    * @return
    * */
    default  CategoryDO selectByName(String categoryName){
        //构建查询条件
        LambdaQueryWrapper<CategoryDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CategoryDO::getName,categoryName);

        //执行查询
        return selectOne(wrapper);
    }
}
