<script setup>
import { computed } from 'vue'
import { Monitor, User, House, Rank, DocumentChecked } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
const role = computed(() => userStore.userInfo?.role || 'STUDENT')

const studentStats = [
    { title: '我的评分', value: '98', icon: Rank, color: '#3b82f6' },
    { title: '本周排名', value: 'Top 10', icon: Monitor, color: '#10b981' }
]

const adminStats = [
    { title: '待办审批', value: '4', icon: DocumentChecked, color: '#f59e0b' },
    { title: '总人数', value: '2,834', icon: User, color: '#6366f1' },
    { title: '违规记录', value: '12', icon: House, color: '#ef4444' }
]

const stats = computed(() => role.value === 'ADMIN' || role.value === 'TEACHER' ? adminStats : studentStats)
</script>

<template>
  <div class="dashboard-container">
    <h2>仪表盘</h2>
    <p class="subtitle">欢迎回来, {{ userStore.userInfo?.realName || userStore.userInfo?.username }}</p>

    <div class="stats-grid">
      <div v-for="stat in stats" :key="stat.title" class="stat-card">
        <div class="stat-icon" :style="{ background: stat.color + '20', color: stat.color }">
          <component :is="stat.icon" />
        </div>
        <div class="stat-info">
          <span class="stat-value">{{ stat.value }}</span>
          <span class="stat-label">{{ stat.title }}</span>
        </div>
      </div>
    </div>
    <div class="content-placeholder">
        <el-empty description="暂无更多动态" />
    </div>
  </div>
</template>

<style scoped>
.dashboard-container {
  padding: 0;
}

h2 {
    margin: 0 0 8px;
    color: #1e293b;
}

.subtitle {
    margin: 0 0 24px;
    color: #64748b;
    font-size: 14px;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-bottom: 24px;
}

.stat-card {
  padding: 24px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  gap: 16px;
  background: white;
  border: 1px solid #e2e8f0;
}

.stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
}

.stat-info {
  display: flex;
  flex-direction: column;
}

.stat-value {
  font-size: 24px;
  font-weight: 700;
  color: #1e293b;
}

.stat-label {
  font-size: 13px;
  color: #64748b;
}

.content-placeholder {
    background: white;
    border-radius: 8px;
    min-height: 400px;
    display: flex;
    justify-content: center;
    align-items: center;
    border: 1px solid #e2e8f0;
}
</style>
