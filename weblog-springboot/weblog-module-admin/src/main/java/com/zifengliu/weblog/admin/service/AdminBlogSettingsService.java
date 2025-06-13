package com.zifengliu.weblog.admin.service;

import com.zifengliu.weblog.admin.model.vo.blogSettings.UpdateBlogSettingsReqVO;
import com.zifengliu.weblog.common.utils.Response;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/5/12 下午4:10
 * @description  博客设置 接口
 **/
public interface AdminBlogSettingsService {
    /*
    * 更新博客设置
    * @param updateBlogSettingsReqVO
    * @return
    * */
    Response updateBlogSettings(UpdateBlogSettingsReqVO updateBlogSettingsReqVO);

    /*
    * 获取博客详情
    * @return
    * */
    Response findDetail();
}
