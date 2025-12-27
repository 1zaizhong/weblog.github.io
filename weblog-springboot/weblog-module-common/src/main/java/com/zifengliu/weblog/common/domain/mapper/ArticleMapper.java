package com.zifengliu.weblog.common.domain.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zifengliu.weblog.common.domain.dos.ArticleContentDO;
import com.zifengliu.weblog.common.domain.dos.ArticleDO;
import com.zifengliu.weblog.common.domain.dos.ArticleDO;
import com.zifengliu.weblog.common.domain.dos.ArticlePublishCountDO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/5/13 下午9:35
 * @description
 **/
public interface ArticleMapper extends BaseMapper<ArticleDO> {

    /*
     * 分页查询（已集成管理员与普通用户隔离逻辑）
     * @param current, size, title, startDate, endDate, type
     * @param userId 当前登录用户的 ID
     * @return
     */
    default Page<ArticleDO> selectPageList(IPage<ArticleDO> page, String title,
                                           LocalDate startDate, LocalDate endDate,
                                           Integer type, Long userId, Integer status) {
        // 构建查询条件
        LambdaQueryWrapper<ArticleDO> wrapper = Wrappers.<ArticleDO>lambdaQuery()
                .like(StringUtils.isNotBlank(title), ArticleDO::getTitle, title)
                .ge(Objects.nonNull(startDate), ArticleDO::getCreateTime, startDate)
                .le(Objects.nonNull(endDate), ArticleDO::getCreateTime, endDate)
                .eq(Objects.nonNull(type), ArticleDO::getType, type)
                .eq(Objects.nonNull(userId), ArticleDO::getUserId, userId)
                .eq(Objects.nonNull(status), ArticleDO::getStatus, status)
                .orderByDesc(ArticleDO::getWeight)
                .orderByDesc(ArticleDO::getCreateTime);

        // 执行分页查询，MyBatis Plus 会将查询到的 total 自动填充进 page 对象中
        return selectPage((Page<ArticleDO>) page, wrapper);
    }

    /**
     * 根据文章 ID 批量分页查询
     *
     * @param current
     * @param size
     * @param articleIds
     * @return
     */
    default Page<ArticleDO> selectPageListByArticleIds(Long current, Long size, List<Long> articleIds) {
        // 分页对象(查询第几页、每页多少数据)
        Page<ArticleDO> page = new Page<>(current, size);

        // 构建查询条件
        LambdaQueryWrapper<ArticleDO> wrapper = Wrappers.<ArticleDO>lambdaQuery()
                .in(ArticleDO::getId, articleIds) // 批量查询
                .orderByDesc(ArticleDO::getCreateTime); // 按创建时间倒叙

        return selectPage(page, wrapper);
    }

    /**
     * 查询上一篇文章（同一作者、已发布、ID 较大者）
     *
     * @param articleId 当前文章ID
     * @param userId 当前文章作者ID
     * @return
     */
    default ArticleDO selectPreArticle(Long articleId, Long userId) {
        return selectOne(Wrappers.<ArticleDO>lambdaQuery()
                .eq(ArticleDO::getUserId, userId)      // 限制为当前作者
                .eq(ArticleDO::getStatus, 2)            // 限制为已公开发布
                .orderByAsc(ArticleDO::getId)          // 按 ID 升序
                .gt(ArticleDO::getId, articleId)       // 查询比当前 ID 大的（逻辑上的上一篇，通常指时间更近的）
                .last("limit 1"));
    }

    /**
     * 查询下一篇文章（同一作者、已发布、ID 较小者）
     *
     * @param articleId 当前文章ID
     * @param userId 当前文章作者ID
     * @return
     */
    default ArticleDO selectNextArticle(Long articleId, Long userId) {
        return selectOne(Wrappers.<ArticleDO>lambdaQuery()
                .eq(ArticleDO::getUserId, userId)      // 限制为当前作者
                .eq(ArticleDO::getStatus, 2)            // 限制为已公开发布
                .orderByDesc(ArticleDO::getId)         // 按 ID 倒序
                .lt(ArticleDO::getId, articleId)       // 查询比当前 ID 小的（逻辑上的下一篇，通常指时间更早的）
                .last("limit 1"));
    }

    /**
     * 阅读量+1
     *
     * @param articleId
     * @return
     */
    default int increaseReadNum(Long articleId) {
        // 执行 SQL : UPDATE t_article SET read_num = read_num + 1 WHERE (id = XX)
        return update(null, Wrappers.<ArticleDO>lambdaUpdate()
                .setSql("read_num = read_num + 1")
                .eq(ArticleDO::getId, articleId));
    }

    /**
     * 查询所有记录的阅读量
     *
     * @return
     */
    default List<ArticleDO> selectAllReadNum() {
        // 设置仅查询 read_num 字段
        return selectList(Wrappers.<ArticleDO>lambdaQuery()
                .select(ArticleDO::getReadNum));
    }

    /**
     * 按日分组，并统计每日发布的文章数量
     *
     * @param startDate
     * @param endDate
     * @return
     */
    @Select("SELECT DATE(create_time) AS date, COUNT(*) AS count\n" +
            "FROM t_article\n" +
            "WHERE create_time >= #{startDate} AND create_time < #{endDate}\n" +
            "GROUP BY DATE(create_time)")
    List<ArticlePublishCountDO> selectDateArticlePublishCount(LocalDate startDate, LocalDate endDate);


    /**
     * 查询最大权重值记录
     *
     * @return
     */
    default ArticleDO selectMaxWeight() {
        return selectOne(Wrappers.<ArticleDO>lambdaQuery()
                .orderByDesc(ArticleDO::getWeight) // 按权重值降序排列
                .last("LIMIT 1")); // 仅查询出一条
    }

    /**
     * 批量更新文章
     *
     * @param articleDO
     * @param ids
     * @return
     */
    default int updateByIds(ArticleDO articleDO, List<Long> ids) {
        return update(articleDO, Wrappers.<ArticleDO>lambdaUpdate()
                .in(ArticleDO::getId, ids));
    }

    /**
     * 批量更新文章
     *
     * @param userId
     * @param
     * @return
     */
    default Long selectSumPvByUserId(Long userId) {
        // 逻辑：SELECT SUM(read_num) FROM t_article WHERE user_id = #{userId}
        QueryWrapper<ArticleDO> wrapper = new QueryWrapper<>();
        wrapper.select("SUM(read_num) as totalPv");
        wrapper.eq(userId != null, "user_id", userId);

        Object obj = selectObjs(wrapper).get(0);
        return obj == null ? 0L : Long.parseLong(obj.toString());
    }

    /**
     * 查询文章发布热力图数据
     */
    // ArticleMapper.java
    default List<ArticlePublishCountDO> selectDateArticlePublishCount(LocalDate startDate, LocalDate endDate, Long userId) {
        // 1. 构建查询条件
        QueryWrapper<ArticleDO> wrapper = new QueryWrapper<>();
        wrapper.select("DATE(create_time) AS date", "COUNT(*) AS count") // 聚合字段
                .ge("create_time", startDate)
                .lt("create_time", endDate)
                .eq(userId != null, "user_id", userId) // 动态判断 userId
                .groupBy("DATE(create_time)");

        // 2. 执行查询并获取结果集 (List<Map<String, Object>>)
        List<Map<String, Object>> maps = selectMaps(wrapper);

        // 3. 将 Map 转换为对应的 DO 对象
        return maps.stream().map(map -> ArticlePublishCountDO.builder()
                .date(LocalDate.parse(map.get("date").toString()))
                .count(Long.valueOf(map.get("count").toString()))
                .build()
        ).collect(Collectors.toList());
    }
}// }
