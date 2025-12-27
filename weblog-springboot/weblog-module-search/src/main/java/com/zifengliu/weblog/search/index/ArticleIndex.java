package com.zifengliu.weblog.search.index;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/16 下午3:41
 * @description
 **/
public interface ArticleIndex {
    /**
     * 索引名称
     */
    String NAME = "article";

    // --------------------- 文档字段 ---------------------
    String COLUMN_ID = "id";

    String COLUMN_TITLE = "title";

    String COLUMN_COVER = "cover";

    String COLUMN_SUMMARY = "summary";

    String COLUMN_CONTENT = "content";

    String COLUMN_CREATE_TIME = "createTime";

    public static final String COLUMN_STATUS = "status";
}
