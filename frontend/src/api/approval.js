import request from '@/utils/request'

export function getPendingTasks(teacherId) {
  return request({
    url: '/api/teacher/approval/tasks',
    method: 'get',
    params: { teacherId }
  })
}

export function submitApprovalAction(data) {
  return request({
    url: '/api/teacher/approval/action',
    method: 'post',
    data
  })
}

export function getApprovalHistory(teacherId) {
  return request({
    url: '/api/teacher/approval/history',
    method: 'get',
    params: { teacherId }
  })
}

export function getDashboardStats(teacherId) {
  return request({
    url: '/api/teacher/approval/dashboard',
    method: 'get',
    params: { teacherId }
  })
}
