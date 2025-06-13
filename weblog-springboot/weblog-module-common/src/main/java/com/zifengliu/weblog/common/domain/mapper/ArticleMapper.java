package com.zifengliu.weblog.common.domain.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zifengliu.weblog.common.domain.dos.ArticleContentDO;
import com.zifengliu.weblog.common.domain.dos.ArticleDO;
import com.zifengliu.weblog.common.domain.dos.ArticleDO;

import java.time.LocalDate;
import java.util.Objects;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/5/13 下午9:35
 * @description
 **/
public interface ArticleMapper extends BaseMapper<ArticleDO> {

    /*
     * 分页查询
     * @param current,size,title,startDate,endDate
     * @return
     * */
    default Page<ArticleDO> selectPageList (long current, long size, String title, LocalDate startDate, LocalDate endDate){

        // 分页对象(查询第几页、每页多少数据)
        Page<ArticleDO> page = new Page<>(current, size);

        // 构建查询条件
        LambdaQueryWrapper<ArticleDO> wrapper = new LambdaQueryWrapper<>();


        wrapper
                .like(StringUtils.isNotBlank(title), ArticleDO::getTitle, title) // like 模块查询
                .ge(Objects.nonNull(startDate), ArticleDO::getCreateTime, startDate) // 大于等于 startDate
                .le(Objects.nonNull(endDate), ArticleDO::getCreateTime, endDate)  // 小于等于 endDate
                .orderByDesc(ArticleDO::getCreateTime); // 按创建时间倒叙


        return   selectPage(page, wrapper);
    }



}
