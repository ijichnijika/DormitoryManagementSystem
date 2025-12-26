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
  try {
    const success = await userStore.login(loginForm.username, loginForm.password)
    if (success) {
      if (rememberMe.value) {
        localStorage.setItem('rememberedUsername', loginForm.username)
        localStorage.setItem('rememberedPassword', btoa(loginForm.password))
      } else {
        localStorage.removeItem('rememberedUsername')
        localStorage.removeItem('rememberedPassword')
      }
      ElMessage.success('登录成功')
      router.push('/dashboard')
    }
  } catch (error) {
    console.error('Login error:', error)
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="login-wrapper">
    <div class="split-layout">
      <div class="visual-side">
        <div class="visual-content">
          <div class="logo-area">
            <h1>DMS</h1>
            <span>智慧宿管</span>
          </div>
          <div class="visual-text">
            <h2>高效 • 便捷 • 智能</h2>
            <p>打造新一代高校宿舍管理体验。实时监控卫生状况，智能数据分析，让校园生活更美好。</p>
          </div>
        </div>
        <div class="overlay"></div>
        <img src="https://picsum.photos/seed/dorm_login_v2/1200/1600" alt="Campus Life" class="bg-image" />
      </div>

      <div class="form-side">
        <div class="form-container">
          <div class="form-header">
            <h2>欢迎回来</h2>
            <p class="subtitle">请登录您的账户以继续</p>
          </div>

          <el-form 
            label-position="top" 
            size="large" 
            class="custom-form"
            hide-required-asterisk
          >
            <el-form-item label="账号">
              <el-input 
                v-model="loginForm.username" 
                :prefix-icon="User" 
                placeholder="请输入学号/工号" 
                class="custom-input"
              />
            </el-form-item>
            
            <el-form-item label="密码">
              <el-input 
                v-model="loginForm.password" 
                type="password" 
                :prefix-icon="Lock" 
                show-password 
                placeholder="请输入密码"
                @keyup.enter="handleLogin" 
                class="custom-input"
              />
            </el-form-item>
            
            <div class="form-actions">
              <el-checkbox v-model="rememberMe" label="记住我" size="default" />
              <el-link type="primary" :underline="false" class="forgot-pwd">忘记密码？</el-link>
            </div>
            
            <el-button 
              type="primary" 
              class="submit-btn" 
              :loading="loading" 
              @click="handleLogin"
              auto-insert-space
            >
              登录
            </el-button>
          </el-form>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.login-wrapper {
  height: 100vh;
  width: 100vw;
  overflow: hidden;
  background: #fff;
  font-family: 'Inter', -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
}

.split-layout {
  display: flex;
  height: 100%;
}

.visual-side {
  flex: 1;
  position: relative;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  padding: 60px;
  color: white;
  background-color: #0f172a;
  overflow: hidden;
}

.bg-image {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  z-index: 0;
  opacity: 0.6;
  filter: grayscale(20%) contrast(110%);
}

.overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, rgba(15, 23, 42, 0.9) 0%, rgba(15, 23, 42, 0.4) 100%);
  z-index: 1;
}

.visual-content {
  position: relative;
  z-index: 2;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.logo-area h1 {
  font-size: 32px;
  font-weight: 800;
  margin: 0;
  letter-spacing: -1px;
}

.logo-area span {
  font-size: 14px;
  opacity: 0.8;
  letter-spacing: 2px;
  text-transform: uppercase;
}

.visual-text h2 {
  font-size: 48px;
  font-weight: 700;
  margin-bottom: 24px;
  line-height: 1.1;
}

.visual-text p {
  font-size: 18px;
  line-height: 1.6;
  opacity: 0.9;
  max-width: 480px;
}

.form-side {
  width: 500px;
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  background: white;
  position: relative;
  z-index: 10;
}

.form-container {
  width: 100%;
  max-width: 380px;
  padding: 40px;
}

.form-header {
  margin-bottom: 40px;
}

.form-header h2 {
  font-size: 30px;
  font-weight: 700;
  color: #1e293b;
  margin: 0 0 10px;
}

.subtitle {
  color: #64748b;
  font-size: 15px;
  margin: 0;
}

:deep(.el-input__wrapper) {
  box-shadow: 0 0 0 1px #e2e8f0 inset;
  padding: 8px 15px;
  border-radius: 8px;
  transition: all 0.3s ease;
}

:deep(.el-input__wrapper:hover) {
  box-shadow: 0 0 0 1px #cbd5e1 inset;
}

:deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 2px #3b82f6 inset !important; 
}

:deep(.el-form-item__label) {
  font-weight: 500;
  color: #334155;
  margin-bottom: 8px;
}

.form-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.submit-btn {
  width: 100%;
  height: 48px;
  font-size: 16px;
  font-weight: 600;
  border-radius: 8px;
  background: #3b82f6;
  border-color: #3b82f6;
  transition: all 0.2s ease;
}

.submit-btn:hover {
  background: #2563eb;
  border-color: #2563eb;
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.25);
}

.submit-btn:active {
  transform: translateY(0);
}

.form-footer {
  margin-top: 24px;
  text-align: center;
  font-size: 14px;
  color: #64748b;
}

@media (max-width: 900px) {
  .visual-side {
    display: none;
  }
  
  .form-side {
    width: 100%;
  }
}
</style>
