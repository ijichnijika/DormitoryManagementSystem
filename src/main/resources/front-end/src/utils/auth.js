/**
 * 权限管理工具模块
 * 统一管理角色常量和权限检查逻辑
 */

// 角色常量定义 - 必须与后端保持一致
export const ROLES = {
    STUDENT: 'STUDENT',       // 普通学生
    INSPECTOR: 'INSPECTOR',   // 卫生检查员
    TEACHER: 'TEACHER',       // 教师
    ADMIN: 'ADMIN'            // 系统管理员
}

// 角色层级定义(用于权限继承)
const ROLE_HIERARCHY = {
    [ROLES.ADMIN]: 4,
    [ROLES.TEACHER]: 3,
    [ROLES.INSPECTOR]: 2,
    [ROLES.STUDENT]: 1
}

/**
 * 获取当前登录用户信息
 * @returns {Object|null} 用户信息
 */
export function getCurrentUser() {
    try {
        const userInfoStr = localStorage.getItem('userInfo')
        return userInfoStr ? JSON.parse(userInfoStr) : null
    } catch (e) {
        console.error('Failed to parse user info:', e)
        return null
    }
}

/**
 * 获取当前用户角色
 * @returns {string[]|null} 角色数组
 */
export function getCurrentRole() {
    const user = getCurrentUser()
    if (!user || !user.role) return null

    // 支持逗号分隔的多角色字符串
    if (typeof user.role === 'string') {
        return user.role.split(',').map(r => r.trim())
    }

    // 如果已经是数组,直接返回
    return Array.isArray(user.role) ? user.role : [user.role]
}

/**
 * 检查用户是否拥有指定角色
 * @param {string} role - 要检查的角色
 * @returns {boolean}
 */
export function hasRole(role) {
    const currentRoles = getCurrentRole()
    if (!currentRoles) return false
    return currentRoles.includes(role)
}

/**
 * 检查用户是否拥有指定角色中的任意一个
 * @param {string[]} roles - 角色数组
 * @returns {boolean}
 */
export function hasAnyRole(roles) {
    if (!roles || roles.length === 0) return true
    const currentRoles = getCurrentRole()
    if (!currentRoles) return false

    // 检查当前用户的任意角色是否在要求的角色列表中
    return currentRoles.some(r => roles.includes(r))
}

/**
 * 检查用户是否拥有指定的所有角色
 * @param {string[]} roles - 角色数组
 * @returns {boolean}
 */
export function hasAllRoles(roles) {
    if (!roles || roles.length === 0) return true
    const currentRoles = getCurrentRole()
    if (!currentRoles) return false

    // 检查要求的所有角色是否都在用户角色列表中
    return roles.every(r => currentRoles.includes(r))
}

/**
 * 检查用户角色是否高于或等于指定角色(基于层级)
 * @param {string} role - 要比较的角色
 * @returns {boolean}
 */
export function hasRoleLevel(role) {
    const currentRoles = getCurrentRole()
    if (!currentRoles || !role) return false

    // 获取用户最高权限等级
    const currentLevel = Math.max(...currentRoles.map(r => ROLE_HIERARCHY[r] || 0))
    const requiredLevel = ROLE_HIERARCHY[role] || 0

    return currentLevel >= requiredLevel
}

/**
 * 检查用户是否可以访问指定页面
 * @param {Object} route - 路由对象(Vue Router route)
 * @returns {boolean}
 */
export function canAccessRoute(route) {
    // 如果路由不需要认证,直接允许
    if (!route.meta?.requiresAuth) return true

    // 检查是否登录
    const token = localStorage.getItem('token')
    if (!token) return false

    // 如果没有指定角色要求,登录即可访问
    const requiredRoles = route.meta?.roles
    if (!requiredRoles || requiredRoles.length === 0) return true

    // 检查角色权限
    const currentRole = getCurrentRole()
    const hasAccess = hasAnyRole(requiredRoles)

    console.log('🔍 权限检查:', {
        path: route.path,
        currentRole,
        requiredRoles,
        hasAccess
    })

    return hasAccess
}

/**
 * 检查用户是否是学生(包括检查员,因为检查员也是学生)
 * @returns {boolean}
 */
export function isStudent() {
    const roles = getCurrentRole()
    if (!roles) return false
    return roles.includes(ROLES.STUDENT) || roles.includes(ROLES.INSPECTOR)
}

/**
 * 检查用户是否是检查员
 * @returns {boolean}
 */
export function isInspector() {
    return hasRole(ROLES.INSPECTOR)
}

/**
 * 检查用户是否是教师
 * @returns {boolean}
 */
export function isTeacher() {
    return hasRole(ROLES.TEACHER)
}

/**
 * 检查用户是否是管理员
 * @returns {boolean}
 */
export function isAdmin() {
    return hasRole(ROLES.ADMIN)
}

/**
 * 检查用户是否是管理人员(教师或管理员)
 * @returns {boolean}
 */
export function isManager() {
    return hasAnyRole([ROLES.TEACHER, ROLES.ADMIN])
}

/**
 * 获取角色显示名称
 * @param {string} role - 角色常量或逗号分隔的多角色字符串
 * @returns {string} 角色中文名称
 */
export function getRoleDisplayName(role) {
    if (!role) return '未知'

    const roleNames = {
        [ROLES.STUDENT]: '学生',
        [ROLES.INSPECTOR]: '检查员',
        [ROLES.TEACHER]: '教师',
        [ROLES.ADMIN]: '管理员'
    }

    // 如果是多角色(逗号分隔)
    if (typeof role === 'string' && role.includes(',')) {
        const roles = role.split(',').map(r => r.trim())
        return roles.map(r => roleNames[r] || '未知').join('、')
    }

    return roleNames[role] || '未知'
}

/**
 * 获取角色标签类型(用于Element Plus Tag组件)
 * @param {string} role - 角色常量或逗号分隔的多角色字符串
 * @returns {string} Element Plus tag type
 */
export function getRoleTagType(role) {
    if (!role) return 'info'

    const tagTypes = {
        [ROLES.STUDENT]: '',
        [ROLES.INSPECTOR]: 'success',
        [ROLES.TEACHER]: 'warning',
        [ROLES.ADMIN]: 'danger'
    }

    // 如果是多角色,返回最高权限角色的标签
    if (typeof role === 'string' && role.includes(',')) {
        const roles = role.split(',').map(r => r.trim())
        // 按优先级排序: ADMIN > TEACHER > INSPECTOR > STUDENT
        const priority = [ROLES.ADMIN, ROLES.TEACHER, ROLES.INSPECTOR, ROLES.STUDENT]
        for (const r of priority) {
            if (roles.includes(r)) {
                return tagTypes[r] || 'info'
            }
        }
    }

    return tagTypes[role] || 'info'
}
