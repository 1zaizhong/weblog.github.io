package com.zifengliu.weblog.common.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zifengliu.weblog.common.domain.dos.StatisticsArticlePVDO;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;


import java.time.LocalDate;
/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/13 下午9:26
 * @description
 **/

public interface StatisticsArticlePVMapper extends BaseMapper<StatisticsArticlePVDO> {

    /**
     * 对指定日期的文章 PV 访问量进行 +1
     * @param date
     * @return
     */
    default int increasePVCount(LocalDate date) {
        return update(null, Wrappers.<StatisticsArticlePVDO>lambdaUpdate()
                .setSql("pv_count = pv_count + 1")
                .eq(StatisticsArticlePVDO::getPvDate, date));
    }
}