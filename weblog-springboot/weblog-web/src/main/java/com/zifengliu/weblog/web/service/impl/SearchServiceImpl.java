package com.zifengliu.weblog.web.service.impl;

import com.google.common.collect.Lists;
import com.zifengliu.weblog.common.utils.PageResponse;
import com.zifengliu.weblog.common.utils.Response;
import com.zifengliu.weblog.search.LuceneHelper;
import com.zifengliu.weblog.search.config.LuceneProperties;
import com.zifengliu.weblog.search.index.ArticleIndex;
import com.zifengliu.weblog.web.search.SearchArticlePageListReqVO;
import com.zifengliu.weblog.web.search.SearchArticlePageListRspVO;
import com.zifengliu.weblog.web.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.StringReader;
import java.util.List;


/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/16 下午4:18
 * @description
 **/

/**
 * 文章搜索业务实现类
 */
@Service
@Slf4j
public class SearchServiceImpl implements SearchService {

    @Autowired
    private LuceneProperties luceneProperties;
    @Autowired
    private LuceneHelper luceneHelper;

    @Override
    public Response searchArticlePageList(SearchArticlePageListReqVO searchArticlePageListReqVO) {
        int current = Math.toIntExact(searchArticlePageListReqVO.getCurrent());
        int size = Math.toIntExact(searchArticlePageListReqVO.getSize());
        // 查询关键词
        String word = searchArticlePageListReqVO.getWord();

        // 1. ======================== 构建组合查询条件 ========================
        Analyzer analyzer = new SmartChineseAnalyzer();
        BooleanQuery.Builder booleanQueryBuilder = new BooleanQuery.Builder();

        try {
            // 条件 A: 关键词匹配 (在标题和摘要中搜索)
            String[] columns = {ArticleIndex.COLUMN_TITLE, ArticleIndex.COLUMN_SUMMARY};
            MultiFieldQueryParser multiParser = new MultiFieldQueryParser(columns, analyzer);
            Query wordQuery = multiParser.parse(word);
            // MUST 表示必须包含关键词
            booleanQueryBuilder.add(wordQuery, BooleanClause.Occur.MUST);

            // 条件 B: 状态过滤 (必须是已发布状态 2)
            // 这里的 IntPoint.newExactQuery 必须对应之前 InitLuceneIndexRunner 存入的 IntPoint 字段
            Query statusQuery = IntPoint.newExactQuery(ArticleIndex.COLUMN_STATUS, 2);
            // MUST 表示必须满足状态为 2
            booleanQueryBuilder.add(statusQuery, BooleanClause.Occur.MUST);

        } catch (ParseException e) {
            log.error("构建 Lucene 查询条件异常: ", e);
        }

        // 生成最终的布尔查询对象
        BooleanQuery finalQuery = booleanQueryBuilder.build();

        // 2. ======================== 执行搜索 ========================
        // 调用 LuceneHelper 中我们新增加的、接收 Query 对象的方法
        long total = luceneHelper.searchTotal(ArticleIndex.NAME, finalQuery);
        List<Document> documents = luceneHelper.search(ArticleIndex.NAME, finalQuery, current, size);

        // 若未查询到相关文档，直接返回
        if (CollectionUtils.isEmpty(documents)) {
            return PageResponse.success(total, current, size, null);
        }

        // 3. ======================== 关键词高亮处理 ========================
        // 高亮只需要针对关键词 word 部分进行解析
        Query highlightQuery = null;
        try {
            highlightQuery = new QueryParser(ArticleIndex.COLUMN_TITLE, analyzer).parse(word);
        } catch (ParseException e) {
            log.error("解析高亮关键词错误:", e);
        }

        // 创建高亮器
        SimpleHTMLFormatter formatter = new SimpleHTMLFormatter("<span style=\"color: #f73131\">", "</span>");
        Highlighter highlighter = new Highlighter(formatter, new QueryScorer(highlightQuery));

        // 返参 VO
        List<SearchArticlePageListRspVO> vos = Lists.newArrayList();

        // 遍历查询到的文档，进行关键词高亮处理
        documents.forEach(document -> {
            try {
                // 文章标题
                String title = document.get(ArticleIndex.COLUMN_TITLE);

                // 获取高亮的片段
                TokenStream tokenStream = analyzer.tokenStream(ArticleIndex.COLUMN_TITLE, new StringReader(title));
                String titleFragment = highlighter.getBestFragment(tokenStream, title);

                // 如果没有匹配到关键词，则返回原始文本
                String highlightedTitle = StringUtils.isNoneBlank(titleFragment) ? titleFragment : title;

                String id = document.get(ArticleIndex.COLUMN_ID);
                String cover = document.get(ArticleIndex.COLUMN_COVER);
                String createTime = document.get(ArticleIndex.COLUMN_CREATE_TIME);
                String summary = document.get(ArticleIndex.COLUMN_SUMMARY);

                // 组装 VO
                SearchArticlePageListRspVO vo = SearchArticlePageListRspVO.builder()
                        .id(Long.valueOf(id))
                        .title(highlightedTitle)
                        .summary(summary)
                        .cover(cover)
                        .createDate(createTime)
                        .build();

                vos.add(vo);
            } catch (Exception e) {
                log.error("文档转换错误: ", e);
            }
        });

        return PageResponse.success(total, current, size, vos);
    }
}