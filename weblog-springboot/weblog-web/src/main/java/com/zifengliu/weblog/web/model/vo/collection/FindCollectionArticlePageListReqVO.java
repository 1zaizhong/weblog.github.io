package com.zifengliu.weblog.web.model.vo.collection;

import com.zifengliu.weblog.common.model.BasePageQuery;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2026/1/1 下午9:24
 * @description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "查询收藏夹内文章列表请求 VO")
public class FindCollectionArticlePageListReqVO extends BasePageQuery { // 继承你的分页基础类
    @NotNull(message = "收藏夹 ID 不能为空")
    private Long directoryId;

    @NotNull(message = "用户 ID 不能为空")
    private Long userId;
}