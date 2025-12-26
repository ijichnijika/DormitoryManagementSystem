<script setup>
import { ref, onMounted } from 'vue'
import { Check, Close } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import http from '@/api/http'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
const list = ref([])
const loading = ref(false)

const fetchData = async () => {
  loading.value = true
  try {
      const res = await http.get('/application/pending')
      if (res.code === 200) {
          list.value = res.data
      }
  } catch (e) {
  } finally {
      loading.value = false
  }
}

const handleApprove = (row) => {
  ElMessageBox.confirm(`确认通过该同学的申请?`, '提示', {
    confirmButtonText: '通过',
    cancelButtonText: '取消',
    type: 'success'
  }).then(async () => {
      try {
          const res = await http.put(`/application/${row.id}/approve?reviewerId=${userStore.userInfo?.id || 1}`)
          if (res.code === 200) {
             ElMessage.success('已审核通过')
             fetchData() 
          }
      } catch(e) { }
  })
}

const handleReject = (row) => {
  ElMessageBox.prompt('请输入驳回理由', '驳回', {
    confirmButtonText: '确认驳回',
    cancelButtonText: '取消',
  }).then(async ({ value }) => {
      try {
          const res = await http.put(`/application/${row.id}/reject?reviewerId=${userStore.userInfo?.id || 1}&reviewComment=${value}`)
          if (res.code === 200) {
              ElMessage.info(`已驳回: ${value}`)
              fetchData()
          }
      } catch(e) { }
  })
}

onMounted(fetchData)
</script>

<template>
  <el-card class="glass-card">
    <template #header>
      <h3>权限申请审批</h3>
    </template>
    
    <el-table :data="list" v-loading="loading" style="width: 100%" class="custom-table" empty-text="暂无待审核申请">
      <el-table-column prop="applicantId" label="申请人ID" width="120" />
      <el-table-column prop="applicationReason" label="申请理由" show-overflow-tooltip />
      <el-table-column prop="applyTime" label="申请时间" width="200">
        <template #default="scope">
            {{ new Date(scope.row.applyTime).toLocaleString() }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200" align="center">
        <template #default="scope">
          <div class="action-buttons">
            <el-button size="small" type="success" :icon="Check" circle @click="handleApprove(scope.row)" />
            <el-button size="small" type="danger" :icon="Close" circle @click="handleReject(scope.row)" />
          </div>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<style scoped>
.glass-card {
    background: white;
    border-radius: 20px;
    border: none;
    padding: 20px;
}

h3 {
    margin: 0;
    font-size: 18px;
    color: var(--color-text-primary);
}

.custom-table {
    border-radius: 12px;
}

:deep(.el-table th.el-table__cell) {
    background: #f8fafc;
    color: var(--color-text-secondary);
}
</style>
