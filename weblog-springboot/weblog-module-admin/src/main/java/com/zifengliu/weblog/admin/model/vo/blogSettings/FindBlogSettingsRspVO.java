package com.zifengliu.weblog.admin.model.vo.blogSettings;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/5/12 下午4:05
 * @description 博客信息详情 请求VO
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindBlogSettingsRspVO {

    private String logo;

    private String name;

    private String author;

    private String introduction;

    private String avatar;





    private Boolean isCommentSensiWordOpen;

    private Boolean isCommentExamineOpen;
}
