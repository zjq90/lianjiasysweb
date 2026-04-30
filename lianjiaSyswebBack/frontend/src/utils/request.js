import axios from 'axios'
import { Message } from 'element-ui'
import router from '@/router'
import store from '@/store'

const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API || 'http://localhost:8080/api',
  timeout: 30000
})

service.interceptors.request.use(
  config => {
    if (store.getters.token) {
      config.headers['Authorization'] = 'Bearer ' + store.getters.token
    }
    return config
  },
  error => {
    console.error('Request error:', error)
    return Promise.reject(error)
  }
)

service.interceptors.response.use(
  response => {
    const res = response.data
    if (res.code !== 200) {
      Message({
        message: res.message || '请求失败',
        type: 'error',
        duration: 5 * 1000
      })
      
      if (res.code === 401) {
        store.dispatch('logout')
        router.push('/login')
      }
      return Promise.reject(new Error(res.message || '请求失败'))
    }
    return res
  },
  error => {
    console.error('Response error:', error)
    const message = error.response?.data?.message || error.message || '网络错误'
    Message({
      message: message,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)

export default service
