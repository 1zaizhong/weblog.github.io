package com.zifengliu.weblog.web.model.vo.collection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2026/1/1 下午10:12
 * @description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CollectionDirectoryRspVO {

    private Long id;

    private String name;

    /**
     * 该收藏夹下的文章总数
     */
    private Integer articlesTotal;
}
