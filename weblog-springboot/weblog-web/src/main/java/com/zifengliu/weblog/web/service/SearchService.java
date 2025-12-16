package com.zifengliu.weblog.web.service;

import com.zifengliu.weblog.common.utils.Response;
import com.zifengliu.weblog.web.search.SearchArticlePageListReqVO;
import lombok.Builder;
import org.springframework.stereotype.Service;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/16 下午4:16
 * @description
 **/
public interface SearchService {
    /**
     * 关键词分页搜索
     * @param searchArticlePageListReqVO
     * @return
     */
    Response searchArticlePageList(SearchArticlePageListReqVO searchArticlePageListReqVO);
}
