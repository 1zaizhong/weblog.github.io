package com.zifengliu.weblog.admin.model.vo.tag;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/4/21 下午7:53
 * @description 标签
 **/
@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
@ApiModel(value = "添加标签 VO")
public class AddTagReqVO {

    @NotEmpty(message = "标签集合不能为空")
    private List<String> tags;



}
