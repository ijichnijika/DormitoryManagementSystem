<script setup>
import { ref, reactive } from 'vue'
import { User, Lock, Iphone, Message } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import http from '@/api/http'

const router = useRouter()
const loading = ref(false)

const form = reactive({
  username: '',
  password: '',
  confirmPassword: '',
  realName: '',
  phone: '',
  email: ''
})

const handleRegister = async () => {
  if (!form.username || !form.password || !form.realName) {
      ElMessage.warning('请填写所有必填项')
      return
  }
  if (form.password !== form.confirmPassword) {
      ElMessage.warning('两次密码输入不一致')
      return
  }

  loading.value = true
  try {
      const payload = {
          username: form.username,
          password: form.password,
          realName: form.realName,
          phone: form.phone,
          email: form.email,
          role: 'STUDENT',
          status: 1
      }
      
      const res = await http.post('/user', payload)
      if (res.code === 200) {
          ElMessage.success('注册成功, 请登录')
          router.push('/login')
      } else {
          ElMessage.error(res.message || '注册失败')
      }
  } catch(e) { } 
  finally {
      loading.value = false
  }
}
</script>

<template>
  <div class="auth-container">
    <div class="auth-card">
      <div class="header">
        <h2>注册账号</h2>
        <p>加入学生宿舍卫生管理系统</p>
      </div>

      <el-form label-position="top" size="large">
        <el-form-item label="用户名 *">
          <el-input v-model="form.username" :prefix-icon="User" placeholder="学号或用户名" />
        </el-form-item>
        
        <el-form-item label="真实姓名 *">
          <el-input v-model="form.realName" :prefix-icon="User" placeholder="请输入真实姓名" />
        </el-form-item>
        
        <el-form-item label="密码 *">
          <el-input 
            v-model="form.password" 
            type="password" 
            :prefix-icon="Lock" 
            show-password 
            placeholder="设置密码（至少6位）" 
          />
        </el-form-item>
        
         <el-form-item label="确认密码 *">
          <el-input 
            v-model="form.confirmPassword" 
            type="password" 
            :prefix-icon="Lock" 
            placeholder="再次输入密码" 
          />
        </el-form-item>

        <el-form-item label="手机号">
          <el-input v-model="form.phone" :prefix-icon="Iphone" placeholder="选填" />
        </el-form-item>
        
        <el-form-item label="邮箱">
          <el-input v-model="form.email" :prefix-icon="Message" placeholder="选填" />
        </el-form-item>

        <el-button type="primary" class="w-100 action-btn" :loading="loading" @click="handleRegister">
          立即注册
        </el-button>
        
        <div class="footer">
            <span class="text-gray">已有账号? </span>
            <el-link type="primary" @click="$router.push('/login')">去登录</el-link>
        </div>
      </el-form>
    </div>
  </div>
</template>

<style scoped>
.auth-container {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f8fafc;
}

.auth-card {
  width: 400px;
  padding: 40px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 6px -1px rgb(0 0 0 / 0.1);
}

.header {
  text-align: center;
  margin-bottom: 30px;
}

.header h2 {
  margin: 0 0 8px;
  font-size: 24px;
  color: #1e293b;
}

.header p {
  margin: 0;
  color: #64748b;
  font-size: 14px;
}

.w-100 {
  width: 100%;
}

.action-btn {
  margin-top: 10px;
  height: 44px;
  font-size: 16px;
  border-radius: 8px;
}

.footer {
    margin-top: 20px;
    text-align: center;
    font-size: 14px;
}

.text-gray {
    color: #64748b;
}
</style>
