import axios from "@/axios";

// 获取博客设置详情
// api/frontend/blogsettings.js

export function getBlogSettingsDetail(userId) {
  
    return axios.post("/blog/settings/detail", { userId: userId })
}

