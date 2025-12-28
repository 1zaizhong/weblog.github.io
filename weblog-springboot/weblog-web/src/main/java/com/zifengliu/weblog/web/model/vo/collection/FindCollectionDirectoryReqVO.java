package com.zifengliu.weblog.web.model.vo.collection;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/28 下午8:43
 * @description
 **/
@Data
@ApiModel(value = "前台查询收藏夹请求 VO")
public class FindCollectionDirectoryReqVO {
    @NotNull(message = "用户 ID 不能为空")
    private Long userId;
}