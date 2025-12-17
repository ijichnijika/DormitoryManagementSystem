import axios from 'axios'
import { ElMessage } from 'element-plus'

const http = axios.create({
    baseURL: '/api', // Proxy target
    timeout: 5000
})

// Request Interceptor
http.interceptors.request.use(
    (config) => {
        // TODO: Add token to header
        // const token = localStorage.getItem('token')
        // if (token) {
        //   config.headers.Authorization = `Bearer ${token}`
        // }
        return config
    },
    (error) => {
        return Promise.reject(error)
    }
)

// Response Interceptor
http.interceptors.response.use(
    (response) => {
        return response.data
    },
    (error) => {
        // 处理HTTP错误(网络错误、超时等)
        if (error.response) {
            // 服务器返回了错误响应
            const res = error.response.data
            ElMessage.error(res.message || error.message || '请求失败')
            return Promise.reject(res || error)
        } else {
            // 网络错误或请求超时
            ElMessage.error(error.message || '网络错误')
            return Promise.reject(error)
        }
    }
)

export default http
