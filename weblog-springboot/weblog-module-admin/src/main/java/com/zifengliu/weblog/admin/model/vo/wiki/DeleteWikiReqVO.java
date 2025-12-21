package com.zifengliu.weblog.admin.model.vo.wiki;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/21 下午5:02
 * @description
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "删除知识库 VO")
public class DeleteWikiReqVO {

    @NotNull(message = "知识库 ID 不能为空")
    private Long id;
}
