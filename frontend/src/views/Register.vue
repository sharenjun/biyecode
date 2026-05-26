<template>
  <div class="register-container">
    <el-card class="register-card">
      <template #header>
        <div class="card-header">
          <h2>用户注册</h2>
        </div>
      </template>

      <el-form :model="form" @submit.prevent="handleRegister" label-width="100px" status-icon>
        
        <el-form-item label="用户名" required>
          <el-input v-model="form.username" placeholder="请输入学号或工号" prefix-icon="User" />
        </el-form-item>

        <el-form-item label="真实姓名" required>
          <el-input v-model="form.realName" placeholder="请输入真实姓名" prefix-icon="Postcard" />
        </el-form-item>

        <el-form-item label="密码" required>
          <el-input 
            v-model="form.password" 
            type="password" 
            placeholder="请输入密码" 
            prefix-icon="Lock" 
            show-password 
          />
        </el-form-item>

        <el-form-item label="邮箱">
          <el-input v-model="form.email" placeholder="请输入邮箱 (选填)" prefix-icon="Message" />
        </el-form-item>

        <el-form-item label="手机号">
          <el-input v-model="form.phone" placeholder="请输入手机号 (选填)" prefix-icon="Iphone" />
        </el-form-item>

        <el-form-item label="角色" required>
          <el-radio-group v-model="form.role">
            <el-radio label="STUDENT">学生</el-radio>
            <el-radio label="TEACHER">教师</el-radio>
            <el-radio label="ADMIN">管理员</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item 
          :label="form.role === 'STUDENT' ? '所在班级' : '管理班级'" 
          required 
          v-if="form.role !== 'ADMIN'"
        >
          <el-input 
            v-model="form.className" 
            :placeholder="form.role === 'STUDENT' ? '例如: 2023级1班' : '例如: 2023级1班, 2班'" 
            prefix-icon="School"
          />
        </el-form-item>

        <el-form-item label="院系ID">
           <el-input-number v-model="form.departmentId" :min="1" />
           <span class="tip-text">(默认为1-计算机系)</span>
        </el-form-item>

        <el-form-item>
          <el-button type="success" :loading="loading" @click="handleRegister" class="w-100">
            注册
          </el-button>
        </el-form-item>

        <div class="links">
          <router-link to="/login">
            <el-link type="primary">已有账号？去登录</el-link>
          </router-link>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { register } from '@/api/auth'
import { ElMessage } from 'element-plus'
import { User, Lock, Postcard, Message, Iphone, School } from '@element-plus/icons-vue'

export default {
  name: 'Register',
  components: {
    // Icons
  },
  data() {
    return {
      form: {
        username: '',
        realName: '',
        password: '',
        role: 'STUDENT',
        departmentId: 1,
        className: '',
        email: '',
        phone: ''
      },
      loading: false
    }
  },
  methods: {
    async handleRegister() {
      if(!this.form.username || !this.form.password || !this.form.realName) {
         ElMessage.warning('请填写必填项')
         return
      }

      this.loading = true
      try {
        await register(this.form)
        ElMessage.success('注册成功，请登录')
        this.$router.push('/login')
      } catch (err) {
        ElMessage.error(err.response?.data || '注册失败')
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  padding: 20px 0;
  background-color: #f0f2f5;
  background-image: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
}
.register-card {
  width: 500px;
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
.tip-text {
  margin-left: 10px;
  font-size: 12px;
  color: #909399;
}
</style>
