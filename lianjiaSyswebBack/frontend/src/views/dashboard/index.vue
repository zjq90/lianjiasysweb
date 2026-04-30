<template>
  <div class="dashboard-container">
    <el-row :gutter="20" style="margin-bottom: 20px;">
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon" style="background: #409EFF;">
            <i class="el-icon-user"></i>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.totalCustomers }}</div>
            <div class="stat-label">客户总数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon" style="background: #67C23A;">
            <i class="el-icon-s-order"></i>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.totalOrders }}</div>
            <div class="stat-label">服务订单</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon" style="background: #E6A23C;">
            <i class="el-icon-money"></i>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ formatAmount(stats.totalRevenue) }}</div>
            <div class="stat-label">总收入(元)</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon" style="background: #F56C6C;">
            <i class="el-icon-service"></i>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.pendingOrders }}</div>
            <div class="stat-label">待处理订单</div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <el-row :gutter="20">
      <el-col :span="16">
        <el-card class="box-card">
          <div slot="header" class="card-header">
            <span>快捷操作</span>
          </div>
          <div class="shortcut-actions">
            <el-button type="primary" icon="el-icon-plus" @click="goToAddCustomer">
              新增客户
            </el-button>
            <el-button type="success" icon="el-icon-s-order" @click="goToAddOrder">
              新增订单
            </el-button>
            <el-button type="warning" icon="el-icon-download" @click="goToExport">
              导出数据
            </el-button>
            <el-button type="info" icon="el-icon-sold-out" @click="goToPendingOrders">
              处理待派单
            </el-button>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="box-card">
          <div slot="header" class="card-header">
            <span>系统信息</span>
          </div>
          <div class="system-info">
            <p><span class="label">当前用户：</span>{{ userInfo.merchantName || '管理员' }}</p>
            <p><span class="label">登录账号：</span>{{ userInfo.username || 'admin' }}</p>
            <p><span class="label">当前时间：</span>{{ currentTime }}</p>
            <p><span class="label">系统版本：</span>1.0.0</p>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import dayjs from 'dayjs'

export default {
  name: 'Dashboard',
  data() {
    return {
      stats: {
        totalCustomers: 0,
        totalOrders: 0,
        totalRevenue: 0,
        pendingOrders: 0
      },
      currentTime: ''
    }
  },
  computed: {
    ...mapGetters(['userInfo'])
  },
  created() {
    this.loadStats()
    this.updateTime()
    this.timeInterval = setInterval(() => {
      this.updateTime()
    }, 1000)
  },
  beforeDestroy() {
    if (this.timeInterval) {
      clearInterval(this.timeInterval)
    }
  },
  methods: {
    formatAmount(amount) {
      if (!amount) return '0.00'
      return (amount / 100).toFixed(2)
    },
    updateTime() {
      this.currentTime = dayjs().format('YYYY-MM-DD HH:mm:ss')
    },
    loadStats() {
      this.stats = {
        totalCustomers: 15,
        totalOrders: 45,
        totalRevenue: 12568000,
        pendingOrders: 3
      }
    },
    goToAddCustomer() {
      this.$router.push('/customers')
    },
    goToAddOrder() {
      this.$router.push('/service/orders')
    },
    goToExport() {
      this.$router.push('/data/export')
    },
    goToPendingOrders() {
      this.$router.push('/service/orders')
    }
  }
}
</script>

<style scoped lang="less">
.stat-card {
  display: flex;
  align-items: center;
  
  .stat-icon {
    width: 60px;
    height: 60px;
    border-radius: 8px;
    display: flex;
    align-items: center;
    justify-content: center;
    
    i {
      font-size: 30px;
      color: #fff;
    }
  }
  
  .stat-info {
    margin-left: 20px;
    
    .stat-value {
      font-size: 24px;
      font-weight: bold;
      color: #303133;
    }
    
    .stat-label {
      font-size: 14px;
      color: #909399;
      margin-top: 5px;
    }
  }
}

.card-header {
  font-size: 16px;
  font-weight: 600;
}

.shortcut-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  
  .el-button {
    min-width: 120px;
  }
}

.system-info {
  p {
    margin-bottom: 12px;
    color: #606266;
    
    .label {
      color: #909399;
    }
    
    &:last-child {
      margin-bottom: 0;
    }
  }
}
</style>
