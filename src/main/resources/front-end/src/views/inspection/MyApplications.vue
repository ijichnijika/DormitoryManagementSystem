<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import http from '@/api/http'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()
const list = ref([])
const loading = ref(false)

const statusMap = {
  0: { label: '待审核', type: 'warning' },
  1: { label: '已通过', type: 'success' },
  2: { label: '已驳回', type: 'danger' }
}

const fetchData = async () => {
  loading.value = true
  try {
    const userId = userStore.userInfo?.id
    if (!userId) {
      ElMessage.error('用户信息丢失')
      return
    }
    
    const res = await http.get(`/application/applicant/${userId}`)
    if (res.code === 200) {
      list.value = res.data || []
    }
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

const goToApply = () => {
  router.push('/inspection/apply')
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
          <div class="header-title">
            <h3>我的检查员申请</h3>
            <p>查看申请状态和审核结果</p>
          </div>
          <el-button type="primary" @click="goToApply">提交新申请</el-button>
        </div>
      </template>

      <el-table 
        :data="list" 
        v-loading="loading" 
        class="custom-table"
        empty-text="您还没有提交过申请"
      >
        <el-table-column prop="id" label="申请ID" width="100" />
        <el-table-column prop="applicationReason" label="申请理由" show-overflow-tooltip />
        <el-table-column prop="applyTime" label="申请时间" width="180">
          <template #default="scope">
            {{ new Date(scope.row.applyTime).toLocaleString() }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="statusMap[scope.row.status]?.type || 'info'">
              {{ statusMap[scope.row.status]?.label || '未知' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="reviewComment" label="审核意见" show-overflow-tooltip>
          <template #default="scope">
            <span v-if="scope.row.reviewComment">{{ scope.row.reviewComment }}</span>
            <span v-else class="text-muted">暂无</span>
          </template>
        </el-table-column>
        <el-table-column prop="reviewTime" label="审核时间" width="180">
          <template #default="scope">
            <span v-if="scope.row.reviewTime">{{ new Date(scope.row.reviewTime).toLocaleString() }}</span>
            <span v-else class="text-muted">-</span>
          </template>
        </el-table-column>
      </el-table>

      <el-alert 
        v-if="list.some(item => item.status === 0)"
        title="您有待审核的申请,请耐心等待教师审批"
        type="info"
        :closable="false"
        class="mt-20"
      />
      
      <el-alert 
        v-if="list.some(item => item.status === 2)"
        title="您有被驳回的申请,可以修改后重新提交"
        type="warning"
        :closable="false"
        class="mt-20"
      />
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

.header-title h3 {
  margin: 0 0 4px;
  font-size: 18px;
}

.header-title p {
  margin: 0;
  font-size: 13px;
  color: #64748b;
}

.custom-table {
  border-radius: 12px;
}

:deep(.el-table th.el-table__cell) {
  background: #f8fafc;
  color: var(--color-text-secondary);
  font-weight: 600;
}

.text-muted {
  color: #94a3b8;
}

.mt-20 {
  margin-top: 20px;
}
</style>
