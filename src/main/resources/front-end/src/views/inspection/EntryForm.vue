<script setup>
import { ref, reactive, onMounted, watch } from 'vue'
import { ElMessage } from 'element-plus'
import http from '@/api/http'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
const formRef = ref()
const buildings = ref([])
const rooms = ref([])
const showRemarksHint = ref(false)

const form = reactive({
  buildingId: '',
  roomId: '',
  totalScore: 100,
  remarks: '',
  checkDate: new Date().toISOString().split('T')[0]
})

const rules = {
  buildingId: [{ required: true, message: '请选择楼栋', trigger: 'change' }],
  roomId: [{ required: true, message: '请选择房间', trigger: 'change' }],
  totalScore: [
    { required: true, message: '请输入分数', trigger: 'blur' },
    { type: 'number', min: 0, max: 100, message: '分数范围0-100', trigger: 'blur' }
  ]
}

// 监听分数变化,低于60分时提示填写备注
watch(() => form.totalScore, (newScore) => {
  showRemarksHint.value = newScore < 60
})

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
  form.roomId = ''
  rooms.value = []
  
  if (!val) return
  
  try {
    const res = await http.get(`/room/building/${val}`)
    if (res.code === 200) {
      rooms.value = res.data || []
    }
  } catch (e) {
    console.error(e)
  }
}

const submitForm = (formEl) => {
  if (!formEl) return
  formEl.validate(async (valid) => {
    if (valid) {
      // 低分未填写备注提示
      if (form.totalScore < 60 && !form.remarks.trim()) {
        ElMessage.warning('分数低于60分,建议填写扣分原因')
        return
      }
      
      try {
        const payload = {
          roomId: form.roomId,
          inspectorId: userStore.userInfo?.id || 0,
          modifierId: userStore.userInfo?.id || 0,
          totalScore: form.totalScore,
          remarks: form.remarks,
          checkDate: form.checkDate
        }
        
        const res = await http.post('/inspection', payload)
        if (res.code === 200) {
          ElMessage.success('提交成功!')
          resetForm(formEl)
        }
      } catch (e) {
        // handled by interceptor
      }
    }
  })
}

const resetForm = (formEl) => {
  if (!formEl) return
  formEl.resetFields()
  form.totalScore = 100
  form.checkDate = new Date().toISOString().split('T')[0]
  rooms.value = []
  showRemarksHint.value = false
}

onMounted(() => {
  fetchBuildings()
})
</script>

<template>
  <div class="page-container">
    <el-card class="glass-card">
      <template #header>
        <div class="card-header">
           <div class="header-titile">
            <h3>录入检查记录</h3>
          </div>
        </div>
      </template>
      
      <el-form :model="form" :rules="rules" ref="formRef" label-position="top" status-icon>
        <el-row :gutter="24">
          <el-col :span="12">
            <el-form-item label="宿舍楼栋" prop="buildingId">
              <el-select v-model="form.buildingId" placeholder="选择楼栋" @change="handleBuildingChange" class="w-100">
                <el-option v-for="b in buildings" :key="b.id" :label="b.buildingName" :value="b.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
             <el-form-item label="房间号" prop="roomId">
              <el-select v-model="form.roomId" placeholder="选择房间" class="w-100">
                <el-option v-for="r in rooms" :key="r.id" :label="r.roomNumber" :value="r.id" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="卫生总分" prop="totalScore">
          <div class="slider-container">
            <span class="score-display">{{ form.totalScore }}</span>
            <el-slider v-model="form.totalScore" :max="100" />
          </div>
        </el-form-item>
        
        <!-- 低分提示 -->
        <el-alert
          v-if="showRemarksHint"
          title="分数低于60分"
          type="warning"
          :closable="false"
          description="建议填写扣分原因或整改意见"
          show-icon
          style="margin-bottom: 16px;"
        />

        <el-form-item label="检查备注" prop="remarks">
          <el-input 
            v-model="form.remarks" 
            type="textarea" 
            placeholder="填写扣分详情或评语..."
            :rows="4" 
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" size="large" @click="submitForm(formRef)">提交记录</el-button>
          <el-button size="large" @click="resetForm(formRef)">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<style scoped>
.page-container {
  max-width: 800px;
  margin: 0 auto;
}

.glass-card {
    background: white;
    border-radius: 24px;
    border: none;
    padding: 20px;
}

.header-titile h3 {
    margin: 0 0 6px;
    font-size: 20px;
    color: var(--color-text-primary);
}

.header-titile p {
    margin: 0;
    color: var(--color-text-secondary);
}

.slider-container {
    display: flex;
    align-items: center;
    gap: 20px;
    width: 100%;
}

.score-display {
    font-size: 24px;
    font-weight: 700;
    color: var(--color-primary);
    min-width: 50px;
}

.w-100 {
    width: 100%;
}
</style>
