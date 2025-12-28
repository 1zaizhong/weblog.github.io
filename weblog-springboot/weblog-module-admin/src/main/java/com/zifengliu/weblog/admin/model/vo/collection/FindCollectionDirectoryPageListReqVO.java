package com.zifengliu.weblog.admin.model.vo.collection;

import com.zifengliu.weblog.common.model.BasePageQuery;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/28 下午6:52
 * @description
 **/
@Data
@ApiModel(value = "查询收藏夹分页数据入参 VO")
public class FindCollectionDirectoryPageListReqVO extends BasePageQuery {
    // 继承 current, size
}