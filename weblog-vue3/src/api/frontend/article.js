import axios from "@/axios";

// 获取文章列表
export function getArticlePageList(data) {
    return axios.post("/article/list", data)
}

// 获取文章详情
export function getArticleDetail(articleId, userId) {
    return axios.post('/article/detail', { 
        articleId: articleId,
        userId: userId 
    })
}

// 点赞/取消点赞文章
export function likeArticle(data) {
    return axios.post('/article/like', data)
}

// 检查是否已点赞
export function checkArticleIsLiked(articleId, userId) {
    return axios.post('/article/isLiked', { articleId, userId })
}
