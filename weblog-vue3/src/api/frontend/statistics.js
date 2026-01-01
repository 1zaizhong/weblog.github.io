import axios from "@/axios";

// 获取统计信息（文章总数、分类总数、标签总数、总访问量）
export function getStatisticsInfo(data) {
    return axios.post("/statistics/info", data)
}

/**
 * 根据文章 ID 获取作者详细统计信息（包括博主信息、文章数据、粉丝数等）
 * @param {Object} data - { articleId: 123 }
 */
export function getAuthorInfo(articleId) {
    return axios.post("/statistics/author/info", { articleId });
}
// 获取博主看板统计数据
export function getAuthorDashboard(authorId) {
    return axios.post("/statistics/author/dashboard", { authorId });
}