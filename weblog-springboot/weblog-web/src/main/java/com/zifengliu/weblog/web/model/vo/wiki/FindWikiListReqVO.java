package com.zifengliu.weblog.web.model.vo.wiki;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/27 下午4:22
 * @description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindWikiListReqVO {
    Long userId;
}
