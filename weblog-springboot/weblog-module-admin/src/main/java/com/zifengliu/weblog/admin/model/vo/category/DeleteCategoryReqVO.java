package com.zifengliu.weblog.admin.model.vo.category;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/4/20 下午4:55
 * @description 删除分类 VO
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value =  "删除分类 VO ")
public class DeleteCategoryReqVO {

    @NotNull(message =  "分类id 不能为空")
    private Long id;
}
