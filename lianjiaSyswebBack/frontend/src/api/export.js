import request from '@/utils/request'

export function exportCustomers(params) {
  return request({
    url: '/export/customers',
    method: 'get',
    params,
    responseType: 'blob'
  })
}
