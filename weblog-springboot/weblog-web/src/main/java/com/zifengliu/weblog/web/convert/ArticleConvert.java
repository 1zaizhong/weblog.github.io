package com.zifengliu.weblog.web.convert;

import com.zifengliu.weblog.common.domain.dos.ArticleDO;
import com.zifengliu.weblog.web.model.vo.article.FindIndexArticlePageListRspVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author: 粟英朝
 * @url: www.quanxiaoha.com
 * @date: 2023/10/8 14:57
 * @description: 文章转换
 **/
@Mapper
public interface ArticleConvert {
    /**
     * 初始化 convert 实例
     */
    ArticleConvert INSTANCE = Mappers.getMapper(ArticleConvert.class);

    /**
     * 将 DO 转化为 VO
     * @param bean
     * @return
     */
    FindIndexArticlePageListRspVO convertDO2VO(ArticleDO bean);

}
