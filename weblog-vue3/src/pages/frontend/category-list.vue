<template>
    <Header></Header>

    <main class="container max-w-screen-2xl mx-auto px-4 md:px-6 py-4">
        
        <div class="grid grid-cols-12 gap-7">
            
            <div class="col-span-12 md:col-span-9 mb-3">
                
                <div class="w-full p-5 bg-white border border-gray-200 rounded-lg dark:bg-gray-800 dark:border-gray-700 shadow-sm">
                    
                    <div class="flex flex-col md:flex-row items-start md:items-center">
                        <h2 class="flex items-center flex-shrink-0 mr-6 font-bold text-gray-900 uppercase dark:text-white mb-4 md:mb-0">
                            <svg t="1698998570037" class="inline icon w-5 h-5 mr-2" viewBox="0 0 1024 1024" version="1.1"
                                xmlns="http://www.w3.org/2000/svg" p-id="21572" width="200" height="200">
                                <path d="M938.666667 464.592593h-853.333334v-265.481482c0-62.577778 51.2-113.777778 113.777778-113.777778h128.948148c15.17037 0 28.444444 3.792593 41.718519 11.377778l98.607407 64.474074h356.503704c62.577778 0 113.777778 51.2 113.777778 113.777778v189.62963z" fill="#3A69DD"></path>
                                <path d="M805.925926 398.222222h-587.851852v-125.155555c0-24.651852 20.859259-45.511111 45.511111-45.511111h496.82963c24.651852 0 45.511111 20.859259 45.511111 45.511111V398.222222z" fill="#D9E3FF"></path>
                                <path d="M843.851852 417.185185h-663.703704v-98.607407c0-28.444444 22.755556-53.096296 53.096296-53.096297h559.407408c28.444444 0 53.096296 22.755556 53.096296 53.096297V417.185185z" fill="#FFFFFF"></path>
                                <path d="M786.962963 938.666667h-549.925926c-83.437037 0-151.703704-68.266667-151.703704-151.703704V341.333333s316.681481 37.925926 430.45926 37.925926c189.62963 0 422.874074-37.925926 422.874074-37.925926v445.62963c0 83.437037-68.266667 151.703704-151.703704 151.703704z" fill="#5F7CF9"></path>
                                <path d="M559.407407 512h-75.851851c-20.859259 0-37.925926-17.066667-37.925926-37.925926s17.066667-37.925926 37.925926-37.925926h75.851851c20.859259 0 37.925926 17.066667 37.925926 37.925926s-17.066667 37.925926-37.925926 37.925926z" fill="#F9D523"></path>
                            </svg>
                            <span>专栏 <span class="ml-1 text-gray-400 font-normal">({{ categories.length }})</span></span>
                        </h2>

                        <div class="text-sm flex flex-wrap gap-3 font-medium text-gray-600 dark:text-white">
                            <a @click="goCategoryArticleListPage(category.id, category.name)"
                                v-for="(category, index) in categories" :key="index"
                                class="cursor-pointer inline-flex items-center px-4 py-2 text-xs font-bold text-center border border-gray-200 rounded-lg hover:bg-sky-50 hover:text-sky-700 hover:border-sky-200 transition-all dark:bg-gray-800 dark:text-gray-300 dark:hover:bg-gray-700 dark:border-gray-700">
                                {{ category.name }}
                                <span class="inline-flex items-center justify-center w-5 h-5 ms-2 text-[10px] font-semibold text-sky-800 bg-sky-200 rounded-full">
                                    {{ category.articlesTotal }}
                                </span>
                            </a>
                        </div>
                    </div>

                    <div class="mt-10 pt-8 border-t border-gray-100 dark:border-gray-700">
                        <div class="flex items-center justify-between mb-6">
                            <h2 class="flex items-center font-bold text-gray-900 uppercase dark:text-white text-sm">
                                <el-icon class="mr-2 text-orange-500"><PriceTag /></el-icon>
                                <span>发现标签</span>
                            </h2>
                            <button @click="initTags" class="text-xs flex items-center text-sky-600 hover:text-sky-700 transition-all active:scale-95">
                                <el-icon class="mr-1"><RefreshRight /></el-icon>换一换
                            </button>
                        </div>

                        <div class="flex flex-wrap gap-3">
                            <div v-if="tags.length === 0" class="text-gray-400 text-sm italic">暂无标签数据...</div>
                            <a v-for="(tag, index) in tags" :key="index"
                                @click="goTagArticleListPage(tag.id, tag.name)"
                                class="cursor-pointer inline-flex items-center px-4 py-1.5 text-sm font-medium text-gray-700 bg-gray-50 border border-gray-200 rounded-md hover:border-sky-400 hover:text-sky-600 hover:bg-sky-50 transition-all dark:bg-gray-800 dark:text-gray-300 dark:border-gray-600">
                                # {{ tag.name }}
                                <span class="ml-1.5 text-[11px] text-gray-400 font-normal">({{ tag.articlesTotal }})</span>
                            </a>
                        </div>
                    </div>
                </div>
            </div>

            <aside class="col-span-12 md:col-span-3">
                <div class="sticky top-[5.5rem] space-y-4">
                    <UserInfoCard></UserInfoCard>
                   
                    <CategoryListCard></CategoryListCard>
                </div>
            </aside>
        </div>
    </main>

    <ScrollToTopButton></ScrollToTopButton>
</template>

<script setup>
import Header from '@/layouts/frontend/components/Header.vue'
import UserInfoCard from '@/layouts/frontend/components/UserInfoCard.vue'
import TagListCard from '@/layouts/frontend/components/TagListCard.vue'
import CategoryListCard from '@/layouts/frontend/components/CategoryListCard.vue'
import ScrollToTopButton from '@/layouts/frontend/components/ScrollToTopButton.vue'
import { PriceTag, RefreshRight } from '@element-plus/icons-vue' 

import { getCategoryList } from '@/api/frontend/category'
import { getTagList } from '@/api/frontend/tag' 

import { ref ,onMounted} from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const categories = ref([])
const tags = ref([]) 

const initCategories = () => {
    const userStr = localStorage.getItem('user')
    const userObj = userStr ? JSON.parse(userStr) : null
    const userId = userObj?.userInfo?.userID 

    getCategoryList({ userId: userId }).then((res) => {
        if (res.success) {
            categories.value = res.data
        }
    })
}

const initTags = () => {
    getTagList().then((res) => {
        if (res.success) {
            tags.value = res.data
        }
    })
}

onMounted(() => {
    initCategories()
    initTags() 
})

const goCategoryArticleListPage = (id, name) => {
    router.push({ path: '/category/article/list', query: { id, name, type: 'category' } })
}

const goTagArticleListPage = (id, name) => {
    router.push({ path: '/category/article/list', query: { id, name, type: 'tag' } })
}
</script>

<style scoped>
button:active {
    transform: scale(0.95);
}
</style>