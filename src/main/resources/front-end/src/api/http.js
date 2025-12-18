import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router'

const http = axios.create({
    baseURL: '/api',
    timeout: 5000
})

http.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('token')
        if (token) {
            config.headers.Authorization = `Bearer ${token}`
        }
        return config
    },
    (error) => {
        return Promise.reject(error)
    }
)

http.interceptors.response.use(
    (response) => {
        return response.data
    },
    (error) => {
        if (error.response) {
            if (error.response.status === 401) {
                localStorage.removeItem('token')
                router.push('/login')
                ElMessage.error('登录已过期，请重新登录')
                return Promise.reject(error)
            }
            const res = error.response.data
            ElMessage.error(res.message || error.message || '请求失败')
            return Promise.reject(res || error)
        } else {
            ElMessage.error(error.message || '网络错误')
            return Promise.reject(error)
        }
    }
)

export default http
