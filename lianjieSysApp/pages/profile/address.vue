<template>
  <view class="address-page">
    <!-- 地址列表 -->
    <view class="address-list">
      <view
        class="address-item card"
        v-for="item in addressList"
        :key="item.id"
        @tap="viewDetail(item)"
      >
        <view class="address-header">
          <view class="address-info">
            <view class="name-phone">
              <text class="name">{{ item.name || '无' }}</text>
              <text class="phone">{{ item.phone || '无' }}</text>
            </view>
            <view class="address-tags" v-if="item.isDefault === 1">
              <text class="default-tag">默认</text>
            </view>
          </view>
        </view>
        <view class="address-body">
          <text class="address-text">
            {{ item.province || '' }}{{ item.city || '' }}{{ item.district || '' }}{{ item.address || '无' }}
          </text>
        </view>
        <view class="address-footer" @tap.stop="handleAction">
          <view class="footer-actions">
            <view class="action-item" @tap="setDefault(item)" v-if="item.isDefault !== 1">
              <text class="action-text">设为默认</text>
            </view>
            <view class="action-item" v-else>
              <text class="action-text disabled">默认地址</text>
            </view>
            <view class="action-item" @tap="editAddress(item)">
              <text class="action-text">编辑</text>
            </view>
            <view class="action-item" @tap="deleteAddress(item)">
              <text class="action-text delete">删除</text>
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 空状态 -->
    <view class="empty" v-if="addressList.length === 0">
      <text class="empty-icon">📍</text>
      <text class="empty-text">暂无收货地址</text>
      <button class="btn btn-primary btn-small" @tap="addAddress">新增地址</button>
    </view>

    <!-- 底部新增按钮 -->
    <view class="fixed-bottom safe-area-bottom" v-if="addressList.length > 0">
      <button class="btn btn-primary btn-block" @tap="addAddress">
        <text class="btn-icon">+</text>
        <text class="btn-text">新增地址</text>
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
      addressList: []
    };
  },
  onLoad() {
    console.log('地址列表页面 onLoad');
    this.loadData();
  },
  onShow() {
    console.log('地址列表页面 onShow');
    this.loadData();
  },
  methods: {
    loadData() {
      console.log('加载地址列表数据');
      this.addressList = storage.AddressStorage.getList();
      console.log('地址列表数据:', this.addressList);
      
      // 默认地址排前面
      this.addressList.sort((a, b) => {
        if (a.isDefault === 1 && b.isDefault !== 1) return -1;
        if (a.isDefault !== 1 && b.isDefault === 1) return 1;
        return 0;
      });
    },
    
    viewDetail(item) {
      console.log('点击查看地址详情:', item);
      navigator.navigateTo({
        pageKey: 'addressDetail',
        params: { id: item.id }
      });
    },
    
    addAddress() {
      console.log('点击新增地址');
      navigator.navigateTo({
        pageKey: 'addressForm'
      });
    },
    
    editAddress(item) {
      console.log('点击编辑地址:', item);
      navigator.navigateTo({
        pageKey: 'addressForm',
        params: { id: item.id }
      });
    },
    
    deleteAddress(item) {
      console.log('点击删除地址:', item);
      uni.showModal({
        title: '删除确认',
        content: `确定删除「${item.name || '未命名'}」的地址吗？`,
        confirmColor: '#FF5252',
        success: (res) => {
          if (res.confirm) {
            uni.showLoading({
              title: '删除中...'
            });
            
            setTimeout(() => {
              try {
                const result = storage.AddressStorage.delete(item.id);
                
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
    },
    
    setDefault(item) {
      console.log('设为默认地址:', item);
      uni.showLoading({
        title: '设置中...'
      });
      
      setTimeout(() => {
        try {
          // 先将所有地址的默认状态设为0
          const allAddresses = storage.AddressStorage.getList();
          allAddresses.forEach(addr => {
            if (addr.id !== item.id && addr.isDefault === 1) {
              storage.AddressStorage.update(addr.id, { isDefault: 0 });
            }
          });
          
          // 设置当前地址为默认
          const result = storage.AddressStorage.update(item.id, { isDefault: 1 });
          
          if (result) {
            uni.hideLoading();
            uni.showToast({
              title: '已设为默认地址',
              icon: 'success'
            });
            this.loadData();
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
    },
    
    handleAction() {
      // 阻止事件冒泡
    }
  }
};
</script>

<style scoped>
.address-page {
  min-height: 100vh;
  background-color: #F8F9FA;
  padding: 30rpx;
  padding-bottom: 120rpx;
}

/* 卡片样式 */
.card {
  background-color: #FFFFFF;
  border-radius: 16rpx;
  margin-bottom: 24rpx;
  overflow: hidden;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

/* 地址列表 */
.address-list {
  display: flex;
  flex-direction: column;
}

.address-item {
  margin-bottom: 24rpx;
}

.address-header {
  padding: 24rpx 30rpx;
  border-bottom: 1rpx solid #F0F0F0;
}

.address-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.name-phone {
  display: flex;
  align-items: center;
}

.name {
  font-size: 30rpx;
  font-weight: 600;
  color: #333333;
}

.phone {
  font-size: 28rpx;
  color: #666666;
  margin-left: 20rpx;
}

.address-tags {
  display: flex;
}

.default-tag {
  padding: 4rpx 16rpx;
  background-color: #E8F5E9;
  color: #4CAF50;
  font-size: 20rpx;
  border-radius: 12rpx;
}

.address-body {
  padding: 20rpx 30rpx;
  border-bottom: 1rpx solid #F0F0F0;
}

.address-text {
  font-size: 26rpx;
  color: #666666;
  line-height: 1.6;
}

.address-footer {
  padding: 16rpx 30rpx;
  background-color: #FAFAFA;
}

.footer-actions {
  display: flex;
  justify-content: flex-end;
}

.action-item {
  padding: 8rpx 20rpx;
  margin-left: 24rpx;
}

.action-text {
  font-size: 24rpx;
  color: #4CAF50;
}

.action-text.delete {
  color: #F44336;
}

.action-text.disabled {
  color: #999999;
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
  margin-bottom: 30rpx;
}

/* 底部固定按钮 */
.fixed-bottom {
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

.btn-small {
  padding: 12rpx 24rpx;
  font-size: 24rpx;
}

.btn-icon {
  font-size: 32rpx;
  margin-right: 8rpx;
}

/* 安全区域 */
.safe-area-bottom {
  padding-bottom: calc(20rpx + constant(safe-area-inset-bottom));
  padding-bottom: calc(20rpx + env(safe-area-inset-bottom));
}
</style>
