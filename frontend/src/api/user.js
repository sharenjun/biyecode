import request from '@/utils/request'

export function updateProfile(data) {
  return request({
    url: '/api/user/profile',
    method: 'post',
    data
  })
}

export function updatePassword(data) {
  return request({
    url: '/api/user/password',
    method: 'post',
    data
  })
}
