<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Edit, Delete } from '@element-plus/icons-vue'
import http from '@/api/http'
import { isManager } from '@/utils/auth'

const activeTab = ref('class')

// 班级数据
const classList = ref([])
const classLoading = ref(false)
const classDialogVisible = ref(false)
const classForm = ref({ id: null, className: '', counselorName: '' })

// 宿舍楼数据
const buildingList = ref([])
const buildingLoading = ref(false)
const buildingDialogVisible = ref(false)
const buildingForm = ref({ id: null, buildingName: '', managerName: '' })

// 房间数据
const roomList = ref([])
const roomLoading = ref(false)
const roomDialogVisible = ref(false)
const roomForm = ref({ id: null, buildingId: null, roomNumber: '', capacity: 4, gender: 0 })

// ========== 班级管理 ==========
const fetchClasses = async () => {
  classLoading.value = true
  try {
    const res = await http.get('/class/all')
    if (res.code === 200) classList.value = res.data || []
  } catch (e) {
    console.error(e)
  } finally {
    classLoading.value = false
  }
}

const handleClassAdd = () => {
  classForm.value = { id: null, className: '', counselorName: '' }
  classDialogVisible.value = true
}

const handleClassEdit = (row) => {
  classForm.value = { ...row }
  classDialogVisible.value = true
}

const saveClass = async () => {
  try {
    const isEdit = !!classForm.value.id
    const res = isEdit 
      ? await http.put('/class', classForm.value)
      : await http.post('/class', classForm.value)
    
    if (res.code === 200) {
      ElMessage.success(isEdit ? '修改成功' : '创建成功')
      classDialogVisible.value = false
      fetchClasses()
    }
  } catch (e) {
    console.error(e)
  }
}

const handleClassDelete = (row) => {
  ElMessageBox.confirm(`确认删除班级 ${row.className} 吗?`, '警告', {
    type: 'warning'
  }).then(async () => {
    try {
      const res = await http.delete(`/class/${row.id}`)
      if (res.code === 200) {
        ElMessage.success('删除成功')
        fetchClasses()
      }
    } catch (e) {
      console.error(e)
    }
  })
}

// ========== 宿舍楼管理 ==========
const fetchBuildings = async () => {
  buildingLoading.value = true
  try {
    const res = await http.get('/building/all')
    if (res.code === 200) {
      buildingList.value = res.data || []
    }
  } catch (e) {
    console.error(e)
  } finally {
    buildingLoading.value = false
  }
}

const handleBuildingAdd = () => {
  buildingForm.value = { id: null, buildingName: '', managerName: '' }
  buildingDialogVisible.value = true
}

const handleBuildingEdit = (row) => {
  buildingForm.value = { ...row }
  buildingDialogVisible.value = true
}

const saveBuilding = async () => {
  try {
    const isEdit = !!buildingForm.value.id
    const res = isEdit 
      ? await http.put('/building', buildingForm.value)
      : await http.post('/building', buildingForm.value)
    
    if (res.code === 200) {
      ElMessage.success(isEdit ? '修改成功' : '创建成功')
      buildingDialogVisible.value = false
      fetchBuildings()
    }
  } catch (e) {
    console.error(e)
  }
}

const handleBuildingDelete = (row) => {
  ElMessageBox.confirm(`确认删除宿舍楼 ${row.buildingName} 吗?`, '警告', {
    type: 'warning'
  }).then(async () => {
    try {
      const res = await http.delete(`/building/${row.id}`)
      if (res.code === 200) {
        ElMessage.success('删除成功')
        fetchBuildings()
      }
    } catch (e) {
      console.error(e)
    }
  })
}

// ========== 房间管理 ==========
const fetchRooms = async () => {
  roomLoading.value = true
  try {
    const res = await http.get('/room/all')
    if (res.code === 200) {
      roomList.value = res.data || []
    }
  } catch (e) {
    console.error(e)
  } finally {
    roomLoading.value = false
  }
}

const handleRoomAdd = () => {
  roomForm.value = { id: null, buildingId: null, roomNumber: '', capacity: 4, gender: 0 }
  roomDialogVisible.value = true
}

const handleRoomEdit = (row) => {
  roomForm.value = { ...row }
  roomDialogVisible.value = true
}

const saveRoom = async () => {
  try {
    const isEdit = !!roomForm.value.id
    const res = isEdit 
      ? await http.put('/room', roomForm.value)
      : await http.post('/room', roomForm.value)
    
    if (res.code === 200) {
      ElMessage.success(isEdit ? '修改成功' : '创建成功')
      roomDialogVisible.value = false
      fetchRooms()
    }
  } catch (e) {
    console.error(e)
  }
}

const handleRoomDelete = (row) => {
  ElMessageBox.confirm(`确认删除房间 ${row.roomNumber} 吗?`, '警告', {
    type: 'warning'
  }).then(async () => {
    try {
      const res = await http.delete(`/room/${row.id}`)
      if (res.code === 200) {
        ElMessage.success('删除成功')
        fetchRooms()
      }
    } catch (e) {
      console.error(e)
    }
  })
}

onMounted(() => {
  if (!isManager()) {
    ElMessage.error('无权限访问')
    return
  }
  fetchClasses()
  fetchBuildings()
  fetchRooms()
})
</script>

<template>
  <div class="page-container">
    <el-card class="glass-card">
      <template #header>
        <div class="header-title">
          <h3>基础数据管理</h3>
          <p>管理班级、宿舍楼、房间等基础信息</p>
        </div>
      </template>

      <el-tabs v-model="activeTab">
        <!-- 班级管理 -->
        <el-tab-pane label="班级管理" name="class">
          <div class="tab-header">
            <el-button type="primary" :icon="Plus" @click="handleClassAdd">新建班级</el-button>
          </div>
          <el-table :data="classList" v-loading="classLoading" class="custom-table">
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="className" label="班级名称" />
            <el-table-column prop="counselorName" label="辅导员" />
            <el-table-column prop="createdAt" label="创建时间" width="180" />
            <el-table-column label="操作" width="150" align="center">
              <template #default="scope">
                <el-button size="small" type="primary" :icon="Edit" circle @click="handleClassEdit(scope.row)" />
                <el-button size="small" type="danger" :icon="Delete" circle @click="handleClassDelete(scope.row)" />
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <!-- 宿舍楼管理 -->
        <el-tab-pane label="宿舍楼管理" name="building">
          <div class="tab-header">
            <el-button type="primary" :icon="Plus" @click="handleBuildingAdd">新建宿舍楼</el-button>
          </div>
          <el-table :data="buildingList" v-loading="buildingLoading" class="custom-table">
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="buildingName" label="楼栋名称" />
            <el-table-column prop="managerName" label="楼管" />
            <el-table-column prop="createdAt" label="创建时间" width="180" />
            <el-table-column label="操作" width="150" align="center">
              <template #default="scope">
                <el-button size="small" type="primary" :icon="Edit" circle @click="handleBuildingEdit(scope.row)" />
                <el-button size="small" type="danger" :icon="Delete" circle @click="handleBuildingDelete(scope.row)" />
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <!-- 房间管理 -->
        <el-tab-pane label="房间管理" name="room">
          <div class="tab-header">
            <el-button type="primary" :icon="Plus" @click="handleRoomAdd">新建房间</el-button>
          </div>
          <el-table :data="roomList" v-loading="roomLoading" class="custom-table">
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="buildingId" label="楼栋ID" width="100" />
            <el-table-column prop="roomNumber" label="房间号" />
            <el-table-column prop="capacity" label="容量" width="80" />
            <el-table-column prop="gender" label="性别" width="80">
              <template #default="scope">
                {{ scope.row.gender === 1 ? '男' : scope.row.gender === 2 ? '女' : '混合' }}
              </template>
            </el-table-column>
            <el-table-column prop="createdAt" label="创建时间" width="180" />
            <el-table-column label="操作" width="150" align="center">
              <template #default="scope">
                <el-button size="small" type="primary" :icon="Edit" circle @click="handleRoomEdit(scope.row)" />
                <el-button size="small" type="danger" :icon="Delete" circle @click="handleRoomDelete(scope.row)" />
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 班级对话框 -->
    <el-dialog v-model="classDialogVisible" title="班级信息" width="450px">
      <el-form :model="classForm" label-width="100px">
        <el-form-item label="班级名称">
          <el-input v-model="classForm.className" placeholder="例如: 软件232" />
        </el-form-item>
        <el-form-item label="辅导员">
          <el-input v-model="classForm.counselorName" placeholder="辅导员姓名" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="classDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveClass">保存</el-button>
      </template>
    </el-dialog>

    <!-- 宿舍楼对话框 -->
    <el-dialog v-model="buildingDialogVisible" title="宿舍楼信息" width="450px">
      <el-form :model="buildingForm" label-width="100px">
        <el-form-item label="楼栋名称">
          <el-input v-model="buildingForm.buildingName" placeholder="例如: 一号楼" />
        </el-form-item>
        <el-form-item label="楼管姓名">
          <el-input v-model="buildingForm.managerName" placeholder="楼管姓名" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="buildingDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveBuilding">保存</el-button>
      </template>
    </el-dialog>

    <!-- 房间对话框 -->
    <el-dialog v-model="roomDialogVisible" title="房间信息" width="450px">
      <el-form :model="roomForm" label-width="100px">
        <el-form-item label="所属楼栋">
          <el-select v-model="roomForm.buildingId" placeholder="选择楼栋" class="w-100">
            <el-option v-for="b in buildingList" :key="b.id" :label="b.buildingName" :value="b.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="房间号">
          <el-input v-model="roomForm.roomNumber" placeholder="例如: 101" />
        </el-form-item>
        <el-form-item label="容量">
          <el-input-number v-model="roomForm.capacity" :min="1" :max="8" />
        </el-form-item>
        <el-form-item label="性别">
          <el-radio-group v-model="roomForm.gender">
            <el-radio :label="1">男</el-radio>
            <el-radio :label="2">女</el-radio>
            <el-radio :label="0">混合</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="roomDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveRoom">保存</el-button>
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

.header-title h3 {
  margin: 0 0 4px;
  font-size: 18px;
}

.header-title p {
  margin: 0;
  font-size: 13px;
  color: #64748b;
}

.tab-header {
  margin-bottom: 16px;
  display: flex;
  justify-content: flex-end;
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
