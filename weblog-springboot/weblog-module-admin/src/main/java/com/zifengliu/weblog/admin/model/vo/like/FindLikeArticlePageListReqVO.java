package com.zifengliu.weblog.admin.model.vo.like;

import com.zifengliu.weblog.common.model.BasePageQuery;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/28 下午5:26
 * @description
 **/
@Data
@ApiModel(value = "查询点赞分页数据入参 VO")
public class FindLikeArticlePageListReqVO extends BasePageQuery {

}