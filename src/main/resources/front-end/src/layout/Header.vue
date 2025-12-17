<script setup>
import { useRouter, useRoute } from 'vue-router'
import { SwitchButton, ArrowDown, Bell } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import { computed } from 'vue'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const pageTitle = computed(() => route.meta.title || 'Dashboard')
const username = computed(() => userStore.userInfo?.username || 'User')

const handleLogout = () => {
  userStore.logout()
  router.push('/login')
}
</script>

<template>
  <div class="header-inner">
    <div class="left-section">
      <h2 class="page-title">{{ pageTitle }}</h2>
    </div>
    
    <div class="right-section">
      <div class="action-btn">
        <el-icon><Bell /></el-icon>
      </div>
      
      <el-dropdown trigger="click">
        <div class="user-dropdown">
          <span class="username">{{ username }}</span>
          <el-icon><ArrowDown /></el-icon>
        </div>
        <template #dropdown>
          <el-dropdown-menu class="custom-dropdown">
            <el-dropdown-item :icon="SwitchButton" @click="handleLogout">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<style scoped>
.header-inner {
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.page-title {
  font-size: 18px;
  font-weight: 600;
  color: var(--color-text-primary);
  margin: 0;
}

.right-section {
  display: flex;
  align-items: center;
  gap: 20px;
}

.action-btn {
  width: 40px;
  height: 40px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  color: var(--color-text-secondary);
  transition: background 0.3s;
}

.action-btn:hover {
  background: #f1f5f9;
  color: var(--color-primary);
}

.user-dropdown {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 6px 12px;
  border-radius: 12px;
  transition: background 0.3s;
}

.user-dropdown:hover {
  background: #f1f5f9;
}

.username {
  font-weight: 500;
  font-size: 14px;
}

/* Dropdown override if possible, though Element Plus uses global portal */
</style>
