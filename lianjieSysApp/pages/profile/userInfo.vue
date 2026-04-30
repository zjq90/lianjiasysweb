<template>
  <view class="user-info-page">
    <!-- 头像区域 -->
    <view class="avatar-section card">
      <view class="avatar-item">
        <text class="label">头像</text>
        <view class="avatar-content" @click="changeAvatar">
          <image :src="userInfo.avatar" class="user-avatar" mode="aspectFill" />
          <text class="arrow">›</text>
        </view>
      </view>
    </view>

    <!-- 基本信息 -->
    <view class="info-section card">
      <view class="info-item" @click="editField('name')">
        <text class="label">姓名</text>
        <view class="value-content">
          <text class="value">{{ userInfo.name }}</text>
          <text class="arrow">›</text>
        </view>
      </view>
      <view class="info-item" @click="editField('phone')">
        <text class="label">手机号</text>
        <view class="value-content">
          <text class="value">{{ userInfo.phone }}</text>
          <text class="arrow">›</text>
        </view>
      </view>
      <view class="info-item" @click="editField('gender')">
        <text class="label">性别</text>
        <view class="value-content">
          <text class="value">{{ genderText }}</text>
          <text class="arrow">›</text>
        </view>
      </view>
      <view class="info-item">
        <text class="label">年龄</text>
        <view class="value-content">
          <text class="value">{{ userInfo.age }}岁</text>
        </view>
      </view>
      <view class="info-item">
        <text class="label">工作年限</text>
        <view class="value-content">
          <text class="value">{{ userInfo.workYears }}年</text>
        </view>
      </view>
    </view>

    <!-- 服务数据 -->
    <view class="stats-section card">
      <view class="section-header">
        <text class="header-title">服务数据</text>
      </view>
      <view class="stats-grid">
        <view class="stat-item">
          <text class="stat-value">{{ userInfo.serviceCount }}</text>
          <text class="stat-label">总服务单数</text>
        </view>
        <view class="stat-item">
          <text class="stat-value">{{ userInfo.goodRate }}%</text>
          <text class="stat-label">好评率</text>
        </view>
        <view class="stat-item">
          <text class="stat-value">{{ userInfo.status === 1 ? '在线' : '离线' }}</text>
          <text class="stat-label">当前状态</text>
        </view>
      </view>
    </view>

    <!-- 账号信息 -->
    <view class="account-section card">
      <view class="section-header">
        <text class="header-title">账号信息</text>
      </view>
      <view class="info-item">
        <text class="label">OpenID</text>
        <view class="value-content">
          <text class="value openid">{{ userInfo.openId }}</text>
        </view>
      </view>
      <view class="info-item">
        <text class="label">注册时间</text>
        <view class="value-content">
          <text class="value">{{ formatTime(userInfo.createTime) }}</text>
        </view>
      </view>
    </view>

    <!-- 保存按钮 -->
    <view class="action-section">
      <button class="btn btn-primary btn-block" @click="saveInfo">保存修改</button>
    </view>
  </view>
</template>

<script>
import { currentUser } from '@/common/data/mockData.js';

export default {
  data() {
    return {
      userInfo: {}
    };
  },
  onLoad() {
    this.loadData();
  },
  computed: {
    genderText() {
      const genderMap = { 1: '男', 2: '女', 0: '未知' };
      return genderMap[this.userInfo.gender] || '未知';
    }
  },
  methods: {
    loadData() {
      this.userInfo = { ...currentUser };
    },
    changeAvatar() {
      uni.showActionSheet({
        itemList: ['拍照', '从相册选择'],
        success: (res) => {
          uni.showToast({
            title: res.tapIndex === 0 ? '拍照功能' : '相册选择功能',
            icon: 'none'
          });
        }
      });
    },
    editField(field) {
      const fieldNames = {
        name: '姓名',
        phone: '手机号',
        gender: '性别'
      };
      
      if (field === 'gender') {
        uni.showActionSheet({
          itemList: ['男', '女'],
          success: (res) => {
            this.userInfo.gender = res.tapIndex + 1;
            uni.showToast({
              title: `已选择：${res.tapIndex === 0 ? '男' : '女'}`,
              icon: 'success'
            });
          }
        });
      } else {
        uni.showToast({
          title: `编辑${fieldNames[field]}功能`,
          icon: 'none'
        });
      }
    },
    formatTime(time) {
      if (!time) return '';
      return time.split(' ')[0];
    },
    saveInfo() {
      uni.showModal({
        title: '保存确认',
        content: '确定要保存修改的信息吗？',
        success: (res) => {
          if (res.confirm) {
            uni.showToast({
              title: '保存成功',
              icon: 'success'
            });
          }
        }
      });
    }
  }
};
</script>

<style scoped>
.user-info-page {
  min-height: 100vh;
  background-color: #F8F9FA;
  padding: 30rpx;
  padding-bottom: 60rpx;
}

/* 卡片样式 */
.card {
  background-color: #FFFFFF;
  border-radius: 16rpx;
  margin-bottom: 24rpx;
  overflow: hidden;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

/* 头像区域 */
.avatar-section {
  padding: 24rpx 30rpx;
}

.avatar-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.label {
  font-size: 28rpx;
  color: #333333;
}

.avatar-content {
  display: flex;
  align-items: center;
}

.user-avatar {
  width: 100rpx;
  height: 100rpx;
  border-radius: 50%;
}

.arrow {
  font-size: 36rpx;
  color: #CCCCCC;
  margin-left: 16rpx;
}

/* 信息项 */
.info-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24rpx 30rpx;
  border-bottom: 1rpx solid #F0F0F0;
}

.info-item:last-child {
  border-bottom: none;
}

.value-content {
  display: flex;
  align-items: center;
}

.value {
  font-size: 28rpx;
  color: #666666;
}

.value.openid {
  font-size: 24rpx;
  max-width: 400rpx;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

/* 统计区域 */
.stats-section {
  padding: 24rpx 0;
}

.section-header {
  padding: 0 30rpx 16rpx;
  border-bottom: 1rpx solid #F0F0F0;
}

.header-title {
  font-size: 28rpx;
  font-weight: 600;
  color: #333333;
}

.stats-grid {
  display: flex;
  padding: 24rpx 0;
}

.stat-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.stat-value {
  font-size: 36rpx;
  font-weight: 600;
  color: #4CAF50;
}

.stat-label {
  font-size: 24rpx;
  color: #999999;
  margin-top: 8rpx;
}

/* 操作按钮 */
.action-section {
  padding-top: 20rpx;
}

.btn {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20rpx 40rpx;
  border-radius: 50rpx;
  font-size: 28rpx;
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
</style>
