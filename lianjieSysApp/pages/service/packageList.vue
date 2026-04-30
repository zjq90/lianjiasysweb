<template>
  <view class="package-list-page">
    <!-- 搜索和筛选区域 -->
    <view class="search-section card">
      <view class="search-box">
        <input
          class="search-input"
          placeholder="搜索套餐..."
          v-model="searchKeyword"
          @confirm="searchPackage"
        />
        <view class="search-btn" @tap="searchPackage">
          <text class="search-icon">🔍</text>
        </view>
      </view>
      <view class="filter-section">
        <view class="filter-item" :class="{ active: currentFilter === 'all' }" @tap="filterPackage('all')">
          <text class="filter-text">全部</text>
        </view>
        <view
          class="filter-item"
          :class="{ active: currentFilter === tag.name }"
          v-for="tag in filterTags"
          :key="tag.id"
          @tap="filterPackage(tag.name)"
        >
          <text class="filter-text">{{ tag.name }}</text>
        </view>
      </view>
    </view>

    <!-- 套餐列表 -->
    <view class="package-list">
      <view
        class="package-item card"
        v-for="item in filteredPackages"
        :key="item.id"
        @tap="viewPackageDetail(item)"
      >
        <image :src="item.image" class="package-image" mode="aspectFill" />
        <view class="package-info">
          <view class="package-header">
            <text class="package-name">{{ item.name }}</text>
            <view class="package-tags">
              <text class="tag tag-primary" v-for="tag in item.tags" :key="tag">{{ tag }}</text>
            </view>
          </view>
          <view class="package-desc">
            <text class="desc-text">{{ item.description }}</text>
          </view>
          <view class="package-specs">
            <view class="spec-item">
              <text class="spec-label">时长：</text>
              <text class="spec-value">{{ item.duration }}</text>
            </view>
            <view class="spec-item">
              <text class="spec-label">面积：</text>
              <text class="spec-value">{{ item.area }}</text>
            </view>
            <view class="spec-item">
              <text class="spec-label">已售：</text>
              <text class="spec-value">{{ item.sales }}份</text>
            </view>
          </view>
          <view class="package-footer">
            <view class="price-section">
              <text class="price-label">¥</text>
              <text class="price-value">{{ item.price }}</text>
              <text class="price-original">原价¥{{ item.originalPrice }}</text>
            </view>
            <view class="action-btn">
              <button class="btn btn-primary btn-small" @click.stop="purchasePackage(item)">立即选购</button>
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 空状态 -->
    <view class="empty" v-if="filteredPackages.length === 0">
      <text class="empty-icon">📋</text>
      <text class="empty-text">暂无相关套餐</text>
    </view>
  </view>
</template>

<script>
import { packageList, packageTags } from '@/common/data/mockData.js';
import navigator from '@/common/utils/navigator.js';

export default {
  data() {
    return {
      packageList: [],
      filterTags: [],
      searchKeyword: '',
      currentFilter: 'all'
    };
  },
  onLoad() {
    this.loadData();
  },
  computed: {
    filteredPackages() {
      let result = this.packageList;
      
      if (this.searchKeyword) {
        result = result.filter(item => 
          item.name.includes(this.searchKeyword) || 
          item.description.includes(this.searchKeyword)
        );
      }
      
      if (this.currentFilter !== 'all') {
        result = result.filter(item => item.tags.includes(this.currentFilter));
      }
      
      return result;
    }
  },
  methods: {
    loadData() {
      this.packageList = packageList;
      this.filterTags = packageTags.slice(0, 4);
    },
    filterPackage(type) {
      this.currentFilter = type;
    },
    searchPackage() {
      uni.showToast({
        title: this.searchKeyword ? `搜索：${this.searchKeyword}` : '请输入搜索关键词',
        icon: 'none'
      });
    },
    viewPackageDetail(item) {
      console.log('点击查看套餐详情:', item);
      navigator.navigateTo({
        pageKey: 'packageDetail',
        params: { id: item.id }
      });
    },
    purchasePackage(item) {
      console.log('点击选购套餐:', item);
      uni.showModal({
        title: '确认选购',
        content: `确定选购「${item.name}」吗？价格：¥${item.price}`,
        success: (res) => {
          if (res.confirm) {
            uni.showToast({
              title: '选购成功',
              icon: 'success'
            });
            
            // 跳转到套餐详情页面
            setTimeout(() => {
              navigator.navigateTo({
                pageKey: 'packageDetail',
                params: { id: item.id }
              });
            }, 1500);
          }
        }
      });
    }
  }
};
</script>

<style scoped>
.package-list-page {
  min-height: 100vh;
  background-color: #F8F9FA;
  padding: 30rpx;
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
  overflow-x: auto;
}

.filter-item {
  flex-shrink: 0;
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

/* 套餐列表 */
.package-list {
  display: flex;
  flex-direction: column;
}

.package-item {
  display: flex;
  margin-bottom: 24rpx;
  overflow: hidden;
}

.package-image {
  width: 200rpx;
  height: 200rpx;
  flex-shrink: 0;
  border-radius: 12rpx 0 0 12rpx;
}

.package-info {
  flex: 1;
  padding: 20rpx;
  display: flex;
  flex-direction: column;
}

.package-header {
  margin-bottom: 12rpx;
}

.package-name {
  display: block;
  font-size: 28rpx;
  font-weight: 600;
  color: #333333;
  margin-bottom: 8rpx;
}

.package-tags {
  display: flex;
  flex-wrap: wrap;
}

.tag {
  display: inline-block;
  padding: 4rpx 12rpx;
  border-radius: 12rpx;
  font-size: 20rpx;
  margin-right: 8rpx;
  margin-bottom: 4rpx;
  background-color: #E8F5E9;
  color: #4CAF50;
}

.package-desc {
  margin-bottom: 12rpx;
}

.desc-text {
  font-size: 24rpx;
  color: #999999;
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.package-specs {
  display: flex;
  flex-wrap: wrap;
  margin-bottom: 16rpx;
}

.spec-item {
  display: flex;
  align-items: center;
  margin-right: 20rpx;
  margin-bottom: 8rpx;
}

.spec-label {
  font-size: 22rpx;
  color: #999999;
}

.spec-value {
  font-size: 22rpx;
  color: #666666;
}

.package-footer {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  margin-top: auto;
}

.price-section {
  display: flex;
  align-items: baseline;
}

.price-label {
  font-size: 24rpx;
  color: #F44336;
  font-weight: 500;
}

.price-value {
  font-size: 36rpx;
  color: #F44336;
  font-weight: 600;
}

.price-original {
  font-size: 22rpx;
  color: #999999;
  text-decoration: line-through;
  margin-left: 12rpx;
}

.action-btn {
  flex-shrink: 0;
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
