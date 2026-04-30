<template>
  <div class="page-container">
    <el-card class="search-form">
      <el-form :inline="true" :model="searchForm" label-width="80px">
        <el-form-item label="订单号">
          <el-input v-model="searchForm.orderNo" placeholder="请输入订单号" clearable />
        </el-form-item>
        <el-form-item label="客户姓名">
          <el-input v-model="searchForm.customerName" placeholder="请输入客户姓名" clearable />
        </el-form-item>
        <el-form-item label="服务类型">
          <el-select v-model="searchForm.serviceType" placeholder="请选择服务类型" clearable>
            <el-option
              v-for="type in serviceTypes"
              :key="type"
              :label="type"
              :value="type"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="订单状态">
          <el-select v-model="searchForm.status" placeholder="请选择订单状态" clearable>
            <el-option
              v-for="item in statusOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    
    <el-card class="table-container">
      <div class="table-toolbar">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增订单</el-button>
      </div>
      
      <el-table :data="tableData" border stripe v-loading="loading">
        <el-table-column prop="orderNo" label="订单号" width="160" />
        <el-table-column prop="customerName" label="客户姓名" width="100" />
        <el-table-column prop="customerPhone" label="客户电话" width="130" />
        <el-table-column prop="serviceType" label="服务类型" width="120" />
        <el-table-column prop="staffName" label="服务人员" width="100">
          <template slot-scope="scope">
            {{ scope.row.staffName || '未派单' }}
          </template>
        </el-table-column>
        <el-table-column prop="serviceFee" label="服务费用" width="100">
          <template slot-scope="scope">
            ¥{{ formatAmount(scope.row.serviceFee) }}
          </template>
        </el-table-column>
        <el-table-column prop="scheduledTime" label="预约时间" width="170">
          <template slot-scope="scope">
            {{ formatTime(scope.row.scheduledTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)" size="small">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="progress" label="服务进度" min-width="150" show-overflow-tooltip />
        <el-table-column label="操作" width="280" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleView(scope.row)">查看</el-button>
            <el-button
              v-if="scope.row.status === '待派单'"
              type="text"
              size="small"
              style="color: #409EFF;"
              @click="handleAssign(scope.row)"
            >
              派单
            </el-button>
            <el-button
              v-if="scope.row.status === '待服务'"
              type="text"
              size="small"
              style="color: #67c23a;"
              @click="handleStart(scope.row)"
            >
              开始服务
            </el-button>
            <el-button
              v-if="scope.row.status === '服务中'"
              type="text"
              size="small"
              style="color: #e6a23c;"
              @click="handleComplete(scope.row)"
            >
              完成服务
            </el-button>
            <el-button
              v-if="scope.row.status !== '已完成' && scope.row.status !== '已取消'"
              type="text"
              size="small"
              style="color: #f56c6c;"
              @click="handleCancel(scope.row)"
            >
              取消
            </el-button>
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
      </div>
    </el-card>
    
    <el-dialog title="新增订单" :visible.sync="addDialogVisible" width="600px">
      <el-form ref="addForm" :model="addForm" :rules="addRules" label-width="100px">
        <el-form-item label="客户姓名" prop="customerName">
          <el-autocomplete
            v-model="addForm.customerName"
            :fetch-suggestions="querySearchCustomer"
            placeholder="请输入客户姓名搜索"
            @select="handleSelectCustomer"
            clearable
          />
        </el-form-item>
        <el-form-item label="服务类型" prop="serviceType">
          <el-select v-model="addForm.serviceType" placeholder="请选择服务类型" style="width: 100%;">
            <el-option
              v-for="type in serviceTypes"
              :key="type"
              :label="type"
              :value="type"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="服务内容">
          <el-input v-model="addForm.serviceContent" type="textarea" :rows="3" placeholder="请输入服务内容" />
        </el-form-item>
        <el-form-item label="服务费用" prop="serviceFee">
          <el-input-number v-model="addForm.serviceFee" :min="0" :precision="2" :step="100" />
        </el-form-item>
        <el-form-item label="预约时间" prop="scheduledTime">
          <el-date-picker
            v-model="addForm.scheduledTime"
            type="datetime"
            placeholder="选择预约时间"
            style="width: 100%;"
          />
        </el-form-item>
        <el-form-item label="服务地址">
          <el-input v-model="addForm.serviceAddress" type="textarea" :rows="2" placeholder="请输入服务地址" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="addDialogVisible = false">取 消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleAddSubmit">确 定</el-button>
      </div>
    </el-dialog>
    
    <el-dialog title="派单" :visible.sync="assignDialogVisible" width="500px">
      <el-form :model="assignForm" label-width="100px">
        <el-form-item label="当前订单">
          <span>{{ currentOrder.orderNo }}</span>
        </el-form-item>
        <el-form-item label="客户">
          <span>{{ currentOrder.customerName }}</span>
        </el-form-item>
        <el-form-item label="服务类型">
          <span>{{ currentOrder.serviceType }}</span>
        </el-form-item>
        <el-form-item label="选择服务人员">
          <el-select v-model="assignForm.staffId" placeholder="请选择服务人员" style="width: 100%;">
            <el-option
              v-for="staff in availableStaff"
              :key="staff.id"
              :label="`${staff.name} - ${staff.phone} - ${getStatusText(staff.status)}`"
              :value="staff.id"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="assignDialogVisible = false">取 消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleAssignSubmit">确 定</el-button>
      </div>
    </el-dialog>
    
    <el-dialog title="完成服务" :visible.sync="completeDialogVisible" width="500px">
      <el-form :model="completeForm" label-width="100px">
        <el-form-item label="服务进度">
          <el-input v-model="completeForm.progress" type="textarea" :rows="3" placeholder="请输入服务完成情况" />
        </el-form-item>
        <el-form-item label="客户反馈">
          <el-input v-model="completeForm.feedback" type="textarea" :rows="2" placeholder="请输入客户反馈（可选）" />
        </el-form-item>
        <el-form-item label="客户评分">
          <el-rate v-model="completeForm.rating" :max="5" show-text />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="completeDialogVisible = false">取 消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleCompleteSubmit">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getServiceOrderList,
  createServiceOrder,
  assignStaff,
  startService,
  completeService,
  cancelOrder
} from '@/api/order'
import { getServiceStaffList, getAvailableStaff } from '@/api/service'
import { getCustomerList } from '@/api/customer'
import dayjs from 'dayjs'

export default {
  name: 'ServiceOrders',
  data() {
    return {
      loading: false,
      submitLoading: false,
      searchForm: {
        orderNo: '',
        customerName: '',
        serviceType: '',
        status: ''
      },
      serviceTypes: ['甲醛检测', '甲醛治理', '深度清洁', '空气净化', '消毒杀菌'],
      statusOptions: [
        { label: '待派单', value: '待派单' },
        { label: '待服务', value: '待服务' },
        { label: '服务中', value: '服务中' },
        { label: '已完成', value: '已完成' },
        { label: '已取消', value: '已取消' }
      ],
      tableData: [],
      pagination: {
        page: 0,
        size: 10,
        total: 0
      },
      addDialogVisible: false,
      addForm: {
        customerName: '',
        customerId: null,
        serviceType: '',
        serviceContent: '',
        serviceFee: 0,
        scheduledTime: '',
        serviceAddress: ''
      },
      addRules: {
        customerName: [{ required: true, message: '请选择客户', trigger: 'blur' }],
        serviceType: [{ required: true, message: '请选择服务类型', trigger: 'change' }],
        serviceFee: [{ required: true, message: '请输入服务费用', trigger: 'blur' }],
        scheduledTime: [{ required: true, message: '请选择预约时间', trigger: 'change' }]
      },
      assignDialogVisible: false,
      currentOrder: {},
      assignForm: {
        staffId: null
      },
      availableStaff: [],
      completeDialogVisible: false,
      completeForm: {
        progress: '',
        feedback: '',
        rating: 5
      },
      customers: []
    }
  },
  created() {
    this.loadData()
    this.loadCustomers()
  },
  methods: {
    formatAmount(amount) {
      if (!amount) return '0.00'
      return amount.toFixed(2)
    },
    formatTime(time) {
      if (!time) return ''
      return dayjs(time).format('YYYY-MM-DD HH:mm:ss')
    },
    getStatusType(status) {
      const types = {
        '待派单': 'warning',
        '待服务': 'primary',
        '服务中': 'danger',
        '已完成': 'success',
        '已取消': 'info'
      }
      return types[status] || 'info'
    },
    getStatusText(status) {
      if (status === '空闲') return '空闲'
      return status
    },
    async loadData() {
      this.loading = true
      try {
        const params = {
          page: this.pagination.page,
          size: this.pagination.size,
          ...this.searchForm
        }
        if (!params.status) delete params.status
        if (!params.serviceType) delete params.serviceType
        
        const res = await getServiceOrderList(params)
        this.tableData = res.data.content || []
        this.pagination.total = res.data.totalElements || 0
      } catch (e) {
        this.tableData = []
      } finally {
        this.loading = false
      }
    },
    async loadCustomers() {
      try {
        const res = await getCustomerList({ page: 0, size: 1000 })
        this.customers = res.data.content || []
      } catch (e) {
        this.customers = []
      }
    },
    querySearchCustomer(queryString, cb) {
      const customers = this.customers
      const results = queryString
        ? customers.filter(item => item.name.toLowerCase().includes(queryString.toLowerCase()))
        : customers
      cb(results)
    },
    handleSelectCustomer(item) {
      this.addForm.customerId = item.id
      this.addForm.serviceAddress = item.address
    },
    handleSearch() {
      this.pagination.page = 0
      this.loadData()
    },
    handleReset() {
      this.searchForm = {
        orderNo: '',
        customerName: '',
        serviceType: '',
        status: ''
      }
      this.handleSearch()
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.loadData()
    },
    handleCurrentChange(val) {
      this.pagination.page = val - 1
      this.loadData()
    },
    handleView(row) {
      this.$message.info('查看订单详情功能')
    },
    handleAdd() {
      this.addForm = {
        customerName: '',
        customerId: null,
        serviceType: '',
        serviceContent: '',
        serviceFee: 0,
        scheduledTime: '',
        serviceAddress: ''
      }
      this.addDialogVisible = true
    },
    async handleAddSubmit() {
      try {
        await this.$refs.addForm.validate()
        if (!this.addForm.customerId) {
          this.$message.warning('请从下拉列表中选择客户')
          return
        }
        this.submitLoading = true
        const data = {
          customerId: this.addForm.customerId,
          serviceType: this.addForm.serviceType,
          serviceContent: this.addForm.serviceContent,
          serviceFee: this.addForm.serviceFee,
          scheduledTime: this.addForm.scheduledTime ? dayjs(this.addForm.scheduledTime).format() : null,
          serviceAddress: this.addForm.serviceAddress,
          status: '待派单'
        }
        await createServiceOrder(data)
        this.$message.success('创建成功')
        this.addDialogVisible = false
        this.loadData()
      } catch (e) {
        this.$message.error(e.message || '创建失败')
      } finally {
        this.submitLoading = false
      }
    },
    async handleAssign(row) {
      this.currentOrder = row
      this.assignForm.staffId = null
      try {
        const res = await getAvailableStaff()
        this.availableStaff = res.data || []
        if (this.availableStaff.length === 0) {
          this.$message.warning('暂无空闲的服务人员')
          return
        }
        this.assignDialogVisible = true
      } catch (e) {
        this.$message.error('获取服务人员失败')
      }
    },
    async handleAssignSubmit() {
      if (!this.assignForm.staffId) {
        this.$message.warning('请选择服务人员')
        return
      }
      try {
        this.submitLoading = true
        await assignStaff(this.currentOrder.id, this.assignForm.staffId)
        this.$message.success('派单成功')
        this.assignDialogVisible = false
        this.loadData()
      } catch (e) {
        this.$message.error(e.message || '派单失败')
      } finally {
        this.submitLoading = false
      }
    },
    async handleStart(row) {
      try {
        await this.$confirm('确定要开始服务吗？', '提示', { type: 'warning' })
        await startService(row.id)
        this.$message.success('服务已开始')
        this.loadData()
      } catch (e) {
        if (e !== 'cancel') {
          this.$message.error(e.message || '操作失败')
        }
      }
    },
    handleComplete(row) {
      this.currentOrder = row
      this.completeForm = {
        progress: '',
        feedback: '',
        rating: 5
      }
      this.completeDialogVisible = true
    },
    async handleCompleteSubmit() {
      try {
        if (!this.completeForm.progress) {
          this.$message.warning('请输入服务完成情况')
          return
        }
        this.submitLoading = true
        await completeService(this.currentOrder.id, this.completeForm)
        this.$message.success('服务已完成')
        this.completeDialogVisible = false
        this.loadData()
      } catch (e) {
        this.$message.error(e.message || '操作失败')
      } finally {
        this.submitLoading = false
      }
    },
    async handleCancel(row) {
      try {
        const remark = await this.$prompt('请输入取消原因（可选）', '取消订单', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputPlaceholder: '请输入取消原因'
        })
        await cancelOrder(row.id, remark.value)
        this.$message.success('订单已取消')
        this.loadData()
      } catch (e) {
        if (e !== 'cancel') {
          this.$message.error(e.message || '操作失败')
        }
      }
    }
  }
}
</script>
