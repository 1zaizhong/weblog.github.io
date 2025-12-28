package com.zifengliu.weblog.admin.model.vo.user;

import com.zifengliu.weblog.common.model.BasePageQuery;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/28 下午11:59
 * @description
 **/
@Data
@ApiModel(value = "查询用户列表请求 VO")
public class FindUserPageListReqVO extends BasePageQuery {
    // 扩展查询条件，比如按用户名搜索
    private String username;
}
