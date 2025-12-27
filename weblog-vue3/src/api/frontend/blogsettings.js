import axios from "@/axios";

// 获取博客设置详情
// api/frontend/blogsettings.js

export function getBlogSettingsDetail(userId) {
    // 注意：这里传的是一个对象，对应后端的 ReqVO
    return axios.post("/blog/settings/detail", { userId: userId })
}

