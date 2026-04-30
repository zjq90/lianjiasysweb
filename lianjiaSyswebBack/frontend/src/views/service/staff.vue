<template>
  <div class="page-container">
    <el-card class="search-form">
      <el-form :inline="true" :model="searchForm" label-width="80px">
        <el-form-item label="姓名">
          <el-input v-model="searchForm.name" placeholder="请输入姓名" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="空闲" value="空闲" />
            <el-option label="忙碌" value="忙碌" />
            <el-option label="休假" value="休假" />
            <el-option label="离职" value="离职" />
          </el-select>
        </el-form-item>
        <el-form-item label="技能">
          <el-select v-model="searchForm.skill" placeholder="请选择技能" clearable>
            <el-option
              v-for="skill in skills"
              :key="skill"
              :label="skill"
              :value="skill"
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
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增服务人员</el-button>
      </div>
      
      <el-table :data="tableData" border stripe v-loading="loading">
        <el-table-column prop="name" label="姓名" width="100" />
        <el-table-column prop="phone" label="手机号" width="130" />
        <el-table-column prop="gender" label="性别" width="60" />
        <el-table-column prop="age" label="年龄" width="60" />
        <el-table-column prop="skill" label="技能" min-width="200">
          <template slot-scope="scope">
            <el-tag
              v-for="(skill, index) in scope.row.skill?.split(',') || []"
              :key="index"
              size="small"
              style="margin-right: 5px; margin-bottom: 5px;"
            >
              {{ skill }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="workArea" label="工作区域" width="150" />
        <el-table-column prop="status" label="状态" width="80">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)" size="small">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="orderCount" label="完成订单" width="100" />
        <el-table-column prop="rating" label="评分" width="100">
          <template slot-scope="scope">
            <el-rate v-model="scope.row.rating" disabled :max="5" show-score text-color="#ff9900" />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button
              v-if="scope.row.status !== '离职'"
              type="text"
              size="small"
              @click="handleChangeStatus(scope.row)"
            >
              更改状态
            </el-button>
            <el-button
              type="text"
              size="small"
              style="color: #f56c6c;"
              @click="handleDelete(scope.row)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
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
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入姓名" />
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
          <el-input-number v-model="form.age" :min="18" :max="65" />
        </el-form-item>
        <el-form-item label="技能" prop="skill">
          <el-select
            v-model="selectedSkills"
            multiple
            placeholder="请选择技能"
            style="width: 100%;"
          >
            <el-option
              v-for="skill in skills"
              :key="skill"
              :label="skill"
              :value="skill"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="工作区域">
          <el-input v-model="form.workArea" placeholder="请输入工作区域" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio label="空闲">空闲</el-radio>
            <el-radio label="忙碌">忙碌</el-radio>
            <el-radio label="休假">休假</el-radio>
            <el-radio label="离职">离职</el-radio>
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
    
    <el-dialog title="更改状态" :visible.sync="statusDialogVisible" width="400px">
      <el-form :model="statusForm" label-width="80px">
        <el-form-item label="当前状态">
          <el-tag :type="getStatusType(currentStaff?.status)">{{ currentStaff?.status }}</el-tag>
        </el-form-item>
        <el-form-item label="目标状态">
          <el-select v-model="statusForm.status" placeholder="请选择目标状态" style="width: 100%;">
            <el-radio label="空闲">空闲</el-radio>
            <el-radio label="忙碌">忙碌</el-radio>
            <el-radio label="休假">休假</el-radio>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="statusDialogVisible = false">取 消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleStatusSubmit">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getServiceStaffList, createServiceStaff, updateServiceStaff, deleteServiceStaff, updateStaffStatus } from '@/api/service'

export default {
  name: 'ServiceStaff',
  data() {
    return {
      loading: false,
      submitLoading: false,
      searchForm: {
        name: '',
        status: '',
        skill: ''
      },
      skills: ['甲醛检测', '甲醛治理', '深度清洁', '空气净化', '消毒杀菌'],
      tableData: [],
      dialogVisible: false,
      dialogTitle: '',
      isEdit: false,
      form: {
        name: '',
        phone: '',
        idCard: '',
        gender: '男',
        age: 30,
        skill: '',
        workArea: '',
        status: '空闲',
        remark: ''
      },
      selectedSkills: [],
      rules: {
        name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
        phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }],
        skill: [{ required: true, message: '请选择技能', trigger: 'change' }]
      },
      statusDialogVisible: false,
      currentStaff: null,
      statusForm: {
        status: ''
      }
    }
  },
  created() {
    this.loadData()
  },
  methods: {
    getStatusType(status) {
      const types = {
        '空闲': 'success',
        '忙碌': 'warning',
        '休假': 'info',
        '离职': 'danger'
      }
      return types[status] || 'info'
    },
    async loadData() {
      this.loading = true
      try {
        const params = { ...this.searchForm }
        if (!params.status) delete params.status
        if (!params.skill) delete params.skill
        
        const res = await getServiceStaffList(params)
        this.tableData = res.data || []
      } catch (e) {
        this.tableData = []
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.loadData()
    },
    handleReset() {
      this.searchForm = {
        name: '',
        status: '',
        skill: ''
      }
      this.loadData()
    },
    handleAdd() {
      this.isEdit = false
      this.dialogTitle = '新增服务人员'
      this.form = {
        name: '',
        phone: '',
        idCard: '',
        gender: '男',
        age: 30,
        skill: '',
        workArea: '',
        status: '空闲',
        remark: ''
      }
      this.selectedSkills = []
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.isEdit = true
      this.dialogTitle = '编辑服务人员'
      this.form = { ...row }
      this.selectedSkills = row.skill?.split(',') || []
      this.dialogVisible = true
    },
    async handleDelete(row) {
      try {
        await this.$confirm('确定要删除该服务人员吗？', '提示', { type: 'warning' })
        await deleteServiceStaff(row.id)
        this.$message.success('删除成功')
        this.loadData()
      } catch (e) {
        if (e !== 'cancel') {
          this.$message.error(e.message || '删除失败')
        }
      }
    },
    handleChangeStatus(row) {
      this.currentStaff = row
      this.statusForm.status = ''
      this.statusDialogVisible = true
    },
    async handleSubmit() {
      try {
        if (this.selectedSkills.length === 0) {
          this.$message.warning('请至少选择一个技能')
          return
        }
        this.form.skill = this.selectedSkills.join(',')
        
        await this.$refs.form.validate()
        this.submitLoading = true
        
        if (this.isEdit) {
          await updateServiceStaff(this.form.id, this.form)
          this.$message.success('更新成功')
        } else {
          await createServiceStaff(this.form)
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
    async handleStatusSubmit() {
      if (!this.statusForm.status) {
        this.$message.warning('请选择目标状态')
        return
      }
      try {
        this.submitLoading = true
        await updateStaffStatus(this.currentStaff.id, this.statusForm.status)
        this.$message.success('状态更新成功')
        this.statusDialogVisible = false
        this.loadData()
      } catch (e) {
        this.$message.error(e.message || '操作失败')
      } finally {
        this.submitLoading = false
      }
    }
  }
}
</script>
