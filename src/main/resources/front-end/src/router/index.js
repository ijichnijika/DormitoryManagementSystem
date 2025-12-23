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
                    roles: ['STUDENT', 'INSPECTOR']
                }
            },
            {
                path: 'inspection/entry',
                name: 'InspectionEntry',
                component: () => import('@/views/inspection/EntryForm.vue'),
                meta: {
                    title: '录入检查',
                    requiresAuth: true,
                    roles: ['INSPECTOR', 'TEACHER', 'ADMIN']
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
            {
                path: 'admin/applications',
                name: 'AdminApplications',
                component: () => import('@/views/admin/Applications.vue'),
                meta: {
                    title: '审批管理',
                    requiresAuth: true,
                    roles: ['TEACHER']
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

router.beforeEach((to, from, next) => {
    const token = localStorage.getItem('token')
    const publicPages = ['/login', '/register']
    const isPublicPage = publicPages.includes(to.path)

    if (to.meta.requiresAuth && !token) {
        ElMessage.warning('请先登录')
        return next({
            path: '/login',
            query: { redirect: to.fullPath }
        })
    }

    if (isPublicPage && token) {
        return next('/dashboard')
    }

    if (to.meta.requiresAuth && token) {
        if (!canAccessRoute(to)) {
            ElMessage.error('您没有权限访问该页面')
            if (from.path && from.path !== '/') {
                return next(false)
            } else {
                return next('/dashboard')
            }
        }
    }

    document.title = (to.meta.title ? `${to.meta.title} - ` : '') + '宿舍卫生管理系统'

    next()
})

export default router
