package com.zifengliu.weblog.admin.model.vo.article;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/5/13 下午9:46
 * @description 更新文章请求
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "更新文章请求 VO")
public class UpdateArticleReqVO {

    @NotNull(message = "文章id不能为空")
    private  Long id;

    @NotBlank(message = "标题不能为空")
    @Length(min = 1, max = 40, message = "标题长度在1-40之间")
    private  String title;

    @NotBlank(message = "内容不能为空")
    private  String content;

    @NotBlank(message = "封面不能为空")
    private  String cover;

    private  String summary;

    @NotNull(message = "分类不能为空")
    private Long categoryId;

    @NotEmpty(message = "文章标签不能为空")
    private List<String> tags;

}
