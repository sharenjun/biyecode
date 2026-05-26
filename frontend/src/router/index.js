import { createRouter, createWebHistory } from 'vue-router'
import ApplyLeave from '../views/student/ApplyLeave.vue'
import MyLeaves from '../views/student/MyLeaves.vue'
import Notifications from '../views/student/Notifications.vue'
import ApprovalTask from '../views/teacher/ApprovalTask.vue'
import TeacherDashboard from '../views/teacher/Dashboard.vue'
import AdminDashboard from '../views/admin/AdminDashboard.vue'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import Profile from '../views/Profile.vue'
import { getUser } from '@/utils/auth'

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/register',
    name: 'Register',
    component: Register
  },
  {
    path: '/profile',
    name: 'Profile',
    component: Profile,
    meta: { requiresAuth: true }
  },
  // Student Routes
  {
    path: '/student/apply',
    name: 'ApplyLeave',
    component: ApplyLeave,
    meta: { requiresAuth: true, role: 'STUDENT' }
  },
  {
    path: '/student/history',
    name: 'MyLeaves',
    component: MyLeaves,
    meta: { requiresAuth: true, role: 'STUDENT' }
  },
  {
    path: '/student/notifications',
    name: 'Notifications',
    component: Notifications,
    meta: { requiresAuth: true, role: 'STUDENT' }
  },
  // Teacher Routes
  {
    path: '/teacher/tasks',
    name: 'ApprovalTask',
    component: ApprovalTask,
    meta: { requiresAuth: true, role: 'TEACHER' }
  },
  {
    path: '/teacher/dashboard',
    name: 'TeacherDashboard',
    component: TeacherDashboard,
    meta: { requiresAuth: true, role: 'TEACHER' }
  },
  {
    path: '/admin/dashboard',
    name: 'AdminDashboard',
    component: AdminDashboard,
    meta: { requiresAuth: true, role: 'ADMIN' }
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

router.beforeEach((to, from, next) => {
  const user = getUser()
  if (to.meta.requiresAuth && !user) {
    next('/login')
  } else {
    next()
  }
})

export default router
