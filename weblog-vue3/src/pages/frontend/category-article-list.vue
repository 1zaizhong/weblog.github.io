<template>
    <Header></Header>

    <main class="container max-w-screen-2xl mx-auto px-4 md:px-6 py-4">
        <div class="grid grid-cols-12 gap-7">
            
            <div class="col-span-12 md:col-span-9 mb-3">
                <div class="w-full p-5 mb-3 bg-white border border-gray-200 rounded-lg dark:bg-gray-800 dark:border-gray-700 shadow-sm">
                    
                    <div class="mb-6">
                        <h2 class="flex items-center mb-4 font-bold text-gray-900 uppercase dark:text-white text-sm">
                            <el-icon class="mr-2 text-blue-500"><FolderOpened /></el-icon>
                            专栏 
                            <span class="ml-2 text-gray-400 font-normal">({{ categories.length }})</span>
                        </h2>
                        <div class="flex flex-wrap gap-3">
                            <a @click="goListPage(category.id, category.name, 'category')"
                                v-for="(category, index) in categories" :key="index"
                                :class="[route.query.type == 'category' && route.query.id == category.id ? 'bg-sky-600 text-white border-sky-600 shadow-md' : 'bg-white text-gray-900 border-gray-300 hover:border-sky-500 hover:text-sky-600']"
                                class="inline-flex cursor-pointer items-center px-4 py-1.5 text-sm font-bold border rounded-full transition-all">
                                {{ category.name }}
                                <span :class="[route.query.type == 'category' && route.query.id == category.id ? 'bg-white text-sky-600' : 'bg-sky-100 text-sky-700']" 
                                      class="inline-flex items-center justify-center min-w-[20px] h-5 ms-2 px-1 text-[10px] font-bold rounded-full">
                                    {{ category.articlesTotal }}
                                </span>
                            </a>
                        </div>
                    </div>

                    <div class="border-t border-gray-100 mb-5 dark:border-gray-700"></div>

                    <div>
                        <div class="flex justify-between items-center mb-4">
                            <h2 class="flex items-center font-bold text-gray-900 uppercase dark:text-white text-sm">
                                <el-icon class="mr-2 text-orange-600"><PriceTag /></el-icon>
                                发现标签
                            </h2>
                            <button @click="shuffleTags" class="flex items-center text-xs font-bold text-sky-600 hover:text-sky-700 cursor-pointer group">
                                <el-icon class="mr-1 group-active:rotate-180 transition-transform duration-500"><Refresh /></el-icon>
                                换一换
                            </button>
                        </div>
                        
                        <div class="flex flex-wrap gap-3">
                            <a @click="goListPage(tag.id, tag.name, 'tag')"
                                v-for="(tag, index) in displayTags" :key="index"
                                :class="[route.query.type == 'tag' && route.query.id == tag.id ? 'bg-gray-900 text-white border-gray-900 shadow-md' : 'bg-white text-black border-gray-400 hover:border-black']"
                                class="inline-flex cursor-pointer items-center px-4 py-1.5 text-sm font-bold border rounded-md transition-all">
                                # {{ tag.name }}
                                <span class="ms-2 text-[11px]" :class="route.query.type == 'tag' && route.query.id == tag.id ? 'text-gray-300' : 'text-gray-500'">
                                    ({{ tag.articlesTotal }})
                                </span>
                            </a>
                        </div>
                    </div>
                </div>

                <div class="p-5 mb-4 border border-gray-200 rounded-lg bg-white dark:bg-gray-800 dark:border-gray-700 shadow-sm">
                    <ol v-if="articles && articles.length > 0" class="divide-y divider-gray-100 dark:divide-gray-700">
                        <li v-for="(article, index) in articles" :key="index">
                            <a @click="goArticleDetailPage(article.id)" class="cursor-pointer items-center block py-4 px-2 sm:flex hover:bg-gray-50 rounded-xl transition-all">
                                <img class="w-full sm:w-44 h-28 object-cover mb-3 mr-5 rounded-lg sm:mb-0 shadow-sm" :src="article.cover" />
                                <div class="flex-1">
                                    <h2 class="text-lg font-bold text-gray-900 dark:text-white mb-2 hover:text-sky-600">
                                        {{ article.title }}
                                    </h2>
                                    <div class="flex items-center text-xs text-gray-500">
                                        <el-icon class="mr-1"><Calendar /></el-icon>
                                        {{ article.createDate }}
                                    </div>
                                </div>
                            </a>
                        </li>
                    </ol>

                    <div v-else class="flex items-center justify-center flex-col py-20">
                        <el-empty description="该目录下暂无相关文章" />
                    </div>
                </div>

                <div class="flex justify-center mt-10" v-if="total > 0">
                    <el-pagination 
                        v-model:current-page="current" 
                        :page-size="size" 
                        layout="prev, pager, next" 
                        :total="total" 
                        @current-change="loadArticles"
                        background 
                    />
                </div>
            </div>

            <aside class="col-span-12 md:col-span-3">
                <div class="sticky top-[5.5rem] space-y-4">
                    <UserInfoCard></UserInfoCard>
                   
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
import { ref, watch, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getCategoryArticlePageList, getCategoryList } from '@/api/frontend/category'
import { getTagArticlePageList, getTagList } from '@/api/frontend/tag'
import { FolderOpened, PriceTag, Calendar, Refresh } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()

const articles = ref([])
const categories = ref([])
const allTags = ref([]) 
const displayTags = ref([]) 

const current = ref(1)
const size = ref(10)
const total = ref(0)

const TAG_DISPLAY_COUNT = 12

const initNavData = () => {
    getCategoryList({}).then(res => {
        if (res.success) categories.value = res.data
    })
    getTagList().then(res => {
        if (res.success) {
            allTags.value = res.data
            shuffleTags() 
        }
    })
}

const shuffleTags = () => {
    if (allTags.value.length === 0) return
    const shuffled = [...allTags.value].sort(() => 0.5 - Math.random())
    displayTags.value = shuffled.slice(0, TAG_DISPLAY_COUNT)
}

const loadArticles = (pageNum = 1) => {
    const id = route.query.id
    const type = route.query.type || 'category'
    if (!id) return

    const params = { current: pageNum, size: size.value, id: id }
    const apiCall = type === 'tag' 
        ? getTagArticlePageList(params) 
        : getCategoryArticlePageList(params)

    apiCall.then(res => {
        if (res.success) {
            articles.value = res.data
            current.value = res.current
            total.value = res.total
        }
    })
}

watch(() => route.query, () => {
    loadArticles(1)
}, { deep: true })

onMounted(() => {
    initNavData()
    loadArticles(1)
})

const goListPage = (id, name, type) => {
    router.push({ query: { id, name, type } })
}

const goArticleDetailPage = (id) => {
    router.push('/article/' + id)
}
</script>

<style scoped>
.group:active .el-icon {
    transform: rotate(180deg);
}
</style>