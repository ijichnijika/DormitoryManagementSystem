<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { User, Lock, Phone, Message } from '@element-plus/icons-vue'
import http from '@/api/http'
import { useUserStore } from '@/stores/user'
import { getRoleDisplayName } from '@/utils/auth'

const userStore = useUserStore()
const loading = ref(false)
const passwordDialogVisible = ref(false)

// 用户信息
const userInfo = reactive({
  id: null,
  username: '',
  realName: '',
  phone: '',
  email: '',
  role: '',
  classId: null,
  roomId: null,
  className: '',
  roomNumber: ''
})

// 修改密码表单
const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

// 可编辑字段
const editableForm = reactive({
  phone: '',
  email: ''
})

// 角色显示名称
const roleDisplayName = computed(() => getRoleDisplayName(userInfo.role))

// 加载用户信息
const fetchUserInfo = async () => {
  loading.value = true
  try {
    const userId = userStore.userInfo?.id
    if (!userId) {
      ElMessage.error('用户信息丢失,请重新登录')
      return
    }
    
    const res = await http.get(`/user/${userId}`)
    if (res.code === 200 && res.data) {
      Object.assign(userInfo, res.data)
      editableForm.phone = res.data.phone || ''
      editableForm.email = res.data.email || ''
      
      // 加载班级信息
      if (res.data.classId) {
        fetchClassName(res.data.classId)
      }
      
      // 加载宿舍信息
      if (res.data.roomId) {
        fetchRoomNumber(res.data.roomId)
      }
    }
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

// 获取班级名称
const fetchClassName = async (classId) => {
  try {
    const res = await http.get(`/class/${classId}`)
    if (res.code === 200 && res.data) {
      userInfo.className = res.data.className
    }
  } catch (e) {
    console.error(e)
  }
}

// 获取宿舍房间号
const fetchRoomNumber = async (roomId) => {
  try {
    const res = await http.get(`/room/${roomId}`)
    if (res.code === 200 && res.data) {
      userInfo.roomNumber = res.data.roomNumber
    }
  } catch (e) {
    console.error(e)
  }
}

// 保存联系方式
const saveContact = async () => {
  try {
    const payload = {
      id: userInfo.id,
      phone: editableForm.phone,
      email: editableForm.email
    }
    
    const res = await http.put('/user', payload)
    if (res.code === 200) {
      ElMessage.success('保存成功')
      // 更新本地用户信息
      userInfo.phone = editableForm.phone
      userInfo.email = editableForm.email
      fetchUserInfo() // 重新加载
    }
  } catch (e) {
    console.error(e)
  }
}

// 修改密码
const changePassword = async () => {
  if (passwordForm.newPassword !== passwordForm.confirmPassword) {
    ElMessage.error('两次输入的密码不一致')
    return
  }
  
  if (passwordForm.newPassword.length < 6) {
    ElMessage.error('密码长度不能少于6位')
    return
  }
  
  try {
    const payload = {
      id: userInfo.id,
      password: passwordForm.newPassword
    }
    
    const res = await http.put('/user', payload)
    if (res.code === 200) {
      ElMessage.success('密码修改成功,请重新登录')
      passwordDialogVisible.value = false
      // 清空表单
      passwordForm.oldPassword = ''
      passwordForm.newPassword = ''
      passwordForm.confirmPassword = ''
    }
  } catch (e) {
    console.error(e)
  }
}

onMounted(() => {
  fetchUserInfo()
})
</script>

<template>
  <div class="page-container">
    <el-row :gutter="20">
      <!-- 左侧: 个人档案 -->
      <el-col :span="16">
        <el-card class="glass-card" v-loading="loading">
          <template #header>
            <div class="header-title">
              <h3>个人档案</h3>
              <p>查看您的基本信息</p>
            </div>
          </template>

          <div class="info-grid">
            <div class="info-item">
              <span class="label">用户名(学号/工号)</span>
              <span class="value">{{ userInfo.username }}</span>
              <el-tag size="small" type="info">只读</el-tag>
            </div>
            
            <div class="info-item">
              <span class="label">真实姓名</span>
              <span class="value">{{ userInfo.realName }}</span>
              <el-tag size="small" type="info">只读</el-tag>
            </div>
            
            <div class="info-item">
              <span class="label">角色</span>
              <span class="value">
                <el-tag :type="userInfo.role === 'ADMIN' ? 'danger' : userInfo.role === 'TEACHER' ? 'warning' : userInfo.role === 'INSPECTOR' ? 'success' : ''">
                  {{ roleDisplayName }}
                </el-tag>
              </span>
            </div>
            
            <div class="info-item">
              <span class="label">班级</span>
              <span class="value">{{ userInfo.className || '未分配' }}</span>
              <el-tag size="small" type="info">只读</el-tag>
            </div>
            
            <div class="info-item">
              <span class="label">宿舍房间</span>
              <span class="value">{{ userInfo.roomNumber || '未分配' }}</span>
              <el-tag size="small" type="info">只读</el-tag>
            </div>
          </div>
        </el-card>

        <!-- 可编辑信息 -->
        <el-card class="glass-card mt-20">
          <template #header>
            <div class="header-title">
              <h3>联系方式</h3>
              <p>可以修改您的联系方式</p>
            </div>
          </template>

          <el-form :model="editableForm" label-width="100px">
            <el-form-item label="手机号码">
              <el-input v-model="editableForm.phone" :prefix-icon="Phone" placeholder="请输入手机号码" />
            </el-form-item>
            
            <el-form-item label="电子邮箱">
              <el-input v-model="editableForm.email" :prefix-icon="Message" placeholder="请输入电子邮箱" />
            </el-form-item>
            
            <el-form-item>
              <el-button type="primary" @click="saveContact">保存修改</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>

      <!-- 右侧: 安全设置 -->
      <el-col :span="8">
        <el-card class="glass-card">
          <template #header>
            <div class="header-title">
              <h3>安全设置</h3>
            </div>
          </template>

          <div class="security-item">
            <el-icon :size="24" color="#3b82f6"><Lock /></el-icon>
            <div class="security-info">
              <h4>登录密码</h4>
              <p>定期修改密码可提高账号安全性</p>
            </div>
            <el-button type="primary" @click="passwordDialogVisible = true">修改</el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 修改密码对话框 -->
    <el-dialog v-model="passwordDialogVisible" title="修改密码" width="450px">
      <el-form :model="passwordForm" label-width="100px">
        <el-form-item label="旧密码">
          <el-input v-model="passwordForm.oldPassword" type="password" placeholder="请输入旧密码" />
        </el-form-item>
        <el-form-item label="新密码">
          <el-input v-model="passwordForm.newPassword" type="password" placeholder="至少6位" />
        </el-form-item>
        <el-form-item label="确认密码">
          <el-input v-model="passwordForm.confirmPassword" type="password" placeholder="再次输入新密码" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="passwordDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="changePassword">确认修改</el-button>
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

.mt-20 {
  margin-top: 20px;
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

.info-grid {
  display: grid;
  gap: 20px;
}

.info-item {
  display: flex;
  align-items: center;
  padding: 16px;
  background: #f8fafc;
  border-radius: 12px;
  gap: 12px;
}

.info-item .label {
  flex: 0 0 140px;
  font-size: 14px;
  color: #64748b;
  font-weight: 500;
}

.info-item .value {
  flex: 1;
  font-size: 15px;
  color: #1e293b;
  font-weight: 600;
}

.security-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 20px;
  background: #f8fafc;
  border-radius: 12px;
}

.security-info {
  flex: 1;
}

.security-info h4 {
  margin: 0 0 4px;
  font-size: 15px;
  color: #1e293b;
}

.security-info p {
  margin: 0;
  font-size: 13px;
  color: #64748b;
}
</style>
