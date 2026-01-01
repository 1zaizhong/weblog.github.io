import Index from '@/pages/frontend/index.vue'
import ArchiveList from '@/pages/frontend/archive-list.vue'
import CategoryList from '@/pages/frontend/category-list.vue'
import CategoryArticleList from '@/pages/frontend/category-article-list.vue'
import ArticleDetail from '@/pages/frontend/article-detail.vue'
import WikiList from '@/pages/frontend/wiki-list.vue'
import WikiDetail from '@/pages/frontend/wiki-detail.vue'
import NotFound from '@/pages/frontend/404.vue'
import Login from '@/pages/admin/login.vue'
import AdminIndex from '@/pages/admin/index.vue'
import AdminArticleList from '@/pages/admin/article-list.vue'
import AdminCategoryList from '@/pages/admin/category-list.vue'
import AdminBlogSettings from '@/pages/admin/blog-settings.vue'
import AdminWikiList from '@/pages/admin/wiki-list.vue'
import AdminCommentList from '@/pages/admin/comment-list.vue'
import { createRouter, createWebHashHistory } from 'vue-router'
import Admin from '@/layouts/admin/admin.vue'

// 统一在这里声明所有路由
const routes = [
    {
        path: '/', // 路由地址，首页
        component: Index,
        meta: { title: 'Weblog 首页' }
    },
    {
        path: '/archive/list', // 归档页
        component: ArchiveList,
        meta: { title: 'Weblog 归档页' }
    },
    {
        path: '/category/list', // 分类列表页
        component: CategoryList,
        meta: { title: 'Weblog 分类列表页' }
    },
    {
        path: '/category/article/list', // 分类文章页
        component: CategoryArticleList,
        meta: { title: 'Weblog 分类文章页' }
    },
   
    {
        path: '/personal/like', 
        component: () => import('@/pages/frontend/user-like.vue'), 
        meta: { title: '我的喜欢' }
    },
    {
        path: '/collection/list', 
        component: () => import('@/pages/frontend/collection-list.vue'), 
        meta: { title: '我的收藏夹' }
    },
    {
        path: '/collection/article/list', 
        component: () => import('@/pages/frontend/collection-article-list.vue'), 
        meta: { title: '收藏内容详情' }
    },
    {
        path: '/article/:articleId', // 文章详情页
        component: ArticleDetail,
        meta: { title: 'Weblog 详情页' }
    },
    {
        path: '/wiki/list', // 知识库
        component: WikiList,
        meta: { title: '知识库' }
    },
    {
        path: '/wiki/:wikiId', // 知识库详情页
        component: WikiDetail,
        meta: { title: '知识库详情' }
    },
    {
        path: '/login', // 登录页
        component: Login,
        meta: { title: 'Weblog 登录页' }
    },
    {
        path: '/:pathMatch(.*)*',
        name: 'NotFound',
        component: NotFound,
        meta: { title: '404 页' }
    },
    {
        path: "/admin", // 后台首页
        component: Admin,
        children: [
            {
                path: "/admin/index",
                component: AdminIndex,
                meta: { title: '仪表盘' }
            },
            {
                path: "/admin/article/list",
                component: AdminArticleList,
                meta: { title: '文章管理' }
            },
            {
                path: "/admin/category/list",
                component: AdminCategoryList,
                meta: { title: '分类管理' }
            },
            // 已删除 AdminTagList 路由
            {
                path: "/admin/blog/settings",
                component: AdminBlogSettings,
                meta: { title: '博客设置' }
            },
            {
                path: "/admin/wiki/list",
                component: AdminWikiList,
                meta: { title: '知识库管理' }
            },
            {
                path: "/admin/like/list",
                component: () => import("@/pages/admin/like.vue"),
                meta: { title: '点赞管理' }
            },
            {
                path: "/admin/collection/list",
                component: () => import("@/pages/admin/collection-list.vue"),
                meta: { title: '收藏管理' }
            },
            {
                path: "/admin/collection/article",
                component: () => import("@/pages/admin/collection-article.vue"),
                meta: { title: '文章收藏' }
            },
            {
                path: "/admin/comment/list",
                component: AdminCommentList,
                meta: { title: '评论管理' }
            },
            {
                path: "/admin/user/list",
                component: () => import("@/pages/admin/user-list.vue"), 
                meta: { title: '用户管理' }
            },
        ]
    }
]

// 创建路由
const router = createRouter({
    history: createWebHashHistory(),
    routes, 
    scrollBehavior() {
        return { top: 0 }
    }
})

export default router