<template>
  <view class="family-detail-page">
    <!-- 家庭信息卡片 -->
    <view class="family-card card">
      <view class="card-header">
        <view class="header-left">
          <text class="family-name">{{ familyInfo.name || '未命名' }}</text>
          <view class="default-tag" v-if="familyInfo.isDefault === 1">
            <text class="tag-text">默认</text>
          </view>
        </view>
      </view>
      
      <view class="card-body">
        <view class="info-row">
          <view class="info-item">
            <text class="info-icon">🏠</text>
            <text class="info-label">户型</text>
            <text class="info-value">{{ familyInfo.roomType || '未设置' }}</text>
          </view>
          <view class="info-item">
            <text class="info-icon">📐</text>
            <text class="info-label">面积</text>
            <text class="info-value">{{ familyInfo.area ? familyInfo.area + '㎡' : '未设置' }}</text>
          </view>
        </view>
        
        <view class="info-row full">
          <text class="info-icon">📍</text>
          <text class="info-label">地址</text>
          <text class="info-value address">{{ familyInfo.address || '未设置' }}</text>
        </view>
        
        <view class="info-row tags" v-if="familyInfo.hasChildren === 1 || familyInfo.hasPets === 1">
          <view class="tag-item" v-if="familyInfo.hasChildren === 1">
            <text class="tag-icon">👶</text>
            <text class="tag-text">有小孩</text>
          </view>
          <view class="tag-item" v-if="familyInfo.hasPets === 1">
            <text class="tag-icon">🐕</text>
            <text class="tag-text">有宠物</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 家庭成员卡片 -->
    <view class="members-card card">
      <view class="card-header">
        <text class="card-title">👥 家庭成员 ({{ familyInfo.members ? familyInfo.members.length : 0 }})</text>
        <view class="manage-btn" @tap="editFamily">
          <text class="manage-text">管理</text>
        </view>
      </view>
      
      <view class="members-list">
        <view
          class="member-item"
          v-for="(member, index) in familyInfo.members"
          :key="member.id || index"
        >
          <view class="member-avatar">
            <text class="avatar-text">{{ member.name ? member.name.charAt(0) : '?' }}</text>
          </view>
          <view class="member-info">
            <text class="member-name">{{ member.name || '未命名' }}</text>
            <text class="member-relation">{{ member.relation || '未设置' }}</text>
          </view>
        </view>
        
        <view class="empty-members" v-if="!familyInfo.members || familyInfo.members.length === 0">
          <text class="empty-icon">👤</text>
          <text class="empty-text">暂无家庭成员</text>
          <text class="empty-hint">点击"管理"按钮添加家庭成员</text>
        </view>
      </view>
    </view>

    <!-- 统计信息 -->
    <view class="stats-card card">
      <view class="card-header">
        <text class="card-title">📊 相关统计</text>
      </view>
      
      <view class="stats-list">
        <view class="stat-item">
          <view class="stat-icon">📋</view>
          <view class="stat-info">
            <text class="stat-value">{{ relatedOrders.length }}</text>
            <text class="stat-label">服务单</text>
          </view>
        </view>
        <view class="stat-item">
          <view class="stat-icon">💳</view>
          <view class="stat-info">
            <text class="stat-value">{{ relatedPackages.length }}</text>
            <text class="stat-label">套餐卡</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 操作按钮 -->
    <view class="action-section card">
      <view class="action-row">
        <view class="action-item" @tap="editFamily">
          <text class="action-icon">✏️</text>
          <text class="action-text">编辑</text>
        </view>
        <view class="action-divider"></view>
        <view class="action-item" @tap="setDefault" v-if="familyInfo.isDefault !== 1">
          <text class="action-icon">⭐</text>
          <text class="action-text">设为默认</text>
        </view>
        <view class="action-divider" v-if="familyInfo.isDefault !== 1"></view>
        <view class="action-item" @tap="deleteFamily">
          <text class="action-icon">🗑️</text>
          <text class="action-text">删除</text>
        </view>
      </view>
    </view>

    <!-- 底部操作按钮 -->
    <view class="bottom-action-section safe-area-bottom">
      <button class="btn btn-primary btn-block" @tap="editFamily">
        编辑家庭信息
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
      familyId: null,
      familyInfo: {},
      relatedOrders: [],
      relatedPackages: []
    };
  },
  onLoad(options) {
    console.log('家庭详情页面 onLoad, options:', options);
    
    if (options && options.id) {
      this.familyId = options.id;
      this.loadFamilyInfo();
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
    if (this.familyId) {
      this.loadFamilyInfo();
    }
  },
  methods: {
    loadFamilyInfo() {
      console.log('加载家庭信息, familyId:', this.familyId);
      const familyInfo = storage.FamilyStorage.getById(this.familyId);
      console.log('获取到的家庭信息:', familyInfo);
      
      if (familyInfo) {
        this.familyInfo = familyInfo;
        // 加载相关数据
        this.loadRelatedData();
      } else {
        uni.showToast({
          title: '家庭不存在',
          icon: 'none'
        });
        setTimeout(() => {
          navigator.navigateBack();
        }, 1500);
      }
    },
    
    loadRelatedData() {
      // 模拟加载相关数据
      this.relatedOrders = [];
      this.relatedPackages = [];
    },
    
    editFamily() {
      console.log('点击编辑家庭');
      navigator.navigateTo({
        pageKey: 'familyForm',
        params: { id: this.familyId }
      });
    },
    
    setDefault() {
      console.log('设为默认家庭');
      uni.showModal({
        title: '确认设为默认',
        content: '确定要将此家庭设为默认家庭吗？',
        success: (res) => {
          if (res.confirm) {
            uni.showLoading({
              title: '设置中...'
            });
            
            setTimeout(() => {
              try {
                // 先将所有家庭的默认状态设为0
                const allFamilies = storage.FamilyStorage.getList();
                allFamilies.forEach(family => {
                  if (family.id !== this.familyId && family.isDefault === 1) {
                    storage.FamilyStorage.update(family.id, { isDefault: 0 });
                  }
                });
                
                // 设置当前家庭为默认
                const result = storage.FamilyStorage.update(this.familyId, { isDefault: 1 });
                
                if (result) {
                  uni.hideLoading();
                  uni.showToast({
                    title: '设置成功',
                    icon: 'success'
                  });
                  this.loadFamilyInfo();
                } else {
                  uni.hideLoading();
                  uni.showToast({
                    title: '设置失败',
                    icon: 'none'
                  });
                }
              } catch (error) {
                uni.hideLoading();
                console.error('设置默认家庭失败:', error);
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
    
    deleteFamily() {
      console.log('点击删除家庭');
      uni.showModal({
        title: '确认删除',
        content: '确定要删除此家庭档案吗？删除后无法恢复。',
        confirmColor: '#FF5252',
        success: (res) => {
          if (res.confirm) {
            uni.showLoading({
              title: '删除中...'
            });
            
            setTimeout(() => {
              try {
                const result = storage.FamilyStorage.delete(this.familyId);
                
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
                console.error('删除家庭失败:', error);
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
.family-detail-page {
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

/* 家庭卡片 */
.family-card {
  background: linear-gradient(135deg, #4CAF50 0%, #66BB6A 100%);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24rpx 30rpx;
}

.header-left {
  display: flex;
  align-items: center;
}

.family-name {
  font-size: 36rpx;
  font-weight: 600;
  color: #FFFFFF;
}

.default-tag {
  background-color: rgba(255, 255, 255, 0.3);
  padding: 6rpx 16rpx;
  border-radius: 20rpx;
  margin-left: 12rpx;
}

.tag-text {
  font-size: 22rpx;
  color: #FFFFFF;
}

.card-body {
  padding: 20rpx 30rpx;
}

.info-row {
  display: flex;
  align-items: center;
  margin-bottom: 16rpx;
}

.info-row:last-child {
  margin-bottom: 0;
}

.info-row.full {
  align-items: flex-start;
}

.info-item {
  display: flex;
  align-items: center;
  margin-right: 40rpx;
}

.info-icon {
  font-size: 28rpx;
  margin-right: 8rpx;
}

.info-label {
  font-size: 26rpx;
  color: rgba(255, 255, 255, 0.8);
  margin-right: 8rpx;
}

.info-value {
  font-size: 26rpx;
  color: #FFFFFF;
  font-weight: 500;
}

.info-value.address {
  flex: 1;
}

.info-row.tags {
  margin-top: 20rpx;
}

.tag-item {
  display: flex;
  align-items: center;
  background-color: rgba(255, 255, 255, 0.2);
  padding: 8rpx 16rpx;
  border-radius: 20rpx;
  margin-right: 16rpx;
}

.tag-icon {
  font-size: 24rpx;
  margin-right: 6rpx;
}

.tag-text {
  font-size: 22rpx;
  color: #FFFFFF;
}

/* 成员卡片 */
.members-card .card-header {
  border-bottom: 1rpx solid #E0E0E0;
}

.card-title {
  font-size: 30rpx;
  font-weight: 600;
  color: #333333;
}

.manage-btn {
  color: #4CAF50;
}

.manage-text {
  font-size: 26rpx;
}

.members-list {
  padding: 20rpx 30rpx;
}

.member-item {
  display: flex;
  align-items: center;
  padding: 16rpx 0;
  border-bottom: 1rpx solid #F0F0F0;
}

.member-item:last-child {
  border-bottom: none;
}

.member-avatar {
  width: 72rpx;
  height: 72rpx;
  background-color: #E8F5E9;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 16rpx;
}

.avatar-text {
  font-size: 32rpx;
  color: #4CAF50;
  font-weight: 500;
}

.member-info {
  display: flex;
  flex-direction: column;
}

.member-name {
  font-size: 28rpx;
  color: #333333;
  font-weight: 500;
}

.member-relation {
  font-size: 24rpx;
  color: #999999;
  margin-top: 4rpx;
}

.empty-members {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 40rpx 0;
}

.empty-icon {
  font-size: 80rpx;
  margin-bottom: 16rpx;
}

.empty-text {
  font-size: 28rpx;
  color: #999999;
  margin-bottom: 8rpx;
}

.empty-hint {
  font-size: 24rpx;
  color: #CCCCCC;
}

/* 统计卡片 */
.stats-list {
  display: flex;
  padding: 20rpx 30rpx;
}

.stat-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20rpx 0;
}

.stat-icon {
  font-size: 48rpx;
  margin-bottom: 12rpx;
}

.stat-info {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.stat-value {
  font-size: 36rpx;
  font-weight: 600;
  color: #333333;
}

.stat-label {
  font-size: 24rpx;
  color: #999999;
  margin-top: 4rpx;
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
