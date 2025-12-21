package com.zifengliu.weblog.web.convert;

import com.zifengliu.weblog.common.domain.dos.CommentDO;
import com.zifengliu.weblog.web.model.vo.comment.FindCommentItemRspVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/21 下午10:45
 * @description
 **/
@Mapper
public interface CommentConvert {
    /**
     * 初始化 convert 实例
     */
    CommentConvert INSTANCE = Mappers.getMapper(CommentConvert.class);

    /**
     * CommentDO -> FindCommentItemRspVO
     * @param bean
     * @return
     */
    FindCommentItemRspVO convertDO2VO(CommentDO bean);

}
