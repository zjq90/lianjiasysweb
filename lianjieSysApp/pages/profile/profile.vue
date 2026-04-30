<template>
  <view class="profile-page">
    <!-- 个人信息头部 -->
    <view class="profile-header">
      <view class="header-bg"></view>
      <view class="user-info" @tap="navigateTo('userInfo')">
        <image :src="userInfo.avatar" class="user-avatar" mode="aspectFill" />
        <view class="user-base">
          <text class="user-name">{{ userInfo.name }}</text>
          <view class="user-tags">
            <text class="tag tag-primary">工作{{ userInfo.workYears }}年</text>
            <text class="tag tag-success">好评率{{ userInfo.goodRate }}%</text>
          </view>
          <text class="user-phone">{{ userInfo.phone }}</text>
        </view>
        <view class="user-arrow">
          <text class="arrow-icon">›</text>
        </view>
      </view>
      
      <!-- 数据统计 -->
      <view class="stats-section">
        <view class="stat-item">
          <text class="stat-value">{{ userInfo.serviceCount }}</text>
          <text class="stat-label">服务单数</text>
        </view>
        <view class="stat-divider"></view>
        <view class="stat-item">
          <text class="stat-value">{{ userInfo.goodRate }}%</text>
          <text class="stat-label">好评率</text>
        </view>
        <view class="stat-divider"></view>
        <view class="stat-item">
          <text class="stat-value">{{ userInfo.workYears }}</text>
          <text class="stat-label">工作年限</text>
        </view>
      </view>
    </view>

    <!-- 功能菜单列表 -->
    <view class="menu-section">
      <view class="menu-group">
        <view class="menu-item" @tap="navigateTo('orders')">
          <view class="menu-icon">
            <text class="icon-text">📋</text>
          </view>
          <view class="menu-content">
            <text class="menu-title">服务单</text>
          </view>
          <view class="menu-arrow">
            <text class="arrow-icon">›</text>
          </view>
        </view>
      </view>

      <view class="menu-group">
        <view class="menu-item" @tap="navigateTo('address')">
          <view class="menu-icon">
            <text class="icon-text">📍</text>
          </view>
          <view class="menu-content">
            <text class="menu-title">地址管理</text>
            <text class="menu-badge">{{ addressCount }}个地址</text>
          </view>
          <view class="menu-arrow">
            <text class="arrow-icon">›</text>
          </view>
        </view>
      </view>

      <view class="menu-group">
        <view class="menu-item" @tap="navigateTo('packageCard')">
          <view class="menu-icon">
            <text class="icon-text">🎫</text>
          </view>
          <view class="menu-content">
            <text class="menu-title">我的套餐卡</text>
            <text class="menu-badge">{{ packageCardCount }}张</text>
          </view>
          <view class="menu-arrow">
            <text class="arrow-icon">›</text>
          </view>
        </view>
      </view>

      <view class="menu-group">
        <view class="menu-item" @tap="navigateTo('family')">
          <view class="menu-icon">
            <text class="icon-text">👨‍👩‍👧‍👦</text>
          </view>
          <view class="menu-content">
            <text class="menu-title">家庭档案</text>
            <text class="menu-badge">{{ familyCount }}个</text>
          </view>
          <view class="menu-arrow">
            <text class="arrow-icon">›</text>
          </view>
        </view>
      </view>

      <view class="menu-group">
        <view class="menu-item" @tap="navigateTo('about')">
          <view class="menu-icon">
            <text class="icon-text">ℹ️</text>
          </view>
          <view class="menu-content">
            <text class="menu-title">关于</text>
          </view>
          <view class="menu-arrow">
            <text class="arrow-icon">›</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 底部退出登录 -->
    <view class="logout-section">
      <button class="logout-btn" @tap="logout">退出登录</button>
    </view>
  </view>
</template>

<script>
import { currentUser } from '@/common/data/mockData.js';
import storage from '@/common/utils/storage.js';
import navigator from '@/common/utils/navigator.js';

export default {
  data() {
    return {
      userInfo: {},
      addressCount: 0,
      packageCardCount: 0,
      familyCount: 0
    };
  },
  onLoad() {
    this.loadData();
  },
  onShow() {
    this.loadData();
  },
  methods: {
    loadData() {
      console.log('个人页面加载数据...');
      this.userInfo = currentUser;
      
      // 使用 storage 获取数据
      this.addressCount = storage.AddressStorage.getList().length;
      this.packageCardCount = storage.PackageCardStorage.getList().length;
      this.familyCount = storage.FamilyStorage.getList().length;
      
      console.log('数据加载完成 - 地址数:', this.addressCount, '套餐卡数:', this.packageCardCount, '家庭数:', this.familyCount);
    },
    navigateTo(page) {
      console.log('navigateTo:', page);
      const pathMap = {
        'userInfo': 'userInfo',
        'orders': 'orders',
        'address': 'address',
        'packageCard': 'packageCardList',
        'family': 'familyList',
        'about': 'about'
      };
      
      const pageKey = pathMap[page];
      console.log('pageKey:', pageKey);
      
      if (pageKey) {
        console.log('执行 navigator.navigateTo:', pageKey);
        navigator.navigateTo(pageKey);
      } else {
        uni.showToast({
          title: '页面不存在',
          icon: 'none'
        });
      }
    },
    logout() {
      console.log('logout');
      uni.showModal({
        title: '退出登录',
        content: '确定要退出登录吗？',
        success: (res) => {
          if (res.confirm) {
            uni.showToast({
              title: '已退出登录',
              icon: 'success',
              duration: 2000
            });
          }
        }
      });
    }
  }
};
</script>

<style scoped>
.profile-page {
  min-height: 100vh;
  background-color: #F8F9FA;
  padding-bottom: 40rpx;
}

/* 个人信息头部 */
.profile-header {
  position: relative;
  padding: 40rpx 30rpx 30rpx;
  background: linear-gradient(135deg, #4CAF50 0%, #81C784 100%);
}

.header-bg {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  opacity: 0.1;
}

.user-info {
  display: flex;
  align-items: center;
  position: relative;
  z-index: 1;
}

.user-avatar {
  width: 120rpx;
  height: 120rpx;
  border-radius: 50%;
  border: 4rpx solid rgba(255, 255, 255, 0.5);
  flex-shrink: 0;
}

.user-base {
  flex: 1;
  margin-left: 24rpx;
}

.user-name {
  display: block;
  font-size: 36rpx;
  font-weight: 600;
  color: #FFFFFF;
  margin-bottom: 12rpx;
}

.user-tags {
  display: flex;
  margin-bottom: 12rpx;
}

.tag {
  display: inline-block;
  padding: 4rpx 12rpx;
  border-radius: 12rpx;
  font-size: 20rpx;
  margin-right: 12rpx;
}

.tag-primary {
  background-color: rgba(255, 255, 255, 0.3);
  color: #FFFFFF;
}

.tag-success {
  background-color: rgba(255, 215, 0, 0.3);
  color: #FFD700;
}

.user-phone {
  font-size: 26rpx;
  color: rgba(255, 255, 255, 0.9);
}

.user-arrow {
  padding-left: 20rpx;
}

.arrow-icon {
  font-size: 44rpx;
  color: rgba(255, 255, 255, 0.8);
}

/* 数据统计 */
.stats-section {
  display: flex;
  justify-content: space-around;
  align-items: center;
  margin-top: 30rpx;
  padding: 24rpx 0;
  background-color: rgba(255, 255, 255, 0.15);
  border-radius: 16rpx;
  position: relative;
  z-index: 1;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.stat-value {
  font-size: 40rpx;
  font-weight: 700;
  color: #FFFFFF;
}

.stat-label {
  font-size: 22rpx;
  color: rgba(255, 255, 255, 0.9);
  margin-top: 6rpx;
}

.stat-divider {
  width: 1rpx;
  height: 60rpx;
  background-color: rgba(255, 255, 255, 0.3);
}

/* 功能菜单 */
.menu-section {
  padding: 20rpx 0;
}

.menu-group {
  background-color: #FFFFFF;
  margin-bottom: 20rpx;
}

.menu-item {
  display: flex;
  align-items: center;
  padding: 28rpx 30rpx;
  border-bottom: 1rpx solid #F0F0F0;
}

.menu-item:last-child {
  border-bottom: none;
}

.menu-icon {
  width: 72rpx;
  height: 72rpx;
  background-color: #E8F5E9;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20rpx;
  flex-shrink: 0;
}

.icon-text {
  font-size: 36rpx;
}

.menu-content {
  flex: 1;
  display: flex;
  align-items: center;
}

.menu-title {
  font-size: 30rpx;
  color: #333333;
}

.menu-badge {
  font-size: 24rpx;
  color: #999999;
  margin-left: 16rpx;
}

.menu-arrow {
  padding-left: 20rpx;
}

.menu-arrow .arrow-icon {
  color: #CCCCCC;
}

/* 退出登录 */
.logout-section {
  padding: 40rpx 30rpx;
}

.logout-btn {
  width: 100%;
  height: 88rpx;
  background-color: #FFFFFF;
  color: #F44336;
  font-size: 30rpx;
  border-radius: 44rpx;
  border: none;
}

.logout-btn:active {
  background-color: #FFEBEE;
}
</style>
