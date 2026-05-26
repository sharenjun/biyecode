<template>
  <div id="app">
    <el-menu
      v-if="user"
      :default-active="activeIndex"
      class="el-menu-demo"
      mode="horizontal"
      background-color="#545c64"
      text-color="#fff"
      active-text-color="#ffd04b"
      router
    >
      <div class="logo">Leave System</div>
      
      <!-- Student Links -->
      <template v-if="user.role === 'STUDENT'">
        <el-menu-item index="/student/apply">我要请假</el-menu-item>
        <el-menu-item index="/student/history">申请记录</el-menu-item>
        <el-menu-item index="/student/notifications">消息通知</el-menu-item>
      </template>

      <!-- Teacher Links -->
      <template v-if="user.role === 'TEACHER'">
        <el-menu-item index="/teacher/tasks">待办审批</el-menu-item>
        <el-menu-item index="/teacher/dashboard">数据看板</el-menu-item>
      </template>

      <!-- Admin Links -->
      <template v-if="user.role === 'ADMIN'">
        <el-menu-item index="/admin/dashboard">系统监控</el-menu-item>
      </template>

      <div class="flex-grow" />

      <el-sub-menu index="user">
        <template #title>欢迎, {{ user.realName }} ({{ getRoleName(user.role) }})</template>
        <el-menu-item index="/profile">个人中心</el-menu-item>
        <el-menu-item @click="logout">退出登录</el-menu-item>
      </el-sub-menu>
    </el-menu>

    <div class="main-content">
      <router-view/>
    </div>
  </div>
</template>

<script>
import { getUser, removeUser } from '@/utils/auth'

export default {
  data() {
    return {
      user: null,
      activeIndex: '/student/apply'
    }
  },
  created() {
    this.checkUser()
    this.activeIndex = this.$route.path
  },
  watch: {
    $route(to) {
      this.checkUser()
      this.activeIndex = to.path
    }
  },
  methods: {
    checkUser() {
      this.user = getUser()
    },
    logout() {
      removeUser()
      this.user = null
      this.$router.push('/login')
    },
    getRoleName(role) {
      const map = {
        'STUDENT': '学生',
        'TEACHER': '教师',
        'ADMIN': '管理员'
      }
      return map[role] || role
    }
  }
}
</script>

<style>
body {
  margin: 0;
  padding: 0;
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', '微软雅黑', Arial, sans-serif;
  background-color: #f5f7fa;
}
.logo {
  color: #fff;
  font-size: 20px;
  font-weight: bold;
  line-height: 60px;
  padding: 0 20px;
  cursor: pointer;
}
.flex-grow {
  flex-grow: 1;
}
.main-content {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}
</style>
