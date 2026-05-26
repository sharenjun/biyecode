<template>
  <div class="my-leaves-container">
    <h2>我的申请记录</h2>
    
    <el-card shadow="never">
      <el-table :data="list" stripe style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="申请ID" width="80" />
        <el-table-column label="类型" width="100">
          <template #default="scope">
            <el-tag>{{ getTypeName(scope.row.leaveTypeId) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="时间段" width="320">
          <template #default="scope">
            {{ formatTime(scope.row.startTime) }} 至 {{ formatTime(scope.row.endTime) }}
          </template>
        </el-table-column>
        <el-table-column label="状态" width="120">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="currentStep" label="当前步骤" width="100" />
        <el-table-column label="申请时间">
          <template #default="scope">
            {{ formatTime(scope.row.createdAt) }}
          </template>
        </el-table-column>
      </el-table>

      <!-- Pagination Controls -->
      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="pageNum"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script>
import { getMyHistory } from '@/api/leave'
import { getUser } from '@/utils/auth'

export default {
  name: 'MyLeaves',
  data() {
    return {
      list: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      loading: false,
      studentId: null
    }
  },
  created() {
    const user = getUser()
    if (user) {
      this.studentId = user.id
      this.fetchData()
    }
  },
  methods: {
    async fetchData() {
      if (!this.studentId) return
      this.loading = true
      try {
        const res = await getMyHistory(this.studentId, this.pageNum, this.pageSize)
        if (res) {
          this.list = res.list
          this.total = res.total
        }
      } catch (e) {
        console.error(e)
      } finally {
        this.loading = false
      }
    },
    handleSizeChange(val) {
      this.pageSize = val
      this.pageNum = 1 
      this.fetchData()
    },
    handleCurrentChange(val) {
      this.pageNum = val
      this.fetchData()
    },
    formatTime(time) {
      if (!time) return ''
      return new Date(time).toLocaleString()
    },
    getTypeName(id) {
        // Simple mapping, or fetch from backend
        const map = { 1: '事假', 2: '病假' }
        return map[id] || '未知'
    },
    getStatusType(status) {
      const map = {
        'APPROVED': 'success',
        'REJECTED': 'danger',
        'PENDING': 'warning',
        'DRAFT': 'info'
      }
      return map[status] || ''
    },
    getStatusText(status) {
      const map = {
        'APPROVED': '已通过',
        'REJECTED': '已驳回',
        'PENDING': '审批中',
        'DRAFT': '草稿'
      }
      return map[status] || status
    }
  }
}
</script>

<style scoped>
.my-leaves-container {
  padding: 20px 0;
}
.pagination-wrapper {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
