package com.zifengliu.weblog.admin.service;

import com.zifengliu.weblog.admin.model.vo.wiki.AddWikiReqVO;
import com.zifengliu.weblog.common.utils.Response;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/21 下午4:55
 * @description
 **/

public interface AdminWikiService {

    /**
     * 新增知识库
     * @param addWikiReqVO
     * @return
     */
    Response addWiki(AddWikiReqVO addWikiReqVO);
}
