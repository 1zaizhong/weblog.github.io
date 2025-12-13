package com.zifengliu.weblog.web.markdown;

import com.zifengliu.weblog.web.markdown.provider.NofollowLinkAttributeProvider;
import com.zifengliu.weblog.web.markdown.renderer.ImageNodeRenderer;
import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.heading.anchor.HeadingAnchorExtension;
import org.commonmark.ext.image.attributes.ImageAttributesExtension;
import org.commonmark.ext.task.list.items.TaskListItemsExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.Arrays;
import java.util.List;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/13 下午2:26
 * @description MarkdownHelper 工具类
 **/

public class MarkdownHelper {

    /**
     * Markdown 解析器
     */
    private final static Parser PARSER;

    /**
     * HTML 渲染器
     */
    private final static HtmlRenderer HTML_RENDERER;


    /**
     * 初始化
     */
    static {
        // Markdown 拓展
        List<Extension> extensions = Arrays.asList(
                TablesExtension.create(), // 表格拓展
                HeadingAnchorExtension.create(), // 标题锚定项
                ImageAttributesExtension.create(), // 图片宽高
                TaskListItemsExtension.create() // 任务列表
        );

        PARSER = Parser.builder().extensions(extensions).build();
        HTML_RENDERER = HtmlRenderer.builder()
                .extensions(extensions)
                .attributeProviderFactory(context -> new NofollowLinkAttributeProvider())
                .nodeRendererFactory(context -> new ImageNodeRenderer(context))
                .build();
    }



    /**
     * 将 Markdown 转换成 HTML
     * @param markdown
     * @return
     * markdown 文本转换为 HTML 代码
     */
    public static String convertMarkdown2Html(String markdown) {
        Node document = PARSER.parse(markdown);
        return HTML_RENDERER.render(document);
    }

    // 测试
    public static void main(String[] args) {

        //markdown 文本转换为 HTML 代码
        //String markdown = "This is *Sparta*";
        //System.out.println(MarkdownHelper.convertMarkdown2Html(markdown));

        //markdown 表格转换的
        String markdown = "| First Header  | Second Header |\n" +
                "| ------------- | ------------- |\n" +
                "| Content Cell  | Content Cell  |\n" +
                "| Content Cell  | Content Cell  |";
        System.out.println(MarkdownHelper.convertMarkdown2Html(markdown));

        //markdown 标题转换的
        String markdow = "# 一级标题\n" +
                "## 二级标题\n";
        System.out.println(MarkdownHelper.convertMarkdown2Html(markdow));

        ////markdown 图片大小
        String q = "![text](/url.png){width=640 height=480}";
        System.out.println(MarkdownHelper.convertMarkdown2Html(q));

        String mwn = "[个人网站域名](http://www.xxx.com \"个人网站域名\")\n" +
                "\n" +
                "[第三方网站域名](http://www.baidu.com \"第三方网站域名\")";
        System.out.println(MarkdownHelper.convertMarkdown2Html(mwn));


        //
        String a = "![图 1-1 技术栈](https://img.quanxiaoha.com/quanxiaoha/169560181378937 \"图 1-1 技术栈\"){width=100 height=100}";
        System.out.println(MarkdownHelper.convertMarkdown2Html(a));

    }

}
