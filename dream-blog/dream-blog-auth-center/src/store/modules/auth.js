import {login, logout, refreshToken} from '@/api/auth'
import {PcCookie, Key} from '@/utils/cookie'

const state = {
    // 用户基本信息
    userInfo: PcCookie.get(Key.userInfoKey) ? JSON.parse(PcCookie.get(Key.userInfoKey)) : null,
    // 访问令牌
    accessToken:  PcCookie.get(Key.accessTokenKey),
    //  刷新令牌，当访问令牌如果过期了，则通过刷新令牌获取新令牌
    refreshToken: PcCookie.get(Key.refreshTokenKey)
}

// 改变状态值
const mutations = {

    // 赋值用户状态 state:状态，data:数据
    SET_USER_STATE(state, data) {
        //解构对象属性
        const {userInfo, access_token, refresh_token} = data

        // 状态赋值
        state.userInfo = userInfo
        state.accessToken = access_token
        state.refreshToken = refresh_token

        //将数据保存在cookie
        PcCookie.set(Key.userInfoKey, userInfo)
        PcCookie.set(Key.accessTokenKey, access_token)
        PcCookie.set(Key.refreshTokenKey, refresh_token)
    },

    // 重置用户状态
    RESET_USER_STATE(state) {
        state.userInfo = null
        state.accessToken = null
        state.refreshToken = null

        // 清除cookie中的数据
        PcCookie.remove(Key.userInfoKey)
        PcCookie.remove(Key.accessTokenKey)
        PcCookie.remove(Key.refreshTokenKey)
    }

}

// 定义行为
const actions = {

    // 登录
    UserLogin({commit}, userData) {
        console.log('UserLogin',userData)
        // 解构赋值
        const {username, password} = userData

        return new Promise((resolve, reject)=> {
            login( {username: username.trim(), password: password } ).then(response => {
                console.log('response',response)
                const {code, data} = response
                if (code === 20000){
                    // 赋值给对应的状态变量
                    commit('SET_USER_STATE', data)
                }
                resolve(response) //正常响应钩子
            }).catch(error => {
                // 重置状态
                commit('RESET_USER_STATE')
                reject(error) // 异常
            })
        })
    },

    // 退出登录
    UserLogout({state, commit}, redirectURL) {
        logout(state.accessToken).then(response=>{
            // 重置状态
            commit('RESET_USER_STATE')
            // 重定向回来源地址, 如果没有指定重定向地址则返回登录页
            window.location.href = redirectURL || '/'
        }).catch(error=>{
            // 重置状态
            commit('RESET_USER_STATE')
            window.location.href = redirectURL || '/'
        })
    },

    // 刷新令牌
    SendRefreshToken({state, commit}) {
        return new Promise((resolve, reject)=>{
            // 调用接口
            if (!state.refreshToken){
                // 重置状态
                commit('RESET_USER_STATE')
                //是否有刷新令牌
                reject('没有刷新令牌')
                return
            }
            // 发送请求
            refreshToken(state.refreshToken).then(response=>{
                console.log('获取到新的用户状态信息',response.data)
                // 更新用户状态数据
                commit('SET_USER_STATE', response.data)
                resolve() //正常钩子
            }).catch(error=>{
                //重置状态
                commit('RESET_USER_STATE')
                reject(error)
            })
        })
    }
}

export default {
    state,
    mutations,
    actions,
}

