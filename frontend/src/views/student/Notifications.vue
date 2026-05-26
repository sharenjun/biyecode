<template>
  <div class="notification-container">
    <h2>消息通知</h2>
    <div v-if="list.length === 0" class="no-data">暂无消息</div>
    <ul class="notification-list">
      <li v-for="item in list" :key="item.id" :class="{ unread: item.isRead === 0 }">
        <div class="content">{{ item.content }}</div>
        <div class="time">{{ formatTime(item.createdAt) }}</div>
      </li>
    </ul>
  </div>
</template>

<script>
import { getNotifications } from '@/api/notification'
import { getUser } from '@/utils/auth'

export default {
  name: 'Notifications',
  data() {
    return {
      list: []
    }
  },
  created() {
    const user = getUser()
    if (user) {
      this.fetchData(user.id)
    }
  },
  methods: {
    async fetchData(id) {
      try {
        const res = await getNotifications(id)
        this.list = res || []
      } catch (e) {
        console.error(e)
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
.notification-container {
  padding: 20px;
  max-width: 600px;
  margin: 0 auto;
}
.notification-list {
  list-style: none;
  padding: 0;
}
.notification-list li {
  padding: 15px;
  border-bottom: 1px solid #eee;
  background: #fff;
}
.notification-list li.unread {
  background: #f0f9eb;
  border-left: 4px solid #67C23A;
}
.time {
  font-size: 0.85em;
  color: #999;
  margin-top: 5px;
}
</style>
