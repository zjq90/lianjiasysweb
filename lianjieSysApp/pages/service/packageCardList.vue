<template>
  <view class="package-card-page">
    <!-- 顶部操作栏 -->
    <view class="action-bar card">
      <view class="tab-section">
        <view
          class="tab-item"
          :class="{ active: currentTab === 'all' }"
          @tap="switchTab('all')"
        >
          <text class="tab-text">全部</text>
          <text class="tab-badge" v-if="allCount > 0">{{ allCount }}</text>
        </view>
        <view
          class="tab-item"
          :class="{ active: currentTab === 'valid' }"
          @tap="switchTab('valid')"
        >
          <text class="tab-text">有效</text>
          <text class="tab-badge" v-if="validCount > 0">{{ validCount }}</text>
        </view>
        <view
          class="tab-item"
          :class="{ active: currentTab === 'expired' }"
          @tap="switchTab('expired')"
        >
          <text class="tab-text">已过期</text>
          <text class="tab-badge" v-if="expiredCount > 0">{{ expiredCount }}</text>
        </view>
      </view>
      <view class="add-btn">
        <button class="btn btn-primary btn-small" @tap="addPackageCard">
          <text class="btn-icon">+</text>
          <text class="btn-text">新增</text>
        </button>
      </view>
    </view>

    <!-- 套餐卡列表 -->
    <view class="card-list">
      <view
        class="card-item"
        :class="{ expired: item.status === 2 }"
        v-for="item in filteredCardList"
        :key="item.id"
        @tap="viewCardDetail(item)"
      >
        <view class="card-header">
          <view class="card-title">
            <text class="card-name">{{ item.name || '未命名' }}</text>
            <text class="card-status" :class="item.status === 1 ? 'status-valid' : 'status-expired'">
              {{ item.status === 1 ? '有效' : '已过期' }}
            </text>
          </view>
          <text class="card-no">卡号：{{ item.cardNo || '未设置' }}</text>
        </view>
        <view class="card-body">
          <view class="card-info">
            <text class="info-label">套餐：</text>
            <text class="info-value">{{ item.packageName || '未设置' }}</text>
          </view>
          <view class="card-info">
            <text class="info-label">有效期至：</text>
            <text class="info-value">{{ item.expireDate || item.endDate || '未设置' }}</text>
          </view>
          <view class="card-count">
            <view class="count-item">
              <text class="count-value">{{ item.remainingTimes || 0 }}</text>
              <text class="count-label">剩余</text>
            </view>
            <view class="count-divider">/</view>
            <view class="count-item">
              <text class="count-value total">{{ item.totalTimes || 0 }}</text>
              <text class="count-label">总次数</text>
            </view>
          </view>
        </view>
        <view class="card-footer">
          <view class="progress-bar">
            <view class="progress-fill" :style="{ width: progressPercent(item) + '%' }"></view>
          </view>
          <view class="progress-info">
            <text class="progress-text">已使用 {{ item.usedTimes || 0 }} 次</text>
          </view>
        </view>
        <view class="card-actions" @tap.stop="handleAction">
          <view class="action-btn" @tap="useCard(item)" v-if="item.status === 1 && item.remainingTimes > 0">
            <text class="action-text use">使用</text>
          </view>
          <view class="action-btn" @tap="editCard(item)">
            <text class="action-text">编辑</text>
          </view>
          <view class="action-btn" @tap="deleteCard(item)">
            <text class="action-text delete">删除</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 空状态 -->
    <view class="empty" v-if="filteredCardList.length === 0">
      <text class="empty-icon">🎫</text>
      <text class="empty-text">暂无套餐卡</text>
      <button class="btn btn-primary btn-small" @tap="addPackageCard" v-if="currentTab === 'all'">新增套餐卡</button>
    </view>
  </view>
</template>

<script>
import storage from '@/common/utils/storage.js';
import navigator from '@/common/utils/navigator.js';

export default {
  data() {
    return {
      cardList: [],
      currentTab: 'all'
    };
  },
  onLoad() {
    console.log('套餐卡列表页面 onLoad');
    this.loadData();
  },
  onShow() {
    console.log('套餐卡列表页面 onShow');
    this.loadData();
  },
  computed: {
    allCount() {
      return this.cardList.length;
    },
    validCount() {
      return this.cardList.filter(item => item.status === 1).length;
    },
    expiredCount() {
      return this.cardList.filter(item => item.status === 2).length;
    },
    filteredCardList() {
      if (this.currentTab === 'all') {
        return this.cardList;
      } else if (this.currentTab === 'valid') {
        return this.cardList.filter(item => item.status === 1);
      } else {
        return this.cardList.filter(item => item.status === 2);
      }
    }
  },
  methods: {
    loadData() {
      console.log('加载套餐卡列表数据');
      this.cardList = storage.PackageCardStorage.getList();
      console.log('套餐卡列表数据:', this.cardList);
      
      // 有效卡片排前面
      this.cardList.sort((a, b) => {
        if (a.status === 1 && b.status !== 1) return -1;
        if (a.status !== 1 && b.status === 1) return 1;
        return 0;
      });
    },
    switchTab(tab) {
      console.log('切换标签:', tab);
      this.currentTab = tab;
    },
    progressPercent(item) {
      const total = item.totalTimes || 0;
      const used = item.usedTimes || 0;
      if (total === 0) return 0;
      return (used / total) * 100;
    },
    addPackageCard() {
      console.log('点击新增套餐卡');
      navigator.navigateTo({
        pageKey: 'packageCardForm'
      });
    },
    useCard(item) {
      console.log('使用套餐卡:', item);
      uni.showModal({
        title: '使用套餐卡',
        content: `确定使用「${item.name || '套餐卡'}」吗？将扣减1次，剩余${(item.remainingTimes || 0) - 1}次。`,
        success: (res) => {
          if (res.confirm) {
            uni.showLoading({
              title: '使用中...'
            });
            
            setTimeout(() => {
              try {
                const newUsedTimes = (item.usedTimes || 0) + 1;
                const newRemainingTimes = (item.remainingTimes || 0) - 1;
                
                const result = storage.PackageCardStorage.update(item.id, {
                  usedTimes: newUsedTimes,
                  remainingTimes: newRemainingTimes
                });
                
                if (result) {
                  uni.hideLoading();
                  uni.showToast({
                    title: '使用成功',
                    icon: 'success'
                  });
                  this.loadData();
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
    viewCardDetail(item) {
      console.log('点击查看套餐卡详情:', item);
      navigator.navigateTo({
        pageKey: 'packageCardDetail',
        params: { id: item.id }
      });
    },
    editCard(item) {
      console.log('点击编辑套餐卡:', item);
      navigator.navigateTo({
        pageKey: 'packageCardForm',
        params: { id: item.id }
      });
    },
    deleteCard(item) {
      console.log('点击删除套餐卡:', item);
      uni.showModal({
        title: '删除确认',
        content: `确定删除「${item.name || '套餐卡'}」吗？删除后无法恢复。`,
        confirmColor: '#FF5252',
        success: (res) => {
          if (res.confirm) {
            uni.showLoading({
              title: '删除中...'
            });
            
            setTimeout(() => {
              try {
                const result = storage.PackageCardStorage.delete(item.id);
                
                if (result) {
                  uni.hideLoading();
                  uni.showToast({
                    title: '删除成功',
                    icon: 'success'
                  });
                  this.loadData();
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
    },
    handleAction() {
      // 阻止事件冒泡
    }
  }
};
</script>

<style scoped>
.package-card-page {
  min-height: 100vh;
  background-color: #F8F9FA;
  padding: 30rpx;
}

/* 顶部操作栏 */
.card {
  background-color: #FFFFFF;
  border-radius: 16rpx;
  margin-bottom: 24rpx;
  overflow: hidden;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.action-bar {
  display: flex;
  flex-direction: column;
  padding: 24rpx 30rpx;
}

.tab-section {
  display: flex;
  margin-bottom: 20rpx;
}

.tab-item {
  display: flex;
  align-items: center;
  padding: 12rpx 28rpx;
  margin-right: 20rpx;
  background-color: #F5F5F5;
  border-radius: 30rpx;
  position: relative;
  transition: all 0.3s ease;
}

.tab-item.active {
  background-color: #E8F5E9;
}

.tab-text {
  font-size: 26rpx;
  color: #666666;
}

.tab-item.active .tab-text {
  color: #4CAF50;
  font-weight: 500;
}

.tab-badge {
  font-size: 20rpx;
  color: #FFFFFF;
  background-color: #F44336;
  border-radius: 20rpx;
  padding: 2rpx 10rpx;
  margin-left: 8rpx;
}

.add-btn {
  align-self: flex-end;
}

.btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  padding: 12rpx 24rpx;
  border-radius: 20rpx;
  font-size: 24rpx;
  font-weight: 500;
  border: none;
}

.btn-primary {
  background-color: #4CAF50;
  color: #FFFFFF;
}

.btn-small {
  padding: 12rpx 24rpx;
  font-size: 24rpx;
}

.btn-icon {
  font-size: 28rpx;
  margin-right: 6rpx;
}

/* 套餐卡列表 */
.card-list {
  display: flex;
  flex-direction: column;
}

.card-item {
  background: linear-gradient(135deg, #4CAF50 0%, #81C784 100%);
  border-radius: 16rpx;
  margin-bottom: 24rpx;
  padding: 24rpx;
  box-shadow: 0 4rpx 16rpx rgba(76, 175, 80, 0.2);
  position: relative;
  overflow: hidden;
}

.card-item::before {
  content: '';
  position: absolute;
  top: -50%;
  right: -20%;
  width: 300rpx;
  height: 300rpx;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 50%;
}

.card-item.expired {
  background: linear-gradient(135deg, #9E9E9E 0%, #BDBDBD 100%);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 20rpx;
  position: relative;
  z-index: 1;
}

.card-title {
  display: flex;
  align-items: center;
}

.card-name {
  font-size: 32rpx;
  font-weight: 600;
  color: #FFFFFF;
}

.card-status {
  font-size: 20rpx;
  padding: 4rpx 12rpx;
  border-radius: 12rpx;
  margin-left: 12rpx;
}

.status-valid {
  background-color: rgba(255, 255, 255, 0.3);
  color: #FFFFFF;
}

.status-expired {
  background-color: rgba(0, 0, 0, 0.2);
  color: #FFFFFF;
}

.card-no {
  font-size: 22rpx;
  color: rgba(255, 255, 255, 0.8);
}

.card-body {
  position: relative;
  z-index: 1;
}

.card-info {
  display: flex;
  margin-bottom: 12rpx;
}

.info-label {
  font-size: 24rpx;
  color: rgba(255, 255, 255, 0.8);
}

.info-value {
  font-size: 24rpx;
  color: #FFFFFF;
}

.card-count {
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 24rpx 0;
  padding: 20rpx;
  background-color: rgba(255, 255, 255, 0.15);
  border-radius: 12rpx;
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

.card-footer {
  position: relative;
  z-index: 1;
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
  background-color: #FFFFFF;
  border-radius: 6rpx;
  transition: width 0.3s ease;
}

.progress-info {
  display: flex;
  justify-content: flex-end;
}

.progress-text {
  font-size: 22rpx;
  color: rgba(255, 255, 255, 0.9);
}

/* 操作按钮 */
.card-actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 20rpx;
  padding-top: 20rpx;
  border-top: 1rpx solid rgba(255, 255, 255, 0.2);
  position: relative;
  z-index: 1;
}

.action-btn {
  padding: 8rpx 20rpx;
  margin-left: 16rpx;
  background-color: rgba(255, 255, 255, 0.2);
  border-radius: 20rpx;
}

.action-text {
  font-size: 24rpx;
  color: #FFFFFF;
}

.action-text.use {
  color: #FFD700;
  font-weight: 500;
}

.action-text.delete {
  color: #FF6B6B;
}

/* 空状态 */
.empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 100rpx 0;
}

.empty-icon {
  font-size: 120rpx;
  color: #999999;
  margin-bottom: 24rpx;
}

.empty-text {
  font-size: 28rpx;
  color: #999999;
}
</style>
