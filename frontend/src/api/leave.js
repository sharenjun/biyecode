import request from '@/utils/request'

export function getLeaveTypes() {
  return request({
    url: '/api/student/leave/types',
    method: 'get'
  })
}

export function submitApplication(data) {
  return request({
    url: '/api/student/leave/apply',
    method: 'post',
    data
  })
}

export function getMyHistory(studentId, pageNum = 1, pageSize = 10) {
  return request({
    url: '/api/student/leave/history',
    method: 'get',
    params: { studentId, pageNum, pageSize }
  })
}
