package com.zifengliu.weblog.admin.model.vo.tag;

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
 * @description 删除标签 VO
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value =  "删除标签 VO ")
public class DeleteTagReqVO {
    @NotNull(message =  "标签id 不能为空")
    private Long id;
}
