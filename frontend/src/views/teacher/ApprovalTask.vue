<template>
  <div class="approval-container">
    <h2>待审批任务</h2>
    
    <el-card shadow="never">
      <div v-if="tasks.length === 0" class="no-tasks">
        <el-empty description="暂无待审批任务" />
      </div>

      <el-table v-else :data="tasks" stripe style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="申请ID" width="80" />
        <el-table-column prop="applicantName" label="申请人" width="120" />
        <el-table-column prop="applicantClassName" label="班级" width="150" />
        <el-table-column label="类型" width="100">
          <template #default="scope">
            <el-tag>{{ getTypeName(scope.row.leaveTypeId) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="时长" width="100">
          <template #default="scope">
            {{ scope.row.durationHours }} 小时
          </template>
        </el-table-column>
        <el-table-column label="提交时间">
          <template #default="scope">
            {{ formatTime(scope.row.createdAt) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="scope">
            <el-button type="primary" size="small" @click="openModal(scope.row)">
              审批
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 审批详情弹窗 -->
    <el-dialog v-model="showModal" title="审批详情" width="500px">
      <div v-if="currentTask" class="detail-content">
        <div class="detail-row">
          <span class="label">申请人:</span> 
          <span class="value">{{ currentTask.applicantName }} (ID: {{ currentTask.applicantId }})</span>
        </div>
        <div class="detail-row">
          <span class="label">所在班级:</span> 
          <span class="value">{{ currentTask.applicantClassName }}</span>
        </div>
        <div class="detail-row">
          <span class="label">联系电话:</span> 
          <span class="value highlight">{{ currentTask.contactPhone || '未填写' }}</span>
        </div>
        <div class="detail-row">
          <span class="label">请假时间:</span> 
          <span class="value">
            {{ formatTime(currentTask.startTime) }} 至 {{ formatTime(currentTask.endTime) }}
          </span>
        </div>
        <div class="detail-row">
          <span class="label">时长:</span> 
          <span class="value">{{ currentTask.durationHours }} 小时</span>
        </div>
        <div class="detail-row">
          <span class="label">请假事由:</span>
          <div class="reason-box">{{ currentTask.reason }}</div>
        </div>
        <div class="detail-row" v-if="currentTask.attachmentUrl">
          <span class="label">附件:</span>
          <el-link :href="backendUrl + currentTask.attachmentUrl" target="_blank" type="primary">
            <el-icon><Document /></el-icon> 点击查看附件
          </el-link>
        </div>
        
        <el-divider />
        
        <div class="approval-section">
          <h4>审批意见</h4>
          <el-input 
            v-model="comment" 
            type="textarea" 
            :rows="3" 
            placeholder="请输入您的审批意见（可选）..." 
          />
          <div class="modal-actions">
            <el-button type="danger" @click="confirmAction('REJECT')">驳回</el-button>
            <el-button type="success" @click="confirmAction('APPROVE')">通过</el-button>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getPendingTasks, submitApprovalAction } from '@/api/approval'
import { getUser } from '@/utils/auth'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Document } from '@element-plus/icons-vue'

export default {
  name: 'ApprovalTask',
  components: { Document },
  data() {
    return {
      tasks: [],
      loading: false,
      teacherId: '', 
      showModal: false,
      currentTask: null,
      comment: '',
      backendUrl: 'http://localhost:8080'
    }
  },
  created() {
    const user = getUser()
    if (user) {
      this.teacherId = user.id
      this.fetchTasks()
    } else {
      this.$router.push('/login')
    }
  },
  methods: {
    async fetchTasks() {
      this.loading = true
      try {
        const res = await getPendingTasks(this.teacherId)
        this.tasks = res || []
      } catch (error) {
        console.error(error)
      } finally {
        this.loading = false
      }
    },
    getTypeName(id) {
        const map = { 1: '事假', 2: '病假' }
        return map[id] || '未知类型'
    },
    formatTime(time) {
        if (!time) return ''
        return new Date(time).toLocaleString()
    },
    openModal(task) {
      this.currentTask = task
      this.comment = ''
      this.showModal = true
    },
    async confirmAction(action) {
      if (!this.currentTask) return
      
      try {
        await ElMessageBox.confirm(
          `确定要${action === 'APPROVE' ? '通过' : '驳回'}该申请吗？`,
          '提示',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: action === 'APPROVE' ? 'success' : 'warning',
          }
        )
        
        await submitApprovalAction({
          applicationId: this.currentTask.id,
          approverId: this.teacherId,
          action: action,
          comment: this.comment
        })
        
        ElMessage.success('操作成功')
        this.showModal = false
        this.fetchTasks() // Refresh list
      } catch (error) {
        if (error !== 'cancel') {
           ElMessage.error('操作失败: ' + (error.response?.data || error.message))
        }
      }
    }
  }
}
</script>

<style scoped>
.approval-container {
  padding: 20px 0;
}
.detail-row {
  margin-bottom: 12px;
  display: flex;
  line-height: 1.5;
}
.detail-row .label {
  font-weight: bold;
  color: #606266;
  width: 80px;
  flex-shrink: 0;
}
.highlight { color: #E6A23C; font-weight: bold; }
.reason-box {
  background: #f5f7fa;
  padding: 10px;
  border-radius: 4px;
  color: #606266;
  flex-grow: 1;
}
.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 15px;
  margin-top: 20px;
}
</style>
