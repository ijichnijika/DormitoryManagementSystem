<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Edit, Delete, Filter } from '@element-plus/icons-vue'
import http from '@/api/http'
import { useUserStore } from '@/stores/user'
import { isTeacher } from '@/utils/auth'

const userStore = useUserStore()
const tableData = ref([])
const loading = ref(false)
const buildings = ref([])
const rooms = ref([])

// 筛选表单
const filterForm = reactive({
  buildingId: '',
  roomId: '',
  minScore: '',
  maxScore: '',
  startDate: '',
  endDate: ''
})

// 编辑对话框
const editDialogVisible = ref(false)
const editForm = reactive({
  id: null,
  roomId: null,
  totalScore: 100,
  remarks: '',
  checkDate: ''
})

// 获取所有检查记录
const fetchData = async () => {
  loading.value = true
  try {
    const res = await http.get('/inspection/all')
    if (res.code === 200) {
      tableData.value = res.data || []
    }
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

// 获取宿舍楼列表(用于筛选)
const fetchBuildings = async () => {
  try {
    const res = await http.get('/building/all')
    if (res.code === 200) {
      buildings.value = res.data || []
    }
  } catch (e) {
    console.error(e)
  }
}

// 宿舍楼变化时获取房间列表
const handleBuildingChange = async (val) => {
  filterForm.roomId = ''
  if (!val) {
    rooms.value = []
    return
  }
  try {
    const res = await http.get(`/room/building/${val}`)
    if (res.code === 200) {
      rooms.value = res.data || []
    }
  } catch (e) {
    console.error(e)
  }
}

// 筛选数据(前端筛选,如果数据量大建议后端实现)
const filteredData = computed(() => {
  let data = [...tableData.value]
  
  if (filterForm.roomId) {
    data = data.filter(item => item.roomId === filterForm.roomId)
  }
  
  if (filterForm.minScore !== '') {
    data = data.filter(item => item.totalScore >= filterForm.minScore)
  }
  
  if (filterForm.maxScore !== '') {
    data = data.filter(item => item.totalScore <= filterForm.maxScore)
  }
  
  if (filterForm.startDate) {
    data = data.filter(item => item.checkDate >= filterForm.startDate)
  }
  
  if (filterForm.endDate) {
    data = data.filter(item => item.checkDate <= filterForm.endDate)
  }
  
  return data
})

// 重置筛选
const resetFilter = () => {
  Object.keys(filterForm).forEach(key => {
    filterForm[key] = ''
  })
  rooms.value = []
}

// 编辑记录
const handleEdit = (row) => {
  editForm.id = row.id
  editForm.roomId = row.roomId
  editForm.totalScore = row.totalScore
  editForm.remarks = row.remarks || ''
  editForm.checkDate = row.checkDate
  editDialogVisible.value = true
}

// 保存编辑
const saveEdit = async () => {
  try {
    const payload = {
      id: editForm.id,
      roomId: editForm.roomId,
      totalScore: editForm.totalScore,
      remarks: editForm.remarks,
      checkDate: editForm.checkDate,
      modifierId: userStore.userInfo?.id
    }
    
    const res = await http.put('/inspection', payload)
    if (res.code === 200) {
      ElMessage.success('修改成功')
      editDialogVisible.value = false
      fetchData()
    }
  } catch (e) {
    console.error(e)
  }
}

// 删除记录
const handleDelete = (row) => {
  ElMessageBox.confirm(
    `确认删除该检查记录吗? (房间ID: ${row.roomId}, 日期: ${row.checkDate})`,
    '警告',
    {
      confirmButtonText: '确认删除',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(async () => {
    try {
      const res = await http.delete(`/inspection/${row.id}`)
      if (res.code === 200) {
        ElMessage.success('删除成功')
        fetchData()
      }
    } catch (e) {
      console.error(e)
    }
  })
}

onMounted(() => {
  if (!isTeacher()) {
    ElMessage.error('无权限访问')
    return
  }
  fetchData()
  fetchBuildings()
})
</script>

<template>
  <div class="page-container">
    <el-card class="glass-card">
      <template #header>
        <div class="card-header">
          <div class="header-title">
            <h3>卫生检查记录管理</h3>
            <p>查看和管理所有宿舍卫生检查记录</p>
          </div>
        </div>
      </template>

      <!-- 筛选区域 -->
      <el-form :model="filterForm" class="filter-form">
        <el-row :gutter="16">
          <el-col :span="6">
            <el-form-item label="宿舍楼">
              <el-select 
                v-model="filterForm.buildingId" 
                placeholder="选择宿舍楼" 
                @change="handleBuildingChange"
                clearable
                class="w-100"
              >
                <el-option 
                  v-for="b in buildings" 
                  :key="b.id" 
                  :label="b.buildingName" 
                  :value="b.id" 
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="房间">
              <el-select 
                v-model="filterForm.roomId" 
                placeholder="选择房间" 
                clearable
                class="w-100"
              >
                <el-option 
                  v-for="r in rooms" 
                  :key="r.id" 
                  :label="r.roomNumber" 
                  :value="r.id" 
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="分数范围">
              <div class="score-range">
                <el-input-number v-model="filterForm.minScore" :min="0" :max="100" placeholder="最低分" />
                <span class="range-separator">-</span>
                <el-input-number v-model="filterForm.maxScore" :min="0" :max="100" placeholder="最高分" />
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="日期范围">
              <el-date-picker
                v-model="filterForm.startDate"
                type="date"
                placeholder="开始日期"
                value-format="YYYY-MM-DD"
                class="w-100"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="6" :offset="18">
            <el-button type="primary" :icon="Filter" @click="fetchData">应用筛选</el-button>
            <el-button @click="resetFilter">重置</el-button>
          </el-col>
        </el-row>
      </el-form>

      <!-- 数据表格 -->
      <el-table 
        :data="filteredData" 
        v-loading="loading" 
        class="custom-table" 
        style="width: 100%"
        empty-text="暂无检查记录"
      >
        <el-table-column prop="id" label="记录ID" width="80" />
        <el-table-column prop="roomId" label="房间ID" width="100" />
        <el-table-column prop="checkDate" label="检查日期" width="120" sortable />
        <el-table-column prop="totalScore" label="得分" width="100" sortable>
          <template #default="scope">
            <span :class="['score-tag', scope.row.totalScore >= 90 ? 'high' : scope.row.totalScore >= 60 ? 'mid' : 'low']">
              {{ scope.row.totalScore }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="remarks" label="备注" show-overflow-tooltip />
        <el-table-column prop="inspectorId" label="检查员ID" width="100" />
        <el-table-column prop="modifierId" label="修改人ID" width="100" />
        <el-table-column label="操作" width="150" align="center" fixed="right">
          <template #default="scope">
            <el-button size="small" type="primary" :icon="Edit" circle @click="handleEdit(scope.row)" />
            <el-button size="small" type="danger" :icon="Delete" circle @click="handleDelete(scope.row)" />
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 编辑对话框 -->
    <el-dialog v-model="editDialogVisible" title="修改检查记录" width="500px">
      <el-form :model="editForm" label-position="top">
        <el-form-item label="总分(0-100)">
          <el-slider v-model="editForm.totalScore" :max="100" show-input />
        </el-form-item>
        <el-form-item label="备注">
          <el-input 
            v-model="editForm.remarks" 
            type="textarea" 
            :rows="4" 
            placeholder="填写修改原因或评语..."
          />
        </el-form-item>
        <el-form-item label="检查日期">
          <el-date-picker
            v-model="editForm.checkDate"
            type="date"
            placeholder="选择日期"
            value-format="YYYY-MM-DD"
            class="w-100"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveEdit">保存</el-button>
      </template>
    </el-dialog>
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
  color: var(--color-text-primary);
}

.header-title p {
  margin: 0;
  font-size: 13px;
  color: var(--color-text-secondary);
}

.filter-form {
  margin-bottom: 20px;
  padding: 20px;
  background: #f8fafc;
  border-radius: 12px;
}

.score-range {
  display: flex;
  align-items: center;
  gap: 8px;
}

.range-separator {
  color: #94a3b8;
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

.custom-table {
  border-radius: 12px;
}

:deep(.el-table th.el-table__cell) {
  background: #f8fafc;
  color: var(--color-text-secondary);
  font-weight: 600;
}

.w-100 {
  width: 100%;
}
</style>
