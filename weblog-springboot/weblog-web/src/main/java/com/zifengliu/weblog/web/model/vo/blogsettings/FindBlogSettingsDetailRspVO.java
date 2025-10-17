package com.zifengliu.weblog.web.model.vo.blogsettings;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/10/17 下午5:06
 * @description 获取博客设置信息 出参
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindBlogSettingsDetailRspVO {
    private  String logo;
    private String name;
    private  String author;
    private String introduction;
    private  String avatar;
    private String githubHomePage;
    private String giteeHomePage;
    private String csdnHomePage;
    private String zhihuHomePage;

}
