import { createRouter, createWebHistory } from 'vue-router'
import { canAccessRoute } from '@/utils/auth'
import { ElMessage } from 'element-plus'

const routes = [
    {
        path: '/',
        component: () => import('@/layout/MainLayout.vue'),
        redirect: '/dashboard',
        children: [
            {
                path: 'dashboard',
                name: 'Dashboard',
                component: () => import('@/views/dashboard/Dashboard.vue'),
                meta: {
                    title: '仪表盘',
                    requiresAuth: true,
                    roles: ['STUDENT', 'INSPECTOR', 'TEACHER', 'ADMIN']
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
            {
                path: 'inspection/my-record',
                name: 'MyRecord',
                component: () => import('@/views/inspection/MyRecord.vue'),
                meta: {
                    title: '我的卫生记录',
                    requiresAuth: true,
                    roles: ['STUDENT', 'INSPECTOR'] // 仅学生和检查员
                }
            },
            {
                path: 'inspection/entry',
                name: 'InspectionEntry',
                component: () => import('@/views/inspection/EntryForm.vue'),
                meta: {
                    title: '录入检查',
                    requiresAuth: true,
                    roles: ['INSPECTOR', 'TEACHER', 'ADMIN'] // 仅检查员和管理人员
                }
            },
            {
                path: 'inspection/apply',
                name: 'InspectionApply',
                component: () => import('@/views/inspection/ApplicationForm.vue'),
                meta: {
                    title: '申请检查员',
                    requiresAuth: true,
                    roles: ['STUDENT', 'INSPECTOR'] // 学生和检查员可申请
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
            {
                path: 'admin/applications',
                name: 'AdminApplications',
                component: () => import('@/views/admin/Applications.vue'),
                meta: {
                    title: '审批管理',
                    requiresAuth: true,
                    roles: ['TEACHER'] // 仅教师
                }
            },
            {
                path: 'management/inspections',
                name: 'InspectionManagement',
                component: () => import('@/views/management/InspectionRecords.vue'),
                meta: {
                    title: '检查记录管理',
                    requiresAuth: true,
                    roles: ['TEACHER'] // 仅教师
                }
            },
            {
                path: 'management/users',
                name: 'UserManagement',
                component: () => import('@/views/management/UserManagement.vue'),
                meta: {
                    title: '用户管理',
                    requiresAuth: true,
                    roles: ['TEACHER', 'ADMIN']
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
    {
        path: '/login',
        name: 'Login',
        component: () => import('@/views/auth/Login.vue'),
        meta: { title: '用户登录' }
    },
    {
        path: '/register',
        name: 'Register',
        component: () => import('@/views/auth/Register.vue'),
        meta: { title: '用户注册' }
    },
    {
        path: '/:pathMatch(.*)*',
        name: 'NotFound',
        redirect: '/dashboard'
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

// 增强的导航守卫 - 支持基于角色的权限控制
router.beforeEach((to, from, next) => {
    const token = localStorage.getItem('token')

    // 公开页面白名单
    const publicPages = ['/login', '/register']
    const isPublicPage = publicPages.includes(to.path)

    // 1. 未登录用户访问受保护页面 -> 跳转到登录页
    if (to.meta.requiresAuth && !token) {
        ElMessage.warning('请先登录')
        return next({
            path: '/login',
            query: { redirect: to.fullPath } // 保存目标路径,登录后跳转
        })
    }

    // 2. 已登录用户访问登录/注册页 -> 跳转到仪表盘
    if (isPublicPage && token) {
        return next('/dashboard')
    }

    // 3. 检查角色权限
    if (to.meta.requiresAuth && token) {
        if (!canAccessRoute(to)) {
            ElMessage.error('您没有权限访问该页面')
            // 如果是从其他页面跳转来的,返回上一页;否则跳转到仪表盘
            if (from.path && from.path !== '/') {
                return next(false) // 取消导航
            } else {
                return next('/dashboard')
            }
        }
    }

    // 4. 设置页面标题
    document.title = (to.meta.title ? `${to.meta.title} - ` : '') + '宿舍卫生管理系统'

    next()
})

export default router
