<template>
    <div class="grid grid-cols-12 gap-6 p-4">
        <div class="col-span-12 md:col-span-3">
            <div class="w-full p-5 mb-5 bg-white border border-gray-200 rounded-lg shadow-sm">
                <h2 class="flex items-center mb-4 font-bold text-gray-900 uppercase">
                    <el-icon class="mr-2 text-sky-500"><FolderOpened /></el-icon>专栏
                </h2>
                <div class="flex flex-col gap-1">
                    <div v-if="categories.length === 0" class="text-gray-400 text-sm p-4 text-center">加载中...</div>
                    <div v-for="(category, index) in categories" :key="index"
                        @click="goCategoryArticleListPage(category.id, category.name)"
                        :class="[route.query.type !== 'tag' && route.query.id == category.id ? 'bg-sky-50 text-sky-600 border-sky-200' : 'text-gray-600 border-transparent hover:bg-gray-50']"
                        class="px-4 py-3 text-sm font-medium border rounded-lg cursor-pointer transition-all flex justify-between items-center">
                        <span>{{ category.name }}</span>
                        <el-icon v-if="route.query.type !== 'tag' && route.query.id == category.id"><ArrowRight /></el-icon>
                    </div>
                </div>
            </div>

            <div class="w-full p-5 bg-white border border-gray-200 rounded-lg shadow-sm">
                <div class="flex items-center justify-between mb-4">
                    <h2 class="flex items-center font-bold text-gray-900 uppercase">
                        <el-icon class="mr-2 text-orange-500"><PriceTag /></el-icon>标签
                    </h2>
                    <button @click="initTags" class="text-xs flex items-center text-sky-600 hover:text-sky-700 active:scale-95">
                        <el-icon class="mr-1"><RefreshRight /></el-icon>换一换
                    </button>
                </div>
                <div class="flex flex-wrap gap-2">
                    <div v-if="tags.length === 0" class="text-gray-400 text-sm p-2 text-center w-full">暂无标签</div>
                    <span v-for="(tag, index) in tags" :key="index"
                        @click="goTagArticleListPage(tag.id, tag.name)"
                        :class="[route.query.type === 'tag' && route.query.id == tag.id ? 'bg-sky-500 text-white border-sky-500' : 'bg-gray-100 text-gray-600 border-gray-200 hover:border-sky-500 hover:text-sky-500 hover:bg-white']"
                        class="px-2 py-1 text-xs border rounded-md cursor-pointer transition-all">
                        # {{ tag.name }}
                    </span>
                </div>
            </div>
        </div>

        <div class="col-span-12 md:col-span-9">
            <div v-if="route.query.id" class="mb-5 p-4 bg-gray-50 border border-gray-200 rounded-lg flex items-center justify-between">
                <div>
                    <span class="text-gray-500 text-sm">当前：</span>
                    <span class="font-bold text-sky-600">{{ route.query.type === 'tag' ? '标签' : '专栏' }}</span>
                    <span class="mx-2 text-gray-300">/</span>
                    <span class="text-gray-800 font-medium">{{ route.query.name }}</span>
                </div>
            </div>

            <div v-if="articles && articles.length > 0">
                <div v-for="(article, index) in articles" :key="index" class="mb-5">
                    <el-card shadow="hover" :body-style="{ padding: '0px' }" class="rounded-xl border-none shadow-sm">
                        <div class="flex flex-col md:flex-row">
                            <img :src="article.cover" class="w-full md:w-56 h-40 object-cover">
                            <div class="p-5 flex flex-col justify-between flex-1">
                                <h3 class="text-lg font-bold hover:text-sky-600 cursor-pointer line-clamp-2">
                                    {{ article.title }}
                                </h3>
                                <div class="flex items-center text-gray-400 text-xs">
                                    <el-icon class="mr-1"><Calendar /></el-icon>
                                    {{ article.createDate }}
                                </div>
                            </div>
                        </div>
                    </el-card>
                </div>
                <div class="flex justify-center mt-10">
                    <el-pagination v-model:current-page="current" :page-size="size" layout="prev, pager, next" :total="total" @current-change="loadArticleData" background />
                </div>
            </div>

            <el-empty v-else description="请选择左侧专栏或标签查看文章" />
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getCategoryList, getCategoryArticlePageList } from '@/api/frontend/category'
import { getTagList, getTagArticlePageList } from '@/api/frontend/tag'
import { FolderOpened, PriceTag, RefreshRight, Calendar, ArrowRight } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()

const categories = ref([])
const tags = ref([])
const articles = ref([])
const current = ref(1)
const size = ref(10)
const total = ref(0)

// 1. 初始化分类列表 (注意后端需要 VO 对象，传空对象防止报错)
const initCategories = () => {
    getCategoryList({}).then(res => {
        if (res.success) categories.value = res.data
    })
}

// 2. 初始化标签
const initTags = () => {
    getTagList().then(res => {
        if (res.success) tags.value = res.data
    })
}

// 3. 加载文章列表
const loadArticleData = (pageNum = 1) => {
    const id = route.query.id
    if (!id) return

    const type = route.query.type || 'category'
    const params = { current: pageNum, size: size.value, id: id }
    
    const apiCall = (type === 'tag') ? getTagArticlePageList(params) : getCategoryArticlePageList(params)

    apiCall.then(res => {
        if (res.success) {
            articles.value = res.data
            total.value = res.total
            current.value = res.current
        }
    })
}

const goCategoryArticleListPage = (id, name) => {
    router.push({ query: { id, name, type: 'category' } })
}

const goTagArticleListPage = (id, name) => {
    router.push({ query: { id, name, type: 'tag' } })
}

// 监听 Query 变化，执行数据加载
watch(() => route.query, (newQuery) => {
    if (newQuery.id) {
        loadArticleData(1)
    }
}, { immediate: true })

onMounted(() => {
    initCategories()
    initTags()
})
</script>