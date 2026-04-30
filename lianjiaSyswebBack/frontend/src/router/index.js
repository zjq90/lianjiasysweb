import Vue from 'vue'
import VueRouter from 'vue-router'
import store from '@/store'

Vue.use(VueRouter)

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: { title: '登录', requiresAuth: false }
  },
  {
    path: '/',
    component: () => import('@/views/layout/index.vue'),
    redirect: '/dashboard',
    meta: { requiresAuth: true },
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/index.vue'),
        meta: { title: '工作台', requiresAuth: true }
      },
      {
        path: 'customers',
        name: 'Customers',
        component: () => import('@/views/customers/index.vue'),
        meta: { title: '客户管理', requiresAuth: true }
      },
      {
        path: 'customers/detail/:id',
        name: 'CustomerDetail',
        component: () => import('@/views/customers/detail.vue'),
        meta: { title: '客户详情', requiresAuth: true, hidden: true }
      },
      {
        path: 'data/export',
        name: 'DataExport',
        component: () => import('@/views/data/export.vue'),
        meta: { title: '数据导出', requiresAuth: true }
      },
      {
        path: 'service/orders',
        name: 'ServiceOrders',
        component: () => import('@/views/service/orders.vue'),
        meta: { title: '服务订单', requiresAuth: true }
      },
      {
        path: 'service/staff',
        name: 'ServiceStaff',
        component: () => import('@/views/service/staff.vue'),
        meta: { title: '服务人员', requiresAuth: true }
      }
    ]
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

router.beforeEach((to, from, next) => {
  document.title = to.meta.title ? to.meta.title + ' - 恋家环保' : '恋家环保商户端'
  
  if (to.meta.requiresAuth !== false && !store.getters.isLoggedIn) {
    next({ path: '/login', query: { redirect: to.fullPath } })
  } else if (to.path === '/login' && store.getters.isLoggedIn) {
    next({ path: '/' })
  } else {
    next()
  }
})

export default router
