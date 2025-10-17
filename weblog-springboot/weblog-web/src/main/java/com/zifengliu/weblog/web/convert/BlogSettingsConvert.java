package com.zifengliu.weblog.web.convert;

import com.zifengliu.weblog.common.domain.dos.BlogSettingsDO;
import com.zifengliu.weblog.web.model.vo.blogsettings.FindBlogSettingsDetailRspVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/10/17 下午5:14
 * @description
 **/
@Mapper
public interface BlogSettingsConvert {
    /*
    * 初始化convert实例
    * */
    BlogSettingsConvert BLOG_SETTINGS_CONVERT=Mappers.getMapper(BlogSettingsConvert.class);

    /*将DO 转换成 VO
    * @param bean
    * @return
    * */
    FindBlogSettingsDetailRspVO convertDo2VO(BlogSettingsDO bean);
}
