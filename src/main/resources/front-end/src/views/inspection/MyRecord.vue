<script setup>
import { ref, onMounted } from 'vue'
import { Calendar, Search } from '@element-plus/icons-vue'
import http from '@/api/http'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'

const userStore = useUserStore()
const tableData = ref([])
const loading = ref(false)
const queryDate = ref('')


const roomId = userStore.userInfo?.roomId || 0

const fetchData = async () => {
  if (!roomId) {
      ElMessage.warning('当前用户未绑定宿舍')
      return
  }

  loading.value = true
  try {
    let res
    if (queryDate.value) {
      
      const date = new Date(queryDate.value)
      const year = date.getFullYear()
      const month = date.getMonth() + 1
      const startDate = `${year}-${String(month).padStart(2, '0')}-01`
      const lastDay = new Date(year, month, 0).getDate()
      const endDate = `${year}-${String(month).padStart(2, '0')}-${String(lastDay).padStart(2, '0')}`
      
      res = await http.get(`/inspection/room/${roomId}/date-range`, {
        params: { startDate, endDate }
      })
    } else {
      res = await http.get(`/inspection/room/${roomId}`)
    }
    
    if (res.code === 200) {
        tableData.value = res.data
    }
  } catch (e) {
  } finally {
      loading.value = false
  }
}

onMounted(() => {
  fetchData()
})
</script>

<template>
  <div class="page-container">
    <el-card class="glass-card">
      <template #header>
        <div class="card-header">
          <div class="header-titile">
            <h3>我的卫生记录</h3>
            <p>查看历史检查评分详情</p>
          </div>
          <div class="filter-box">
             <el-date-picker
                v-model="queryDate"
                type="month"
                placeholder="选择月份"
                :prefix-icon="Calendar"
                style="width: 160px; margin-right: 12px"
              />
            <el-button type="primary" :icon="Search" @click="fetchData">查询</el-button>
          </div>
        </div>
      </template>

      <el-table :data="tableData" style="width: 100%" v-loading="loading" class="custom-table">
        <el-table-column prop="checkDate" label="日期" width="180" sortable />
        <el-table-column prop="totalScore" label="得分" width="120" sortable>
          <template #default="scope">
            <span :class="['score-tag', scope.row.totalScore >= 90 ? 'high' : scope.row.totalScore >= 60 ? 'mid' : 'low']">
              {{ scope.row.totalScore }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="remarks" label="备注" show-overflow-tooltip />
        <el-table-column prop="inspectorId" label="检查员ID" width="120" />
      </el-table>
    </el-card>
  </div>
</template>

<style scoped>
.page-container {
  padding: 0;
}

.glass-card {
    background: white;
    border-radius: 20px;
    border: none;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-titile h3 {
    margin: 0 0 4px;
    font-size: 18px;
    color: var(--color-text-primary);
}

.header-titile p {
    margin: 0;
    font-size: 13px;
    color: var(--color-text-secondary);
}

.filter-box {
  display: flex;
}

.score-tag {
    font-weight: 700;
    padding: 4px 12px;
    border-radius: 8px;
    font-size: 14px;
}

.score-tag.high {
    color: #10b981;
    background: #ecfdf5;
}

.score-tag.mid {
    color: #f59e0b;
    background: #fffbeb;
}

.score-tag.low {
    color: #ef4444;
    background: #fef2f2;
}


:deep(.el-table) {
    --el-table-border-color: transparent;
    --el-table-header-bg-color: #f8fafc;
    border-radius: 12px;
}

:deep(.el-table th.el-table__cell) {
    color: var(--color-text-secondary);
    font-weight: 600;
}
</style>
