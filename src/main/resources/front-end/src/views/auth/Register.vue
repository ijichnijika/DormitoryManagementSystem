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
  } catch(e) { 
    console.error(e)
  } finally {
      loading.value = false
  }
}
</script>

<template>
  <div class="register-wrapper">
    <div class="split-layout">
      <!-- Left Side: Visual -->
      <div class="visual-side">
        <div class="visual-content">
          <div class="logo-area">
            <h1>DMS</h1>
            <span>智慧宿管</span>
          </div>
          <div class="visual-text">
            <h2>加入我们</h2>
            <p>创建一个账户，开始体验更智能的宿舍生活。便捷的报修、透明的卫生成绩，一切尽在掌握。</p>
          </div>
        </div>
        <div class="overlay"></div>
        <img src="https://picsum.photos/seed/dorm_register/1200/1600" alt="Dorm Life" class="bg-image" />
      </div>

      <!-- Right Side: Interaction -->
      <div class="form-side">
        <div class="form-scroll-container">
          <div class="form-container">
            <div class="form-header">
              <h2>创建新账号</h2>
              <p class="subtitle">请填写以下信息完成注册</p>
            </div>

            <el-form 
              label-position="top" 
              size="large" 
              class="custom-form"
              hide-required-asterisk
            >
              <div class="form-grid">
                <el-form-item label="用户名">
                  <el-input 
                    v-model="form.username" 
                    :prefix-icon="User" 
                    placeholder="请输入学号/工号" 
                    class="custom-input"
                  />
                </el-form-item>
                
                <el-form-item label="真实姓名">
                  <el-input 
                    v-model="form.realName" 
                    :prefix-icon="User" 
                    placeholder="请输入您的姓名" 
                    class="custom-input"
                  />
                </el-form-item>
              </div>

              <div class="form-grid">
                <el-form-item label="设置密码">
                  <el-input 
                    v-model="form.password" 
                    type="password" 
                    :prefix-icon="Lock" 
                    show-password 
                    placeholder="6位以上字符" 
                    class="custom-input"
                  />
                </el-form-item>
                
                <el-form-item label="确认密码">
                  <el-input 
                    v-model="form.confirmPassword" 
                    type="password" 
                    :prefix-icon="Lock" 
                    placeholder="再次输入密码" 
                    class="custom-input"
                  />
                </el-form-item>
              </div>

              <el-form-item label="手机号码 (选填)">
                <el-input 
                  v-model="form.phone" 
                  :prefix-icon="Iphone" 
                  placeholder="用于接收通知" 
                  class="custom-input"
                />
              </el-form-item>
              
              <el-form-item label="电子邮箱 (选填)">
                <el-input 
                  v-model="form.email" 
                  :prefix-icon="Message" 
                  placeholder="example@school.edu.cn" 
                  class="custom-input"
                />
              </el-form-item>

              <el-button 
                type="primary" 
                class="submit-btn" 
                :loading="loading" 
                @click="handleRegister"
                auto-insert-space
              >
                立即注册
              </el-button>
              
              <div class="form-footer">
                  <span class="text-gray">已有账号? </span>
                  <el-link type="primary" :underline="false" @click="$router.push('/login')">直接登录</el-link>
              </div>
            </el-form>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.register-wrapper {
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

/* Left Visual Side (Same as Login) */
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

/* Right Form Side */
.form-side {
  width: 600px; /* Slightly wider for register form */
  flex-shrink: 0;
  background: white;
  position: relative;
  z-index: 10;
  display: flex;
  flex-direction: column;
}

.form-scroll-container {
  flex: 1;
  overflow-y: auto; /* Allow scrolling if height is small */
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px 0;
}

.form-container {
  width: 100%;
  max-width: 460px;
  padding: 0 40px;
}

.form-header {
  margin-bottom: 30px;
  text-align: left;
}

.form-header h2 {
  font-size: 28px;
  font-weight: 700;
  color: #1e293b;
  margin: 0 0 8px;
}

.subtitle {
  color: #64748b;
  font-size: 15px;
  margin: 0;
}

.form-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
}

/* Custom Element Plus Overrides */
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
  margin-bottom: 6px;
  line-height: 1.5;
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
  margin-top: 10px;
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

/* Responsive */
@media (max-width: 900px) {
  .visual-side {
    display: none;
  }
  
  .form-side {
    width: 100%;
  }

  .form-container {
    max-width: 100%;
    padding: 0 20px;
  }
}

@media (max-width: 600px) {
  .form-grid {
    grid-template-columns: 1fr;
    gap: 0;
  }
}
</style>
