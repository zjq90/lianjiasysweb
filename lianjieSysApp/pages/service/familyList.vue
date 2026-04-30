<template>
  <view class="family-list-page">
    <!-- 顶部操作栏 -->
    <view class="action-bar card">
      <view class="action-title">
        <text class="title-text">家庭档案管理</text>
        <text class="title-count">共 {{ familyList.length }} 个档案</text>
      </view>
      <view class="add-btn">
        <button class="btn btn-primary btn-small" @tap="addFamily">
          <text class="btn-icon">+</text>
          <text class="btn-text">新增</text>
        </button>
      </view>
    </view>

    <!-- 家庭档案列表 -->
    <view class="family-list">
      <view
        class="family-item card"
        v-for="item in familyList"
        :key="item.id"
        @tap="viewFamilyDetail(item)"
      >
        <view class="family-header">
          <view class="family-info">
            <text class="family-name">{{ item.name || '未命名' }}</text>
            <view class="default-tag" v-if="item.isDefault === 1">
              <text class="tag-text">默认</text>
            </view>
          </view>
          <view class="family-actions" @tap.stop="handleAction">
            <text class="action-text" @tap="editFamily(item)">编辑</text>
            <text class="action-text delete" @tap="deleteFamily(item)">删除</text>
          </view>
        </view>
        <view class="family-body">
          <view class="info-row">
            <view class="info-item">
              <text class="info-icon">🏠</text>
              <text class="info-text">{{ item.roomType || '未设置' }} · {{ item.area ? item.area + '㎡' : '未设置' }}</text>
            </view>
            <view class="info-item">
              <text class="info-icon">👥</text>
              <text class="info-text">{{ item.memberCount || 0 }}位成员</text>
            </view>
          </view>
          <view class="info-row">
            <view class="info-item full">
              <text class="info-icon">📍</text>
              <text class="info-text address">{{ item.address || '未设置' }}</text>
            </view>
          </view>
          <view class="info-row tags-row" v-if="item.hasChildren === 1 || item.hasPets === 1">
            <view class="tag-item" v-if="item.hasChildren === 1">
              <text class="tag-icon">👶</text>
              <text class="tag-label">有小孩</text>
            </view>
            <view class="tag-item" v-if="item.hasPets === 1">
              <text class="tag-icon">🐕</text>
              <text class="tag-label">有宠物</text>
            </view>
          </view>
        </view>
        <view class="family-footer" v-if="item.members && item.members.length > 0">
          <view class="members-header">
            <text class="members-title">家庭成员</text>
          </view>
          <view class="members-list">
            <view
              class="member-item"
              v-for="member in item.members.slice(0, 4)"
              :key="member.id"
            >
              <view class="member-avatar">
                <text class="avatar-text">{{ member.name ? member.name.charAt(0) : '?' }}</text>
              </view>
              <view class="member-info">
                <text class="member-name">{{ member.name || '未命名' }}</text>
                <text class="member-relation">{{ member.relation || '未设置' }}</text>
              </view>
            </view>
            <view class="member-item more" v-if="item.members.length > 4">
              <view class="member-avatar">
                <text class="avatar-text">+{{ item.members.length - 4 }}</text>
              </view>
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 空状态 -->
    <view class="empty" v-if="familyList.length === 0">
      <text class="empty-icon">👨‍👩‍👧‍👦</text>
      <text class="empty-text">暂无家庭档案</text>
      <button class="btn btn-primary btn-small" @tap="addFamily">新增家庭档案</button>
    </view>
  </view>
</template>

<script>
import storage from '@/common/utils/storage.js';
import navigator from '@/common/utils/navigator.js';

export default {
  data() {
    return {
      familyList: []
    };
  },
  onLoad() {
    console.log('家庭列表页面 onLoad');
    this.loadData();
  },
  onShow() {
    console.log('家庭列表页面 onShow');
    this.loadData();
  },
  methods: {
    loadData() {
      console.log('加载家庭列表数据');
      this.familyList = storage.FamilyStorage.getList();
      console.log('家庭列表数据:', this.familyList);
      
      // 默认家庭排前面
      this.familyList.sort((a, b) => {
        if (a.isDefault === 1 && b.isDefault !== 1) return -1;
        if (a.isDefault !== 1 && b.isDefault === 1) return 1;
        return 0;
      });
    },
    
    addFamily() {
      console.log('点击新增家庭');
      navigator.navigateTo({
        pageKey: 'familyForm'
      });
    },
    
    viewFamilyDetail(item) {
      console.log('点击查看家庭详情:', item);
      navigator.navigateTo({
        pageKey: 'familyDetail',
        params: { id: item.id }
      });
    },
    
    editFamily(item) {
      console.log('点击编辑家庭:', item);
      navigator.navigateTo({
        pageKey: 'familyForm',
        params: { id: item.id }
      });
    },
    
    deleteFamily(item) {
      console.log('点击删除家庭:', item);
      uni.showModal({
        title: '删除确认',
        content: `确定删除「${item.name || '未命名'}」的家庭档案吗？删除后无法恢复。`,
        confirmColor: '#FF5252',
        success: (res) => {
          if (res.confirm) {
            uni.showLoading({
              title: '删除中...'
            });
            
            setTimeout(() => {
              try {
                const result = storage.FamilyStorage.delete(item.id);
                
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
    },
    
    handleAction() {
      // 阻止事件冒泡
    }
  }
};
</script>

<style scoped>
.family-list-page {
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
  justify-content: space-between;
  align-items: center;
  padding: 24rpx 30rpx;
}

.action-title {
  display: flex;
  flex-direction: column;
}

.title-text {
  font-size: 32rpx;
  font-weight: 600;
  color: #333333;
}

.title-count {
  font-size: 24rpx;
  color: #999999;
  margin-top: 6rpx;
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

/* 家庭档案列表 */
.family-list {
  display: flex;
  flex-direction: column;
}

.family-item {
  margin-bottom: 24rpx;
}

.family-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24rpx 30rpx;
  border-bottom: 1rpx solid #E0E0E0;
}

.family-info {
  display: flex;
  align-items: center;
}

.family-name {
  font-size: 30rpx;
  font-weight: 600;
  color: #333333;
}

.default-tag {
  background-color: #E8F5E9;
  padding: 4rpx 16rpx;
  border-radius: 12rpx;
  margin-left: 12rpx;
}

.tag-text {
  font-size: 20rpx;
  color: #4CAF50;
}

.family-actions {
  display: flex;
}

.action-text {
  font-size: 24rpx;
  color: #4CAF50;
  padding: 8rpx 16rpx;
  margin-left: 16rpx;
}

.action-text.delete {
  color: #F44336;
}

.family-body {
  padding: 20rpx 30rpx;
}

.info-row {
  display: flex;
  margin-bottom: 16rpx;
}

.info-row:last-child {
  margin-bottom: 0;
}

.info-item {
  display: flex;
  align-items: center;
  margin-right: 40rpx;
}

.info-item.full {
  flex: 1;
  margin-right: 0;
}

.info-icon {
  font-size: 28rpx;
  margin-right: 8rpx;
}

.info-text {
  font-size: 26rpx;
  color: #666666;
}

.info-text.address {
  flex: 1;
}

.tags-row {
  margin-top: 12rpx;
}

.tag-item {
  display: flex;
  align-items: center;
  background-color: #F5F5F5;
  padding: 8rpx 16rpx;
  border-radius: 20rpx;
  margin-right: 16rpx;
}

.tag-icon {
  font-size: 24rpx;
  margin-right: 6rpx;
}

.tag-label {
  font-size: 22rpx;
  color: #666666;
}

.family-footer {
  padding: 20rpx 30rpx;
  background-color: #FAFAFA;
  border-top: 1rpx solid #E0E0E0;
}

.members-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16rpx;
}

.members-title {
  font-size: 26rpx;
  font-weight: 500;
  color: #333333;
}

.members-action {
  font-size: 24rpx;
  color: #4CAF50;
}

.members-list {
  display: flex;
  flex-wrap: wrap;
}

.member-item {
  display: flex;
  align-items: center;
  width: 50%;
  padding: 12rpx 0;
}

.member-item.more {
  width: auto;
}

.member-avatar {
  width: 64rpx;
  height: 64rpx;
  background-color: #E8F5E9;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12rpx;
}

.avatar-text {
  font-size: 28rpx;
  color: #4CAF50;
  font-weight: 500;
}

.member-info {
  display: flex;
  flex-direction: column;
}

.member-name {
  font-size: 26rpx;
  color: #333333;
}

.member-relation {
  font-size: 22rpx;
  color: #999999;
  margin-top: 4rpx;
}

.family-reminder {
  padding: 16rpx 30rpx;
  border-top: 1rpx solid #E0E0E0;
}

.reminder-item {
  display: flex;
  align-items: center;
}

.reminder-icon {
  font-size: 28rpx;
  margin-right: 8rpx;
}

.reminder-text {
  font-size: 24rpx;
  color: #666666;
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
</style>
