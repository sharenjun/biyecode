<template>
  <div class="dashboard-container">
    <h2>数据看板</h2>
    
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="mb-4">
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>今日全校请假数</span>
            </div>
          </template>
          <div class="stat-number">{{ stats.todayLeaveCount }}</div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>我今日处理单数</span>
            </div>
          </template>
          <div class="stat-number">{{ stats.myProcessedCount }}</div>
        </el-card>
      </el-col>
    </el-row>

    <h2>审批历史记录</h2>
    <el-card shadow="never">
      <el-table :data="history" stripe style="width: 100%">
        <el-table-column prop="recordId" label="ID" width="80" />
        <el-table-column prop="realName" label="申请人" width="120" />
        <el-table-column prop="className" label="班级" width="150" />
        <el-table-column prop="leaveTypeName" label="类型" width="100" />
        <el-table-column label="电话" width="150">
          <template #default="scope">
            {{ scope.row.contactPhone || '-' }}
          </template>
        </el-table-column>
        <el-table-column label="附件" width="80">
          <template #default="scope">
            <el-link 
              v-if="scope.row.attachmentUrl" 
              :href="backendUrl + scope.row.attachmentUrl" 
              target="_blank" 
              type="primary"
              :underline="false"
            >
              <el-icon><Document /></el-icon>
            </el-link>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column label="我的操作" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.action === 'APPROVE' ? 'success' : 'danger'">
              {{ scope.row.action === 'APPROVE' ? '通过' : '驳回' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="comment" label="我的意见" show-overflow-tooltip />
        <el-table-column label="操作时间" width="180">
          <template #default="scope">
            {{ formatTime(scope.row.actionTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100" fixed="right">
          <template #default="scope">
            <el-button size="small" @click="openDetail(scope.row)">查看</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 详情弹窗 -->
    <el-dialog v-model="showModal" title="申请详情回顾" width="500px">
      <div v-if="currentItem" class="detail-content">
        <div class="detail-row">
          <span class="label">申请人:</span> 
          <span class="value">{{ currentItem.realName }} (ID: {{ currentItem.applicantId }})</span>
        </div>
        <div class="detail-row">
          <span class="label">班级:</span> 
          <span class="value">{{ currentItem.className }}</span>
        </div>
        <div class="detail-row">
          <span class="label">请假类型:</span> 
          <span class="value">{{ currentItem.leaveTypeName }}</span>
        </div>
        <div class="detail-row">
          <span class="label">时间:</span> 
          <span class="value">
            {{ formatTime(currentItem.startTime) }} - {{ formatTime(currentItem.endTime) }}
            ({{ currentItem.durationHours }}小时)
          </span>
        </div>
        <div class="detail-row">
          <span class="label">事由:</span> 
          <div class="reason-box">{{ currentItem.reason }}</div>
        </div>
        <div class="detail-row" v-if="currentItem.attachmentUrl">
          <span class="label">附件:</span>
          <el-link :href="backendUrl + currentItem.attachmentUrl" target="_blank" type="primary">点击查看附件</el-link>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getDashboardStats, getApprovalHistory } from '@/api/approval'
import { getUser } from '@/utils/auth'
import { Document } from '@element-plus/icons-vue'

export default {
  name: 'TeacherDashboard',
  components: { Document },
  data() {
    return {
      stats: {
        todayLeaveCount: 0,
        myProcessedCount: 0
      },
      history: [],
      showModal: false,
      currentItem: null,
      backendUrl: 'http://localhost:8080'
    }
  },
  created() {
    const user = getUser()
    if (user) {
      this.loadData(user.id)
    }
  },
  methods: {
    async loadData(id) {
      try {
        const [statsRes, historyRes] = await Promise.all([
          getDashboardStats(id),
          getApprovalHistory(id)
        ])
        this.stats = statsRes || { todayLeaveCount: 0, myProcessedCount: 0 }
        this.history = historyRes || []
      } catch (e) {
        console.error(e)
      }
    },
    formatTime(time) {
      if (!time) return ''
      return new Date(time).toLocaleString()
    },
    openDetail(item) {
      this.currentItem = item
      this.showModal = true
    }
  }
}
</script>

<style scoped>
.dashboard-container {
  /* padding: 20px; handled by main-content */
}
.mb-4 {
  margin-bottom: 20px;
}
.stat-number {
  font-size: 32px;
  font-weight: bold;
  color: #409EFF;
  text-align: center;
  padding: 10px 0;
}
.detail-row {
  margin-bottom: 12px;
  display: flex;
}
.detail-row .label {
  font-weight: bold;
  color: #606266;
  width: 80px;
  flex-shrink: 0;
}
.detail-row .value {
  color: #303133;
}
.reason-box {
  background: #f5f7fa;
  padding: 10px;
  border-radius: 4px;
  color: #606266;
  flex-grow: 1;
}
</style>
