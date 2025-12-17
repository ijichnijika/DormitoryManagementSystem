import { defineStore } from 'pinia'
import { ref } from 'vue'
import http from '@/api/http'
import { ElMessage } from 'element-plus'

export const useUserStore = defineStore('user', () => {
    const token = ref(localStorage.getItem('token'))
    const userInfo = ref(JSON.parse(localStorage.getItem('userInfo') || 'null'))

    const login = async (username, password) => {
        try {
            const res = await http.post('/user/login', { username, password })

            if (res.code === 200 && res.data) {
                const user = res.data
                token.value = 'token-' + user.id
                userInfo.value = user

                localStorage.setItem('token', token.value)
                localStorage.setItem('userInfo', JSON.stringify(user))
                ElMessage.success('登录成功')
                return true
            } else {
                ElMessage.error(res.message || '登录失败')
                return false
            }
        } catch (error) {
            // 错误已在拦截器中显示
            console.error('登录错误:', error)
            return false
        }
    }

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
