<template>
  <div class="admin-dashboard">
    <h2>系统操作日志</h2>
    <div class="table-container">
      <table class="log-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>操作人</th>
            <th>角色</th>
            <th>动作</th>
            <th>IP地址</th>
            <th>时间</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="log in logs" :key="log.id">
            <td>{{ log.id }}</td>
            <td>{{ log.realName }} ({{ log.username }})</td>
            <td>{{ log.role }}</td>
            <td>{{ log.action }}</td>
            <td>{{ log.ipAddress }}</td>
            <td>{{ formatTime(log.createdAt) }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
import request from '@/utils/request'
import { getUser } from '@/utils/auth'

export default {
  name: 'AdminDashboard',
  data() {
    return {
      logs: []
    }
  },
  created() {
    const user = getUser()
    if (user && user.role === 'ADMIN') {
      this.fetchLogs()
    } else {
      this.$router.push('/login')
    }
  },
  methods: {
    async fetchLogs() {
      try {
        const res = await request.get('/api/admin/logs')
        this.logs = res || []
      } catch (error) {
        console.error(error)
      }
    },
    formatTime(time) {
      if (!time) return ''
      return new Date(time).toLocaleString()
    }
  }
}
</script>

<style scoped>
.admin-dashboard {
  padding: 20px;
}
.table-container {
  overflow-x: auto;
}
.log-table {
  width: 100%;
  border-collapse: collapse;
  background: white;
  box-shadow: 0 1px 3px rgba(0,0,0,0.1);
}
.log-table th, .log-table td {
  border: 1px solid #eee;
  padding: 12px;
  text-align: left;
}
.log-table th {
  background-color: #f8f9fa;
  font-weight: bold;
  color: #333;
}
.log-table tr:hover {
  background-color: #f5f7fa;
}
</style>
