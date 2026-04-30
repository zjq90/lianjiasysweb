<template>
  <view class="user-list-page">
    <!-- 搜索区域 -->
    <view class="search-section card">
      <view class="search-box">
        <input
          class="search-input"
          placeholder="搜索用户名或手机号..."
          v-model="searchKeyword"
          @confirm="searchUser"
        />
        <view class="search-btn" @tap="searchUser">
          <text class="search-icon">🔍</text>
        </view>
      </view>
      <view class="filter-section">
        <view
          class="filter-item"
          :class="{ active: currentFilter === 'all' }"
          @tap="filterUser('all')"
        >
          <text class="filter-text">全部</text>
        </view>
        <view
          class="filter-item"
          :class="{ active: currentFilter === 'active' }"
          @tap="filterUser('active')"
        >
          <text class="filter-text">正常</text>
        </view>
        <view
          class="filter-item"
          :class="{ active: currentFilter === 'inactive' }"
          @tap="filterUser('inactive')"
        >
          <text class="filter-text">禁用</text>
        </view>
      </view>
    </view>

    <!-- 顶部统计 -->
    <view class="stats-section">
      <view class="stat-item card" @tap="filterUser('all')">
        <text class="stat-value">{{ userList.length }}</text>
        <text class="stat-label">总用户数</text>
      </view>
      <view class="stat-item card" @tap="filterUser('active')">
        <text class="stat-value">{{ activeCount }}</text>
        <text class="stat-label">正常用户</text>
      </view>
      <view class="stat-item card" @tap="filterUser('inactive')">
        <text class="stat-value">{{ inactiveCount }}</text>
        <text class="stat-label">禁用用户</text>
      </view>
    </view>

    <!-- 用户列表 -->
    <view class="user-list">
      <view
        class="user-item card"
        v-for="item in filteredUsers"
        :key="item.id"
      >
        <view class="user-header">
          <view class="user-info" @tap="viewUserDetail(item)">
            <image :src="item.avatar" class="user-avatar" mode="aspectFill" />
            <view class="user-base">
              <view class="user-name-row">
                <text class="user-name">{{ item.name }}</text>
                <view
                  class="status-tag"
                  :class="item.status === 1 ? 'status-active' : 'status-inactive'"
                >
                  <text class="status-text">{{ item.status === 1 ? '正常' : '禁用' }}</text>
                </view>
              </view>
              <text class="user-phone">{{ item.phone }}</text>
              <text class="user-openid">ID: {{ item.id }}</text>
            </view>
          </view>
          <view class="user-actions">
            <view class="action-btn" @tap="editUser(item)">
              <text class="action-text">编辑</text>
            </view>
            <view class="action-btn" @tap="toggleStatus(item)">
              <text class="action-text" :class="item.status === 1 ? 'text-danger' : 'text-success'">
                {{ item.status === 1 ? '禁用' : '启用' }}
              </text>
            </view>
            <view class="action-btn" @tap="deleteUser(item)">
              <text class="action-text delete">删除</text>
            </view>
          </view>
        </view>
        <view class="user-footer">
          <view class="footer-info">
            <text class="footer-label">创建时间：</text>
            <text class="footer-value">{{ item.createTime }}</text>
          </view>
          <view class="footer-actions">
            <text class="footer-action" @tap="viewUserDetail(item)">查看详情</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 空状态 -->
    <view class="empty" v-if="filteredUsers.length === 0">
      <text class="empty-icon">👥</text>
      <text class="empty-text">暂无用户</text>
      <button class="btn btn-primary btn-small" @tap="addUser">新增用户</button>
    </view>

    <!-- 底部新增按钮 -->
    <view class="fixed-bottom safe-area-bottom" v-if="filteredUsers.length > 0">
      <button class="btn btn-primary btn-block" @tap="addUser">
        <text class="btn-icon">+</text>
        <text class="btn-text">新增用户</text>
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
      userList: [],
      searchKeyword: '',
      currentFilter: 'all'
    };
  },
  onLoad() {
    console.log('用户列表页面 onLoad');
    this.loadData();
  },
  onShow() {
    console.log('用户列表页面 onShow - 刷新数据');
    this.loadData();
  },
  computed: {
    activeCount() {
      return this.userList.filter(item => item.status === 1).length;
    },
    inactiveCount() {
      return this.userList.filter(item => item.status === 2).length;
    },
    filteredUsers() {
      let result = this.userList;
      
      if (this.searchKeyword && this.searchKeyword.trim() !== '') {
        const keyword = this.searchKeyword.trim();
        result = result.filter(item => 
          (item.name && item.name.includes(keyword)) || 
          (item.phone && item.phone.includes(keyword))
        );
      }
      
      if (this.currentFilter === 'active') {
        result = result.filter(item => item.status === 1);
      } else if (this.currentFilter === 'inactive') {
        result = result.filter(item => item.status === 2);
      }
      
      return result;
    }
  },
  methods: {
    loadData() {
      console.log('加载用户列表数据...');
      try {
        this.userList = storage.UserStorage.getList();
        console.log('加载成功，用户数量:', this.userList.length);
      } catch (error) {
        console.error('加载用户列表失败:', error);
        uni.showToast({
          title: '数据加载失败',
          icon: 'none'
        });
      }
    },
    
    filterUser(type) {
      console.log('筛选用户, type:', type);
      this.currentFilter = type;
    },
    
    searchUser() {
      console.log('搜索用户, keyword:', this.searchKeyword);
      if (!this.searchKeyword || this.searchKeyword.trim() === '') {
        uni.showToast({
          title: '请输入搜索关键词',
          icon: 'none'
        });
        return;
      }
      uni.showToast({
        title: `搜索：${this.searchKeyword}`,
        icon: 'none'
      });
    },
    
    addUser() {
      console.log('点击新增用户');
      navigator.navigateTo('userForm');
    },
    
    viewUserDetail(item) {
      console.log('查看用户详情:', item);
      navigator.navigateTo('userDetail', { id: item.id });
    },
    
    editUser(item) {
      console.log('编辑用户:', item);
      navigator.navigateTo('userForm', { id: item.id });
    },
    
    toggleStatus(item) {
      const currentStatus = item.status;
      const newStatus = currentStatus === 1 ? 2 : 1;
      const actionText = currentStatus === 1 ? '禁用' : '启用';
      
      console.log(`切换用户状态: ${actionText}`, item);
      
      uni.showModal({
        title: '状态确认',
        content: `确定要${actionText}用户「${item.name}」吗？`,
        success: (res) => {
          if (res.confirm) {
            console.log('确认操作');
            uni.showLoading({
              title: `${actionText}中...`
            });
            
            setTimeout(() => {
              try {
                const result = storage.UserStorage.updateStatus(item.id, newStatus);
                uni.hideLoading();
                
                if (result) {
                  console.log('更新成功');
                  // 更新本地数据
                  const index = this.userList.findIndex(u => u.id === item.id);
                  if (index > -1) {
                    this.userList[index].status = newStatus;
                  }
                  uni.showToast({
                    title: `${actionText}成功`,
                    icon: 'success'
                  });
                } else {
                  console.error('更新失败');
                  uni.showToast({
                    title: `${actionText}失败`,
                    icon: 'none'
                  });
                }
              } catch (error) {
                uni.hideLoading();
                console.error('更新状态失败:', error);
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
    
    deleteUser(item) {
      console.log('删除用户:', item);
      
      uni.showModal({
        title: '删除确认',
        content: `确定删除用户「${item.name}」吗？删除后无法恢复。`,
        confirmText: '删除',
        confirmColor: '#F44336',
        success: (res) => {
          if (res.confirm) {
            console.log('确认删除');
            uni.showLoading({
              title: '删除中...'
            });
            
            setTimeout(() => {
              try {
                const result = storage.UserStorage.delete(item.id);
                uni.hideLoading();
                
                if (result) {
                  console.log('删除成功');
                  // 更新本地数据
                  const index = this.userList.findIndex(u => u.id === item.id);
                  if (index > -1) {
                    this.userList.splice(index, 1);
                  }
                  uni.showToast({
                    title: '删除成功',
                    icon: 'success'
                  });
                } else {
                  console.error('删除失败');
                  uni.showToast({
                    title: '删除失败',
                    icon: 'none'
                  });
                }
              } catch (error) {
                uni.hideLoading();
                console.error('删除用户失败:', error);
                uni.showToast({
                  title: '删除失败',
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
.user-list-page {
  min-height: 100vh;
  background-color: #F8F9FA;
  padding: 30rpx;
  padding-bottom: 120rpx;
}

/* 搜索区域 */
.card {
  background-color: #FFFFFF;
  border-radius: 16rpx;
  margin-bottom: 24rpx;
  overflow: hidden;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.search-box {
  display: flex;
  align-items: center;
  padding: 24rpx 30rpx;
  border-bottom: 1rpx solid #E0E0E0;
}

.search-input {
  flex: 1;
  height: 72rpx;
  background-color: #F5F5F5;
  border-radius: 36rpx;
  padding: 0 24rpx;
  font-size: 26rpx;
}

.search-btn {
  width: 72rpx;
  height: 72rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-left: 16rpx;
  background-color: #E8F5E9;
  border-radius: 50%;
}

.search-icon {
  font-size: 32rpx;
}

.filter-section {
  display: flex;
  padding: 20rpx 30rpx;
}

.filter-item {
  padding: 12rpx 28rpx;
  margin-right: 20rpx;
  background-color: #F5F5F5;
  border-radius: 30rpx;
  transition: all 0.3s ease;
}

.filter-item.active {
  background-color: #E8F5E9;
}

.filter-text {
  font-size: 24rpx;
  color: #666666;
}

.filter-item.active .filter-text {
  color: #4CAF50;
  font-weight: 500;
}

/* 统计区域 */
.stats-section {
  display: flex;
  justify-content: space-between;
  margin-bottom: 24rpx;
}

.stat-item {
  width: 31%;
  padding: 24rpx 16rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.stat-value {
  font-size: 40rpx;
  font-weight: 600;
  color: #4CAF50;
}

.stat-label {
  font-size: 24rpx;
  color: #999999;
  margin-top: 8rpx;
}

/* 用户列表 */
.user-list {
  display: flex;
  flex-direction: column;
}

.user-item {
  margin-bottom: 24rpx;
}

.user-header {
  padding: 24rpx 30rpx;
  border-bottom: 1rpx solid #E0E0E0;
}

.user-info {
  display: flex;
  align-items: flex-start;
  margin-bottom: 20rpx;
}

.user-avatar {
  width: 96rpx;
  height: 96rpx;
  border-radius: 50%;
  margin-right: 20rpx;
  flex-shrink: 0;
}

.user-base {
  flex: 1;
}

.user-name-row {
  display: flex;
  align-items: center;
  margin-bottom: 8rpx;
}

.user-name {
  font-size: 30rpx;
  font-weight: 600;
  color: #333333;
}

.status-tag {
  padding: 4rpx 16rpx;
  border-radius: 12rpx;
  margin-left: 12rpx;
}

.status-active {
  background-color: #E8F5E9;
}

.status-inactive {
  background-color: #FFEBEE;
}

.status-text {
  font-size: 20rpx;
}

.status-active .status-text {
  color: #4CAF50;
}

.status-inactive .status-text {
  color: #F44336;
}

.user-phone {
  display: block;
  font-size: 26rpx;
  color: #666666;
  margin-bottom: 6rpx;
}

.user-openid {
  display: block;
  font-size: 22rpx;
  color: #999999;
}

.user-actions {
  display: flex;
  justify-content: flex-end;
  padding-top: 16rpx;
  border-top: 1rpx dashed #E0E0E0;
}

.action-btn {
  padding: 8rpx 20rpx;
  margin-left: 16rpx;
  background-color: #F5F5F5;
  border-radius: 20rpx;
}

.action-text {
  font-size: 24rpx;
  color: #4CAF50;
}

.action-text.delete {
  color: #F44336;
}

.text-danger {
  color: #F44336;
}

.text-success {
  color: #4CAF50;
}

.user-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16rpx 30rpx;
  background-color: #FAFAFA;
}

.footer-info {
  display: flex;
  align-items: center;
}

.footer-label {
  font-size: 22rpx;
  color: #999999;
}

.footer-value {
  font-size: 22rpx;
  color: #666666;
}

.footer-action {
  font-size: 24rpx;
  color: #4CAF50;
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
