package com.zifengliu.weblog.admin.convert;

import com.zifengliu.weblog.admin.model.vo.wiki.FindWikiPageListRspVO;
import com.zifengliu.weblog.common.domain.dos.WikiDO;

import org.mapstruct.factory.Mappers;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/21 下午5:17
 * @description
 **/

@Mapper
public interface WikiConvert {
    /**
     * 初始化 convert 实例
     */
    WikiConvert INSTANCE = Mappers.getMapper(WikiConvert.class);

    /**
     * WikiDO -> FindWikiPageListRspVO
     * @param bean
     * @return
     */
    @Mapping(target = "isTop", expression = "java(bean.getWeight() > 0)")
    FindWikiPageListRspVO convertDO2VO(WikiDO bean);

}
