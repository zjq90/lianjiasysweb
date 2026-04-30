<template>
  <view class="package-card-form-page">
    <!-- 基础信息区域 -->
    <view class="form-section card">
      <view class="section-header">
        <text class="section-title">💳 基础信息</text>
      </view>
      
      <view class="form-item">
        <text class="form-label">套餐卡名称</text>
        <input
          class="form-input"
          type="text"
          placeholder="请输入套餐卡名称"
          v-model="formData.name"
          maxlength="50"
        />
      </view>
      
      <view class="form-item">
        <text class="form-label">卡号</text>
        <input
          class="form-input"
          type="text"
          placeholder="自动生成，可修改"
          v-model="formData.cardNo"
          maxlength="20"
        />
      </view>
      
      <view class="form-item">
        <text class="form-label">关联套餐</text>
        <picker
          :value="packageIndex"
          :range="packageOptions"
          range-key="name"
          @change="onPackageChange"
        >
          <view class="picker-content">
            <text class="picker-value">{{ formData.packageName || '请选择' }}</text>
            <text class="picker-arrow">›</text>
          </view>
        </picker>
      </view>
      
      <view class="form-item">
        <text class="form-label">关联用户</text>
        <picker
          :value="userIndex"
          :range="userOptions"
          range-key="name"
          @change="onUserChange"
        >
          <view class="picker-content">
            <text class="picker-value">{{ formData.userName || '请选择' }}</text>
            <text class="picker-arrow">›</text>
          </view>
        </picker>
      </view>
    </view>

    <!-- 次数设置区域 -->
    <view class="form-section card">
      <view class="section-header">
        <text class="section-title">🔢 次数设置</text>
      </view>
      
      <view class="form-item">
        <text class="form-label">总次数</text>
        <input
          class="form-input"
          type="number"
          placeholder="请输入总次数"
          v-model="formData.totalTimes"
          maxlength="5"
        />
      </view>
      
      <view class="form-item">
        <text class="form-label">已使用次数</text>
        <input
          class="form-input"
          type="number"
          placeholder="请输入已使用次数"
          v-model="formData.usedTimes"
          maxlength="5"
        />
      </view>
      
      <view class="info-row">
        <text class="info-label">剩余次数：</text>
        <text class="info-value highlight">{{ remainingTimes }}</text>
      </view>
    </view>

    <!-- 有效期设置区域 -->
    <view class="form-section card">
      <view class="section-header">
        <text class="section-title">📅 有效期设置</text>
      </view>
      
      <view class="form-item">
        <text class="form-label">开始日期</text>
        <picker
          mode="date"
          :value="formData.startDate"
          @change="onStartDateChange"
        >
          <view class="picker-content">
            <text class="picker-value">{{ formData.startDate || '请选择' }}</text>
            <text class="picker-arrow">›</text>
          </view>
        </picker>
      </view>
      
      <view class="form-item">
        <text class="form-label">结束日期</text>
        <picker
          mode="date"
          :value="formData.endDate"
          @change="onEndDateChange"
        >
          <view class="picker-content">
            <text class="picker-value">{{ formData.endDate || '请选择' }}</text>
            <text class="picker-arrow">›</text>
          </view>
        </picker>
      </view>
      
      <view class="form-item switch-item">
        <view class="switch-info">
          <text class="switch-label">设为有效</text>
          <text class="switch-desc">关闭后状态为已过期</text>
        </view>
        <switch
          :checked="formData.status === 1"
          @change="onStatusChange"
          color="#4CAF50"
        />
      </view>
    </view>

    <!-- 备注信息 -->
    <view class="form-section card">
      <view class="section-header">
        <text class="section-title">📝 备注信息</text>
      </view>
      
      <view class="form-item textarea-item">
        <textarea
          class="form-textarea"
          placeholder="请输入备注信息（可选）"
          v-model="formData.remark"
          maxlength="500"
        />
      </view>
    </view>

    <!-- 底部操作按钮 -->
    <view class="action-section safe-area-bottom">
      <button class="btn btn-primary btn-block" @tap="submitForm">
        {{ isEdit ? '保存修改' : '新增套餐卡' }}
      </button>
      <button class="btn btn-outline btn-block" @tap="cancelForm" v-if="isEdit">
        取消
      </button>
    </view>
  </view>
</template>

<script>
import storage from '@/common/utils/storage.js';
import navigator from '@/common/utils/navigator.js';

export default {
  data() {
    return {
      isEdit: false,
      cardId: null,
      cardInfo: {},
      packageOptions: [],
      packageIndex: 0,
      userOptions: [],
      userIndex: 0,
      formData: {
        name: '',
        cardNo: '',
        packageId: null,
        packageName: '',
        userId: null,
        userName: '',
        totalTimes: '',
        usedTimes: '0',
        startDate: '',
        endDate: '',
        status: 1,
        remark: ''
      }
    };
  },
  computed: {
    remainingTimes() {
      const total = parseInt(this.formData.totalTimes) || 0;
      const used = parseInt(this.formData.usedTimes) || 0;
      return Math.max(0, total - used);
    }
  },
  onLoad(options) {
    console.log('套餐卡表单页面 onLoad, options:', options);
    
    // 加载套餐和用户选项
    this.loadOptions();
    
    // 生成默认卡号
    this.generateCardNo();
    
    if (options && options.id) {
      this.isEdit = true;
      this.cardId = options.id;
      this.loadCardInfo();
    }
    
    uni.setNavigationBarTitle({
      title: this.isEdit ? '编辑套餐卡' : '新增套餐卡'
    });
  },
  methods: {
    generateCardNo() {
      const now = new Date();
      const year = now.getFullYear();
      const month = String(now.getMonth() + 1).padStart(2, '0');
      const day = String(now.getDate()).padStart(2, '0');
      const random = String(Math.floor(Math.random() * 10000)).padStart(4, '0');
      this.formData.cardNo = `PC${year}${month}${day}${random}`;
    },
    
    loadOptions() {
      // 加载套餐列表
      const packages = storage.PackageCardStorage.getList();
      const packageSet = new Map();
      packages.forEach(p => {
        if (p.packageName) {
          packageSet.set(p.packageName, { id: p.packageId || 1, name: p.packageName });
        }
      });
      
      // 添加默认套餐选项
      if (packageSet.size === 0) {
        packageSet.set('基础清洁套餐', { id: 1, name: '基础清洁套餐' });
        packageSet.set('深度清洁套餐', { id: 2, name: '深度清洁套餐' });
        packageSet.set('全屋消毒套餐', { id: 3, name: '全屋消毒套餐' });
      }
      
      this.packageOptions = Array.from(packageSet.values());
      
      // 加载用户列表
      const users = storage.UserStorage.getList();
      this.userOptions = users.map(u => ({ id: u.id, name: u.name || u.realName || '未命名用户' }));
      
      if (this.userOptions.length === 0) {
        this.userOptions = [
          { id: 1, name: '张三' },
          { id: 2, name: '李四' }
        ];
      }
    },
    
    loadCardInfo() {
      console.log('加载套餐卡信息, cardId:', this.cardId);
      const cardInfo = storage.PackageCardStorage.getById(this.cardId);
      console.log('获取到的套餐卡信息:', cardInfo);
      
      if (cardInfo) {
        this.cardInfo = cardInfo;
        this.formData = {
          name: cardInfo.name || '',
          cardNo: cardInfo.cardNo || '',
          packageId: cardInfo.packageId || null,
          packageName: cardInfo.packageName || '',
          userId: cardInfo.userId || null,
          userName: cardInfo.userName || '',
          totalTimes: cardInfo.totalTimes ? String(cardInfo.totalTimes) : '',
          usedTimes: cardInfo.usedTimes ? String(cardInfo.usedTimes) : '0',
          startDate: cardInfo.startDate || '',
          endDate: cardInfo.expireDate || cardInfo.endDate || '',
          status: cardInfo.status || 1,
          remark: cardInfo.remark || ''
        };
        
        // 设置套餐选择索引
        const pkgIdx = this.packageOptions.findIndex(p => p.id === this.formData.packageId || p.name === this.formData.packageName);
        if (pkgIdx !== -1) {
          this.packageIndex = pkgIdx;
        }
        
        // 设置用户选择索引
        const usrIdx = this.userOptions.findIndex(u => u.id === this.formData.userId || u.name === this.formData.userName);
        if (usrIdx !== -1) {
          this.userIndex = usrIdx;
        }
      } else {
        uni.showToast({
          title: '套餐卡不存在',
          icon: 'none'
        });
        setTimeout(() => {
          navigator.navigateBack();
        }, 1500);
      }
    },
    
    onPackageChange(e) {
      console.log('套餐选择变化:', e);
      this.packageIndex = e.detail.value;
      const selected = this.packageOptions[e.detail.value];
      this.formData.packageId = selected.id;
      this.formData.packageName = selected.name;
    },
    
    onUserChange(e) {
      console.log('用户选择变化:', e);
      this.userIndex = e.detail.value;
      const selected = this.userOptions[e.detail.value];
      this.formData.userId = selected.id;
      this.formData.userName = selected.name;
    },
    
    onStartDateChange(e) {
      console.log('开始日期变化:', e);
      this.formData.startDate = e.detail.value;
    },
    
    onEndDateChange(e) {
      console.log('结束日期变化:', e);
      this.formData.endDate = e.detail.value;
    },
    
    onStatusChange(e) {
      console.log('状态开关变化:', e);
      this.formData.status = e.detail.value ? 1 : 2;
    },
    
    submitForm() {
      console.log('提交表单, formData:', this.formData);
      
      // 验证表单
      if (!this.formData.name || this.formData.name.trim() === '') {
        uni.showToast({
          title: '请输入套餐卡名称',
          icon: 'none'
        });
        return;
      }
      
      if (!this.formData.cardNo || this.formData.cardNo.trim() === '') {
        uni.showToast({
          title: '请输入卡号',
          icon: 'none'
        });
        return;
      }
      
      if (!this.formData.totalTimes || parseInt(this.formData.totalTimes) <= 0) {
        uni.showToast({
          title: '请输入有效的总次数',
          icon: 'none'
        });
        return;
      }
      
      const total = parseInt(this.formData.totalTimes) || 0;
      const used = parseInt(this.formData.usedTimes) || 0;
      
      if (used > total) {
        uni.showToast({
          title: '已使用次数不能大于总次数',
          icon: 'none'
        });
        return;
      }
      
      uni.showLoading({
        title: this.isEdit ? '保存中...' : '新增中...'
      });
      
      setTimeout(() => {
        try {
          if (this.isEdit) {
            // 编辑模式
            const result = storage.PackageCardStorage.update(this.cardId, {
              name: this.formData.name.trim(),
              cardNo: this.formData.cardNo.trim(),
              packageId: this.formData.packageId,
              packageName: this.formData.packageName,
              userId: this.formData.userId,
              userName: this.formData.userName,
              totalTimes: total,
              usedTimes: used,
              remainingTimes: total - used,
              startDate: this.formData.startDate,
              expireDate: this.formData.endDate,
              status: this.formData.status,
              remark: this.formData.remark
            });
            
            if (result) {
              uni.hideLoading();
              uni.showToast({
                title: '修改成功',
                icon: 'success'
              });
              
              setTimeout(() => {
                navigator.navigateBack();
              }, 1500);
            } else {
              uni.hideLoading();
              uni.showToast({
                title: '修改失败',
                icon: 'none'
              });
            }
          } else {
            // 新增模式
            const result = storage.PackageCardStorage.add({
              name: this.formData.name.trim(),
              cardNo: this.formData.cardNo.trim(),
              packageId: this.formData.packageId,
              packageName: this.formData.packageName,
              userId: this.formData.userId,
              userName: this.formData.userName,
              totalTimes: total,
              usedTimes: used,
              remainingTimes: total - used,
              startDate: this.formData.startDate,
              expireDate: this.formData.endDate,
              status: this.formData.status,
              remark: this.formData.remark
            });
            
            if (result) {
              uni.hideLoading();
              uni.showToast({
                title: '新增成功',
                icon: 'success'
              });
              
              setTimeout(() => {
                navigator.navigateBack();
              }, 1500);
            } else {
              uni.hideLoading();
              uni.showToast({
                title: '新增失败',
                icon: 'none'
              });
            }
          }
        } catch (error) {
          uni.hideLoading();
          console.error('保存套餐卡失败:', error);
          uni.showToast({
            title: '操作失败',
            icon: 'none'
          });
        }
      }, 300);
    },
    
    cancelForm() {
      uni.showModal({
        title: '确认取消',
        content: '确定要取消编辑吗？修改的内容将不会保存。',
        success: (res) => {
          if (res.confirm) {
            navigator.navigateBack();
          }
        }
      });
    }
  }
};
</script>

<style scoped>
.package-card-form-page {
  min-height: 100vh;
  background-color: #F8F9FA;
  padding: 30rpx;
  padding-bottom: 180rpx;
}

/* 卡片样式 */
.card {
  background-color: #FFFFFF;
  border-radius: 16rpx;
  margin-bottom: 24rpx;
  overflow: hidden;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

/* 区域标题 */
.section-header {
  padding: 20rpx 30rpx;
  border-bottom: 1rpx solid #E0E0E0;
}

.section-title {
  font-size: 30rpx;
  font-weight: 600;
  color: #333333;
}

/* 表单项样式 */
.form-item {
  display: flex;
  align-items: center;
  padding: 28rpx 30rpx;
  border-bottom: 1rpx solid #F0F0F0;
}

.form-item:last-child {
  border-bottom: none;
}

.form-item.textarea-item {
  align-items: flex-start;
}

.form-label {
  width: 180rpx;
  font-size: 28rpx;
  color: #333333;
  flex-shrink: 0;
}

.form-input {
  flex: 1;
  font-size: 28rpx;
  color: #333333;
  background-color: transparent;
  border: none;
  outline: none;
  text-align: right;
}

.form-input::placeholder {
  color: #CCCCCC;
}

.form-textarea {
  flex: 1;
  height: 160rpx;
  font-size: 28rpx;
  color: #333333;
  background-color: transparent;
  border: none;
  outline: none;
}

.form-textarea::placeholder {
  color: #CCCCCC;
}

/* 选择器样式 */
.picker-content {
  flex: 1;
  display: flex;
  justify-content: flex-end;
  align-items: center;
}

.picker-value {
  font-size: 28rpx;
  color: #333333;
}

.picker-value:empty::before {
  content: '请选择';
  color: #CCCCCC;
}

.picker-arrow {
  font-size: 36rpx;
  color: #CCCCCC;
  margin-left: 8rpx;
}

/* 信息行 */
.info-row {
  display: flex;
  align-items: center;
  padding: 16rpx 30rpx;
  background-color: #F9F9F9;
}

.info-label {
  font-size: 26rpx;
  color: #999999;
}

.info-value {
  font-size: 28rpx;
  color: #333333;
  font-weight: 500;
}

.info-value.highlight {
  color: #4CAF50;
  font-size: 32rpx;
}

/* 开关样式 */
.switch-item {
  justify-content: space-between;
}

.switch-info {
  display: flex;
  flex-direction: column;
}

.switch-label {
  font-size: 28rpx;
  color: #333333;
}

.switch-desc {
  font-size: 22rpx;
  color: #999999;
  margin-top: 4rpx;
}

/* 操作按钮区域 */
.action-section {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 20rpx 30rpx;
  background-color: #FFFFFF;
  box-shadow: 0 -2rpx 10rpx rgba(0, 0, 0, 0.05);
}

.btn {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 24rpx 40rpx;
  border-radius: 50rpx;
  font-size: 30rpx;
  font-weight: 500;
  border: none;
  margin-bottom: 16rpx;
}

.btn:last-child {
  margin-bottom: 0;
}

.btn-primary {
  background-color: #4CAF50;
  color: #FFFFFF;
}

.btn-outline {
  background-color: transparent;
  color: #4CAF50;
  border: 2rpx solid #4CAF50;
}

.btn-block {
  width: 100%;
}

/* 安全区域 */
.safe-area-bottom {
  padding-bottom: calc(20rpx + constant(safe-area-inset-bottom));
  padding-bottom: calc(20rpx + env(safe-area-inset-bottom));
}
</style>
