<template>
  <div class="page-container">
    <el-card>
      <div slot="header" class="card-header">
        <span>客户详情</span>
        <div>
          <el-button type="primary" size="small" @click="goBack">返回列表</el-button>
        </div>
      </div>
      
      <el-tabs v-model="activeTab">
        <el-tab-pane label="基本信息" name="basic">
          <div class="detail-container">
            <div class="detail-row">
              <span class="detail-label">客户姓名：</span>
              <span class="detail-value">{{ customer.name }}</span>
            </div>
            <div class="detail-row">
              <span class="detail-label">手机号：</span>
              <span class="detail-value">{{ customer.phone }}</span>
            </div>
            <div class="detail-row">
              <span class="detail-label">身份证号：</span>
              <span class="detail-value">{{ customer.idCard }}</span>
            </div>
            <div class="detail-row">
              <span class="detail-label">性别：</span>
              <span class="detail-value">{{ customer.gender }}</span>
            </div>
            <div class="detail-row">
              <span class="detail-label">年龄：</span>
              <span class="detail-value">{{ customer.age }}</span>
            </div>
            <div class="detail-row">
              <span class="detail-label">地址：</span>
              <span class="detail-value">{{ customer.address }}</span>
            </div>
            <div class="detail-row">
              <span class="detail-label">家庭标签：</span>
              <span class="detail-value">
                <el-tag :type="getTagType(customer.familyTag)" size="small">
                  {{ customer.familyTag }}
                </el-tag>
              </span>
            </div>
            <div class="detail-row">
              <span class="detail-label">会员等级：</span>
              <span class="detail-value">
                <el-tag :type="getLevelType(customer.memberLevel)" size="small">
                  {{ customer.memberLevel }}
                </el-tag>
              </span>
            </div>
            <div class="detail-row">
              <span class="detail-label">累计消费：</span>
              <span class="detail-value" style="color: #f56c6c; font-weight: bold;">
                ¥{{ formatAmount(customer.totalConsumption) }}
              </span>
            </div>
            <div class="detail-row">
              <span class="detail-label">订单数量：</span>
              <span class="detail-value">{{ customer.orderCount }}</span>
            </div>
            <div class="detail-row">
              <span class="detail-label">状态：</span>
              <span class="detail-value">
                <el-tag :type="customer.status === 1 ? 'success' : 'danger'" size="small">
                  {{ customer.status === 1 ? '正常' : '禁用' }}
                </el-tag>
              </span>
            </div>
            <div class="detail-row">
              <span class="detail-label">备注：</span>
              <span class="detail-value">{{ customer.remark || '无' }}</span>
            </div>
          </div>
        </el-tab-pane>
        
        <el-tab-pane label="家庭档案" name="family">
          <div v-if="familyArchive" class="detail-container">
            <div class="detail-row">
              <span class="detail-label">家庭成员数量：</span>
              <span class="detail-value">{{ familyArchive.familyMemberCount }} 人</span>
            </div>
            <div class="detail-row">
              <span class="detail-label">是否有老人：</span>
              <span class="detail-value">{{ familyArchive.hasElderly }}</span>
            </div>
            <div class="detail-row">
              <span class="detail-label">是否有小孩：</span>
              <span class="detail-value">{{ familyArchive.hasChildren }}</span>
            </div>
            <div class="detail-row">
              <span class="detail-label">是否有宠物：</span>
              <span class="detail-value">{{ familyArchive.hasPets }}</span>
            </div>
            <div class="detail-row">
              <span class="detail-label">房屋类型：</span>
              <span class="detail-value">{{ familyArchive.housingType }}</span>
            </div>
            <div class="detail-row">
              <span class="detail-label">房屋面积：</span>
              <span class="detail-value">{{ familyArchive.housingArea }} 平方米</span>
            </div>
            <div class="detail-row">
              <span class="detail-label">装修风格：</span>
              <span class="detail-value">{{ familyArchive.decorationStyle }}</span>
            </div>
            <div class="detail-row">
              <span class="detail-label">特殊需求：</span>
              <span class="detail-value">{{ familyArchive.specialNeeds || '无' }}</span>
            </div>
            <div class="detail-row">
              <span class="detail-label">过敏信息：</span>
              <span class="detail-value">{{ familyArchive.allergyInfo || '无' }}</span>
            </div>
            <div class="detail-row">
              <span class="detail-label">备注：</span>
              <span class="detail-value">{{ familyArchive.remark || '无' }}</span>
            </div>
          </div>
          <div v-else class="empty-tip">
            <el-empty description="暂无家庭档案信息">
              <el-button type="primary" @click="handleEditFamily">添加家庭档案</el-button>
            </el-empty>
          </div>
        </el-tab-pane>
        
        <el-tab-pane label="消费记录" name="consumption">
          <el-table :data="consumptionRecords" border stripe>
            <el-table-column prop="orderNo" label="订单号" width="180" />
            <el-table-column prop="serviceType" label="服务类型" width="120" />
            <el-table-column prop="amount" label="消费金额" width="100">
              <template slot-scope="scope">
                ¥{{ formatAmount(scope.row.amount) }}
              </template>
            </el-table-column>
            <el-table-column prop="discountAmount" label="优惠金额" width="100">
              <template slot-scope="scope">
                ¥{{ formatAmount(scope.row.discountAmount) }}
              </template>
            </el-table-column>
            <el-table-column prop="actualAmount" label="实付金额" width="100">
              <template slot-scope="scope">
                <span style="color: #f56c6c;">¥{{ formatAmount(scope.row.actualAmount) }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="paymentMethod" label="支付方式" width="100" />
            <el-table-column prop="status" label="状态" width="100">
              <template slot-scope="scope">
                <el-tag :type="getStatusType(scope.row.status)" size="small">
                  {{ scope.row.status }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="consumptionTime" label="消费时间" width="180">
              <template slot-scope="scope">
                {{ formatTime(scope.row.consumptionTime) }}
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script>
import {
  getCustomerById,
  getFamilyArchiveByCustomerId,
  getConsumptionRecordsByCustomerId
} from '@/api/customer'
import dayjs from 'dayjs'

export default {
  name: 'CustomerDetail',
  data() {
    return {
      activeTab: 'basic',
      customerId: null,
      customer: {},
      familyArchive: null,
      consumptionRecords: []
    }
  },
  created() {
    this.customerId = this.$route.params.id
    this.loadData()
  },
  methods: {
    formatAmount(amount) {
      if (!amount) return '0.00'
      return (amount / 100).toFixed(2)
    },
    formatTime(time) {
      if (!time) return ''
      return dayjs(time).format('YYYY-MM-DD HH:mm:ss')
    },
    getTagType(tag) {
      const types = {
        'VIP家庭': 'danger',
        '特殊需求家庭': 'warning',
        '环保家庭': 'success',
        '母婴家庭': 'primary'
      }
      return types[tag] || 'info'
    },
    getLevelType(level) {
      const types = {
        '钻石会员': 'danger',
        '金卡会员': 'warning',
        '银卡会员': 'primary'
      }
      return types[level] || 'info'
    },
    getStatusType(status) {
      const types = {
        '已支付': 'success',
        '待支付': 'warning',
        '已取消': 'danger',
        '已完成': 'primary'
      }
      return types[status] || 'info'
    },
    async loadData() {
      try {
        const [customerRes, familyRes, consumptionRes] = await Promise.all([
          getCustomerById(this.customerId),
          getFamilyArchiveByCustomerId(this.customerId).catch(() => ({ data: null })),
          getConsumptionRecordsByCustomerId(this.customerId).catch(() => ({ data: [] }))
        ])
        this.customer = customerRes.data
        this.familyArchive = familyRes.data
        this.consumptionRecords = consumptionRes.data || []
      } catch (e) {
        this.$message.error('加载数据失败')
      }
    },
    goBack() {
      this.$router.go(-1)
    },
    handleEditFamily() {
      this.$message.info('家庭档案编辑功能')
    }
  }
}
</script>

<style scoped lang="less">
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 16px;
  font-weight: 600;
}

.empty-tip {
  padding: 40px;
}
</style>
