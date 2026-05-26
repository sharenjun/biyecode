<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <div class="card-header">
          <h2>请假管理系统</h2>
        </div>
      </template>
      
      <el-form :model="form" @submit.prevent="handleLogin" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="form.username" placeholder="请输入学号/工号" prefix-icon="User" />
        </el-form-item>
        
        <el-form-item label="密码">
          <el-input 
            v-model="form.password" 
            type="password" 
            placeholder="请输入密码" 
            prefix-icon="Lock" 
            show-password 
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handleLogin" class="w-100">
            登录
          </el-button>
        </el-form-item>
        
        <div class="links">
          <router-link to="/register">
            <el-link type="primary">没有账号？去注册</el-link>
          </router-link>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { login } from '@/api/auth'
import { setUser, setToken } from '@/utils/auth'
import { User, Lock } from '@element-plus/icons-vue' // Icons need to be registered or imported
import { ElMessage } from 'element-plus'

export default {
  name: 'Login',
  components: {
    // Note: In real setup, icons usually global registered. 
    // Here simplified.
  },
  data() {
    return {
      form: {
        username: '',
        password: ''
      },
      loading: false
    }
  },
  methods: {
    async handleLogin() {
      if(!this.form.username || !this.form.password) {
        ElMessage.warning('请输入用户名和密码')
        return
      }

      this.loading = true
      try {
        const res = await login(this.form)
        setToken(res.token)
        setUser(res.user)
        
        const user = res.user
        ElMessage.success('登录成功')
        
        if (user.role === 'TEACHER') {
          this.$router.push('/teacher/tasks')
        } else if (user.role === 'ADMIN') {
          this.$router.push('/admin/dashboard')
        } else {
          this.$router.push('/student/apply')
        }
      } catch (err) {
        ElMessage.error(err.response?.data || '登录失败')
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f0f2f5;
  background-image: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
}
.login-card {
  width: 400px;
}
.card-header {
  text-align: center;
}
.card-header h2 {
  margin: 0;
  color: #303133;
}
.w-100 {
  width: 100%;
}
.links {
  text-align: right;
  margin-top: 10px;
}
</style>
