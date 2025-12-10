package com.zifengliu.weblog.web.service;

import com.zifengliu.weblog.common.utils.Response;
import com.zifengliu.weblog.web.model.vo.archive.FindArchiveArticlePageListReqVO;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/10 下午3:20
 * @description
 **/
public interface ArchiveService {
    /**
     * 获取文章归档分页数据
     * @param findArchiveArticlePageListReqVO
     * @return
     */
    Response findArchivePageList(FindArchiveArticlePageListReqVO findArchiveArticlePageListReqVO);
}
