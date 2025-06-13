package com.zifengliu.weblog.admin.convert;

import com.zifengliu.weblog.admin.model.vo.blogSettings.FindBlogSettingsRspVO;
import com.zifengliu.weblog.admin.model.vo.blogSettings.UpdateBlogSettingsReqVO;
import com.zifengliu.weblog.common.domain.dos.BlogSettingsDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/5/12 下午5:12
 * @description
 **/
@Mapper
public interface BlogSettingsConvert {
    /**
     * 初始化 convert 实例
     */
    BlogSettingsConvert INSTANCE = Mappers.getMapper(BlogSettingsConvert.class);


    /**
     * 将 VO 转化为 DO
     * @param bean
     * @return
     */
    BlogSettingsDO convertVO2DO(UpdateBlogSettingsReqVO bean);

    /**
     * 将 VO 转化为 DO
     * @param bean
     * @return
     */
    FindBlogSettingsRspVO convertDO2VO(BlogSettingsDO bean);


}