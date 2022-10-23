import request from '@/utils/request'

//
const headers = {
    'Content-Type': 'application/x-www-form-urlencoded'
}

//  请求头添加 Authrization: Basic client_id: client_secret
const auth = {
    username: "blog-admin",
    password: "123456",
}

//登录
export function login(data) {
    //  promise
    return request({
        headers,
        auth,
        url: `/auth/login`,
        method: 'post',
        params: data
    })
}

//
export function getXieYi() {
    return request({
        url:`${window.location.href}/xieyi.html`, //访问的是public/xieyi.html
        method: 'get'
    })
}

// 查询用户名是否被注册
export function getUserByUsername(username) {
    return request({
        url: `/system/api/user/username/${username}`,
        method: 'get'
    })
}

// 注册
export function register(data) {
    return request({
        url: `/system/api/user/register`,
        method: 'post',
        data // data: data
    })
}

//退出登录
export function logout(accessToken) {
    return request({
        url: '/auth/logout',
        method: 'get',
        params: {
            accessToken
        }
    })
}

// 刷新令牌
export function refreshToken(refreshToken) {
    return request({
        headers,
        auth,
        url: '/auth/user/refreshToken',
        method: 'get',
        params: {
            refreshToken
        }
    })
}