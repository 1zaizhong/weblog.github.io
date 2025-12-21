package com.zifengliu.weblog.admin.model.vo.wiki;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/21 下午5:45
 * @description
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "更新知识库目录数据入参 VO")
public class UpdateWikiCatalogReqVO {

    /**
     * 知识库 ID
     */
    @NotNull(message = "知识库 ID 不能为空")
    private Long id;

    /**
     * 目录
     * 针对集合 UpdateWikiCatalogItemReqVO 进行字段校验
     */
    @Valid
    private List<UpdateWikiCatalogItemReqVO> catalogs;


}