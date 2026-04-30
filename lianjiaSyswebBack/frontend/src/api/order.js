import request from '@/utils/request'

export function getServiceOrderList(params) {
  return request({
    url: '/service-orders',
    method: 'get',
    params
  })
}

export function getServiceOrderById(id) {
  return request({
    url: '/service-orders/' + id,
    method: 'get'
  })
}

export function createServiceOrder(data) {
  return request({
    url: '/service-orders',
    method: 'post',
    data
  })
}

export function updateServiceOrder(id, data) {
  return request({
    url: '/service-orders/' + id,
    method: 'put',
    data
  })
}

export function deleteServiceOrder(id) {
  return request({
    url: '/service-orders/' + id,
    method: 'delete'
  })
}

export function assignStaff(orderId, staffId) {
  return request({
    url: `/service-orders/${orderId}/assign`,
    method: 'post',
    params: { staffId }
  })
}

export function startService(orderId) {
  return request({
    url: `/service-orders/${orderId}/start`,
    method: 'post'
  })
}

export function completeService(orderId, data) {
  return request({
    url: `/service-orders/${orderId}/complete`,
    method: 'post',
    params: data
  })
}

export function cancelOrder(orderId, remark) {
  return request({
    url: `/service-orders/${orderId}/cancel`,
    method: 'post',
    params: { remark }
  })
}
