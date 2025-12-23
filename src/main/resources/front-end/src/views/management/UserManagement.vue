<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Plus, Edit, Delete } from '@element-plus/icons-vue'
import http from '@/api/http'

import { isManager, isAdmin, ROLES, getRoleDisplayName } from '@/utils/auth'


const tableData = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('新建用户')
const classes = ref([])
const rooms = ref([])
const buildings = ref([])
const searchKeyword = ref('')

const pagination = ref({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const userForm = ref({
  id: null,
  username: '',
  password: '',
  realName: '',
  phone: '',
  email: '',
  role: ROLES.STUDENT,
  classId: null,
  roomId: null,
  status: 1
})

const selectedRoles = ref([])
const selectedBuildingId = ref(null)

const roleOptions = [
  { label: '学生', value: ROLES.STUDENT },
  { label: '检查员', value: ROLES.INSPECTOR },
  { label: '教师', value: ROLES.TEACHER },
  { label: '管理员', value: ROLES.ADMIN }
]

const filteredRooms = computed(() => {
  if (!selectedBuildingId.value) return []
  return rooms.value.filter(r => r.buildingId === selectedBuildingId.value)
})

const fetchData = async () => {
  loading.value = true
  try {
    const res = await http.get('/user/page', {
      params: {
        pageNum: pagination.value.pageNum,
        pageSize: pagination.value.pageSize,
        keyword: searchKeyword.value || undefined
      }
    })
    if (res.code === 200) {
      tableData.value = res.data?.records || []
      pagination.value.total = res.data?.total || 0
    }
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

const fetchClasses = async () => {
  try {
    const res = await http.get('/class/all')
    if (res.code === 200) {
      classes.value = res.data || []
    }
  } catch (e) {
    console.error(e)
  }
}

const fetchRooms = async () => {
  try {
    const res = await http.get('/room/all')
    if (res.code === 200) {
      rooms.value = res.data || []
    }
  } catch (e) {
    console.error(e)
  }
}

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

const handleSearch = () => {
  pagination.value.pageNum = 1
  fetchData()
}

const handleAdd = () => {
  dialogTitle.value = '新建用户'
  resetForm()
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑用户'
  userForm.value = { ...row }
  selectedRoles.value = row.role ? row.role.split(',') : []
  if (row.roomId) {
    const room = rooms.value.find(r => r.id === row.roomId)
    selectedBuildingId.value = room ? room.buildingId : null
  } else {
    selectedBuildingId.value = null
  }
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确认删除用户 ${row.realName}(${row.username}) 吗?`, '警告', {
    confirmButtonText: '确认',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const res = await http.delete(`/user/${row.id}`)
      if (res.code === 200) {
        ElMessage.success('删除成功')
        fetchData()
      }
    } catch (e) {
      console.error(e)
    }
  })
}

const updateRoleString = () => {
  userForm.value.role = selectedRoles.value.join(',')
}

const onBuildingChange = () => {
  userForm.value.roomId = null
}

const saveUser = async () => {
  try {
    const isEdit = !!userForm.value.id
    const res = isEdit 
      ? await http.put('/user', userForm.value)
      : await http.post('/user', userForm.value)
    
    if (res.code === 200) {
      ElMessage.success(isEdit ? '修改成功' : '创建成功')
      dialogVisible.value = false
      fetchData()
    }
  } catch (e) {
    console.error(e)
  }
}

const resetForm = () => {
  userForm.value = {
    id: null,
    username: '',
    password: '',
    realName: '',
    phone: '',
    email: '',
    role: ROLES.STUDENT,
    classId: null,
    roomId: null,
    status: 1
  }
  selectedRoles.value = [ROLES.STUDENT]
  selectedBuildingId.value = null
}

const handleSizeChange = (val) => {
  pagination.value.pageSize = val
  pagination.value.pageNum = 1
  fetchData()
}

const handleCurrentChange = (val) => {
  pagination.value.pageNum = val
  fetchData()
}

const getRoleTagType = (role) => {
  if (!role) return ''
  if (role.includes('ADMIN')) return 'danger'
  if (role.includes('TEACHER')) return 'warning'
  if (role.includes('INSPECTOR')) return 'success'
  return ''
}

const formatRoles = (role) => {
  if (!role) return '-'
  return role.split(',').map(r => getRoleDisplayName(r)).join('、')
}

onMounted(() => {
  if (!isManager()) {
    ElMessage.error('无权限访问')
    return
  }
  fetchData()
  fetchClasses()
  fetchRooms()
  fetchBuildings()
})

watch(selectedRoles, updateRoleString)
</script>

<template>
  <div class="page-container">
    <el-card class="glass-card">
      <template #header>
        <div class="card-header">
          <div class="header-title">
            <h3>用户管理</h3>
            <p>管理系统所有用户信息</p>
          </div>
          <div class="header-actions">
            <el-input
              v-model="searchKeyword"
              placeholder="搜索姓名或用户名"
              :prefix-icon="Search"
              clearable
              class="search-input"
              @keyup.enter="handleSearch"
              @clear="handleSearch"
            />
            <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
            <el-button type="primary" :icon="Plus" @click="handleAdd">新建用户</el-button>
          </div>
        </div>
      </template>

      <el-table :data="tableData" v-loading="loading" class="custom-table">
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="realName" label="姓名" width="100" />
        <el-table-column prop="role" label="角色" width="150">
          <template #default="scope">
            <el-tag :type="getRoleTagType(scope.row.role)">
              {{ formatRoles(scope.row.role) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="电话" width="130" />
        <el-table-column prop="email" label="邮箱" show-overflow-tooltip />
        <el-table-column prop="className" label="班级" width="120">
          <template #default="scope">
            {{ scope.row.className || '-' }}
          </template>
        </el-table-column>
        <el-table-column label="宿舍" width="160">
          <template #default="scope">
            <span v-if="scope.row.buildingName && scope.row.roomNumber">
              {{ scope.row.buildingName }} - {{ scope.row.roomNumber }}
            </span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" align="center" fixed="right">
          <template #default="scope">
            <el-button size="small" type="primary" :icon="Edit" circle @click="handleEdit(scope.row)" />
            <el-button size="small" type="danger" :icon="Delete" circle @click="handleDelete(scope.row)" />
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.pageNum"
          v-model:page-size="pagination.pageSize"
          :total="pagination.total"
          :page-sizes="[10, 20, 50]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form :model="userForm" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="用户名">
              <el-input v-model="userForm.username" placeholder="请输入用户名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="姓名">
              <el-input v-model="userForm.realName" placeholder="请输入姓名" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="密码">
              <el-input v-model="userForm.password" type="password" placeholder="留空则不修改" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="角色">
              <el-select 
                v-model="selectedRoles"
                multiple
                class="w-100"
                :disabled="!isAdmin()"
                placeholder="请选择角色"
              >
                <el-option v-for="opt in roleOptions" :key="opt.value" :label="opt.label" :value="opt.value" />
              </el-select>
              <div v-if="!isAdmin()" class="role-hint">仅管理员可修改角色</div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="电话">
              <el-input v-model="userForm.phone" placeholder="请输入电话" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱">
              <el-input v-model="userForm.email" placeholder="请输入邮箱" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="班级">
              <el-select v-model="userForm.classId" class="w-100" clearable placeholder="请选择班级">
                <el-option v-for="c in classes" :key="c.id" :label="c.className" :value="c.id" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="宿舍楼">
              <el-select v-model="selectedBuildingId" class="w-100" clearable placeholder="请选择楼栋" @change="onBuildingChange">
                <el-option v-for="b in buildings" :key="b.id" :label="b.buildingName" :value="b.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="房间号">
              <el-select v-model="userForm.roomId" class="w-100" clearable :disabled="!selectedBuildingId" placeholder="请先选择楼栋">
                <el-option v-for="r in filteredRooms" :key="r.id" :label="r.roomNumber" :value="r.id" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveUser">保存</el-button>
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
}

.header-title p {
  margin: 0;
  font-size: 13px;
  color: #64748b;
}

.header-actions {
  display: flex;
  gap: 12px;
  align-items: center;
}

.search-input {
  width: 200px;
}

.custom-table {
  border-radius: 12px;
}

:deep(.el-table th.el-table__cell) {
  background: #f8fafc;
  color: var(--color-text-secondary);
  font-weight: 600;
}

.pagination-container {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}

.role-hint {
  font-size: 12px;
  color: #f59e0b;
  margin-top: 4px;
}

.w-100 {
  width: 100%;
}
</style>
