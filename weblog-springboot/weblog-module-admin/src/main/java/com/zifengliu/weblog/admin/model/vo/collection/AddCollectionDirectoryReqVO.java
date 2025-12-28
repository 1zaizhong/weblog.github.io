package com.zifengliu.weblog.admin.model.vo.collection;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/28 下午6:51
 * @description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "新增收藏夹 VO")
public class AddCollectionDirectoryReqVO {
    @NotBlank(message = "收藏夹名称不能为空")
    private String name;
}