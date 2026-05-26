import request from '@/utils/request'

export function getNotifications(studentId) {
  return request({
    url: '/api/student/leave/notifications',
    method: 'get',
    params: { studentId }
  })
}
