<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Edit, Delete, Filter, Refresh } from '@element-plus/icons-vue'
import http from '@/api/http'
import { useUserStore } from '@/stores/user'
import { isTeacher } from '@/utils/auth'

const userStore = useUserStore()
const tableData = ref([])
const loading = ref(false)
const buildings = ref([])
const rooms = ref([])

// 分页参数
const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

// 查询表单
const queryForm = reactive({
  buildingId: null,
  roomId: null,
  inspectorId: null,
  minScore: null,
  maxScore: null,
  startDate: null,
  endDate: null
})

// 日期范围选择器
const dateRange = ref(null)

// 编辑对话框
const editDialogVisible = ref(false)
const editForm = reactive({
  id: null,
  roomId: null,
  totalScore: 100,
  remarks: '',
  checkDate: ''
})

// 调用后端分页API
const fetchData = async () => {
  loading.value = true
  try {
    const payload = {
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize,
      ...queryForm
    }
    const res = await http.post('/inspection/page', payload)
    if (res.code === 200) {
      tableData.value = res.data.records || []
      pagination.total = res.data.total || 0
    }
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

// 获取宿舍楼列表
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
  queryForm.roomId = null
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

// 搜索
const handleSearch = () => {
  pagination.pageNum = 1
  fetchData()
}

// 重置筛选
const resetFilter = () => {
  Object.keys(queryForm).forEach(key => {
    queryForm[key] = null
  })
  dateRange.value = null
  rooms.value = []
  pagination.pageNum = 1
  fetchData()
}

// 日期范围变化处理
const handleDateChange = (val) => {
  if (val && val.length === 2) {
    queryForm.startDate = val[0]
    queryForm.endDate = val[1]
  } else {
    queryForm.startDate = null
    queryForm.endDate = null
  }
}

// 页码变化
const handleCurrentChange = (val) => {
  pagination.pageNum = val
  fetchData()
}

// 每页条数变化
const handleSizeChange = (val) => {
  pagination.pageSize = val
  pagination.pageNum = 1
  fetchData()
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
    `确认删除该检查记录吗?`,
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
    <el-card class="main-card">
      <template #header>
        <div class="card-header">
          <h3>卫生检查记录管理</h3>
        </div>
      </template>

      <!-- 筛选区域 -->
      <el-form :model="queryForm" class="filter-form" inline>
        <el-form-item label="宿舍楼">
          <el-select
            v-model="queryForm.buildingId"
            placeholder="全部"
            @change="handleBuildingChange"
            clearable
            style="width: 130px"
          >
            <el-option
              v-for="b in buildings"
              :key="b.id"
              :label="b.buildingName"
              :value="b.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="房间">
          <el-select
            v-model="queryForm.roomId"
            placeholder="全部"
            clearable
            style="width: 110px"
            :disabled="!queryForm.buildingId"
          >
            <el-option
              v-for="r in rooms"
              :key="r.id"
              :label="r.roomNumber"
              :value="r.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="分数">
          <el-input-number v-model="queryForm.minScore" :min="0" :max="100" placeholder="最低" style="width: 85px" controls-position="right" />
          <span style="margin: 0 4px; color: #999;">-</span>
          <el-input-number v-model="queryForm.maxScore" :min="0" :max="100" placeholder="最高" style="width: 85px" controls-position="right" />
        </el-form-item>
        <el-form-item label="日期">
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="-"
            start-placeholder="开始"
            end-placeholder="结束"
            value-format="YYYY-MM-DD"
            @change="handleDateChange"
            style="width: 210px"
          />
        </el-form-item>
        <el-form-item class="action-buttons">
          <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
          <el-button :icon="Refresh" @click="resetFilter">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 数据表格 -->
      <el-table
        :data="tableData"
        v-loading="loading"
        stripe
        style="width: 100%"
        empty-text="暂无检查记录"
      >
        <el-table-column prop="id" label="ID" width="60" align="center" />
        <el-table-column label="宿舍房间" width="150" align="center">
          <template #default="{ row }">
            <div class="room-cell">
              <span class="building-name">{{ row.buildingName }}</span>
              <span class="room-sep"> - </span>
              <span class="room-number">{{ row.roomNumber }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="checkDate" label="检查日期" width="120" align="center" sortable />
        <el-table-column prop="totalScore" label="得分" width="100" align="center" sortable>
          <template #default="{ row }">
            <span :class="['score-tag', row.totalScore >= 90 ? 'high' : row.totalScore >= 60 ? 'mid' : 'low']">
              {{ row.totalScore }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="remarks" label="备注" min-width="180" show-overflow-tooltip />
        <el-table-column prop="inspectorId" label="检查员" width="80" align="center" />
        <el-table-column prop="modifierId" label="修改人" width="80" align="center" />
        <el-table-column label="操作" width="120" align="center" fixed="right">
          <template #default="{ row }">
            <el-button size="small" type="primary" :icon="Edit" circle @click="handleEdit(row)" />
            <el-button size="small" type="danger" :icon="Delete" circle @click="handleDelete(row)" />
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-wrapper">
        <span class="total-text">共 {{ pagination.total }} 条</span>
        <el-pagination
          v-model:current-page="pagination.pageNum"
          v-model:page-size="pagination.pageSize"
          :total="pagination.total"
          :page-sizes="[10, 20, 50]"
          layout="sizes, prev, pager, next, jumper"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
          background
        />
      </div>
    </el-card>

    <!-- 编辑对话框 -->
    <el-dialog v-model="editDialogVisible" title="修改检查记录" width="450px">
      <el-form :model="editForm" label-width="80px">
        <el-form-item label="得分">
          <el-slider v-model="editForm.totalScore" :max="100" show-input />
        </el-form-item>
        <el-form-item label="检查日期">
          <el-date-picker
            v-model="editForm.checkDate"
            type="date"
            placeholder="选择日期"
            value-format="YYYY-MM-DD"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="备注">
          <el-input
            v-model="editForm.remarks"
            type="textarea"
            :rows="3"
            placeholder="填写备注..."
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
  padding: 16px;
}

.main-card {
  border-radius: 16px;
  border: none;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
}

.card-header h3 {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
}

.filter-form {
  margin-bottom: 16px;
  padding: 16px 20px;
  background: #f8fafc;
  border-radius: 12px;
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 12px; /* 使用gap统一间距 */
}

.filter-form :deep(.el-form-item) {
  margin-bottom: 0;
  margin-right: 0; /* 移除右边距，由gap控制 */
}

.filter-form :deep(.action-buttons) {
  margin-left: auto;
  margin-right: 0;
}

/* 房间单元格样式 */
.room-cell {
  display: inline-flex;
  align-items: center;
}

.room-cell .building-name {
  color: #6366f1;
  font-weight: 600;
  font-size: 13px;
}

.room-cell .room-sep {
  color: #cbd5e1;
  margin: 0 4px;
}

.room-cell .room-number {
  color: #334155;
  font-weight: 500;
}

.score-tag {
  display: inline-block;
  padding: 2px 12px;
  border-radius: 6px;
  font-weight: 600;
  font-size: 14px;
}

.score-tag.high {
  color: #52c41a;
  background: #f6ffed;
}

.score-tag.mid {
  color: #faad14;
  background: #fffbe6;
}

.score-tag.low {
  color: #ff4d4f;
  background: #fff2f0;
}

:deep(.el-table) {
  border-radius: 12px;
}

:deep(.el-table th.el-table__cell) {
  background: #f8fafc;
}

.pagination-wrapper {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 16px;
  margin-top: 20px;
  padding-top: 16px;
  border-top: 1px solid #f0f0f0;
}

.total-text {
  color: #666;
  font-size: 13px;
}
</style>
