export const ROLES = {
    STUDENT: 'STUDENT',
    INSPECTOR: 'INSPECTOR',
    TEACHER: 'TEACHER',
    ADMIN: 'ADMIN'
}

const ROLE_HIERARCHY = {
    [ROLES.ADMIN]: 4,
    [ROLES.TEACHER]: 3,
    [ROLES.INSPECTOR]: 2,
    [ROLES.STUDENT]: 1
}

export function getCurrentUser() {
    try {
        const userInfoStr = localStorage.getItem('userInfo')
        return userInfoStr ? JSON.parse(userInfoStr) : null
    } catch (e) {
        console.error('Failed to parse user info:', e)
        return null
    }
}

export function getCurrentRole() {
    const user = getCurrentUser()
    if (!user || !user.role) return null

    if (typeof user.role === 'string') {
        return user.role.split(',').map(r => r.trim())
    }

    return Array.isArray(user.role) ? user.role : [user.role]
}

export function hasRole(role) {
    const currentRoles = getCurrentRole()
    if (!currentRoles) return false
    return currentRoles.includes(role)
}

export function hasAnyRole(roles) {
    if (!roles || roles.length === 0) return true
    const currentRoles = getCurrentRole()
    if (!currentRoles) return false
    return currentRoles.some(r => roles.includes(r))
}

export function hasAllRoles(roles) {
    if (!roles || roles.length === 0) return true
    const currentRoles = getCurrentRole()
    if (!currentRoles) return false
    return roles.every(r => currentRoles.includes(r))
}

export function hasRoleLevel(role) {
    const currentRoles = getCurrentRole()
    if (!currentRoles || !role) return false

    const currentLevel = Math.max(...currentRoles.map(r => ROLE_HIERARCHY[r] || 0))
    const requiredLevel = ROLE_HIERARCHY[role] || 0

    return currentLevel >= requiredLevel
}

export function canAccessRoute(route) {
    if (!route.meta?.requiresAuth) return true

    const token = localStorage.getItem('token')
    if (!token) return false

    const requiredRoles = route.meta?.roles
    if (!requiredRoles || requiredRoles.length === 0) return true

    return hasAnyRole(requiredRoles)
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

export function getRoleDisplayName(role) {
    if (!role) return '未知'

    const roleNames = {
        [ROLES.STUDENT]: '学生',
        [ROLES.INSPECTOR]: '检查员',
        [ROLES.TEACHER]: '教师',
        [ROLES.ADMIN]: '管理员'
    }

    if (typeof role === 'string' && role.includes(',')) {
        const roles = role.split(',').map(r => r.trim())
        return roles.map(r => roleNames[r] || '未知').join('、')
    }

    return roleNames[role] || '未知'
}

export function getRoleTagType(role) {
    if (!role) return 'info'

    const tagTypes = {
        [ROLES.STUDENT]: '',
        [ROLES.INSPECTOR]: 'success',
        [ROLES.TEACHER]: 'warning',
        [ROLES.ADMIN]: 'danger'
    }

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
