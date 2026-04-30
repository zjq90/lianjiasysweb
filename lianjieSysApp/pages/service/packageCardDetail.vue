<template>
  <view class="package-card-detail-page">
    <!-- 套餐卡头部信息 -->
    <view class="card-header-section">
      <view class="card-card">
        <view class="card-info">
          <view class="card-title-row">
            <text class="card-name">{{ cardInfo.name || '未命名' }}</text>
            <view class="status-tag" :class="cardInfo.status === 1 ? 'status-valid' : 'status-expired'">
              <text class="status-text">{{ cardInfo.status === 1 ? '有效' : '已过期' }}</text>
            </view>
          </view>
          <text class="card-no">卡号：{{ cardInfo.cardNo || '未设置' }}</text>
          
          <view class="count-section">
            <view class="count-item">
              <text class="count-value remaining">{{ cardInfo.remainingTimes || 0 }}</text>
              <text class="count-label">剩余</text>
            </view>
            <view class="count-divider">/</view>
            <view class="count-item">
              <text class="count-value total">{{ cardInfo.totalTimes || 0 }}</text>
              <text class="count-label">总次数</text>
            </view>
          </view>
          
          <view class="progress-section">
            <view class="progress-bar">
              <view class="progress-fill" :style="{ width: progressPercent + '%' }"></view>
            </view>
            <text class="progress-text">已使用 {{ cardInfo.usedTimes || 0 }} 次</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 基础信息卡片 -->
    <view class="info-card card">
      <view class="card-header">
        <text class="card-title">📋 基础信息</text>
      </view>
      
      <view class="card-body">
        <view class="info-row">
          <text class="info-label">关联套餐</text>
          <text class="info-value">{{ cardInfo.packageName || '未设置' }}</text>
        </view>
        
        <view class="info-row">
          <text class="info-label">关联用户</text>
          <text class="info-value">{{ cardInfo.userName || '未设置' }}</text>
        </view>
        
        <view class="info-row">
          <text class="info-label">开始日期</text>
          <text class="info-value">{{ cardInfo.startDate || '未设置' }}</text>
        </view>
        
        <view class="info-row">
          <text class="info-label">到期日期</text>
          <text class="info-value" :class="{ expired: isExpired }">{{ cardInfo.expireDate || cardInfo.endDate || '未设置' }}</text>
        </view>
      </view>
    </view>

    <!-- 使用记录卡片 -->
    <view class="info-card card">
      <view class="card-header">
        <text class="card-title">📝 使用记录</text>
      </view>
      
      <view class="card-body">
        <view class="record-list" v-if="useRecords.length > 0">
          <view class="record-item" v-for="(record, index) in useRecords" :key="index">
            <view class="record-info">
              <text class="record-title">使用套餐卡</text>
              <text class="record-time">{{ record.time }}</text>
            </view>
            <view class="record-detail">
              <text class="record-content">-1次</text>
            </view>
          </view>
        </view>
        
        <view class="empty-records" v-else>
          <text class="empty-icon">📭</text>
          <text class="empty-text">暂无使用记录</text>
        </view>
      </view>
    </view>

    <!-- 备注信息 -->
    <view class="info-card card" v-if="cardInfo.remark">
      <view class="card-header">
        <text class="card-title">💬 备注信息</text>
      </view>
      
      <view class="card-body">
        <text class="remark-text">{{ cardInfo.remark }}</text>
      </view>
    </view>

    <!-- 操作按钮 -->
    <view class="action-section card">
      <view class="action-row">
        <view class="action-item" @tap="useCard" v-if="cardInfo.status === 1 && cardInfo.remainingTimes > 0">
          <text class="action-icon">🎯</text>
          <text class="action-text use">使用</text>
        </view>
        <view class="action-divider" v-if="cardInfo.status === 1 && cardInfo.remainingTimes > 0"></view>
        <view class="action-item" @tap="editCard">
          <text class="action-icon">✏️</text>
          <text class="action-text">编辑</text>
        </view>
        <view class="action-divider"></view>
        <view class="action-item" @tap="deleteCard">
          <text class="action-icon">🗑️</text>
          <text class="action-text delete">删除</text>
        </view>
      </view>
    </view>

    <!-- 底部操作按钮 -->
    <view class="bottom-action-section safe-area-bottom">
      <button class="btn btn-primary btn-block" @tap="useCard" v-if="cardInfo.status === 1 && cardInfo.remainingTimes > 0">
        使用套餐卡
      </button>
      <button class="btn btn-primary btn-block" @tap="editCard" v-else>
        编辑套餐卡
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
      cardId: null,
      cardInfo: {},
      useRecords: []
    };
  },
  computed: {
    progressPercent() {
      const total = this.cardInfo.totalTimes || 0;
      const used = this.cardInfo.usedTimes || 0;
      if (total === 0) return 0;
      return (used / total) * 100;
    },
    isExpired() {
      return this.cardInfo.status === 2;
    }
  },
  onLoad(options) {
    console.log('套餐卡详情页面 onLoad, options:', options);
    
    if (options && options.id) {
      this.cardId = options.id;
      this.loadCardInfo();
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
    if (this.cardId) {
      this.loadCardInfo();
    }
  },
  methods: {
    loadCardInfo() {
      console.log('加载套餐卡信息, cardId:', this.cardId);
      const cardInfo = storage.PackageCardStorage.getById(this.cardId);
      console.log('获取到的套餐卡信息:', cardInfo);
      
      if (cardInfo) {
        this.cardInfo = cardInfo;
        // 生成模拟使用记录
        this.generateUseRecords();
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
    
    generateUseRecords() {
      const usedTimes = this.cardInfo.usedTimes || 0;
      if (usedTimes > 0) {
        this.useRecords = [];
        const now = new Date();
        for (let i = 1; i <= Math.min(usedTimes, 5); i++) {
          const date = new Date(now.getTime() - i * 24 * 60 * 60 * 1000);
          this.useRecords.push({
            time: `${date.getMonth() + 1}月${date.getDate()}日 ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`
          });
        }
      }
    },
    
    useCard() {
      console.log('使用套餐卡');
      uni.showModal({
        title: '使用套餐卡',
        content: `确定使用「${this.cardInfo.name || '套餐卡'}」吗？将扣减1次，剩余${(this.cardInfo.remainingTimes || 0) - 1}次。`,
        success: (res) => {
          if (res.confirm) {
            uni.showLoading({
              title: '使用中...'
            });
            
            setTimeout(() => {
              try {
                const newUsedTimes = (this.cardInfo.usedTimes || 0) + 1;
                const newRemainingTimes = (this.cardInfo.remainingTimes || 0) - 1;
                
                const result = storage.PackageCardStorage.update(this.cardId, {
                  usedTimes: newUsedTimes,
                  remainingTimes: newRemainingTimes
                });
                
                if (result) {
                  uni.hideLoading();
                  uni.showToast({
                    title: '使用成功',
                    icon: 'success'
                  });
                  this.loadCardInfo();
                } else {
                  uni.hideLoading();
                  uni.showToast({
                    title: '使用失败',
                    icon: 'none'
                  });
                }
              } catch (error) {
                uni.hideLoading();
                console.error('使用套餐卡失败:', error);
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
    
    editCard() {
      console.log('点击编辑套餐卡');
      navigator.navigateTo({
        pageKey: 'packageCardForm',
        params: { id: this.cardId }
      });
    },
    
    deleteCard() {
      console.log('点击删除套餐卡');
      uni.showModal({
        title: '确认删除',
        content: '确定要删除此套餐卡吗？删除后无法恢复。',
        confirmColor: '#FF5252',
        success: (res) => {
          if (res.confirm) {
            uni.showLoading({
              title: '删除中...'
            });
            
            setTimeout(() => {
              try {
                const result = storage.PackageCardStorage.delete(this.cardId);
                
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
                console.error('删除套餐卡失败:', error);
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
.package-card-detail-page {
  min-height: 100vh;
  background-color: #F8F9FA;
  padding: 30rpx;
  padding-bottom: 180rpx;
}

/* 套餐卡头部 */
.card-header-section {
  margin-bottom: 24rpx;
}

.card-card {
  background: linear-gradient(135deg, #4CAF50 0%, #81C784 100%);
  border-radius: 16rpx;
  padding: 30rpx;
  box-shadow: 0 4rpx 16rpx rgba(76, 175, 80, 0.2);
}

.card-title-row {
  display: flex;
  align-items: center;
  margin-bottom: 12rpx;
}

.card-name {
  font-size: 36rpx;
  font-weight: 600;
  color: #FFFFFF;
}

.status-tag {
  padding: 6rpx 16rpx;
  border-radius: 20rpx;
  margin-left: 12rpx;
}

.status-valid {
  background-color: rgba(255, 255, 255, 0.3);
}

.status-expired {
  background-color: rgba(0, 0, 0, 0.2);
}

.status-text {
  font-size: 22rpx;
  color: #FFFFFF;
}

.card-no {
  font-size: 24rpx;
  color: rgba(255, 255, 255, 0.8);
  margin-bottom: 24rpx;
}

/* 次数区域 */
.count-section {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 24rpx 0;
  background-color: rgba(255, 255, 255, 0.15);
  border-radius: 12rpx;
  margin-bottom: 20rpx;
}

.count-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.count-value {
  font-size: 56rpx;
  font-weight: 700;
  color: #FFFFFF;
}

.count-value.remaining {
  color: #FFD700;
}

.count-value.total {
  color: rgba(255, 255, 255, 0.7);
}

.count-label {
  font-size: 22rpx;
  color: rgba(255, 255, 255, 0.8);
  margin-top: 4rpx;
}

.count-divider {
  font-size: 48rpx;
  color: rgba(255, 255, 255, 0.5);
  margin: 0 40rpx;
}

/* 进度条 */
.progress-section {
  display: flex;
  flex-direction: column;
}

.progress-bar {
  height: 12rpx;
  background-color: rgba(255, 255, 255, 0.3);
  border-radius: 6rpx;
  overflow: hidden;
  margin-bottom: 12rpx;
}

.progress-fill {
  height: 100%;
  background-color: #FFD700;
  border-radius: 6rpx;
  transition: width 0.3s ease;
}

.progress-text {
  font-size: 22rpx;
  color: rgba(255, 255, 255, 0.9);
  text-align: right;
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
  padding: 20rpx 30rpx;
  border-bottom: 1rpx solid #E0E0E0;
}

.card-title {
  font-size: 30rpx;
  font-weight: 600;
  color: #333333;
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
  font-size: 28rpx;
  color: #999999;
}

.info-value {
  font-size: 28rpx;
  color: #333333;
  font-weight: 500;
}

.info-value.expired {
  color: #F44336;
}

/* 使用记录 */
.record-list {
  display: flex;
  flex-direction: column;
}

.record-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16rpx 0;
  border-bottom: 1rpx solid #F0F0F0;
}

.record-item:last-child {
  border-bottom: none;
}

.record-info {
  display: flex;
  flex-direction: column;
}

.record-title {
  font-size: 28rpx;
  color: #333333;
}

.record-time {
  font-size: 22rpx;
  color: #999999;
  margin-top: 4rpx;
}

.record-detail {
  display: flex;
  align-items: center;
}

.record-content {
  font-size: 28rpx;
  color: #F44336;
  font-weight: 500;
}

.empty-records {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 30rpx 0;
}

.empty-icon {
  font-size: 60rpx;
  margin-bottom: 12rpx;
}

.empty-text {
  font-size: 26rpx;
  color: #999999;
}

/* 备注 */
.remark-text {
  font-size: 28rpx;
  color: #666666;
  line-height: 1.6;
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

.action-text.use {
  color: #FFD700;
}

.action-text.delete {
  color: #F44336;
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
