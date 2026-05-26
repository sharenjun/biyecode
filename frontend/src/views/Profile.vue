<template>
  <div class="profile-container">
    <h2>个人中心</h2>
    
    <div class="profile-section">
      <h3>基本信息</h3>
      <div class="info-item">
        <label>用户名 (学工号):</label> <span>{{ user.username }}</span>
      </div>
      <div class="info-item">
        <label>姓名:</label> <span>{{ user.realName }}</span>
      </div>
      <div class="info-item">
        <label>角色:</label> <span>{{ user.role }}</span>
      </div>
      <div class="info-item">
        <label>班级:</label> <span>{{ user.className }}</span>
      </div>
      
      <form @submit.prevent="handleUpdateProfile" class="edit-form">
        <div class="form-group">
          <label>邮箱:</label>
          <input type="email" v-model="form.email" />
        </div>
        <div class="form-group">
          <label>手机号:</label>
          <input type="text" v-model="form.phone" />
        </div>
        <button type="submit" class="btn-primary">保存修改</button>
      </form>
    </div>

    <div class="profile-section">
      <h3>修改密码</h3>
      <form @submit.prevent="handleUpdatePassword" class="edit-form">
        <div class="form-group">
          <label>旧密码:</label>
          <input type="password" v-model="pwdForm.oldPassword" required />
        </div>
        <div class="form-group">
          <label>新密码:</label>
          <input type="password" v-model="pwdForm.newPassword" required />
        </div>
        <button type="submit" class="btn-warning">修改密码</button>
      </form>
    </div>
  </div>
</template>

<script>
import { getUser, setUser } from '@/utils/auth'
import { updateProfile, updatePassword } from '@/api/user'

export default {
  name: 'Profile',
  data() {
    return {
      user: {},
      form: {
        userId: '',
        email: '',
        phone: ''
      },
      pwdForm: {
        userId: '',
        oldPassword: '',
        newPassword: ''
      }
    }
  },
  created() {
    const user = getUser()
    if (user) {
      this.user = user
      this.form.userId = user.id
      this.form.email = user.email || ''
      this.form.phone = user.phone || ''
      this.pwdForm.userId = user.id
    }
  },
  methods: {
    async handleUpdateProfile() {
      try {
        await updateProfile(this.form)
        alert('个人信息更新成功')
        // Update local storage
        const newUser = { ...this.user, email: this.form.email, phone: this.form.phone }
        setUser(newUser)
        this.user = newUser
      } catch (error) {
        alert('更新失败: ' + (error.response?.data || error.message))
      }
    },
    async handleUpdatePassword() {
      try {
        await updatePassword(this.pwdForm)
        alert('密码修改成功，请重新登录')
        this.$router.push('/login')
      } catch (error) {
        alert('修改失败: ' + (error.response?.data || error.message))
      }
    }
  }
}
</script>

<style scoped>
.profile-container {
  max-width: 600px;
  margin: 20px auto;
  padding: 20px;
}
.profile-section {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.1);
  margin-bottom: 20px;
}
.info-item {
  margin-bottom: 10px;
  color: #666;
}
.info-item label {
  font-weight: bold;
  margin-right: 10px;
  display: inline-block;
  width: 100px;
}
.edit-form {
  margin-top: 20px;
  border-top: 1px solid #eee;
  padding-top: 20px;
}
.form-group {
  margin-bottom: 15px;
}
.form-group label {
  display: block;
  margin-bottom: 5px;
}
.form-group input {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
  box-sizing: border-box;
}
.btn-primary {
  background-color: #409EFF;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
}
.btn-warning {
  background-color: #E6A23C;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
}
</style>
