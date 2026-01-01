package com.zifengliu.weblog.common.domain.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zifengliu.weblog.common.domain.dos.CommentDO;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/21 下午10:09
 * @description
 **/
public interface CommentMapper extends BaseMapper<CommentDO> {
    /**
     * 根据路由地址、状态查询对应的评论
     * @param routerUrl
     * @return
     */
    default List<CommentDO> selectByRouterUrlAndStatus(String routerUrl, Integer status) {
        return selectList(Wrappers.<CommentDO>lambdaQuery()
                .eq(CommentDO::getRouterUrl, routerUrl) // 按路由地址查询
                .eq(CommentDO::getStatus, status) // 按状态查询
                .orderByDesc(CommentDO::getCreateTime) // 按创建时间倒序
        );
    }

    /**
     * 分页查询
     * @param current
     * @param size
     * @param startDate
     * @param endDate
     * @return
     */
    // CommentMapper.java

    default Page<CommentDO> selectPageList(Long current, Long size, String routerUrl,
                                            LocalDate startDate, LocalDate endDate,
                                            Integer status, Long userId) {
        Page<CommentDO> page = new Page<>(current, size);

        LambdaQueryWrapper<CommentDO> wrapper = Wrappers.<CommentDO>lambdaQuery()
                .like(StringUtils.isNotBlank(routerUrl), CommentDO::getRouterUrl, routerUrl)
                .eq(Objects.nonNull(status), CommentDO::getStatus, status)
                .ge(Objects.nonNull(startDate), CommentDO::getCreateTime, startDate)
                .le(Objects.nonNull(endDate), CommentDO::getCreateTime, endDate)
                .eq(Objects.nonNull(userId), CommentDO::getUserId, userId) // 关键过滤
                .orderByDesc(CommentDO::getCreateTime);

        return selectPage(page, wrapper);
    }

    /**
     * 根据 reply_comment_id 查询评论
     * @param replyCommentId
     * @return
     */
    default List<CommentDO> selectByReplyCommentId(Long replyCommentId) {
        return selectList(Wrappers.<CommentDO>lambdaQuery()
                .eq(CommentDO::getReplyCommentId, replyCommentId)
                .orderByDesc(CommentDO::getCreateTime)
        );
    }

    /**
     * 根据 parent_comment_id 删除
     * @param id
     * @return
     */
    default int deleteByParentCommentId(Long id) {
        return delete(Wrappers.<CommentDO>lambdaQuery()
                .eq(CommentDO::getParentCommentId, id));
    }

}
