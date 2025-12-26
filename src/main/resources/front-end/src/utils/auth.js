/**
 * 权限工具函数库
 * 核心设计：支持多角色（逗号分隔字符串，如"STUDENT,INSPECTOR"）
 */

// 角色常量定义
export const ROLES = {
    STUDENT: 'STUDENT',     // 学生
    INSPECTOR: 'INSPECTOR', // 检查员
    TEACHER: 'TEACHER',     // 教师
    ADMIN: 'ADMIN'          // 管理员
}

// 角色权限层级（数字越大权限越高）
const ROLE_HIERARCHY = {
    [ROLES.ADMIN]: 4,
    [ROLES.TEACHER]: 3,
    [ROLES.INSPECTOR]: 2,
    [ROLES.STUDENT]: 1
}

// 获取当前登录用户信息
export function getCurrentUser() {
    try {
        const userInfoStr = localStorage.getItem('userInfo')
        return userInfoStr ? JSON.parse(userInfoStr) : null
    } catch (e) {
        console.error('Failed to parse user info:', e)
        return null
    }
}

// 获取当前用户的所有角色数组
export function getCurrentRole() {
    const user = getCurrentUser()
    if (!user || !user.role) return null

    // 支持逗号分隔的字符串格式
    if (typeof user.role === 'string') {
        return user.role.split(',').map(r => r.trim())
    }

    return Array.isArray(user.role) ? user.role : [user.role]
}

// 判断当前用户是否拥有指定角色
export function hasRole(role) {
    const currentRoles = getCurrentRole()
    if (!currentRoles) return false
    return currentRoles.includes(role)
}

// 判断当前用户是否拥有任意一个指定角色
export function hasAnyRole(roles) {
    if (!roles || roles.length === 0) return true
    const currentRoles = getCurrentRole()
    if (!currentRoles) return false
    return currentRoles.some(r => roles.includes(r))
}

// 判断当前用户是否拥有所有指定角色
export function hasAllRoles(roles) {
    if (!roles || roles.length === 0) return true
    const currentRoles = getCurrentRole()
    if (!currentRoles) return false
    return roles.every(r => currentRoles.includes(r))
}

// 判断当前用户权限等级是否满足要求
export function hasRoleLevel(role) {
    const currentRoles = getCurrentRole()
    if (!currentRoles || !role) return false

    const currentLevel = Math.max(...currentRoles.map(r => ROLE_HIERARCHY[r] || 0))
    const requiredLevel = ROLE_HIERARCHY[role] || 0

    return currentLevel >= requiredLevel
}

// 判断当前用户是否可以访问指定路由
export function canAccessRoute(route) {
    if (!route.meta?.requiresAuth) return true // 公开路由

    const token = localStorage.getItem('token')
    if (!token) return false // 需要登录但未登录

    const requiredRoles = route.meta?.roles
    if (!requiredRoles || requiredRoles.length === 0) return true // 无角色限制

    return hasAnyRole(requiredRoles) // 检查角色权限
}

export function isStudent() {
    const roles = getCurrentRole()
    if (!roles) return false
    return roles.includes(ROLES.STUDENT) || roles.includes(ROLES.INSPECTOR)
}

export function isInspector() {
    return hasRole(ROLES.INSPECTOR)
}

export function isTeacher() {
    return hasRole(ROLES.TEACHER)
}

export function isAdmin() {
    return hasRole(ROLES.ADMIN)
}

export function isManager() {
    return hasAnyRole([ROLES.TEACHER, ROLES.ADMIN])
}

// 将角色代码转换为中文显示名称
export function getRoleDisplayName(role) {
    if (!role) return '未知'

    const roleNames = {
        [ROLES.STUDENT]: '学生',
        [ROLES.INSPECTOR]: '检查员',
        [ROLES.TEACHER]: '教师',
        [ROLES.ADMIN]: '管理员'
    }

    // 处理多角色情况
    if (typeof role === 'string' && role.includes(',')) {
        const roles = role.split(',').map(r => r.trim())
        return roles.map(r => roleNames[r] || '未知').join('、')
    }

    return roleNames[role] || '未知'
}

// 获取角色标签的Element Plus类型（用于显示不同颜色）
export function getRoleTagType(role) {
    if (!role) return 'info'

    const tagTypes = {
        [ROLES.STUDENT]: '',
        [ROLES.INSPECTOR]: 'success',
        [ROLES.TEACHER]: 'warning',
        [ROLES.ADMIN]: 'danger'
    }

    // 多角色时，返回最高权限角色的标签类型
    if (typeof role === 'string' && role.includes(',')) {
        const roles = role.split(',').map(r => r.trim())
        const priority = [ROLES.ADMIN, ROLES.TEACHER, ROLES.INSPECTOR, ROLES.STUDENT]
        for (const r of priority) {
            if (roles.includes(r)) {
                return tagTypes[r] || 'info'
            }
        }
    }

    return tagTypes[role] || 'info'
}
