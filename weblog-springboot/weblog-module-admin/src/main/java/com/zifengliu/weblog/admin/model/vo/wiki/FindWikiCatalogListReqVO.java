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
 * @date 2025/12/21 下午5:33
 * @description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "查询知识库目录数据入参 VO")
public class FindWikiCatalogListReqVO {

    /**
     * 知识库 ID
     */
    @NotNull(message = "知识库 ID 不能为空")
    private Long id;

}