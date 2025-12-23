<script setup>
import { useRoute } from 'vue-router'
import { HomeFilled, List, Document, User } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import { computed } from 'vue'
import { isStudent, isInspector, isTeacher, isAdmin, getRoleDisplayName, getRoleTagType } from '@/utils/auth'

const route = useRoute()
const userStore = useUserStore()

// 角色显示名称和标签类型
const roleDisplayName = computed(() => {
  const role = userStore.userInfo?.role
  return role ? getRoleDisplayName(role) : '访客'
})

const roleTagType = computed(() => {
  const role = userStore.userInfo?.role
  return role ? getRoleTagType(role) : 'info'
})
</script>

<template>
  <div class="sidebar-container">
    <div class="logo-area">
      <div class="logo-icon">
        <el-icon><HomeFilled /></el-icon>
      </div>
      <div class="logo-text">
        <h2>宿管系统</h2>
      </div>
    </div>

    <el-menu
      :default-active="route.path"
      class="custom-menu"
      text-color="#64748b"
      active-text-color="#3b82f6"
      background-color="transparent"
      router
    >
      <div class="menu-label">常规</div>
      <el-menu-item index="/dashboard">
        <el-icon><HomeFilled /></el-icon>
        <span>仪表盘</span>
      </el-menu-item>
      
      <el-menu-item index="/profile">
        <el-icon><User /></el-icon>
        <span>个人信息</span>
      </el-menu-item>
      
      <template v-if="isStudent()">
        <div class="menu-label">卫生管理</div>
        <el-sub-menu index="inspection">
          <template #title>
            <el-icon><List /></el-icon>
            <span>检查事务</span>
          </template>
          <el-menu-item index="/inspection/my-record">我的记录</el-menu-item>
          <el-menu-item index="/inspection/my-applications">我的申请</el-menu-item>
          <el-menu-item index="/inspection/apply">申请检查员</el-menu-item>
          
          <el-menu-item v-if="isInspector()" index="/inspection/entry">录入检查</el-menu-item>
        </el-sub-menu>
      </template>

      <template v-if="isTeacher()">
        <div class="menu-label">教师管理</div>
        <el-sub-menu index="teacher">
          <template #title>
            <el-icon><Document /></el-icon>
            <span>管理中心</span>
          </template>
          <el-menu-item index="/admin/applications">申请审批</el-menu-item>
          <el-menu-item index="/management/inspections">检查记录</el-menu-item>
          <el-menu-item index="/management/users">用户管理</el-menu-item>
          <el-menu-item index="/management/basic-data">基础数据</el-menu-item>
        </el-sub-menu>
      </template>

      <template v-if="isAdmin()">
        <div class="menu-label">系统管理</div>
        <el-sub-menu index="admin">
          <template #title>
            <el-icon><Document /></el-icon>
            <span>管理中心</span>
          </template>
          <el-menu-item index="/management/users">用户管理</el-menu-item>
          <el-menu-item index="/management/basic-data">基础数据</el-menu-item>
        </el-sub-menu>
      </template>
    </el-menu>

    <div class="sidebar-footer">
      <el-tag :type="roleTagType" effect="plain" class="role-tag">
        {{ roleDisplayName }}
      </el-tag>
      <div class="user-name">{{ userStore.userInfo?.realName || userStore.userInfo?.username }}</div>
    </div>
  </div>
</template>

<style scoped>
.sidebar-container {
  height: 100%;
  display: flex;
  flex-direction: column;
  padding: 16px;
  background: white;
  border-right: 1px solid #e2e8f0;
}

.logo-area {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 0 12px 24px;
}

.logo-icon {
  width: 32px;
  height: 32px;
  background: var(--color-primary);
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}

.logo-text h2 {
  margin: 0;
  font-size: 16px;
  color: #1e293b;
}

.custom-menu {
  border: none;
  flex: 1;
}

.menu-label {
    padding: 12px 12px 4px;
    font-size: 12px;
    color: #94a3b8;
    font-weight: 600;
}

:deep(.el-menu-item) {
    border-radius: 6px;
    margin-bottom: 2px;
    height: 40px;
}

:deep(.el-menu-item.is-active) {
    background: #eff6ff;
    color: var(--color-primary);
    font-weight: 500;
}

.sidebar-footer {
    border-top: 1px solid #f1f5f9;
    padding-top: 16px;
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 8px;
}

.role-tag {
    font-size: 12px;
    font-weight: 500;
}

.user-name {
    font-size: 13px;
    color: #64748b;
    font-weight: 500;
    max-width: 100%;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}
</style>
