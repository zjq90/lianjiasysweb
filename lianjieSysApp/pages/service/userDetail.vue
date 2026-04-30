<template>
  <view class="user-detail-page">
    <!-- 用户信息卡片 -->
    <view class="user-card card">
      <view class="card-header">
        <image :src="userInfo.avatar" class="user-avatar" mode="aspectFill" />
        <view class="user-base">
          <view class="name-row">
            <text class="user-name">{{ userInfo.name }}</text>
            <view
              class="status-tag"
              :class="userInfo.status === 1 ? 'status-active' : 'status-inactive'"
            >
              <text class="status-text">{{ userInfo.status === 1 ? '正常' : '禁用' }}</text>
            </view>
          </view>
          <text class="user-phone">{{ userInfo.phone }}</text>
        </view>
      </view>
      
      <view class="card-body">
        <view class="info-row">
          <text class="info-label">用户ID</text>
          <text class="info-value">{{ userInfo.id }}</text>
        </view>
        <view class="info-row">
          <text class="info-label">OpenID</text>
          <text class="info-value openid">{{ userInfo.openId || '系统自动生成' }}</text>
        </view>
        <view class="info-row">
          <text class="info-label">创建时间</text>
          <text class="info-value">{{ userInfo.createTime }}</text>
        </view>
      </view>
    </view>

    <!-- 操作按钮 -->
    <view class="action-section card">
      <view class="action-row">
        <view class="action-item" @tap="editUser">
          <view class="action-icon edit">
            <text class="icon-text">✏️</text>
          </view>
          <text class="action-text">编辑</text>
        </view>
        
        <view class="action-item" @tap="toggleStatus" v-if="userInfo.status === 1">
          <view class="action-icon disable">
            <text class="icon-text">🚫</text>
          </view>
          <text class="action-text">禁用</text>
        </view>
        
        <view class="action-item" @tap="toggleStatus" v-else>
          <view class="action-icon enable">
            <text class="icon-text">✅</text>
          </view>
          <text class="action-text">启用</text>
        </view>
        
        <view class="action-item" @tap="deleteUser">
          <view class="action-icon delete">
            <text class="icon-text">🗑️</text>
          </view>
          <text class="action-text">删除</text>
        </view>
      </view>
    </view>

    <!-- 相关数据 -->
    <view class="related-section card">
      <view class="section-header">
        <text class="section-title">📋 相关记录</text>
      </view>
      <view class="related-list">
        <view class="related-item" @tap="viewRelated('family')">
          <view class="related-icon">
            <text class="icon-text">👨‍👩‍👧‍👦</text>
          </view>
          <view class="related-info">
            <text class="related-title">家庭档案</text>
            <text class="related-count">0 个档案</text>
          </view>
          <text class="related-arrow">›</text>
        </view>
        
        <view class="related-item" @tap="viewRelated('address')">
          <view class="related-icon">
            <text class="icon-text">📍</text>
          </view>
          <view class="related-info">
            <text class="related-title">收货地址</text>
            <text class="related-count">0 个地址</text>
          </view>
          <text class="related-arrow">›</text>
        </view>
        
        <view class="related-item" @tap="viewRelated('packageCard')">
          <view class="related-icon">
            <text class="icon-text">🎫</text>
          </view>
          <view class="related-info">
            <text class="related-title">套餐卡</text>
            <text class="related-count">0 张</text>
          </view>
          <text class="related-arrow">›</text>
        </view>
        
        <view class="related-item" @tap="viewRelated('orders')">
          <view class="related-icon">
            <text class="icon-text">📋</text>
          </view>
          <view class="related-info">
            <text class="related-title">服务单</text>
            <text class="related-count">0 单</text>
          </view>
          <text class="related-arrow">›</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import storage from '@/common/utils/storage.js';
import navigator from '@/common/utils/navigator.js';

export default {
  data() {
    return {
      userId: null,
      userInfo: {}
    };
  },
  onLoad(options) {
    console.log('用户详情页面 onLoad, options:', options);
    
    if (options && options.id) {
      this.userId = options.id;
      this.loadUserInfo();
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
    // 页面显示时重新加载数据，确保编辑后的数据能刷新
    if (this.userId) {
      this.loadUserInfo();
    }
  },
  methods: {
    loadUserInfo() {
      console.log('加载用户信息, userId:', this.userId);
      const userInfo = storage.UserStorage.getById(this.userId);
      console.log('获取到的用户信息:', userInfo);
      
      if (userInfo) {
        this.userInfo = userInfo;
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
    
    editUser() {
      console.log('编辑用户:', this.userInfo);
      navigator.navigateTo('userForm', { id: this.userInfo.id });
    },
    
    toggleStatus() {
      const currentStatus = this.userInfo.status;
      const newStatus = currentStatus === 1 ? 2 : 1;
      const actionText = currentStatus === 1 ? '禁用' : '启用';
      
      uni.showModal({
        title: '确认操作',
        content: `确定要${actionText}用户「${this.userInfo.name}」吗？`,
        success: (res) => {
          if (res.confirm) {
            uni.showLoading({
              title: `${actionText}中...`
            });
            
            setTimeout(() => {
              const result = storage.UserStorage.updateStatus(this.userId, newStatus);
              uni.hideLoading();
              
              if (result) {
                this.userInfo.status = newStatus;
                uni.showToast({
                  title: `${actionText}成功`,
                  icon: 'success'
                });
              } else {
                uni.showToast({
                  title: `${actionText}失败`,
                  icon: 'none'
                });
              }
            }, 500);
          }
        }
      });
    },
    
    deleteUser() {
      uni.showModal({
        title: '删除确认',
        content: `确定要删除用户「${this.userInfo.name}」吗？删除后无法恢复。`,
        confirmText: '删除',
        confirmColor: '#F44336',
        success: (res) => {
          if (res.confirm) {
            uni.showLoading({
              title: '删除中...'
            });
            
            setTimeout(() => {
              const result = storage.UserStorage.delete(this.userId);
              uni.hideLoading();
              
              if (result) {
                uni.showToast({
                  title: '删除成功',
                  icon: 'success'
                });
                
                setTimeout(() => {
                  navigator.navigateBack();
                }, 1500);
              } else {
                uni.showToast({
                  title: '删除失败',
                  icon: 'none'
                });
              }
            }, 500);
          }
        }
      });
    },
    
    viewRelated(type) {
      console.log('查看相关数据, type:', type);
      
      const pageMap = {
        'family': 'familyList',
        'address': 'address',
        'packageCard': 'packageCardList',
        'orders': 'orders'
      };
      
      if (pageMap[type]) {
        uni.showToast({
          title: `查看${type === 'family' ? '家庭档案' : type === 'address' ? '地址' : type === 'packageCard' ? '套餐卡' : '服务单'}`,
          icon: 'none'
        });
      }
    }
  }
};
</script>

<style scoped>
.user-detail-page {
  min-height: 100vh;
  background-color: #F8F9FA;
  padding: 30rpx;
}

/* 卡片样式 */
.card {
  background-color: #FFFFFF;
  border-radius: 16rpx;
  margin-bottom: 24rpx;
  overflow: hidden;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

/* 用户信息卡片 */
.card-header {
  display: flex;
  align-items: center;
  padding: 30rpx;
  border-bottom: 1rpx solid #F0F0F0;
}

.user-avatar {
  width: 120rpx;
  height: 120rpx;
  border-radius: 50%;
  flex-shrink: 0;
}

.user-base {
  flex: 1;
  margin-left: 24rpx;
}

.name-row {
  display: flex;
  align-items: center;
  margin-bottom: 12rpx;
}

.user-name {
  font-size: 36rpx;
  font-weight: 600;
  color: #333333;
}

.status-tag {
  padding: 4rpx 16rpx;
  border-radius: 12rpx;
  margin-left: 16rpx;
}

.status-active {
  background-color: #E8F5E9;
}

.status-inactive {
  background-color: #FFEBEE;
}

.status-text {
  font-size: 22rpx;
}

.status-active .status-text {
  color: #4CAF50;
}

.status-inactive .status-text {
  color: #F44336;
}

.user-phone {
  font-size: 28rpx;
  color: #666666;
}

.card-body {
  padding: 20rpx 30rpx;
}

.info-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16rpx 0;
  border-bottom: 1rpx solid #F0F0F0;
}

.info-row:last-child {
  border-bottom: none;
}

.info-label {
  font-size: 26rpx;
  color: #999999;
}

.info-value {
  font-size: 26rpx;
  color: #333333;
  max-width: 400rpx;
  text-align: right;
}

.info-value.openid {
  font-size: 24rpx;
  color: #666666;
}

/* 操作按钮区域 */
.action-row {
  display: flex;
  justify-content: space-around;
  padding: 30rpx 0;
}

.action-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.action-icon {
  width: 80rpx;
  height: 80rpx;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 12rpx;
}

.action-icon.edit {
  background-color: #E3F2FD;
}

.action-icon.disable {
  background-color: #FFEBEE;
}

.action-icon.enable {
  background-color: #E8F5E9;
}

.action-icon.delete {
  background-color: #FFEBEE;
}

.icon-text {
  font-size: 36rpx;
}

.action-text {
  font-size: 24rpx;
  color: #666666;
}

/* 相关数据区域 */
.section-header {
  padding: 20rpx 30rpx;
  border-bottom: 1rpx solid #E0E0E0;
}

.section-title {
  font-size: 28rpx;
  font-weight: 600;
  color: #333333;
}

.related-list {
  display: flex;
  flex-direction: column;
}

.related-item {
  display: flex;
  align-items: center;
  padding: 24rpx 30rpx;
  border-bottom: 1rpx solid #F0F0F0;
}

.related-item:last-child {
  border-bottom: none;
}

.related-icon {
  width: 64rpx;
  height: 64rpx;
  background-color: #E8F5E9;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20rpx;
  flex-shrink: 0;
}

.related-info {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.related-title {
  font-size: 28rpx;
  color: #333333;
  margin-bottom: 4rpx;
}

.related-count {
  font-size: 22rpx;
  color: #999999;
}

.related-arrow {
  font-size: 36rpx;
  color: #CCCCCC;
}
</style>
