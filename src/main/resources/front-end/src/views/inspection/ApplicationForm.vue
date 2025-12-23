<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import http from '@/api/http'
import { useUserStore } from '@/stores/user'
import { isInspector } from '@/utils/auth'

const userStore = useUserStore()
const formRef = ref()
const hasPendingApplication = ref(false)
const form = reactive({
  applicationReason: ''
})

const rules = {
  applicationReason: [
    { required: true, message: '请输入申请理由', trigger: 'blur' },
    { min: 5, message: '理由不能少于5个字', trigger: 'blur' }
  ]
}

const alreadyInspector = computed(() => isInspector())

const checkPendingApplication = async () => {
  try {
    const userId = userStore.userInfo?.id
    if (!userId) return
    
    const res = await http.get(`/application/applicant/${userId}`)
    if (res.code === 200 && res.data) {
      hasPendingApplication.value = res.data.some(app => app.status === 0)
    }
  } catch (e) {
    console.error(e)
  }
}

const submitForm = (formEl) => {
  if (!formEl) return
  
  if (alreadyInspector.value) {
    ElMessage.warning('您已经是检查员,无需再次申请')
    return
  }
  
  if (hasPendingApplication.value) {
    ElMessage.warning('您已有待审核的申请,请耐心等待')
    return
  }
  
  formEl.validate(async (valid) => {
    if (valid) {
        try {
            const payload = {
                applicantId: userStore.userInfo?.id || 0,
                applicationReason: form.applicationReason,
                status: 0,
                applyTime: new Date().toISOString()
            }
            
            const res = await http.post('/application', payload)
            if (res.code === 200) {
                ElMessage.success('申请提交成功')
                formEl.resetFields()
                hasPendingApplication.value = true
            }
        } catch (e) {
        }
    }
  })
}

onMounted(() => {
  checkPendingApplication()
})
</script>

<template>
  <el-card class="app-card glass-card">
    <template #header>
      <h3>申请检查员权限</h3>
    </template>
    
    <el-alert 
      v-if="alreadyInspector"
      title="您已经是检查员"
      type="success"
      :closable="false"
      description="您已拥有检查员权限,可以进行卫生检查录入工作"
      show-icon
      class="alert-margin"
    />
    
    <el-alert 
      v-else-if="hasPendingApplication"
      title="申请审核中"
      type="warning"
      :closable="false"
      description="您的申请正在审核中,请耐心等待教师审批"
      show-icon
      class="alert-margin"
    />
    
    <template v-else>
      <div class="info-alert">
          <p>💡 说明: 审批通过后，您将获得宿舍卫生检查与录入的权限。</p>
      </div>

      <el-form :model="form" :rules="rules" ref="formRef" label-position="top">
        <el-form-item label="申请理由" prop="applicationReason">
          <el-input 
            v-model="form.applicationReason" 
            type="textarea" 
            :rows="6" 
            placeholder="请简述您申请成为检查员的理由..." 
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="large" class="w-100" @click="submitForm(formRef)">确认提交</el-button>
        </el-form-item>
      </el-form>
    </template>
  </el-card>
</template>

<style scoped>
.glass-card {
    background: white;
    border-radius: 20px;
    border: none;
    max-width: 600px;
    margin: 40px auto;
    padding: 20px;
    box-shadow: var(--shadow-md);
}

h3 {
    margin: 0;
    text-align: center;
    font-size: 22px;
}

.alert-margin {
    margin-bottom: 20px;
}

.info-alert {
    background: #eff6ff;
    color: #1d4ed8;
    padding: 12px;
    border-radius: 12px;
    margin-bottom: 24px;
    font-size: 14px;
}

.w-100 {
    width: 100%;
}
</style>
