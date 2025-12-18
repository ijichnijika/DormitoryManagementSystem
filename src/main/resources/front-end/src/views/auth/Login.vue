<script setup>
import { ref, reactive, onMounted } from 'vue'
import { User, Lock } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()

const loginForm = reactive({
  username: '',
  password: ''
})
const loading = ref(false)
const rememberMe = ref(false)

onMounted(() => {
  const savedUsername = localStorage.getItem('rememberedUsername')
  const savedPassword = localStorage.getItem('rememberedPassword')
  if (savedUsername) {
    loginForm.username = savedUsername
    if (savedPassword) {
      loginForm.password = atob(savedPassword)
    }
    rememberMe.value = true
  }
})

const handleLogin = async () => {
  if (!loginForm.username || !loginForm.password) {
    ElMessage.warning('请输入用户名和密码')
    return
  }

  loading.value = true
  const success = await userStore.login(loginForm.username, loginForm.password)
  loading.value = false

  if (success) {
    if (rememberMe.value) {
      localStorage.setItem('rememberedUsername', loginForm.username)
      localStorage.setItem('rememberedPassword', btoa(loginForm.password))
    } else {
      localStorage.removeItem('rememberedUsername')
      localStorage.removeItem('rememberedPassword')
    }
    router.push('/dashboard')
  }
}
</script>

<template>
  <div class="auth-container">
    <div class="auth-card">
      <div class="header">
        <h2>欢迎回来</h2>
        <p>登录学生宿舍卫生管理系统</p>
      </div>

      <el-form label-position="top" size="large">
        <el-form-item label="账号">
          <el-input 
            v-model="loginForm.username" 
            :prefix-icon="User" 
            placeholder="学号/用户名" 
          />
        </el-form-item>
        <el-form-item label="密码">
          <el-input 
            v-model="loginForm.password" 
            type="password" 
            :prefix-icon="Lock" 
            show-password 
            placeholder="密码"
            @keyup.enter="handleLogin" 
          />
        </el-form-item>
        
        <el-form-item>
          <el-checkbox v-model="rememberMe">记住我</el-checkbox>
        </el-form-item>
        
        <el-button type="primary" class="w-100 action-btn" :loading="loading" @click="handleLogin">
          立即登录
        </el-button>
        
        <div class="footer">
            <span class="text-gray">还没有账号? </span>
            <el-link type="primary" @click="$router.push('/register')">立即注册</el-link>
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
  background-color: #f1f5f9;
}

.auth-card {
  width: 400px;
  padding: 40px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 10px 15px -3px rgb(0 0 0 / 0.1);
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
