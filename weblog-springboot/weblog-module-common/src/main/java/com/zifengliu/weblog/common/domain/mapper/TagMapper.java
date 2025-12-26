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
    default Page<TagDO> selectPageList(long current, long size, String name,
                                       LocalDate startDate, LocalDate endDate,
                                       Long userId) { // 增加 userId 参数
        Page<TagDO> page = new Page<>(current, size);
        LambdaQueryWrapper<TagDO> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(TagDO::getUserId, userId) // 核心：强制过滤用户
                .like(Objects.nonNull(name), TagDO::getName, name)
                .ge(Objects.nonNull(startDate), TagDO::getCreateTime, startDate)
                .le(Objects.nonNull(endDate), TagDO::getCreateTime, endDate)
                .orderByDesc(TagDO::getCreateTime);

        return selectPage(page, wrapper);
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
