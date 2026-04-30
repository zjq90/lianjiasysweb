<template>
  <view class="address-detail-page">
    <!-- 地址信息卡片 -->
    <view class="address-card card">
      <view class="card-header">
        <text class="card-title">📌 地址信息</text>
        <view class="status-badge" v-if="addressInfo.isDefault === 1">
          <text class="status-text">默认</text>
        </view>
      </view>
      
      <view class="card-body">
        <view class="info-row">
          <text class="info-label">收货人</text>
          <text class="info-value">{{ addressInfo.name || '无' }}</text>
        </view>
        
        <view class="info-row">
          <text class="info-label">手机号码</text>
          <text class="info-value">{{ addressInfo.phone || '无' }}</text>
        </view>
        
        <view class="info-row">
          <text class="info-label">所在地区</text>
          <text class="info-value">{{ addressInfo.province || '' }}{{ addressInfo.city || '' }}{{ addressInfo.district || '无' }}</text>
        </view>
        
        <view class="info-row full-width">
          <text class="info-label">详细地址</text>
          <text class="info-value">{{ addressInfo.address || '无' }}</text>
        </view>
        
        <view class="info-row">
          <text class="info-label">创建时间</text>
          <text class="info-value">{{ addressInfo.createTime || '无' }}</text>
        </view>
      </view>
    </view>

    <!-- 操作按钮区域 -->
    <view class="action-section card">
      <view class="action-row">
        <view class="action-item" @tap="editAddress">
          <text class="action-icon">✏️</text>
          <text class="action-text">编辑</text>
        </view>
        <view class="action-divider"></view>
        <view class="action-item" @tap="setDefault" v-if="addressInfo.isDefault !== 1">
          <text class="action-icon">⭐</text>
          <text class="action-text">设为默认</text>
        </view>
        <view class="action-divider" v-if="addressInfo.isDefault !== 1"></view>
        <view class="action-item" @tap="deleteAddress">
          <text class="action-icon">🗑️</text>
          <text class="action-text">删除</text>
        </view>
      </view>
    </view>

    <!-- 底部操作按钮 -->
    <view class="bottom-action-section safe-area-bottom">
      <button class="btn btn-primary btn-block" @tap="editAddress">
        编辑地址
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
      addressId: null,
      addressInfo: {}
    };
  },
  onLoad(options) {
    console.log('地址详情页面 onLoad, options:', options);
    
    if (options && options.id) {
      this.addressId = options.id;
      this.loadAddressInfo();
    } else {
      uni.showToast({
        title: '参数错误',
        icon: 'none'
      });
      setTimeout(() => {
        navigator.navigateBack();
      }, 1500);
    }
  },
  onShow() {
    if (this.addressId) {
      this.loadAddressInfo();
    }
  },
  methods: {
    loadAddressInfo() {
      console.log('加载地址信息, addressId:', this.addressId);
      const addressInfo = storage.AddressStorage.getById(this.addressId);
      console.log('获取到的地址信息:', addressInfo);
      
      if (addressInfo) {
        this.addressInfo = addressInfo;
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
    
    editAddress() {
      console.log('点击编辑地址');
      navigator.navigateTo({
        pageKey: 'addressForm',
        params: { id: this.addressId }
      });
    },
    
    setDefault() {
      console.log('设为默认地址');
      uni.showModal({
        title: '确认设为默认',
        content: '确定要将此地址设为默认地址吗？',
        success: (res) => {
          if (res.confirm) {
            uni.showLoading({
              title: '设置中...'
            });
            
            setTimeout(() => {
              try {
                // 先将所有地址的默认状态设为0
                const allAddresses = storage.AddressStorage.getList();
                allAddresses.forEach(addr => {
                  if (addr.id !== this.addressId && addr.isDefault === 1) {
                    storage.AddressStorage.update(addr.id, { isDefault: 0 });
                  }
                });
                
                // 设置当前地址为默认
                const result = storage.AddressStorage.update(this.addressId, { isDefault: 1 });
                
                if (result) {
                  uni.hideLoading();
                  uni.showToast({
                    title: '设置成功',
                    icon: 'success'
                  });
                  this.loadAddressInfo();
                } else {
                  uni.hideLoading();
                  uni.showToast({
                    title: '设置失败',
                    icon: 'none'
                  });
                }
              } catch (error) {
                uni.hideLoading();
                console.error('设置默认地址失败:', error);
                uni.showToast({
                  title: '操作失败',
                  icon: 'none'
                });
              }
            }, 300);
          }
        }
      });
    },
    
    deleteAddress() {
      console.log('点击删除地址');
      uni.showModal({
        title: '确认删除',
        content: '确定要删除此地址吗？删除后无法恢复。',
        confirmColor: '#FF5252',
        success: (res) => {
          if (res.confirm) {
            uni.showLoading({
              title: '删除中...'
            });
            
            setTimeout(() => {
              try {
                const result = storage.AddressStorage.delete(this.addressId);
                
                if (result) {
                  uni.hideLoading();
                  uni.showToast({
                    title: '删除成功',
                    icon: 'success'
                  });
                  setTimeout(() => {
                    navigator.navigateBack();
                  }, 1500);
                } else {
                  uni.hideLoading();
                  uni.showToast({
                    title: '删除失败',
                    icon: 'none'
                  });
                }
              } catch (error) {
                uni.hideLoading();
                console.error('删除地址失败:', error);
                uni.showToast({
                  title: '操作失败',
                  icon: 'none'
                });
              }
            }, 300);
          }
        }
      });
    }
  }
};
</script>

<style scoped>
.address-detail-page {
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

/* 地址卡片样式 */
.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20rpx 30rpx;
  border-bottom: 1rpx solid #E0E0E0;
}

.card-title {
  font-size: 30rpx;
  font-weight: 600;
  color: #333333;
}

.status-badge {
  background-color: #E8F5E9;
  padding: 6rpx 16rpx;
  border-radius: 20rpx;
}

.status-text {
  font-size: 22rpx;
  color: #4CAF50;
  font-weight: 500;
}

.card-body {
  padding: 20rpx 30rpx;
}

.info-row {
  display: flex;
  align-items: center;
  padding: 16rpx 0;
}

.info-row.full-width {
  flex-direction: column;
  align-items: flex-start;
}

.info-label {
  width: 180rpx;
  font-size: 28rpx;
  color: #999999;
  flex-shrink: 0;
}

.info-row.full-width .info-label {
  width: 100%;
  margin-bottom: 8rpx;
}

.info-value {
  flex: 1;
  font-size: 28rpx;
  color: #333333;
  text-align: right;
}

.info-row.full-width .info-value {
  text-align: left;
  width: 100%;
}

/* 操作按钮区域 */
.action-row {
  display: flex;
  align-items: center;
  padding: 10rpx 0;
}

.action-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 20rpx 10rpx;
}

.action-icon {
  font-size: 40rpx;
  margin-bottom: 8rpx;
}

.action-text {
  font-size: 24rpx;
  color: #666666;
}

.action-divider {
  width: 1rpx;
  height: 60rpx;
  background-color: #E0E0E0;
}

/* 底部操作按钮 */
.bottom-action-section {
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
}

.btn-primary {
  background-color: #4CAF50;
  color: #FFFFFF;
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
