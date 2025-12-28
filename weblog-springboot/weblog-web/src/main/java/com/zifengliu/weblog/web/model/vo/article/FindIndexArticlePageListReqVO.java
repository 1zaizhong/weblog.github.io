package com.zifengliu.weblog.web.model.vo.article;

import com.zifengliu.weblog.common.model.BasePageQuery;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: 粟英朝
 * @date: 2025-09-15 14:07
 * @description: 首页-文章分页
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindIndexArticlePageListReqVO extends BasePageQuery {
    private Long current;
    private Long size;
    private Long userId;


}
