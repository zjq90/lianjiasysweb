<template>
  <view class="address-form-page">
    <!-- 表单区域 -->
    <view class="form-section card">
      <view class="form-item">
        <text class="form-label">收货人</text>
        <input
          class="form-input"
          type="text"
          placeholder="请输入收货人姓名"
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
        <text class="form-label">所在地区</text>
        <picker
          mode="multiSelector"
          :value="regionIndex"
          :range="regionList"
          @change="onRegionChange"
          @columnchange="onRegionColumnChange"
        >
          <view class="picker-content">
            <text class="picker-value">{{ formData.province || '请选择' }}{{ formData.city || '' }}{{ formData.district || '' }}</text>
            <text class="picker-arrow">›</text>
          </view>
        </picker>
      </view>
      
      <view class="form-item">
        <text class="form-label">详细地址</text>
        <textarea
          class="form-textarea"
          placeholder="请输入详细地址，如街道、门牌号等"
          v-model="formData.address"
          maxlength="200"
        />
      </view>
      
      <view class="form-item default-item">
        <text class="form-label">设为默认</text>
        <switch
          :checked="formData.isDefault === 1"
          @change="onDefaultChange"
          color="#4CAF50"
        />
      </view>
    </view>

    <!-- 提示信息 -->
    <view class="tips-section card" v-if="isEdit">
      <view class="tips-header">
        <text class="tips-title">📝 编辑说明</text>
      </view>
      <view class="tips-content">
        <text class="tips-text">• 地址ID：{{ addressInfo.id }}</text>
        <text class="tips-text">• 创建时间：{{ addressInfo.createTime }}</text>
      </view>
    </view>

    <!-- 底部操作按钮 -->
    <view class="action-section safe-area-bottom">
      <button class="btn btn-primary btn-block" @tap="submitForm">
        {{ isEdit ? '保存修改' : '新增地址' }}
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
      addressId: null,
      addressInfo: {},
      formData: {
        name: '',
        phone: '',
        province: '',
        city: '',
        district: '',
        address: '',
        isDefault: 0
      },
      regionIndex: [0, 0, 0],
      regionList: [
        ['北京市', '上海市', '广东省', '江苏省', '浙江省'],
        ['北京市'],
        ['东城区', '西城区', '朝阳区', '海淀区', '丰台区']
      ]
    };
  },
  onLoad(options) {
    console.log('地址表单页面 onLoad, options:', options);
    
    if (options && options.id) {
      this.isEdit = true;
      this.addressId = options.id;
      this.loadAddressInfo();
    }
    
    uni.setNavigationBarTitle({
      title: this.isEdit ? '编辑地址' : '新增地址'
    });
  },
  methods: {
    loadAddressInfo() {
      console.log('加载地址信息, addressId:', this.addressId);
      const addressInfo = storage.AddressStorage.getById(this.addressId);
      console.log('获取到的地址信息:', addressInfo);
      
      if (addressInfo) {
        this.addressInfo = addressInfo;
        this.formData = {
          name: addressInfo.name || '',
          phone: addressInfo.phone || '',
          province: addressInfo.province || '',
          city: addressInfo.city || '',
          district: addressInfo.district || '',
          address: addressInfo.address || '',
          isDefault: addressInfo.isDefault || 0
        };
      } else {
        uni.showToast({
          title: '地址不存在',
          icon: 'none'
        });
        setTimeout(() => {
          navigator.navigateBack();
        }, 1500);
      }
    },
    
    onRegionChange(e) {
      console.log('地区选择变化:', e);
      const val = e.detail.value;
      this.formData.province = this.regionList[0][val[0]];
      this.formData.city = this.regionList[1][val[1]];
      this.formData.district = this.regionList[2][val[2]];
      this.regionIndex = val;
    },
    
    onRegionColumnChange(e) {
      console.log('地区列变化:', e);
      // 简化处理，实际项目中需要根据省市动态获取区县数据
    },
    
    onDefaultChange(e) {
      console.log('默认地址开关变化:', e);
      this.formData.isDefault = e.detail.value ? 1 : 0;
    },
    
    submitForm() {
      console.log('提交表单, formData:', this.formData);
      
      // 验证表单
      if (!this.formData.name || this.formData.name.trim() === '') {
        uni.showToast({
          title: '请输入收货人姓名',
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
      
      if (!this.formData.address || this.formData.address.trim() === '') {
        uni.showToast({
          title: '请输入详细地址',
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
            const result = storage.AddressStorage.update(this.addressId, {
              name: this.formData.name.trim(),
              phone: this.formData.phone.trim(),
              province: this.formData.province,
              city: this.formData.city,
              district: this.formData.district,
              address: this.formData.address.trim(),
              isDefault: this.formData.isDefault
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
            const result = storage.AddressStorage.add({
              name: this.formData.name.trim(),
              phone: this.formData.phone.trim(),
              province: this.formData.province || '北京市',
              city: this.formData.city || '北京市',
              district: this.formData.district || '朝阳区',
              address: this.formData.address.trim(),
              isDefault: this.formData.isDefault
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
          console.error('保存地址失败:', error);
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
.address-form-page {
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

.form-textarea {
  flex: 1;
  height: 160rpx;
  font-size: 28rpx;
  color: #333333;
  background-color: transparent;
  border: none;
  outline: none;
  text-align: right;
}

.form-textarea::placeholder {
  color: #CCCCCC;
}

.default-item {
  justify-content: space-between;
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

.picker-value:empty::before {
  content: '请选择';
  color: #CCCCCC;
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
