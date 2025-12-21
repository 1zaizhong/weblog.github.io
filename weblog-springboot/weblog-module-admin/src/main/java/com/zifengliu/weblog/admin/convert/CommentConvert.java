package com.zifengliu.weblog.admin.convert;

import com.zifengliu.weblog.admin.model.vo.comment.FindCommentPageListRspVO;
import com.zifengliu.weblog.common.domain.dos.CommentDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/22 上午12:12
 * @description  评论 DO 转换为 VO
 **/

@Mapper
public interface CommentConvert {
    /**
     * 初始化 convert 实例
     */
    CommentConvert INSTANCE = Mappers.getMapper(CommentConvert.class);

    /**
     * 将 DO 转化为 VO
     * @param bean
     * @return
     */
    FindCommentPageListRspVO convertDO2VO(CommentDO bean);

}