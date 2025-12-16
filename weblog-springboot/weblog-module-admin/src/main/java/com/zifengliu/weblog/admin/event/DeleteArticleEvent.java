package com.zifengliu.weblog.admin.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/16 下午5:36
 * @description
 **/

@Getter
public class DeleteArticleEvent extends ApplicationEvent {

    /**
     * 文章 ID
     */
    private Long articleId;

    public DeleteArticleEvent(Object source, Long articleId) {
        super(source);
        this.articleId = articleId;
    }
}