<template>
  <view class="service-page">
    <!-- 场景化导购区域 -->
    <view class="guide-section card">
      <view class="card-header">
        <text class="header-title">🏠 场景化导购</text>
        <text class="header-more" @tap="navigateTo('packageList')">查看全部 ></text>
      </view>
      <view class="card-body">
        <view class="tag-section">
          <text class="tag-label">选择场景标签：</text>
          <view class="tag-list">
            <view
              class="tag-item"
              :class="{ active: selectedTags.includes(tag.id) }"
              v-for="tag in packageTags"
              :key="tag.id"
              @tap="toggleTag(tag.id)"
            >
              <text class="tag-icon">{{ tag.icon }}</text>
              <text class="tag-name">{{ tag.name }}</text>
            </view>
          </view>
        </view>
        <view class="recommend-btn">
          <button class="btn btn-primary btn-block" @tap="recommendPackage">智能推荐套餐</button>
        </view>
      </view>
    </view>

    <!-- 功能模块入口 -->
    <view class="module-section">
      <view class="section-header">
        <text class="section-title">📋 服务管理</text>
      </view>
      
      <!-- 套餐卡管理 -->
      <view class="module-item card" @tap="navigateTo('packageCardList')">
        <view class="module-icon">
          <text class="icon-text">🎫</text>
        </view>
        <view class="module-content">
          <text class="module-title">套餐卡管理</text>
          <text class="module-desc">新增、编辑、删除套餐卡，使用扣减次数</text>
        </view>
        <view class="module-arrow">
          <text class="arrow-icon">›</text>
        </view>
      </view>

      <!-- 家庭档案管理 -->
      <view class="module-item card" @tap="navigateTo('familyList')">
        <view class="module-icon">
          <text class="icon-text">👨‍👩‍👧‍👦</text>
        </view>
        <view class="module-content">
          <text class="module-title">家庭档案管理</text>
          <text class="module-desc">管理家庭档案和家庭成员信息</text>
        </view>
        <view class="module-arrow">
          <text class="arrow-icon">›</text>
        </view>
      </view>

      <!-- 用户管理 -->
      <view class="module-item card" @tap="navigateTo('userList')">
        <view class="module-icon">
          <text class="icon-text">👥</text>
        </view>
        <view class="module-content">
          <text class="module-title">用户管理</text>
          <text class="module-desc">管理用户信息，更新用户状态</text>
        </view>
        <view class="module-arrow">
          <text class="arrow-icon">›</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { packageTags } from '@/common/data/mockData.js';
import navigator from '@/common/utils/navigator.js';

export default {
  data() {
    return {
      packageTags: [],
      selectedTags: []
    };
  },
  onLoad() {
    this.loadData();
  },
  methods: {
    loadData() {
      console.log('服务页面加载数据...');
      this.packageTags = packageTags;
    },
    toggleTag(tagId) {
      console.log('toggleTag:', tagId);
      const index = this.selectedTags.indexOf(tagId);
      if (index > -1) {
        this.selectedTags.splice(index, 1);
      } else {
        this.selectedTags.push(tagId);
      }
      console.log('selectedTags:', this.selectedTags);
    },
    recommendPackage() {
      console.log('recommendPackage, selectedTags:', this.selectedTags);
      if (this.selectedTags.length === 0) {
        uni.showToast({
          title: '请先选择场景标签',
          icon: 'none',
          duration: 2000
        });
        return;
      }
      uni.showToast({
        title: '正在跳转...',
        icon: 'none',
        duration: 1000
      });
      navigator.navigateTo('packageList');
    },
    navigateTo(path) {
      console.log('navigateTo:', path);
      const validPaths = ['packageList', 'packageCardList', 'familyList', 'userList'];
      if (validPaths.includes(path)) {
        uni.showToast({
          title: '正在跳转...',
          icon: 'none',
          duration: 1000
        });
        navigator.navigateTo(path);
      } else {
        uni.showToast({
          title: '页面不存在',
          icon: 'none'
        });
      }
    }
  }
};
</script>

<style scoped>
.service-page {
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

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24rpx 30rpx;
  border-bottom: 1rpx solid #E0E0E0;
}

.header-title {
  font-size: 30rpx;
  font-weight: 600;
  color: #333333;
}

.header-more {
  font-size: 24rpx;
  color: #4CAF50;
}

.card-body {
  padding: 24rpx 30rpx;
}

/* 标签样式 */
.tag-section {
  margin-bottom: 24rpx;
}

.tag-label {
  display: block;
  font-size: 26rpx;
  color: #666666;
  margin-bottom: 16rpx;
}

.tag-list {
  display: flex;
  flex-wrap: wrap;
}

.tag-item {
  display: flex;
  align-items: center;
  padding: 12rpx 20rpx;
  background-color: #F5F5F5;
  border-radius: 30rpx;
  margin-right: 16rpx;
  margin-bottom: 16rpx;
  transition: all 0.3s ease;
}

.tag-item.active {
  background-color: #E8F5E9;
}

.tag-icon {
  font-size: 28rpx;
  margin-right: 8rpx;
}

.tag-name {
  font-size: 24rpx;
  color: #666666;
}

.tag-item.active .tag-name {
  color: #4CAF50;
}

/* 推荐按钮 */
.recommend-btn {
  padding: 10rpx 0;
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

/* 模块区域 */
.module-section {
  margin-top: 10rpx;
}

.section-header {
  margin-bottom: 20rpx;
}

.section-title {
  font-size: 30rpx;
  font-weight: 600;
  color: #333333;
}

/* 模块项 */
.module-item {
  display: flex;
  align-items: center;
  padding: 30rpx;
  margin-bottom: 20rpx;
}

.module-icon {
  width: 96rpx;
  height: 96rpx;
  background-color: #E8F5E9;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 24rpx;
  flex-shrink: 0;
}

.icon-text {
  font-size: 44rpx;
}

.module-content {
  flex: 1;
}

.module-title {
  display: block;
  font-size: 30rpx;
  font-weight: 500;
  color: #333333;
  margin-bottom: 8rpx;
}

.module-desc {
  display: block;
  font-size: 24rpx;
  color: #999999;
  line-height: 1.5;
}

.module-arrow {
  padding-left: 20rpx;
}

.arrow-icon {
  font-size: 40rpx;
  color: #CCCCCC;
}
</style>
