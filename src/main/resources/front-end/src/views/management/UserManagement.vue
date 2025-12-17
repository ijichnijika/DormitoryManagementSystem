<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Plus, Edit, Delete } from '@element-plus/icons-vue'
import http from '@/api/http'
import { useUserStore } from '@/stores/user'
import { isManager, isAdmin, ROLES, getRoleDisplayName } from '@/utils/auth'

const userStore = useUserStore()
const tableData = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('新建用户')
const classes = ref([])
const rooms = ref([])

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

const roleOptions = [
  { label: '学生', value: ROLES.STUDENT },
  { label: '检查员', value: ROLES.INSPECTOR },
  { label: '教师', value: ROLES.TEACHER },
  { label: '管理员', value: ROLES.ADMIN }
]

const fetchData = async () => {
  loading.value = true
  try {
    const res = await http.get('/user/all')
    if (res.code === 200) {
      tableData.value = res.data || []
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

const handleAdd = () => {
  dialogTitle.value = '新建用户'
  resetForm()
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑用户'
  userForm.value = { ...row }
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
}

onMounted(() => {
  if (!isManager()) {
    ElMessage.error('无权限访问')
    return
  }
  fetchData()
  fetchClasses()
  fetchRooms()
})
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
          <el-button type="primary" :icon="Plus" @click="handleAdd">新建用户</el-button>
        </div>
      </template>

      <el-table :data="tableData" v-loading="loading" class="custom-table">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="realName" label="姓名" width="120" />
        <el-table-column prop="role" label="角色" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.role === 'ADMIN' ? 'danger' : scope.row.role === 'TEACHER' ? 'warning' : ''">
              {{ getRoleDisplayName(scope.row.role) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="电话" width="130" />
        <el-table-column prop="email" label="邮箱" show-overflow-tooltip />
        <el-table-column prop="classId" label="班级ID" width="80" />
        <el-table-column prop="roomId" label="房间ID" width="80" />
        <el-table-column label="操作" width="150" align="center" fixed="right">
          <template #default="scope">
            <el-button size="small" type="primary" :icon="Edit" circle @click="handleEdit(scope.row)" />
            <el-button size="small" type="danger" :icon="Delete" circle @click="handleDelete(scope.row)" />
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 用户编辑对话框 -->
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
                v-model="userForm.role" 
                class="w-100"
                :disabled="!isAdmin()"
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
              <el-select v-model="userForm.classId" class="w-100" clearable>
                <el-option v-for="c in classes" :key="c.id" :label="c.className" :value="c.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="宿舍房间">
              <el-select v-model="userForm.roomId" class="w-100" clearable>
                <el-option v-for="r in rooms" :key="r.id" :label="r.roomNumber" :value="r.id" />
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

.custom-table {
  border-radius: 12px;
}

:deep(.el-table th.el-table__cell) {
  background: #f8fafc;
  color: var(--color-text-secondary);
  font-weight: 600;
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
