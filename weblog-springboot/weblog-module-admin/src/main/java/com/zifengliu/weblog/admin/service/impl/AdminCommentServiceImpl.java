package com.zifengliu.weblog.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zifengliu.weblog.admin.convert.CommentConvert;
import com.zifengliu.weblog.admin.model.vo.comment.DeleteCommentReqVO;
import com.zifengliu.weblog.admin.model.vo.comment.ExamineCommentReqVO;
import com.zifengliu.weblog.admin.model.vo.comment.FindCommentPageListReqVO;
import com.zifengliu.weblog.admin.model.vo.comment.FindCommentPageListRspVO;
import com.zifengliu.weblog.admin.service.AdminCommentService;
import com.zifengliu.weblog.common.domain.dos.CommentDO;
import com.zifengliu.weblog.common.domain.dos.UserDO;
import com.zifengliu.weblog.common.domain.mapper.CommentMapper;
import com.zifengliu.weblog.common.domain.mapper.UserMapper;
import com.zifengliu.weblog.common.enums.CommentStatusEnum;
import com.zifengliu.weblog.common.enums.ResponseCodeEnum;
import com.zifengliu.weblog.common.exception.BizException;
import com.zifengliu.weblog.common.utils.PageResponse;
import com.zifengliu.weblog.common.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/22 上午12:13
 * @description
 **/
@Service
@Slf4j
public class AdminCommentServiceImpl implements AdminCommentService {

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private UserMapper userMapper;

    //拿到登录信息id,用来后面用
    private Long getLoginUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDO userDO = userMapper.selectOne(Wrappers.<UserDO>lambdaQuery()
                .eq(UserDO::getUsername, authentication.getName()));
        if (userDO == null) {
            throw new BizException(ResponseCodeEnum.USERNAME_NOT_FOUND);
        }
        return userDO.getUserId();
    }

    /**
     * 查询评论分页数据
     *
     * @param findCommentPageListReqVO
     * @return
     */
    @Override
    public Response findCommentPageList(FindCommentPageListReqVO findCommentPageListReqVO) {
        // 获取当前页、以及每页需要展示的数据数量
        Long current = findCommentPageListReqVO.getCurrent();
        Long size = findCommentPageListReqVO.getSize();
        LocalDate startDate = findCommentPageListReqVO.getStartDate();
        LocalDate endDate = findCommentPageListReqVO.getEndDate();
        String routerUrl = findCommentPageListReqVO.getRouterUrl();
        Integer status = findCommentPageListReqVO.getStatus();
        // 1. 获取当前登录用户
        Long loginUserId = getLoginUserId();
        // 管理员看全部，普通用户看自己
        Long userId = Objects.equals(loginUserId, 1L) ? null : loginUserId;

        // 执行分页查询
        Page<CommentDO> commentDOPage = commentMapper.selectPageList(current, size, routerUrl, startDate, endDate, status,userId);

        List<CommentDO> commentDOS = commentDOPage.getRecords();

        // DO 转 VO
        List<FindCommentPageListRspVO> vos = null;
        if (!CollectionUtils.isEmpty(commentDOS)) {
            vos = commentDOS.stream()
                    .map(commentDO -> CommentConvert.INSTANCE.convertDO2VO(commentDO))
                    .collect(Collectors.toList());
        }

        return PageResponse.success(commentDOPage, vos);
    }

    /**
     * 删除评论
     *
     * @param deleteCommentReqVO
     * @return
     * 逻辑:在方法上添加 @Transactional(rollbackFor = Exception.class) 事务回滚注解，
     * 因为删除评论逻辑中，可能会多次更新数据库，需要保证整个操作的原子性；
     * 接着，查询想要删除的评论，通过 replayCommentId 字段值是否为空，来判断其类型，
     * 是一级评论呢，还是二级评论；
     * 如果是一级评论，除了将该记录本身删除掉外，还需要将其所有子评论删除；
     * 如果是二级评论，删除本身的同时，需要判断该评论是否被会回复了，
     * 如果有被回复，需要将回复的评论删除掉，注意，回复的评论被回复了，均需要删除，这里使用到了递归调用
     * ，直到下级数据全部删除成功为止，避免留下脏数据。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response deleteComment(DeleteCommentReqVO deleteCommentReqVO) {
        Long commentId = deleteCommentReqVO.getId();


        // 查询该评论是一级评论，还是二级评论
        CommentDO commentDO = commentMapper.selectById(commentId);

        // 判断评论是否存在
        if (Objects.isNull(commentDO)) {
            log.warn("该评论不存在, commentId: {}", commentId);
            throw new BizException(ResponseCodeEnum.COMMENT_NOT_FOUND);
        }

        Long loginUserId = getLoginUserId();
        if (!Objects.equals(loginUserId, 1L) && !Objects.equals(commentDO.getUserId(), loginUserId)) {
            throw new BizException(ResponseCodeEnum.UNAUTHORIZED); // 抛出无权限异常
        }
        // 删除评论
        commentMapper.deleteById(commentId);

        Long replayCommentId = commentDO.getReplyCommentId();

        // 一级评论
        if (Objects.isNull(replayCommentId)) {
            // 删除子评论
            commentMapper.deleteByParentCommentId(commentId);
        } else { // 二级评论
            // 删除此评论, 以及此评论下的所有回复
            deleteAllChildComment(commentId);
        }

        return Response.success();
    }

    /**
     * 递归删除所有子评论
     * @param commentId
     */
    private void deleteAllChildComment(Long commentId) {
        // 查询此评论的所有回复
        List<CommentDO> childCommentDOS = commentMapper.selectByReplyCommentId(commentId);

        if (CollectionUtils.isEmpty(childCommentDOS))
            return;

        // 循环递归删除
        childCommentDOS.forEach(childCommentDO -> {
            Long childCommentId = childCommentDO.getId();

            commentMapper.deleteById(childCommentId);
            // 递归调用
            deleteAllChildComment(childCommentId);
        });

    }


    /**
     * 评论审核
     *
     * @param examineCommentReqVO
     * @return
     * 逻辑:
     * 通过提交上来的评论 ID 查询对应的评论；
     * 若为空，则主动抛出业务异常，提示前端该评论不存在；
     * 否则，获取这条评论的状态，判断其状态，如果不等于待审核状态，提示前端状态不正确，无法被审核；
     * 最后，更新数据库中的该条评论；
     */
    @Override
    public Response examine(ExamineCommentReqVO examineCommentReqVO) {
        Long commentId = examineCommentReqVO.getId();
        Integer status = examineCommentReqVO.getStatus();
        String reason = examineCommentReqVO.getReason();
        Long loginUserId = getLoginUserId();

        //权限
        if (!Objects.equals(loginUserId, 1L)) {
            log.warn("非管理员用户尝试执行审核操作, userId: {}", loginUserId);
            throw new BizException(ResponseCodeEnum.UNAUTHORIZED); // 确保你的枚举里有这个错误码
        }

        // 根据提交的评论 ID 查询该条评论
        CommentDO commentDO = commentMapper.selectById(commentId);


        // 判空
        if (Objects.isNull(commentDO)) {
            log.warn("该评论不存在, commentId: {}", commentId);
            throw new BizException(ResponseCodeEnum.COMMENT_NOT_FOUND);
        }

        // 评论当前状态
        Integer currStatus = commentDO.getStatus();

        // 若未处于待审核状态
        if (!Objects.equals(currStatus, CommentStatusEnum.WAIT_EXAMINE.getCode())) {
            log.warn("该评论未处于待审核状态, commentId: {}", commentId);
            throw new BizException(ResponseCodeEnum.COMMENT_STATUS_NOT_WAIT_EXAMINE);
        }

        // 更新评论
        commentMapper.updateById(CommentDO.builder()
                .id(commentId)
                .status(status)
                .reason(reason)
                .updateTime(LocalDateTime.now())
                .build());

        return Response.success();
    }

}