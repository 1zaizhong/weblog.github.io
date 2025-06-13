import router from '@/router/index'
import { getToken } from '@/composables/cookie'
import { showMessage } from '@/composables/util'
import { showPageLoading } from '@/composables/util'
import { hidePageLoading } from '@/composables/util'

//全局路由 前置守卫
router.beforeEach((to, from, next) => {
    console.log('==> 全局路由前置守卫')

    //展示页面加载 Loading 
    showPageLoading()

    //若用户想访问后台 比如/amdin/*
    //未登录就强制跳转登录页
    let  token = getToken()
    if(!token && to.path.startsWith('/admin')){
        showMessage('请先登录', 'warning')
        next({path: '/login'})
    }else if( token && to.path == '/login'){
       //提示已经登录,不需要再次登录
       showMessage('已经登陆,请勿重复登录' , 'warning')
       //跳转到后台首页
       next({path: '/admin/index' })
    }else{
        next();
    }
    
    
})

//全局路由 后置守卫
router.beforeEach((to, from ) => {
   //动态设置 Title
   let title = (to.meta.title ? to.meta.title : '') + '- weblog'
   document.title = title

   //隐藏页面加载Loading 
   hidePageLoading()
})
