<template>
    <Header></Header>

    <main class="container max-w-screen-xl mx-auto px-4 md:px-6 py-4">
        <div class="grid grid-cols-4 gap-7">
            <div class="col-span-4 md:col-span-3 mb-3">
                <div class="w-full p-5 mb-4 bg-white border border-gray-200 rounded-lg dark:bg-gray-800 dark:border-gray-700">
                    <h2 class="text-xl font-bold text-gray-900 dark:text-white flex items-center">
                        <span class="text-red-500 mr-2">❤️</span> 我的喜欢
                    </h2>
                </div>

                <div class="p-5 mb-4 border border-gray-200 rounded-lg bg-white dark:bg-gray-800 dark:border-gray-700">
                    <ol v-if="articles && articles.length > 0" class="divide-y divider-gray-200 dark:divide-gray-700">
                        <li v-for="(article, index) in articles" :key="index">
                            <a @click="goArticleDetailPage(article.id)" class="cursor-pointer items-center block p-3 sm:flex hover:bg-gray-50 hover:rounded-lg dark:hover:bg-gray-700 transition-colors">
                                <img class="w-32 h-20 mb-3 mr-4 rounded-lg object-cover sm:mb-0 shadow-sm" :src="article.cover" />
                                <div class="text-gray-600 dark:text-gray-400">
                                    <h2 class="text-lg font-bold text-gray-900 dark:text-white mb-2">
                                        {{ article.title }}
                                    </h2>
                                    <div class="flex items-center text-xs text-gray-500">
                                        <svg class="w-3 h-3 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
                                        </svg>
                                        {{ article.createTime }}
                                    </div>
                                </div>
                            </a>
                        </li>
                    </ol>

                    <div v-else class="flex flex-col items-center justify-center py-20">
                        <span class="text-6xl mb-4">🤍</span>
                        <p class="text-gray-400">还没有喜欢的文章，快去首页转转吧~</p>
                    </div>
                </div>

                <nav v-if="pages > 1" class="mt-10 flex justify-center">
                    <ul class="flex items-center -space-x-px h-10 text-base">
                        <li>
                            <a @click="getLikeArticles(current - 1)" 
                               class="flex items-center justify-center px-4 h-10 ml-0 leading-tight text-gray-500 bg-white border border-gray-300 rounded-l-lg hover:bg-gray-100 dark:bg-gray-800 dark:border-gray-700"
                               :class="[current > 1 ? 'cursor-pointer' : 'cursor-not-allowed opacity-50']">
                                <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 6 10"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 1 1 5l4 4" /></svg>
                            </a>
                        </li>
                        <li v-for="pageNo in pages" :key="pageNo">
                            <a @click="getLikeArticles(pageNo)"
                               class="flex items-center justify-center px-4 h-10 leading-tight border transition-colors cursor-pointer"
                               :class="[pageNo == current ? 'text-sky-600 bg-sky-50 border-sky-500 z-10' : 'text-gray-500 bg-white border-gray-300 hover:bg-gray-100']">
                                {{ pageNo }}
                            </a>
                        </li>
                        <li>
                            <a @click="getLikeArticles(current + 1)"
                               class="flex items-center justify-center px-4 h-10 leading-tight text-gray-500 bg-white border border-gray-300 rounded-r-lg hover:bg-gray-100 dark:bg-gray-800 dark:border-gray-700"
                               :class="[current < pages ? 'cursor-pointer' : 'cursor-not-allowed opacity-50']">
                                <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 6 10"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="m1 9 4-4-4-4" /></svg>
                            </a>
                        </li>
                    </ul>
                </nav>
            </div>

            <aside class="col-span-4 md:col-span-1">
                <div class="sticky top-[5.5rem]">
                    <UserInfoCard></UserInfoCard>
                    <CategoryListCard></CategoryListCard>
                    <TagListCard></TagListCard>
                </div>
            </aside>
        </div>
    </main>

    <ScrollToTopButton></ScrollToTopButton>
    <Footer></Footer>
</template>

<script setup>
import Header from '@/layouts/frontend/components/Header.vue'
import Footer from '@/layouts/frontend/components/Footer.vue'
import UserInfoCard from '@/layouts/frontend/components/UserInfoCard.vue'
import TagListCard from '@/layouts/frontend/components/TagListCard.vue'
import CategoryListCard from '@/layouts/frontend/components/CategoryListCard.vue'
import ScrollToTopButton from '@/layouts/frontend/components/ScrollToTopButton.vue'
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getLikeArticlePageList } from '@/api/frontend/article'

const router = useRouter()

// 状态变量
const articles = ref([])
const current = ref(1)
const size = ref(10) // 每页显示 10 条数据
const total = ref(0)
const pages = ref(0)

// 获取点赞文章分页数据
function getLikeArticles(pageNo) {
    if (pageNo < 1 || (pages.value > 0 && pageNo > pages.value)) return
    
    // 从本地获取当前登录用户 ID
    const userStr = localStorage.getItem('user')
    const userObj = userStr ? JSON.parse(userStr) : null
    const loginUserId = userObj?.userInfo?.userID

    if (!loginUserId) {
        console.warn("未获取到用户登录信息")
        return
    }

    getLikeArticlePageList({ 
        current: pageNo, 
        size: size.value, 
        userId: loginUserId 
    }).then((res) => {
        if (res.success && res.data) {
            // 解析后端 Response -> PageResponse 结构
            const pageData = res.data;
            articles.value = pageData.data
            current.value = pageData.current
            total.value = pageData.total
            pages.value = pageData.pages
        }
    })
}

// 跳转文章详情页
const goArticleDetailPage = (articleId) => {
    router.push('/article/' + articleId)
}

onMounted(() => {
    getLikeArticles(current.value)
})
</script>