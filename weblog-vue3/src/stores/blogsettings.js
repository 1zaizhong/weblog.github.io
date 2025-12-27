import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getBlogSettingsDetail } from '@/api/frontend/blogsettings'

export const useBlogSettingsStore = defineStore('blogsettings', () => {
  // 1. 初始化博客设置对象
  const blogSettings = ref({
    avatar: '',
    author: '',
    introduction: ''
  })

  // 2. ，增加 userId 参数
  function getBlogSettings(userId) {
    console.log('正在获取 ID 为', userId, '的博客设置')
    // 将 userId 传给 API
    return getBlogSettingsDetail(userId).then(res => {
      if (res.success) {
        blogSettings.value = res.data
      }
      return res
    })
  }

  return { blogSettings, getBlogSettings }
})