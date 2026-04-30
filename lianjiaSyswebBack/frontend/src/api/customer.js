import request from '@/utils/request'

export function getCustomerList(params) {
  return request({
    url: '/customers',
    method: 'get',
    params
  })
}

export function getCustomerById(id) {
  return request({
    url: '/customers/' + id,
    method: 'get'
  })
}

export function createCustomer(data) {
  return request({
    url: '/customers',
    method: 'post',
    data
  })
}

export function updateCustomer(id, data) {
  return request({
    url: '/customers/' + id,
    method: 'put',
    data
  })
}

export function deleteCustomer(id) {
  return request({
    url: '/customers/' + id,
    method: 'delete'
  })
}

export function getFamilyTags() {
  return request({
    url: '/customers/family-tags',
    method: 'get'
  })
}

export function getMemberLevels() {
  return request({
    url: '/customers/member-levels',
    method: 'get'
  })
}

export function getFamilyArchiveByCustomerId(customerId) {
  return request({
    url: '/family-archives/customer/' + customerId,
    method: 'get'
  })
}

export function createFamilyArchive(data) {
  return request({
    url: '/family-archives',
    method: 'post',
    data
  })
}

export function updateFamilyArchive(id, data) {
  return request({
    url: '/family-archives/' + id,
    method: 'put',
    data
  })
}

export function getConsumptionRecordsByCustomerId(customerId) {
  return request({
    url: '/consumption-records/customer/' + customerId,
    method: 'get'
  })
}

export function getConsumptionRecordList(params) {
  return request({
    url: '/consumption-records',
    method: 'get',
    params
  })
}

export function createConsumptionRecord(data) {
  return request({
    url: '/consumption-records',
    method: 'post',
    data
  })
}

export function updateConsumptionRecord(id, data) {
  return request({
    url: '/consumption-records/' + id,
    method: 'put',
    data
  })
}

export function deleteConsumptionRecord(id) {
  return request({
    url: '/consumption-records/' + id,
    method: 'delete'
  })
}
