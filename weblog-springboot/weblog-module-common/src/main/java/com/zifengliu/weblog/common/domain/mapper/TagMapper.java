package com.zifengliu.weblog.common.domain.mapper;

import  com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zifengliu.weblog.common.domain.dos.TagDO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/4/9 下午8:13
 * @description
 **/
public interface TagMapper extends BaseMapper<TagDO> {
    /*
     * 分页查询
     * @param current,size,name,startDate,endDate
     * @return
     * */
   default Page<TagDO> selectPageList (long current, long size, String name, LocalDate startDate, LocalDate endDate){
       //分页对象
       Page<TagDO> page = new Page<>(current,size);
       //构建查询条件
       LambdaQueryWrapper<TagDO> wrapper = new LambdaQueryWrapper<>();
       wrapper.like(Objects.nonNull(name), TagDO::getName ,name)//模糊查询
               .ge(Objects.nonNull(startDate), TagDO::getCreateTime, startDate)//大于等于开始时间
               .le(Objects.nonNull(endDate), TagDO::getCreateTime, endDate)//小于等于结束时间
               .orderByDesc(TagDO::getCreateTime);//根据创建时间降序排序

      return   selectPage(page, wrapper);
   }


   /*
   * 根据标签词模糊查询
   * @param key
   * @return
   * */
    default List<TagDO> selectByKey(String key){
        LambdaQueryWrapper<TagDO> wrapper = new LambdaQueryWrapper<>();


        //构造模糊参数查询条件
        wrapper.like(TagDO::getName,key)
                .orderByDesc(TagDO::getCreateTime);

        return selectList(wrapper);

    }

    /**
     * 根据标签 ID 批量查询
     * @param tagIds
     * @return
     */
    default List<TagDO> selectByIds(List<Long> tagIds) {
        return selectList(Wrappers.<TagDO>lambdaQuery()
                .in(TagDO::getId, tagIds));
    }


}
