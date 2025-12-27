package com.zifengliu.weblog.web.service;

import com.zifengliu.weblog.admin.model.vo.article.FindArticleDetailReqVO;
import com.zifengliu.weblog.common.utils.Response;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/10/17 下午5:07
 * @description
 **/
public interface BlogSettingsService {
    /**
     * 获取博客设置详情
     * @param userId
     * @return
     */
    Response findDetail(Long userId);

}
