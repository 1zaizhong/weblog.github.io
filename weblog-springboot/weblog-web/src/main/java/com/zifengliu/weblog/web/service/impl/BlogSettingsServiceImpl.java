package com.zifengliu.weblog.web.service.impl;

import com.zifengliu.weblog.common.domain.dos.BlogSettingsDO;
import com.zifengliu.weblog.common.domain.mapper.BlogSettingsMapper;
import com.zifengliu.weblog.common.utils.Response;
import com.zifengliu.weblog.web.convert.BlogSettingsConvert;
import com.zifengliu.weblog.web.model.vo.blogsettings.FindBlogSettingsDetailRspVO;
import com.zifengliu.weblog.web.service.BlogSettingsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/10/17 下午5:07
 * @description
 **/
@Service
@Slf4j
public class BlogSettingsServiceImpl  implements BlogSettingsService {

    @Autowired
    private BlogSettingsMapper blogSettingsMapper;

    /*\
    * 获取博客设置信息
    * @return
    * */
    @Override
    public Response findDetail() {
        //查询博客信息(约定的ID 为1)
        BlogSettingsDO blogSettingsDO = blogSettingsMapper.selectById(1L);

        //DO 转VO
        FindBlogSettingsDetailRspVO vo = BlogSettingsConvert.BLOG_SETTINGS_CONVERT.convertDo2VO(blogSettingsDO);

         return Response.success(vo);
    }
}
