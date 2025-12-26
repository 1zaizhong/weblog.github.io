package com.zifengliu.weblog.web.model.vo.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/27 上午12:37
 * @description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindCategoryListReqVO {
    private Long getUserId;

}
