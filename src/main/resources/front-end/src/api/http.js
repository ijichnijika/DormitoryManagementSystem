/**
 * Axios HTTP客户端配置
 * 职责：统一管理API请求、自动添加JWT Token、统一错误处理
 */

import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router'

// 创建axios实例
const http = axios.create({
    baseURL: '/api', // API基础路径
    timeout: 5000 // 请求超时时间（5秒）
})

/**
 * 请求拦截器
 * 在每个请求发送前自动添加JWT Token到Authorization头
 */
http.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('token')
        if (token) {
            config.headers.Authorization = `Bearer ${token}` // 添加JWT Token
        }
        return config
    },
    (error) => {
        return Promise.reject(error)
    }
)

/**
 * 响应拦截器
 * 统一处理响应数据和错误
 */
http.interceptors.response.use(
    (response) => {
        return response.data // 直接返回数据部分，简化调用
    },
    (error) => {
        if (error.response) {
            // HTTP错误响应
            if (error.response.status === 401) {
                // 401未授权：Token过期或无效，清除登录信息并跳转到登录页
                localStorage.removeItem('token')
                router.push('/login')
                ElMessage.error('登录已过期，请重新登录')
                return Promise.reject(error)
            }
            // 其他HTTP错误
            const res = error.response.data
            ElMessage.error(res.message || error.message || '请求失败')
            return Promise.reject(res || error)
        } else {
            // 网络错误（无响应）
            ElMessage.error(error.message || '网络错误')
            return Promise.reject(error)
        }
    }
)

export default http
