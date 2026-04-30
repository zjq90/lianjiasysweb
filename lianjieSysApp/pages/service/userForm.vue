<template>
  <view class="user-form-page">
    <!-- 表单区域 -->
    <view class="form-section card">
      <view class="form-item">
        <text class="form-label">用户姓名</text>
        <input
          class="form-input"
          type="text"
          placeholder="请输入用户姓名"
          v-model="formData.name"
          maxlength="20"
        />
      </view>
      
      <view class="form-item">
        <text class="form-label">手机号码</text>
        <input
          class="form-input"
          type="number"
          placeholder="请输入手机号码"
          v-model="formData.phone"
          maxlength="11"
        />
      </view>
      
      <view class="form-item">
        <text class="form-label">用户状态</text>
        <picker
          :value="statusIndex"
          :range="statusOptions"
          range-key="label"
          @change="onStatusChange"
        >
          <view class="picker-content">
            <text class="picker-value">{{ statusOptions[statusIndex].label }}</text>
            <text class="picker-arrow">›</text>
          </view>
        </picker>
      </view>
    </view>

    <!-- 提示信息 -->
    <view class="tips-section card" v-if="isEdit">
      <view class="tips-header">
        <text class="tips-title">📝 编辑说明</text>
      </view>
      <view class="tips-content">
        <text class="tips-text">• 用户ID：{{ userInfo.id }}</text>
        <text class="tips-text">• OpenID：{{ userInfo.openId || '系统自动生成' }}</text>
        <text class="tips-text">• 创建时间：{{ userInfo.createTime }}</text>
      </view>
    </view>

    <!-- 底部操作按钮 -->
    <view class="action-section safe-area-bottom">
      <button class="btn btn-primary btn-block" @tap="submitForm">
        {{ isEdit ? '保存修改' : '新增用户' }}
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
      userId: null,
      userInfo: {},
      formData: {
        name: '',
        phone: '',
        status: 1
      },
      statusIndex: 0,
      statusOptions: [
        { value: 1, label: '正常' },
        { value: 2, label: '禁用' }
      ]
    };
  },
  onLoad(options) {
    console.log('用户表单页面 onLoad, options:', options);
    
    if (options && options.id) {
      this.isEdit = true;
      this.userId = options.id;
      this.loadUserInfo();
    }
    
    // 设置导航栏标题
    uni.setNavigationBarTitle({
      title: this.isEdit ? '编辑用户' : '新增用户'
    });
  },
  methods: {
    loadUserInfo() {
      console.log('加载用户信息, userId:', this.userId);
      const userInfo = storage.UserStorage.getById(this.userId);
      console.log('获取到的用户信息:', userInfo);
      
      if (userInfo) {
        this.userInfo = userInfo;
        this.formData = {
          name: userInfo.name || '',
          phone: userInfo.phone || '',
          status: userInfo.status || 1
        };
        
        // 设置状态选择器索引
        const statusIndex = this.statusOptions.findIndex(item => item.value === userInfo.status);
        this.statusIndex = statusIndex > -1 ? statusIndex : 0;
      } else {
        uni.showToast({
          title: '用户不存在',
          icon: 'none'
        });
        setTimeout(() => {
          navigator.navigateBack();
        }, 1500);
      }
    },
    
    onStatusChange(e) {
      console.log('状态选择器变化:', e);
      const index = e.detail.value;
      this.statusIndex = index;
      this.formData.status = this.statusOptions[index].value;
    },
    
    submitForm() {
      console.log('提交表单, formData:', this.formData);
      
      // 验证表单
      if (!this.formData.name || this.formData.name.trim() === '') {
        uni.showToast({
          title: '请输入用户姓名',
          icon: 'none'
        });
        return;
      }
      
      if (!this.formData.phone || this.formData.phone.trim() === '') {
        uni.showToast({
          title: '请输入手机号码',
          icon: 'none'
        });
        return;
      }
      
      if (!/^1[3-9]\d{9}$/.test(this.formData.phone)) {
        uni.showToast({
          title: '手机号码格式不正确',
          icon: 'none'
        });
        return;
      }
      
      // 检查手机号是否已存在（编辑时排除自己）
      const existingUser = storage.UserStorage.getByPhone(this.formData.phone);
      if (existingUser && (!this.isEdit || existingUser.id !== this.userId)) {
        uni.showToast({
          title: '该手机号已被使用',
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
            const result = storage.UserStorage.update(this.userId, {
              name: this.formData.name.trim(),
              phone: this.formData.phone.trim(),
              status: this.formData.status
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
            const result = storage.UserStorage.add({
              name: this.formData.name.trim(),
              phone: this.formData.phone.trim(),
              status: this.formData.status
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
          console.error('保存用户失败:', error);
          uni.showToast({
            title: '操作失败',
            icon: 'none'
          });
        }
      }, 500);
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
.user-form-page {
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

/* 选择器样式 */
.picker-content {
  flex: 1;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.picker-value {
  font-size: 28rpx;
  color: #333333;
}

.picker-arrow {
  font-size: 36rpx;
  color: #CCCCCC;
}

/* 提示信息样式 */
.tips-header {
  padding: 20rpx 30rpx;
  border-bottom: 1rpx solid #E0E0E0;
}

.tips-title {
  font-size: 28rpx;
  font-weight: 600;
  color: #333333;
}

.tips-content {
  padding: 20rpx 30rpx;
  display: flex;
  flex-direction: column;
}

.tips-text {
  font-size: 24rpx;
  color: #999999;
  margin-bottom: 12rpx;
}

.tips-text:last-child {
  margin-bottom: 0;
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
