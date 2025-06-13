package com.zifengliu.weblog.admin.model.vo.tag;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/4/21 下午7:53
 * @description 标签模糊查询VO
 **/
@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
@ApiModel(value = "标签模糊查询VO")
public class SearchTagReqVO {

    @NotBlank(message = "标签查询关键词不能为空")
    private String key;
}
