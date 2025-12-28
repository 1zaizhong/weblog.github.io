package com.zifengliu.weblog.common.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zifengliu.weblog.common.domain.dos.ArticleLikeDO;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/28 下午3:18
 * @description
 **/
public interface ArticleLikeMapper extends BaseMapper<ArticleLikeDO> {
    // 关联 t_article 表获取标题和封面
}