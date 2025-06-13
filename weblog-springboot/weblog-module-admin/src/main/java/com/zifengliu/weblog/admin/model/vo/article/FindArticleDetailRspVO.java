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
 * @description 发布文章请求
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindArticleDetailRspVO {
    private Long id;

    //标题
    private  String title;


    //内容
    private  String content;


    /*封面
    * */
    private  String cover;

    /*
    * 摘要
    * */
    private  String summary;


    /*
    * 分类*/
    private Long categoryId;

    /*标签集合*/
    private List<Long> tagIds;

}
