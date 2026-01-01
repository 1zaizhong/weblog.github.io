<template>
    <div class="w-full py-5 px-2 mb-3 bg-white border border-gray-200 rounded-lg dark:bg-gray-800 dark:border-gray-700">
        <div class="flex flex-col items-center">
            <!-- 博主头像 -->
            <div class="relative mb-4">
                <img class="w-14 h-14 rounded-full shadow"
                :src="blogSettingsStore.blogSettings.avatar"/>    
                <span
                    class="bottom-0 left-10 absolute w-3.5 h-3.5 bg-green-400 border-2 border-white dark:border-gray-800 rounded-full"></span>
            </div>
            
            <!-- 博主昵称 -->
            <h5 class="mb-2 text-xl font-medium text-gray-900 dark:text-white">{{ blogSettingsStore.blogSettings.author }}</h5>
            <!-- 介绍语 -->
            <span class="mb-6 text-sm text-gray-500 dark:text-gray-400" data-tooltip-target="introduction-tooltip-bottom"
                data-tooltip-placement="bottom">{{ blogSettingsStore.blogSettings.introduction }}</span>
            <div id="introduction-tooltip-bottom" role="tooltip"
                class="absolute z-10 invisible inline-block px-3 py-2 text-xs font-medium text-white bg-gray-900 rounded shadow-sm opacity-0 tooltip dark:bg-gray-700">
                介绍语
                <div class="tooltip-arrow" data-popper-arrow></div>
            </div>


             <!-- 文章数量、分类数量、标签数量、总访问量 -->
             <!-- flex 布局，justify-center 水平居中，gap-5 设置 flex 内子元素的间距 -->
             <div class="flex justify-center gap-5 mb-2 dark:text-gray-400">
                <!-- flex 布局，items-center 垂直居中，flex-col 设置子元素上下排列，hover: 用于设置鼠标移动到上面的样式，字体颜色、放大效果，cursor-pointer 指定鼠标移动到上面为小手指样式 -->
                <div 
                    class="flex items-center flex-col gap-1 hover:text-sky-600 hover:scale-110 cursor-pointer">
                    <!-- 字体大小为 text-lg , 字体加粗 -->
                    <CountTo :value="statisticsInfo.articleTotalCount" customClass="text-lg font-bold"></CountTo>
                    <!-- 字体大小为 text-sm -->
                    <div class="text-sm">文章</div>
                </div>
                <div 
                    class="flex items-center flex-col gap-1 hover:text-sky-600 hover:scale-110 cursor-pointer">
                    <CountTo :value="statisticsInfo.categoryTotalCount" customClass="text-lg font-bold"></CountTo>
                    <div class="text-sm">分类</div>
                </div>
                <div
                    class="flex items-center flex-col gap-1 hover:text-sky-600 hover:scale-110 cursor-pointer">
                    <CountTo :value="statisticsInfo.tagTotalCount" customClass="text-lg font-bold"></CountTo>
                    <div class="text-sm">标签</div>
                </div>
                <div class="flex items-center flex-col gap-1">
                    <CountTo :value="statisticsInfo.pvTotalCount" customClass="text-lg font-bold"></CountTo>
                    <div class="text-sm">总访问量</div>
                </div>
            </div>

            
        </div>
    </div>
</template>
<script setup>
import { useBlogSettingsStore } from '@/stores/blogsettings'
import { initTooltips } from 'flowbite'
import { onMounted, ref } from 'vue'
import { getStatisticsInfo } from '@/api/frontend/statistics'
import CountTo from '@/components/CountTo.vue'

const blogSettingsStore = useBlogSettingsStore()

const statisticsInfo = ref({
    articleTotalCount: 0,
    categoryTotalCount: 0,
    tagTotalCount: 0,
    pvTotalCount: 0
})

onMounted(() => {
    initTooltips();

    // 1. 获取本地存储的用户信息
    const userStr = localStorage.getItem('user')
    if (userStr) {
        const userObj = JSON.parse(userStr)
        const currentLoginUserId = userObj.userInfo?.userID 

        if (currentLoginUserId) {
            
            blogSettingsStore.getBlogSettings(currentLoginUserId)

            // 3. 获取统计信息
            getStatisticsInfo({ userId: currentLoginUserId }).then((res) => {
                if (res.success) {
                    statisticsInfo.value = res.data
                }
            })
        }
    }
})
</script>