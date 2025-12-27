package com.zifengliu.weblog.common.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zifengliu.weblog.common.domain.dos.StatisticsArticlePVDO;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


import java.time.LocalDate;
import java.util.List;

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
    /**
     * 查询最近一周的文章 PV 访问量记录
     * @return
     */
    default List<StatisticsArticlePVDO> selectLatestWeekRecords() {
        return selectList(Wrappers.<StatisticsArticlePVDO>lambdaQuery()
                .le(StatisticsArticlePVDO::getPvDate, LocalDate.now().plusDays(1)) // 小于明天
                .orderByDesc(StatisticsArticlePVDO::getPvDate)
                .last("limit 7")); // 仅查询七条
    }
    /**
     * 查询最近一周的文章 PV 访问量记录 (支持按用户过滤)
     */
    @Select("<script>" +
            "SELECT DATE(create_time) AS pv_date, SUM(read_num) AS pv_count " +
            "FROM t_article " +
            "WHERE is_deleted = 0 " +
            "AND create_time >= DATE_SUB(CURDATE(), INTERVAL 7 DAY) " +
            "<if test='userId != null'>" +
            "AND user_id = #{userId} " +
            "</if>" +
            "GROUP BY DATE(create_time) " +
            "ORDER BY pv_date ASC" +
            "</script>")
    List<StatisticsArticlePVDO> selectLatestWeekRecords(@Param("userId") Long userId);
}