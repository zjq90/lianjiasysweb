import request from '@/utils/request'

export function getServiceStaffList(params) {
  return request({
    url: '/service-staff',
    method: 'get',
    params
  })
}

export function getAvailableStaff() {
  return request({
    url: '/service-staff/available',
    method: 'get'
  })
}

export function getServiceStaffById(id) {
  return request({
    url: '/service-staff/' + id,
    method: 'get'
  })
}

export function createServiceStaff(data) {
  return request({
    url: '/service-staff',
    method: 'post',
    data
  })
}

export function updateServiceStaff(id, data) {
  return request({
    url: '/service-staff/' + id,
    method: 'put',
    data
  })
}

export function deleteServiceStaff(id) {
  return request({
    url: '/service-staff/' + id,
    method: 'delete'
  })
}

export function updateStaffStatus(id, status) {
  return request({
    url: `/service-staff/${id}/status`,
    method: 'put',
    params: { status }
  })
}
