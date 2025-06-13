package com.zifengliu.weblog.admin.convert;

import com.zifengliu.weblog.admin.model.vo.article.FindArticleDetailRspVO;
import com.zifengliu.weblog.common.domain.dos.ArticleDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/5/14 下午10:02
 * @description
 **/
@Mapper
public interface ArticleDetailConvert {
    /*
    * 初始化convert 实例
    * */
    ArticleDetailConvert INSTANCE = Mappers.getMapper(ArticleDetailConvert.class);
    /*
    * DO转VO
    * */
    FindArticleDetailRspVO convertDO2VO(ArticleDO bean);
}
