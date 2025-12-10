package com.zifengliu.weblog.web.model.vo.archive;

import com.zifengliu.weblog.common.model.BasePageQuery;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/10 下午3:18
 * @description
 **/
@Data
@Builder
@ApiModel(value = "文章归档分页 VO")
public class FindArchiveArticlePageListReqVO extends BasePageQuery {
}

