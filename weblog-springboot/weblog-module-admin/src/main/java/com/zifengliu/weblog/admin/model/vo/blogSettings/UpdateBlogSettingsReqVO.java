package com.zifengliu.weblog.admin.model.vo.blogSettings;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/5/12 下午4:05
 * @description 更新博客设置请求VO
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = " 博客基础信息修改 VO")
public class UpdateBlogSettingsReqVO {

    @NotBlank(message = "博客 LOGO 不能为空")
    private String logo;

    @NotBlank(message = "博客名称不能为空")
    private String name;

    @NotBlank(message = "博客作者不能为空")
    private String author;


    private String introduction;

    @NotBlank(message = "博客头像不能为空")
    private String avatar;




    @NotNull(message = "请设置评论敏感词过滤是否开启")
    private Boolean isCommentSensiWordOpen;

    @NotNull(message = "请设置评论审核是否开启")
    private Boolean isCommentExamineOpen;
}

