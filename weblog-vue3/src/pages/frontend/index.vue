<template>
    <Header></Header>

    <!-- 主内容区域 -->
    <main class="container max-w-screen-xl mx-auto px-4 md:px-6 py-4">
        <!-- grid 表格布局，分为 4 列 -->
        <div class="grid grid-cols-4 gap-7">
            <!-- 左边栏，占用 3 列 -->
            <div class="col-span-4 md:col-span-3 mb-3">
                <!-- 文章列表，grid 表格布局，分为 2 列 -->
                <div class="grid grid-cols-2 gap-4">
                    <div v-for="(article, index) in articles" :key="index" class="col-span-2 md:col-span-1 animate__animated animate__fadeInUp">
                        <div class=" relative bg-white hover:scale-[1.03] h-full border border-gray-200 rounded-lg dark:bg-gray-800 dark:border-gray-700">
                            <!-- 文章封面 -->
                            <a @click="goArticleDetailPage(article.id)" class="cursor-pointer">
                                <img class="rounded-t-lg h-48 w-full"
                                    :src="article.cover" />
                            </a>
                            <div class="p-5 flex flex-col min-h-max">
                                <!-- 标签 -->
                                <div class="mb-3">
                                    <span v-for="(tag, tagIndex) in article.tags" :key="tagIndex" @click="goTagArticleListPage(tag.id, tag.name)"
                                        class="cursor-pointer bg-green-100 text-green-800 text-xs font-medium mr-2 px-2.5 py-0.5 
                                        rounded hover:bg-green-200 hover:text-green-900 dark:bg-green-900 
                                        dark:hover:bg-green-950
                                        dark:text-green-300">
                                        {{ tag.name }}
                                    </span>
                                </div>
                                <!-- 文章标题 -->
                                <a @click="goArticleDetailPage(article.id)" class="cursor-pointer">
                                    <h2 class="mb-2 text-2xl font-bold tracking-tight text-gray-900 dark:text-white">
                                        <span class="hover:border-gray-600 hover:border-b-2 dark:hover:border-gray-400">{{ article.title }}</span>
                                    </h2>
                                </a>
                                <!-- 文章摘要 -->
                                <p v-if="article.summary" class="mb-3 font-normal text-gray-500 dark:text-gray-400">{{ article.summary }}</p>
                                <!-- 文章发布时间、所属分类 -->
                                <p class="mt-auto flex items-center font-normal text-gray-400 text-sm dark:text-gray-400">
                                    <!-- 发布时间 -->
                                    <svg class="inline w-3 h-3 mr-2 text-gray-400" aria-hidden="true"
                                        xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 20 20">
                                        <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round"
                                            stroke-width="2"
                                            d="M5 1v3m5-3v3m5-3v3M1 7h18M5 11h10M2 3h16a1 1 0 0 1 1 1v14a1 1 0 0 1-1 1H2a1 1 0 0 1-1-1V4a1 1 0 0 1 1-1Z" />
                                    </svg>
                                    {{ article.createDate }}

                                    <!-- 所属分类 -->
                                    <svg class="inline w-3 h-3 ml-5 mr-2 text-gray-400" aria-hidden="true"
                                        xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 18 18">
                                        <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round"
                                            stroke-width="2"
                                            d="M1 5v11a1 1 0 0 0 1 1h14a1 1 0 0 0 1-1V6a1 1 0 0 0-1-1H1Zm0 0V2a1 1 0 0 1 1-1h5.443a1 1 0 0 1 .8.4l2.7 3.6H1Z" />
                                    </svg>
                                    <a @click="goCategoryArticleListPage(article.category.id, article.category.name)" class="cursor-pointer text-gray-400 hover:underline">{{ article.category.name }}</a>
                                </p>
                            </div>

                            <!-- 是否置顶 -->
                            <div v-if="article.isTop" class="absolute inline-flex items-center justify-center w-14 h-7 text-xs font-bold text-white bg-red-500 border-2 border-white rounded-full -top-2 -end-2 dark:border-gray-900">
                                置顶
                            </div>

                        </div>
                    </div>
                </div>
                
                <!-- 分页 -->
                <nav class="flex justify-center items-center mt-10" aria-label="Page navigation example">
                    <ul class="inline-flex -space-x-px text-base h-10">
                        <li>
                            <a @click="getArticles(current - 1)" 
                            class="flex items-center justify-center px-4 h-10 ms-0 leading-tight text-gray-500 bg-white border border-e-0 border-gray-300 rounded-s-lg hover:bg-gray-100 hover:text-gray-700 dark:bg-gray-800 dark:border-gray-700 dark:text-gray-400 dark:hover:bg-gray-700 dark:hover:text-white"
                            :class="[current == 1 ? 'cursor-not-allowed opacity-50' : 'cursor-pointer']">
                                上一页
                            </a>
                        </li>
                        
                        <li v-for="(pageNo, index) in pagesList" :key="index">
                            <a @click="getArticles(pageNo)" 
                            class="flex items-center justify-center px-4 h-10 leading-tight border border-gray-300 hover:bg-gray-100 hover:text-gray-700 dark:border-gray-700 dark:bg-gray-800 dark:text-gray-400 dark:hover:bg-gray-700 dark:hover:text-white"
                            :class="[current == pageNo ? 'text-blue-600 bg-blue-50 border-blue-300 hover:bg-blue-100 hover:text-blue-700' : 'text-gray-500 bg-white']">
                            {{ pageNo }}
                            </a>
                        </li>
                        
                        <li>
                            <a @click="getArticles(current + 1)" 
                            class="flex items-center justify-center px-4 h-10 leading-tight text-gray-500 bg-white border border-gray-300 rounded-e-lg hover:bg-gray-100 hover:text-gray-700 dark:bg-gray-800 dark:border-gray-700 dark:text-gray-400 dark:hover:bg-gray-700 dark:hover:text-white"
                            :class="[current == pagesList.length ? 'cursor-not-allowed opacity-50' : 'cursor-pointer']">
                                下一页
                            </a>
                        </li>
                    </ul>
                </nav>
            </div>


            <!-- 右边侧边栏，占用一列 -->
            <aside class="col-span-4 md:col-span-1 animate__animated animate__fadeInUp">
                <div class="sticky top-[5.5rem]">
                    <!-- 博主信息 -->
                    <UserInfoCard></UserInfoCard>
                    <!--热度排行 -->
                    <HotArticle></HotArticle>
                </div>
            </aside>
    </div>

    </main>

    <!-- 返回顶部 -->
    <ScrollToTopButton></ScrollToTopButton>

    <Footer></Footer>
</template>

<script setup>
import Header from '@/layouts/frontend/components/Header.vue'
import Footer from '@/layouts/frontend/components/Footer.vue'
import UserInfoCard from '@/layouts/frontend/components/UserInfoCard.vue'
import CategoryListCard from '@/layouts/frontend/components/CategoryListCard.vue'
import TagListCard from '@/layouts/frontend/components/TagListCard.vue'
import ScrollToTopButton from '@/layouts/frontend/components/ScrollToTopButton.vue'
import { initTooltips } from 'flowbite'
import { onMounted, ref } from 'vue'
import { getArticlePageList } from '@/api/frontend/article'
import { useRouter } from 'vue-router'
import HotArticle  from '@/layouts/frontend/components/HotArticle.vue'

const router = useRouter()

// 跳转分类文章列表页
const goCategoryArticleListPage = (id, name) => {
    // 跳转时通过 query 携带参数（分类 ID、分类名称）
    router.push({path: '/category/article/list', query: {id, name}})
}


// initialize components based on data attribute selectors
onMounted(() => {
    initTooltips();
})

// 文章集合
const articles = ref([])
// 当前页码
const current = ref(1)
// 每页显示的文章数
const size = ref(6)
// 总文章数
const total = ref(0)
// 总共多少页
const pages = ref(0)

const pagesList = ref([])
// n拿id
const userStr = localStorage.getItem('user')
let loginUserId = null
if (userStr) {
    const userObj = JSON.parse(userStr)
    loginUserId = userObj.userInfo?.userID // 拿到当前登录人的 ID
}

function getArticles(currentNo) {
    // 上下限校验：如果点击的页码超出范围，则不跳转
    let totalPages = Math.ceil(total.value / size.value)
    if (currentNo < 1 || (totalPages > 0 && currentNo > totalPages)) return
    
    getArticlePageList({
        current: currentNo, 
        size: size.value,
        userId: loginUserId
    }).then((res) => {
        if (res.success) {
            articles.value = res.data
            current.value = res.current
            total.value = res.total
            size.value = res.size
            
            // 计算总页数
            let tPages = Math.ceil(res.total / res.size)
            // 生成页码数组，例如 [1, 2, 3]
            pagesList.value = Array.from({ length: tPages }, (v, k) => k + 1)
        }
    })
}

getArticles(current.value)

// 跳转文章详情页
const goArticleDetailPage = (articleId, loginUserId) => {
    router.push('/article/' + articleId)
}

// 跳转标签文章列表页
const goTagArticleListPage = (id, name) => {
    // 跳转时通过 query 携带参数（标签 ID、标签名称）
    router.push({path: '/tag/article/list', query: {id, name}})
}
</script>
