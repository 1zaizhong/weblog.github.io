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
// 获取热门文章排行
export function getHotArticleList() {
    return axios.post("/article/hot/list", {})
}
// 分页查询我的点赞列表
export function getLikeArticlePageList(data) {
    return axios.post("/article/like/list", data);
}
// 获取指定博主（用户ID）的文章分页列表
export function getAuthorArticlePageList(data) {
    return axios.post("/article/author/page", data);
}