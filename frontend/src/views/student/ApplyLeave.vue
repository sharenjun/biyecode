<template>
  <div class="apply-container">
    <el-card class="apply-card">
      <template #header>
        <div class="card-header">
          <h3><el-icon><EditPen /></el-icon> 申请请假</h3>
        </div>
      </template>

      <el-form :model="form" @submit.prevent="handleSubmit" label-width="100px" label-position="top">
        
        <!-- 个人信息 (只读) -->
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="学号">
              <el-input v-model="userInfo.username" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="姓名">
              <el-input v-model="userInfo.realName" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="班级">
              <el-input v-model="userInfo.className" disabled />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="联系电话" required>
          <el-input v-model="form.contactPhone" placeholder="辅导员能联系到你的电话" prefix-icon="Phone" />
        </el-form-item>

        <el-form-item label="请假类型" required>
          <el-select v-model="form.leaveTypeId" placeholder="请选择类型" class="w-100">
            <el-option 
              v-for="type in leaveTypes" 
              :key="type.id" 
              :label="type.name" 
              :value="type.id" 
            />
          </el-select>
        </el-form-item>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="开始时间" required>
              <el-date-picker
                v-model="form.startTime"
                type="datetime"
                placeholder="选择开始时间"
                class="w-100"
                value-format="YYYY-MM-DD HH:mm:ss"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束时间" required>
              <el-date-picker
                v-model="form.endTime"
                type="datetime"
                placeholder="选择结束时间"
                class="w-100"
                value-format="YYYY-MM-DD HH:mm:ss"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="请假事由" required>
          <el-input 
            v-model="form.reason" 
            type="textarea" 
            :rows="4" 
            placeholder="请详细说明请假原因..." 
          />
        </el-form-item>

        <el-form-item label="附件证明 (图片/PDF)">
          <el-upload
            class="upload-demo"
            action="http://localhost:8080/api/upload"
            :show-file-list="false"
            :on-success="handleUploadSuccess"
            :on-error="handleUploadError"
            :before-upload="beforeUpload"
          >
            <el-button v-if="!form.attachmentUrl" type="primary">点击上传</el-button>
            <div v-else class="uploaded-file">
              <el-icon color="#67C23A"><CircleCheck /></el-icon> 
              <span class="success-text">已上传</span>
              <el-link :href="backendUrl + form.attachmentUrl" target="_blank" type="primary" class="ml-2">查看附件</el-link>
              <el-button link type="danger" @click.stop="form.attachmentUrl = ''" class="ml-2">删除</el-button>
            </div>
            <template #tip>
              <div class="el-upload__tip">
                支持 jpg/png/pdf 文件，且不超过 5MB
              </div>
            </template>
          </el-upload>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" size="large" :loading="isSubmitting" @click="handleSubmit" class="submit-btn">
            提交申请
          </el-button>
        </el-form-item>

      </el-form>
    </el-card>
  </div>
</template>

<script>
import { getLeaveTypes, submitApplication } from '@/api/leave'
import { getUser } from '@/utils/auth'
import { ElMessage } from 'element-plus'
import { EditPen, Phone, CircleCheck } from '@element-plus/icons-vue'

export default {
  name: 'ApplyLeave',
  components: { EditPen, Phone, CircleCheck },
  data() {
    return {
      leaveTypes: [],
      userInfo: {
        username: '',
        realName: '',
        className: ''
      },
      form: {
        applicantId: '',
        leaveTypeId: '',
        startTime: '',
        endTime: '',
        reason: '',
        contactPhone: '',
        attachmentUrl: ''
      },
      isSubmitting: false,
      backendUrl: 'http://localhost:8080'
    }
  },
  created() {
    const user = getUser()
    if (user) {
      this.form.applicantId = user.id
      this.userInfo.username = user.username
      this.userInfo.realName = user.realName
      this.userInfo.className = user.className || '未知班级'
      if (user.phone) this.form.contactPhone = user.phone
    } else {
      this.$router.push('/login')
    }
    this.fetchLeaveTypes()
  },
  methods: {
    async fetchLeaveTypes() {
      try {
        const response = await getLeaveTypes()
        this.leaveTypes = response || []
      } catch (error) {
        console.error('Failed to load leave types', error)
      }
    },
    beforeUpload(file) {
      const isLt5M = file.size / 1024 / 1024 < 5
      if (!isLt5M) {
        ElMessage.error('上传文件大小不能超过 5MB!')
      }
      return isLt5M
    },
    handleUploadSuccess(res) {
      // res is the file path returned by backend string
      this.form.attachmentUrl = res
      ElMessage.success('上传成功')
    },
    handleUploadError() {
      ElMessage.error('上传失败，请重试')
    },
    async handleSubmit() {
      if (!this.form.leaveTypeId || !this.form.startTime || !this.form.endTime || !this.form.reason || !this.form.contactPhone) {
        ElMessage.warning('请填写所有必填项')
        return
      }
      
      if (new Date(this.form.startTime) >= new Date(this.form.endTime)) {
        ElMessage.warning('结束时间必须晚于开始时间')
        return
      }

      this.isSubmitting = true
      try {
        await submitApplication(this.form)
        ElMessage.success('申请提交成功！')
        // Reset fields
        this.form.reason = ''
        this.form.startTime = ''
        this.form.endTime = ''
        this.form.attachmentUrl = ''
      } catch (error) {
        ElMessage.error('提交失败: ' + (error.response?.data || error.message))
      } finally {
        this.isSubmitting = false
      }
    }
  }
}
</script>

<style scoped>
.apply-container {
  max-width: 800px;
  margin: 20px auto;
}
.card-header {
  display: flex;
  align-items: center;
}
.card-header h3 {
  margin: 0;
  display: flex;
  align-items: center;
  gap: 10px;
}
.w-100 {
  width: 100%;
}
.uploaded-file {
  display: flex;
  align-items: center;
  background: #f0f9eb;
  padding: 5px 15px;
  border-radius: 4px;
}
.success-text {
  margin-left: 5px;
  color: #67C23A;
  font-size: 14px;
}
.ml-2 {
  margin-left: 10px;
}
.submit-btn {
  width: 200px;
  margin-top: 20px;
}
</style>
