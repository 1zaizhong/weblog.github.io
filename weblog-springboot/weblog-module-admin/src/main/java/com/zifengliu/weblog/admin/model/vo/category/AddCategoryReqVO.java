package com.zifengliu.weblog.admin.model.vo.category;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;


import javax.validation.constraints.NotBlank;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/4/9 下午9:03
 * @description 前端对应数据实体
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel( value = "添加分类VO")
public class AddCategoryReqVO {
    @NotBlank(message = "分类名字不能为空")
    @Length(min = 1,max = 20,message = "分类名字长度在1-20之间")
    private  String name;
}
