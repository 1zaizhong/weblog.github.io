import axios from "@/axios";

// 登录接口
export function login(username, password) {
    return axios.post("/login", {username, password})
}

// 获取登录用户信息
export function getUserInfo() {
    return axios.post("/admin/user/info")
}

// 修改用户密码
export function updateAdminPassword(data) {
    return axios.post("/admin/password/update", data)
}
export function addUser(data) {
    return axios.post("/admin/user/add", data)
}
// 添加这个导出
export function deleteUser(userId) {
    return axios.post("/admin/user/delete", { userId })
}

// 获取用户列表
export function findAllUsers() {
    return axios.post("/admin/user/list/all")
}

export function searchUser(username) {
    return axios.post("/admin/user/search", { 
        username: username 
    })
}