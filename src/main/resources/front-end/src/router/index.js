/**
 * Vue Router 路由配置
 * 
 * 本文件定义了系统的所有路由和全局导航守卫
 * 包括:
 * - 路由配置(页面路径、组件、元信息)
 * - 权限控制(登录验证、角色验证)
 * - 页面标题设置
 * 
 * @module router
 */

import { createRouter, createWebHistory } from 'vue-router'
import { canAccessRoute } from '@/utils/auth'
import { ElMessage } from 'element-plus'

/**
 * 路由配置数组
 * 
 * meta 元信息说明:
 * - title: 页面标题
 * - requiresAuth: 是否需要登录(true/false)
 * - roles: 允许访问的角色数组,如 ['STUDENT', 'TEACHER']
 */
const routes = [
    {
        path: '/',
        component: () => import('@/layout/MainLayout.vue'), // 主布局(包含侧边栏和头部)
        redirect: '/dashboard', // 访问根路径时重定向到仪表盘
        children: [
            // ==================== 通用页面 ====================
            {
                path: 'dashboard',
                name: 'Dashboard',
                component: () => import('@/views/dashboard/Dashboard.vue'),
                meta: {
                    title: '仪表盘',
                    requiresAuth: true,
                    roles: ['STUDENT', 'INSPECTOR', 'TEACHER', 'ADMIN'] // 所有角色都可访问
                }
            },
            {
                path: 'profile',
                name: 'UserProfile',
                component: () => import('@/views/profile/UserProfile.vue'),
                meta: {
                    title: '个人信息',
                    requiresAuth: true,
                    roles: ['STUDENT', 'INSPECTOR', 'TEACHER', 'ADMIN']
                }
            },

            // ==================== 学生/检查员页面 ====================
            {
                path: 'inspection/my-record',
                name: 'MyRecord',
                component: () => import('@/views/inspection/MyRecord.vue'),
                meta: {
                    title: '我的卫生记录',
                    requiresAuth: true,
                    roles: ['STUDENT', 'INSPECTOR'] // 学生和检查员可访问
                }
            },
            {
                path: 'inspection/apply',
                name: 'InspectionApply',
                component: () => import('@/views/inspection/ApplicationForm.vue'),
                meta: {
                    title: '申请检查员',
                    requiresAuth: true,
                    roles: ['STUDENT', 'INSPECTOR']
                }
            },
            {
                path: 'inspection/my-applications',
                name: 'MyApplications',
                component: () => import('@/views/inspection/MyApplications.vue'),
                meta: {
                    title: '我的申请',
                    requiresAuth: true,
                    roles: ['STUDENT', 'INSPECTOR']
                }
            },

            // ==================== 检查员/教师/管理员页面 ====================
            {
                path: 'inspection/entry',
                name: 'InspectionEntry',
                component: () => import('@/views/inspection/EntryForm.vue'),
                meta: {
                    title: '录入检查',
                    requiresAuth: true,
                    roles: ['INSPECTOR', 'TEACHER', 'ADMIN'] // 检查员及以上权限
                }
            },

            // ==================== 教师专属页面 ====================
            {
                path: 'admin/applications',
                name: 'AdminApplications',
                component: () => import('@/views/admin/Applications.vue'),
                meta: {
                    title: '审批管理',
                    requiresAuth: true,
                    roles: ['TEACHER'] // 仅教师可访问
                }
            },
            {
                path: 'management/inspections',
                name: 'InspectionManagement',
                component: () => import('@/views/management/InspectionRecords.vue'),
                meta: {
                    title: '检查记录管理',
                    requiresAuth: true,
                    roles: ['TEACHER']
                }
            },

            // ==================== 教师/管理员页面 ====================
            {
                path: 'management/users',
                name: 'UserManagement',
                component: () => import('@/views/management/UserManagement.vue'),
                meta: {
                    title: '用户管理',
                    requiresAuth: true,
                    roles: ['TEACHER', 'ADMIN'] // 教师和管理员可访问
                }
            },
            {
                path: 'management/basic-data',
                name: 'BasicDataManagement',
                component: () => import('@/views/management/BasicData.vue'),
                meta: {
                    title: '基础数据',
                    requiresAuth: true,
                    roles: ['TEACHER', 'ADMIN']
                }
            }
        ]
    },

    // ==================== 公开页面(无需登录) ====================
    {
        path: '/login',
        name: 'Login',
        component: () => import('@/views/auth/Login.vue'),
        meta: { title: '用户登录' }
        // 注意:没有 requiresAuth,所以无需登录即可访问
    },
    {
        path: '/register',
        name: 'Register',
        component: () => import('@/views/auth/Register.vue'),
        meta: { title: '用户注册' }
    },

    // ==================== 404 路由 ====================
    {
        path: '/:pathMatch(.*)*', //匹配所有未定义路由
        name: 'NotFound',
        redirect: '/dashboard' // 未找到的路由重定向到仪表盘
    }
]

/**
 * 创建路由实例
 * 使用 HTML5 History 模式(无 # 号,URL 更美观)
 */
const router = createRouter({
    history: createWebHistory(), // 使用 HTML5 History API
    routes
})

/**
 * 全局前置导航守卫
 * 在每次路由跳转前执行,用于权限控制
 * 
 * 守卫执行流程:
 * 1. 检查是否需要登录 (requiresAuth)
 * 2. 检查已登录用户访问公开页面的情况
 * 3. 检查角色权限 (roles)
 * 4. 设置页面标题
 * 5. 放行或拦截
 * 
 * @param {Object} to - 即将要进入的目标路由对象
 * @param {Object} from - 当前导航正要离开的路由
 * @param {Function} next - 调用该方法来 resolve 这个钩子
 */
router.beforeEach((to, from, next) => {
    // 1. 获取登录 token
    const token = localStorage.getItem('token')

    // 2. 定义公开页面列表(无需登录即可访问)
    const publicPages = ['/login', '/register']
    const isPublicPage = publicPages.includes(to.path)

    // ==================== 第一道关卡:登录验证 ====================
    // 如果目标页面需要登录(requiresAuth = true)且用户未登录(无 token)
    if (to.meta.requiresAuth && !token) {
        ElMessage.warning('请先登录')
        return next({
            path: '/login',
            query: { redirect: to.fullPath } // 保存原始目标路径,登录后自动跳转
        })
    }

    // ==================== 第二道关卡:已登录用户访问公开页面 ====================
    // 如果用户已登录,却访问登录/注册页面,自动跳转到仪表盘
    if (isPublicPage && token) {
        return next('/dashboard')
    }

    // ==================== 第三道关卡:角色权限验证 ====================
    // 如果页面需要登录,且用户已登录,检查角色权限
    if (to.meta.requiresAuth && token) {
        if (!canAccessRoute(to)) {
            // 角色权限不足,拒绝访问
            ElMessage.error('您没有权限访问该页面')

            // 如果是从其他页面跳转来的,阻止跳转(停留在原页面)
            if (from.path && from.path !== '/') {
                return next(false)
            } else {
                // 如果是直接访问或从根路径来的,跳转到仪表盘
                return next('/dashboard')
            }
        }
    }

    // ==================== 设置页面标题 ====================
    // 格式: "页面标题 - 宿舍卫生管理系统"
    document.title = (to.meta.title ? `${to.meta.title} - ` : '') + '宿舍卫生管理系统'

    // ==================== 通过所有检查,放行 ====================
    next()
})

export default router
