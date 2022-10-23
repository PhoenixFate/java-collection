import Vue from 'vue'
import Roouter from 'vue-router'

Vue.use(Roouter)

const router = new  Roouter({
    mode: 'history',
    routes: [
        {
            path:'/',
            component: () => import('@/components/LoginPage'),
            children: [
                {
                    path: '',
                    component: ()=> import('@/views/auth/login')
                },
            ]
        },
        {
            path:'/refresh',
            component: () => import('@/components/LoginPage'),
            children: [
                {
                    path: '',
                    component: ()=> import('@/views/auth/refresh')
                },
            ]
        }
    ]
})

import store from "../store";

//路由拦截器，每次路由跳转都会通过这个拦截器
router.beforeEach((to, from, next) => {
    if(to.path === '/logout'){
        // 退出操作
        store.dispatch('UserLogout', to.query.redirectURL)
    }else {
        next()
    }
})

export default router;