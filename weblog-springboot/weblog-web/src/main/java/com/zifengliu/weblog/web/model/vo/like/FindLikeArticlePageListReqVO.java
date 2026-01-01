package com.zifengliu.weblog.web.model.vo.like;

import com.zifengliu.weblog.common.model.BasePageQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2026/1/1 下午10:45
 * @description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindLikeArticlePageListReqVO extends BasePageQuery {
    private Long userId;
}