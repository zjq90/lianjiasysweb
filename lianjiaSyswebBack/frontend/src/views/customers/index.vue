<template>
  <div class="page-container">
    <el-card class="search-form">
      <el-form :inline="true" :model="searchForm" label-width="80px">
        <el-form-item label="客户姓名">
          <el-input v-model="searchForm.name" placeholder="请输入客户姓名" clearable />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="searchForm.phone" placeholder="请输入手机号" clearable />
        </el-form-item>
        <el-form-item label="家庭标签">
          <el-select v-model="searchForm.familyTag" placeholder="请选择家庭标签" clearable>
            <el-option
              v-for="tag in familyTags"
              :key="tag"
              :label="tag"
              :value="tag"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="会员等级">
          <el-select v-model="searchForm.memberLevel" placeholder="请选择会员等级" clearable>
            <el-option
              v-for="level in memberLevels"
              :key="level"
              :label="level"
              :value="level"
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
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增客户</el-button>
        <el-button type="warning" icon="el-icon-download" @click="handleExport">导出Excel</el-button>
      </div>
      
      <el-table :data="tableData" border stripe v-loading="loading" @row-click="handleRowClick">
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
        <el-table-column prop="totalConsumption" label="累计消费" width="110">
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
        <el-table-column label="操作" width="180" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click.stop="handleView(scope.row)">查看</el-button>
            <el-button type="text" size="small" @click.stop="handleEdit(scope.row)">编辑</el-button>
            <el-button type="text" size="small" style="color: #f56c6c;" @click.stop="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <div class="pagination-container">
        <el-pagination
          background
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pagination.page"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pagination.size"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total"
        />
      </div>
    </el-card>
    
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="600px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="客户姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入客户姓名" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="身份证号">
          <el-input v-model="form.idCard" placeholder="请输入身份证号" />
        </el-form-item>
        <el-form-item label="性别">
          <el-radio-group v-model="form.gender">
            <el-radio label="男">男</el-radio>
            <el-radio label="女">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="年龄">
          <el-input-number v-model="form.age" :min="1" :max="120" />
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="form.address" type="textarea" :rows="2" placeholder="请输入地址" />
        </el-form-item>
        <el-form-item label="家庭标签">
          <el-select v-model="form.familyTag" placeholder="请选择家庭标签">
            <el-option
              v-for="tag in familyTags"
              :key="tag"
              :label="tag"
              :value="tag"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="会员等级">
          <el-select v-model="form.memberLevel" placeholder="请选择会员等级">
            <el-option
              v-for="level in memberLevels"
              :key="level"
              :label="level"
              :value="level"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">正常</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getCustomerList,
  createCustomer,
  updateCustomer,
  deleteCustomer,
  getFamilyTags,
  getMemberLevels
} from '@/api/customer'
import { exportCustomers } from '@/api/export'

export default {
  name: 'Customers',
  data() {
    return {
      loading: false,
      submitLoading: false,
      searchForm: {
        name: '',
        phone: '',
        familyTag: '',
        memberLevel: ''
      },
      tableData: [],
      pagination: {
        page: 0,
        size: 10,
        total: 0
      },
      dialogVisible: false,
      dialogTitle: '',
      isEdit: false,
      familyTags: [],
      memberLevels: [],
      form: {
        name: '',
        phone: '',
        idCard: '',
        gender: '男',
        age: 30,
        address: '',
        familyTag: '',
        memberLevel: '普通会员',
        status: 1,
        remark: ''
      },
      rules: {
        name: [{ required: true, message: '请输入客户姓名', trigger: 'blur' }],
        phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }]
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
          ...this.searchForm
        })
        this.tableData = res.data.content || []
        this.pagination.total = res.data.totalElements || 0
      } catch (e) {
        this.tableData = []
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.pagination.page = 0
      this.loadData()
    },
    handleReset() {
      this.searchForm = {
        name: '',
        phone: '',
        familyTag: '',
        memberLevel: ''
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
    handleRowClick(row) {
      this.handleView(row)
    },
    handleView(row) {
      this.$router.push(`/customers/detail/${row.id}`)
    },
    handleAdd() {
      this.isEdit = false
      this.dialogTitle = '新增客户'
      this.form = {
        name: '',
        phone: '',
        idCard: '',
        gender: '男',
        age: 30,
        address: '',
        familyTag: '普通家庭',
        memberLevel: '普通会员',
        status: 1,
        remark: ''
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.isEdit = true
      this.dialogTitle = '编辑客户'
      this.form = { ...row }
      this.dialogVisible = true
    },
    async handleDelete(row) {
      try {
        await this.$confirm('确定要删除该客户吗？', '提示', {
          type: 'warning'
        })
        await deleteCustomer(row.id)
        this.$message.success('删除成功')
        this.loadData()
      } catch (e) {
        if (e !== 'cancel') {
          this.$message.error(e.message || '删除失败')
        }
      }
    },
    async handleSubmit() {
      try {
        await this.$refs.form.validate()
        this.submitLoading = true
        
        if (this.isEdit) {
          await updateCustomer(this.form.id, this.form)
          this.$message.success('更新成功')
        } else {
          await createCustomer(this.form)
          this.$message.success('创建成功')
        }
        
        this.dialogVisible = false
        this.loadData()
      } catch (e) {
        this.$message.error(e.message || '操作失败')
      } finally {
        this.submitLoading = false
      }
    },
    async handleExport() {
      try {
        const res = await exportCustomers(this.searchForm)
        const blob = new Blob([res], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        const timestamp = new Date().toISOString().slice(0, 10)
        link.setAttribute('download', `客户数据_${timestamp}.xlsx`)
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        this.$message.success('导出成功')
      } catch (e) {
        this.$message.error('导出失败')
      }
    }
  }
}
</script>
