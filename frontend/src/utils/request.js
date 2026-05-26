import axios from 'axios'
import { getToken, removeUser } from '@/utils/auth'
import router from '@/router' // Need router to redirect

const service = axios.create({
  baseURL: 'http://localhost:8080', // Adjust if your backend runs on a different port
  timeout: 5000
})

// Request interceptor
service.interceptors.request.use(
  config => {
    // You can add token here if you have one
    const token = getToken()
    if (token) {
      config.headers['Authorization'] = 'Bearer ' + token
    }
    return config
  },
  error => {
    console.log(error)
    return Promise.reject(error)
  }
)

// Response interceptor
service.interceptors.response.use(
  response => {
    const res = response.data
    // You can add custom code checking here
    return res
  },
  error => {
    console.log('err' + error)
    if (error.response && error.response.status === 401) {
      removeUser()
      router.push('/login')
      alert('登录已过期，请重新登录')
    }
    return Promise.reject(error)
  }
)

export default service
