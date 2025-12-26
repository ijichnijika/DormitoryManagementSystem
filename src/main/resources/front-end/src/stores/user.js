/**
 * 用户状态管理（Pinia Store）
 * 管理用户登录状态、用户信息、提供登录/登出方法
 */

import { defineStore } from 'pinia'
import { ref } from 'vue'
import http from '@/api/http'
import { ElMessage } from 'element-plus'

export const useUserStore = defineStore('user', () => {
    // 状态：JWT Token
    const token = ref(localStorage.getItem('token'))
    // 状态：用户信息对象
    const userInfo = ref(JSON.parse(localStorage.getItem('userInfo') || 'null'))

    // 登录方法
    const login = async (username, password) => {
        try {
            const res = await http.post('/user/login', { username, password })

            if (res.code === 200 && res.data) {
                const { user, token: jwtToken } = res.data
                // 更新状态
                token.value = jwtToken
                userInfo.value = user

                // 持久化到localStorage
                localStorage.setItem('token', jwtToken)
                localStorage.setItem('userInfo', JSON.stringify(user))
                ElMessage.success('登录成功')
                return true
            } else {
                ElMessage.error(res.message || '登录失败')
                return false
            }
        } catch (error) {
            console.error('登录错误:', error)
            return false
        }
    }

    // 登出方法
    const logout = () => {
        token.value = null
        userInfo.value = null
        localStorage.removeItem('token')
        localStorage.removeItem('userInfo')
    }

    return {
        token,
        userInfo,
        login,
        logout
    }
})
