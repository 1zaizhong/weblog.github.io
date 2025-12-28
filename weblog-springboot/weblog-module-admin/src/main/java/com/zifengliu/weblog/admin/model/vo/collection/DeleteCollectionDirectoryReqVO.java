package com.zifengliu.weblog.admin.model.vo.collection;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/28 下午6:52
 * @description
 **/
@Data
@ApiModel(value = "删除收藏夹 VO")
public class DeleteCollectionDirectoryReqVO {
    @NotNull(message = "收藏夹 ID 不能为空")
    private Long id;
}