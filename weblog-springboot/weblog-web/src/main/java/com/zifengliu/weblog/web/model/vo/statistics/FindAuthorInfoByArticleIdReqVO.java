package com.zifengliu.weblog.web.model.vo.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2026/1/2 上午4:20
 * @description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindAuthorInfoByArticleIdReqVO {
    @NotNull(message = "文章 ID 不能为空")
    private Long articleId;
}