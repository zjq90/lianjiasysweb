<template>
  <div class="page-container">
    <el-card>
      <div slot="header" class="card-header">
        <span>数据导出</span>
      </div>
      
      <el-form :model="form" label-width="120px" class="search-form">
        <el-form-item label="客户姓名">
          <el-input v-model="form.name" placeholder="请输入客户姓名" clearable style="width: 300px;" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="form.phone" placeholder="请输入手机号" clearable style="width: 300px;" />
        </el-form-item>
        <el-form-item label="家庭标签">
          <el-select v-model="form.familyTag" placeholder="请选择家庭标签" clearable style="width: 300px;">
            <el-option
              v-for="tag in familyTags"
              :key="tag"
              :label="tag"
              :value="tag"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="会员等级">
          <el-select v-model="form.memberLevel" placeholder="请选择会员等级" clearable style="width: 300px;">
            <el-option
              v-for="level in memberLevels"
              :key="level"
              :label="level"
              :value="level"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status" placeholder="请选择状态" clearable style="width: 300px;">
            <el-option label="正常" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handlePreview">预览数据</el-button>
          <el-button type="success" icon="el-icon-download" @click="handleExport" :loading="exportLoading">
            导出Excel
          </el-button>
          <el-button @click="handleReset">重置条件</el-button>
        </el-form-item>
      </el-form>
      
      <el-divider>预览数据</el-divider>
      
      <el-table :data="previewData" border stripe v-loading="loading">
        <el-table-column prop="id" label="客户ID" width="80" />
        <el-table-column prop="name" label="客户姓名" width="100" />
        <el-table-column prop="phone" label="手机号" width="130" />
        <el-table-column prop="gender" label="性别" width="60" />
        <el-table-column prop="age" label="年龄" width="60" />
        <el-table-column prop="address" label="地址" min-width="200" show-overflow-tooltip />
        <el-table-column prop="familyTag" label="家庭标签" width="120">
          <template slot-scope="scope">
            <el-tag :type="getTagType(scope.row.familyTag)" size="small">
              {{ scope.row.familyTag }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="memberLevel" label="会员等级" width="100">
          <template slot-scope="scope">
            <el-tag :type="getLevelType(scope.row.memberLevel)" size="small">
              {{ scope.row.memberLevel }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="totalConsumption" label="累计消费(元)" width="120">
          <template slot-scope="scope">
            ¥{{ formatAmount(scope.row.totalConsumption) }}
          </template>
        </el-table-column>
        <el-table-column prop="orderCount" label="订单数" width="70" />
        <el-table-column prop="status" label="状态" width="80">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'" size="small">
              {{ scope.row.status === 1 ? '正常' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
      
      <div class="pagination-container">
        <el-pagination
          background
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pagination.page + 1"
          :page-sizes="[10, 20, 50]"
          :page-size="pagination.size"
          layout="total, sizes, prev, pager, next"
          :total="pagination.total"
        />
        <div style="margin-left: 20px; line-height: 32px; color: #909399;">
          共 <span style="color: #409EFF; font-weight: bold;">{{ pagination.total }}</span> 条数据
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import { getCustomerList, getFamilyTags, getMemberLevels } from '@/api/customer'
import { exportCustomers } from '@/api/export'

export default {
  name: 'DataExport',
  data() {
    return {
      loading: false,
      exportLoading: false,
      form: {
        name: '',
        phone: '',
        familyTag: '',
        memberLevel: '',
        status: null
      },
      familyTags: [],
      memberLevels: [],
      previewData: [],
      pagination: {
        page: 0,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.loadDictData()
    this.loadData()
  },
  methods: {
    formatAmount(amount) {
      if (!amount) return '0.00'
      return (amount / 100).toFixed(2)
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
    async loadDictData() {
      try {
        const [tagsRes, levelsRes] = await Promise.all([
          getFamilyTags(),
          getMemberLevels()
        ])
        this.familyTags = tagsRes.data || ['普通家庭', 'VIP家庭', '特殊需求家庭', '环保家庭', '母婴家庭']
        this.memberLevels = levelsRes.data || ['普通会员', '银卡会员', '金卡会员', '钻石会员']
      } catch (e) {
        this.familyTags = ['普通家庭', 'VIP家庭', '特殊需求家庭', '环保家庭', '母婴家庭']
        this.memberLevels = ['普通会员', '银卡会员', '金卡会员', '钻石会员']
      }
    },
    async loadData() {
      this.loading = true
      try {
        const res = await getCustomerList({
          page: this.pagination.page,
          size: this.pagination.size,
          ...this.form
        })
        this.previewData = res.data.content || []
        this.pagination.total = res.data.totalElements || 0
      } catch (e) {
        this.previewData = []
      } finally {
        this.loading = false
      }
    },
    handlePreview() {
      this.pagination.page = 0
      this.loadData()
    },
    handleReset() {
      this.form = {
        name: '',
        phone: '',
        familyTag: '',
        memberLevel: '',
        status: null
      }
      this.handlePreview()
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.loadData()
    },
    handleCurrentChange(val) {
      this.pagination.page = val - 1
      this.loadData()
    },
    async handleExport() {
      try {
        this.exportLoading = true
        const exportParams = { ...this.form }
        if (exportParams.status === null) {
          delete exportParams.status
        }
        const res = await exportCustomers(exportParams)
        const blob = new Blob([res], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        const timestamp = new Date().toISOString().slice(0, 10)
        link.setAttribute('download', `客户数据导出_${timestamp}.xlsx`)
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        this.$message.success('导出成功')
      } catch (e) {
        this.$message.error('导出失败')
      } finally {
        this.exportLoading = false
      }
    }
  }
}
</script>

<style scoped lang="less">
.card-header {
  font-size: 16px;
  font-weight: 600;
}

.search-form {
  padding: 20px;
  background: #f5f7fa;
  border-radius: 4px;
}
</style>
