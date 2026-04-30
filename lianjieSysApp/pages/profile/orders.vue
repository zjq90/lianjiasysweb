<template>
  <view class="orders-page">
    <!-- 顶部标签 -->
    <view class="tab-section card">
      <view
        class="tab-item"
        :class="{ active: currentTab === 'all' }"
        @click="switchTab('all')"
      >
        <text class="tab-text">全部</text>
        <text class="tab-badge" v-if="allCount > 0">{{ allCount }}</text>
      </view>
      <view
        class="tab-item"
        :class="{ active: currentTab === 'pending' }"
        @click="switchTab('pending')"
      >
        <text class="tab-text">待接单</text>
        <text class="tab-badge" v-if="pendingCount > 0">{{ pendingCount }}</text>
      </view>
      <view
        class="tab-item"
        :class="{ active: currentTab === 'accepted' }"
        @click="switchTab('accepted')"
      >
        <text class="tab-text">已接单</text>
        <text class="tab-badge" v-if="acceptedCount > 0">{{ acceptedCount }}</text>
      </view>
      <view
        class="tab-item"
        :class="{ active: currentTab === 'ongoing' }"
        @click="switchTab('ongoing')"
      >
        <text class="tab-text">进行中</text>
        <text class="tab-badge" v-if="ongoingCount > 0">{{ ongoingCount }}</text>
      </view>
      <view
        class="tab-item"
        :class="{ active: currentTab === 'completed' }"
        @click="switchTab('completed')"
      >
        <text class="tab-text">已完成</text>
        <text class="tab-badge" v-if="completedCount > 0">{{ completedCount }}</text>
      </view>
    </view>

    <!-- 服务单列表 -->
    <view class="order-list">
      <view
        class="order-item card"
        v-for="item in filteredOrders"
        :key="item.id"
        @click="viewOrderDetail(item)"
      >
        <view class="order-header">
          <text class="order-no">{{ item.orderNo }}</text>
          <text class="order-status" :class="getStatusClass(item.status)">{{ item.statusText }}</text>
        </view>
        <view class="order-body">
          <view class="info-row">
            <text class="info-label">服务项目：</text>
            <text class="info-value">{{ item.packageName }}</text>
          </view>
          <view class="info-row">
            <text class="info-label">客户姓名：</text>
            <text class="info-value">{{ item.customerName }}</text>
          </view>
          <view class="info-row">
            <text class="info-label">联系电话：</text>
            <text class="info-value">{{ item.customerPhone }}</text>
          </view>
          <view class="info-row">
            <text class="info-label">服务地址：</text>
            <text class="info-value address">{{ item.address }}</text>
          </view>
          <view class="info-row">
            <text class="info-label">预约时间：</text>
            <text class="info-value time">{{ item.appointmentTime }}</text>
          </view>
        </view>
        <view class="order-footer">
          <view class="price-section">
            <text class="price-label">¥</text>
            <text class="price-value">{{ item.price }}</text>
          </view>
          <view class="action-section">
            <button
              class="btn btn-outline btn-small"
              v-if="item.status === 1"
              @click.stop="acceptOrder(item)"
            >接单</button>
            <button
              class="btn btn-primary btn-small"
              v-if="item.status === 2"
              @click.stop="startService(item)"
            >开始服务</button>
            <button
              class="btn btn-primary btn-small"
              v-if="item.status === 3"
              @click.stop="completeOrder(item)"
            >完成服务</button>
            <button
              class="btn btn-outline btn-small"
              v-if="item.status === 4"
              @click.stop="viewOrderDetail(item)"
            >查看评价</button>
          </view>
        </view>
      </view>
    </view>

    <!-- 空状态 -->
    <view class="empty" v-if="filteredOrders.length === 0">
      <text class="empty-icon">📋</text>
      <text class="empty-text">暂无相关服务单</text>
    </view>
  </view>
</template>

<script>
import { orderList } from '@/common/data/mockData.js';

export default {
  data() {
    return {
      orderList: [],
      currentTab: 'all'
    };
  },
  onLoad() {
    this.loadData();
  },
  computed: {
    allCount() {
      return this.orderList.length;
    },
    pendingCount() {
      return this.orderList.filter(item => item.status === 1).length;
    },
    acceptedCount() {
      return this.orderList.filter(item => item.status === 2).length;
    },
    ongoingCount() {
      return this.orderList.filter(item => item.status === 3).length;
    },
    completedCount() {
      return this.orderList.filter(item => item.status === 4).length;
    },
    filteredOrders() {
      if (this.currentTab === 'all') {
        return this.orderList;
      } else if (this.currentTab === 'pending') {
        return this.orderList.filter(item => item.status === 1);
      } else if (this.currentTab === 'accepted') {
        return this.orderList.filter(item => item.status === 2);
      } else if (this.currentTab === 'ongoing') {
        return this.orderList.filter(item => item.status === 3);
      } else if (this.currentTab === 'completed') {
        return this.orderList.filter(item => item.status === 4);
      }
      return this.orderList;
    }
  },
  methods: {
    loadData() {
      this.orderList = orderList;
    },
    switchTab(tab) {
      this.currentTab = tab;
    },
    getStatusClass(status) {
      const classMap = {
        1: 'status-pending',
        2: 'status-accepted',
        3: 'status-ongoing',
        4: 'status-completed',
        5: 'status-cancelled'
      };
      return classMap[status] || '';
    },
    viewOrderDetail(item) {
      uni.showToast({
        title: `查看服务单：${item.orderNo}`,
        icon: 'none'
      });
    },
    acceptOrder(item) {
      uni.showModal({
        title: '接单确认',
        content: `确定接受「${item.packageName}」的服务单吗？`,
        success: (res) => {
          if (res.confirm) {
            item.status = 2;
            item.statusText = '已接单';
            uni.showToast({
              title: '接单成功',
              icon: 'success'
            });
          }
        }
      });
    },
    startService(item) {
      uni.showModal({
        title: '开始服务',
        content: '确定开始服务吗？',
        success: (res) => {
          if (res.confirm) {
            item.status = 3;
            item.statusText = '进行中';
            uni.showToast({
              title: '已开始服务',
              icon: 'success'
            });
          }
        }
      });
    },
    completeOrder(item) {
      uni.showModal({
        title: '完成服务',
        content: '确定完成服务吗？',
        success: (res) => {
          if (res.confirm) {
            item.status = 4;
            item.statusText = '已完成';
            uni.showToast({
              title: '服务已完成',
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
.orders-page {
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

/* 标签样式 */
.tab-section {
  display: flex;
  padding: 20rpx 30rpx;
  overflow-x: auto;
}

.tab-item {
  display: flex;
  align-items: center;
  flex-shrink: 0;
  padding: 12rpx 24rpx;
  margin-right: 16rpx;
  background-color: #F5F5F5;
  border-radius: 30rpx;
  transition: all 0.3s ease;
}

.tab-item.active {
  background-color: #E8F5E9;
}

.tab-text {
  font-size: 24rpx;
  color: #666666;
}

.tab-item.active .tab-text {
  color: #4CAF50;
  font-weight: 500;
}

.tab-badge {
  font-size: 18rpx;
  color: #FFFFFF;
  background-color: #F44336;
  border-radius: 20rpx;
  padding: 2rpx 8rpx;
  margin-left: 6rpx;
}

/* 服务单列表 */
.order-list {
  display: flex;
  flex-direction: column;
}

.order-item {
  margin-bottom: 24rpx;
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20rpx 30rpx;
  border-bottom: 1rpx solid #F0F0F0;
}

.order-no {
  font-size: 26rpx;
  color: #999999;
}

.order-status {
  font-size: 24rpx;
  padding: 4rpx 16rpx;
  border-radius: 12rpx;
}

.status-pending {
  background-color: #FFF3E0;
  color: #FF9800;
}

.status-accepted {
  background-color: #E3F2FD;
  color: #2196F3;
}

.status-ongoing {
  background-color: #E8F5E9;
  color: #4CAF50;
}

.status-completed {
  background-color: #F5F5F5;
  color: #999999;
}

.status-cancelled {
  background-color: #FFEBEE;
  color: #F44336;
}

.order-body {
  padding: 20rpx 30rpx;
  border-bottom: 1rpx solid #F0F0F0;
}

.info-row {
  display: flex;
  margin-bottom: 12rpx;
}

.info-row:last-child {
  margin-bottom: 0;
}

.info-label {
  font-size: 26rpx;
  color: #999999;
  width: 160rpx;
  flex-shrink: 0;
}

.info-value {
  font-size: 26rpx;
  color: #333333;
  flex: 1;
}

.info-value.address {
  line-height: 1.5;
}

.info-value.time {
  color: #4CAF50;
}

.order-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20rpx 30rpx;
  background-color: #FAFAFA;
}

.price-section {
  display: flex;
  align-items: baseline;
}

.price-label {
  font-size: 24rpx;
  color: #F44336;
}

.price-value {
  font-size: 32rpx;
  font-weight: 600;
  color: #F44336;
}

.action-section {
  display: flex;
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
  margin-left: 16rpx;
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

.btn-small {
  padding: 12rpx 24rpx;
  font-size: 24rpx;
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
