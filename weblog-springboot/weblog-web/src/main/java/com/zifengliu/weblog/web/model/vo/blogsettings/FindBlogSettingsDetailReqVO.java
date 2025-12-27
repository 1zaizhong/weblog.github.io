package com.zifengliu.weblog.web.model.vo.blogsettings;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/28 上午5:06
 * @description 获取博客设置详情请求 VO
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindBlogSettingsDetailReqVO {

    @NotNull(message = "用户 ID 不能为空")
    private Long userId;

}