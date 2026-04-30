<template>
  <view class="home-page">
    <!-- 轮播图 -->
    <view class="banner-section">
      <swiper
        class="banner-swiper"
        :indicator-dots="true"
        :autoplay="true"
        :interval="3000"
        :duration="500"
        indicator-color="rgba(255,255,255,0.5)"
        indicator-active-color="#4CAF50"
        @change="onBannerChange"
      >
        <swiper-item
          v-for="item in bannerList"
          :key="item.id"
          class="banner-item"
        >
          <view class="banner-click-area" @click="onBannerClick(item)">
            <image :src="item.image" class="banner-image" mode="aspectFill" />
            <view class="banner-info">
              <text class="banner-title">{{ item.title }}</text>
              <text class="banner-desc">{{ item.desc }}</text>
            </view>
          </view>
        </swiper-item>
      </swiper>
    </view>

    <!-- 快捷入口 -->
    <view class="quick-entry card">
      <view class="grid">
        <view class="grid-item" @tap="navigateTo('service')">
          <view class="grid-item-icon">
            <text class="icon-text">🧹</text>
          </view>
          <text class="grid-item-text">套餐推荐</text>
        </view>
        <view class="grid-item" @tap="navigateTo('packageCard')">
          <view class="grid-item-icon">
            <text class="icon-text">🎫</text>
          </view>
          <text class="grid-item-text">套餐卡</text>
        </view>
        <view class="grid-item" @tap="navigateTo('family')">
          <view class="grid-item-icon">
            <text class="icon-text">👨‍👩‍👧‍👦</text>
          </view>
          <text class="grid-item-text">家庭档案</text>
        </view>
        <view class="grid-item" @tap="navigateTo('user')">
          <view class="grid-item-icon">
            <text class="icon-text">👥</text>
          </view>
          <text class="grid-item-text">用户管理</text>
        </view>
      </view>
    </view>

    <!-- 案例展示 -->
    <view class="case-section">
      <view class="section-header">
        <text class="section-title">案例展示</text>
        <text class="section-more" @tap="viewMoreCases">查看更多 ></text>
      </view>
      <view class="case-list">
        <view
          class="case-item"
          v-for="item in caseList"
          :key="item.id"
          @tap="viewCaseDetail(item)"
        >
          <image :src="item.image" class="case-image" mode="aspectFill" />
          <view class="case-info">
            <text class="case-title">{{ item.title }}</text>
            <text class="case-desc">{{ item.desc }}</text>
            <view class="case-tags">
              <text
                class="tag tag-primary"
                v-for="tag in item.tags"
                :key="tag"
              >{{ tag }}</text>
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 测试区域 -->
    <view class="test-section card">
      <view class="test-header">
        <text class="test-title">🔧 功能测试</text>
      </view>
      <view class="test-buttons">
        <button class="test-btn" @tap="testToast">测试 Toast</button>
        <button class="test-btn" @tap="testModal">测试 Modal</button>
        <button class="test-btn" @tap="testNavigation">测试跳转</button>
      </view>
    </view>
  </view>
</template>

<script>
import { bannerList, caseList } from '@/common/data/mockData.js';
import navigator from '@/common/utils/navigator.js';

export default {
  data() {
    return {
      bannerList: [],
      caseList: [],
      currentBannerIndex: 0
    };
  },
  onLoad() {
    console.log('首页 onLoad');
    this.loadData();
  },
  onShow() {
    console.log('首页 onShow');
  },
  onReady() {
    console.log('首页 onReady - 页面渲染完成');
  },
  methods: {
    loadData() {
      console.log('首页开始加载数据...');
      try {
        this.bannerList = bannerList;
        this.caseList = caseList;
        console.log('数据加载成功，bannerList:', this.bannerList.length, 'caseList:', this.caseList.length);
      } catch (error) {
        console.error('数据加载失败:', error);
        uni.showToast({
          title: '数据加载失败',
          icon: 'none',
          duration: 2000
        });
      }
    },
    onBannerChange(e) {
      this.currentBannerIndex = e.detail.current;
      console.log('轮播图切换到索引:', this.currentBannerIndex);
    },
    onBannerClick(item) {
      console.log('点击广告:', item);
      uni.showToast({
        title: `点击了：${item.title}`,
        icon: 'none',
        duration: 2000
      });
    },
    navigateTo(type) {
      console.log('navigateTo called with type:', type);
      
      const paths = {
        'service': 'packageList',
        'packageCard': 'packageCardList',
        'family': 'familyList',
        'user': 'userList'
      };
      
      const pageKey = paths[type];
      console.log('pageKey:', pageKey);
      
      if (pageKey) {
        uni.showToast({
          title: '正在跳转...',
          icon: 'none',
          duration: 1000
        });
        
        setTimeout(() => {
          console.log('执行 navigator.navigateTo:', pageKey);
          navigator.navigateTo(pageKey);
        }, 500);
      } else {
        console.log('未知的页面类型:', type);
        uni.showToast({
          title: '页面不存在',
          icon: 'none'
        });
      }
    },
    viewMoreCases() {
      console.log('viewMoreCases');
      uni.showToast({
        title: '加载更多案例...',
        icon: 'none'
      });
    },
    viewCaseDetail(item) {
      console.log('viewCaseDetail:', item);
      uni.showModal({
        title: '案例详情',
        content: `案例：${item.title}\n描述：${item.desc}`,
        showCancel: false,
        confirmText: '知道了'
      });
    },
    // 测试方法
    testToast() {
      console.log('测试 Toast');
      uni.showToast({
        title: 'Toast 测试成功！',
        icon: 'success',
        duration: 2000
      });
    },
    testModal() {
      console.log('测试 Modal');
      uni.showModal({
        title: 'Modal 测试',
        content: '这是一个测试弹窗，点击确认或取消查看效果。',
        success: (res) => {
          if (res.confirm) {
            uni.showToast({
              title: '点击了确认',
              icon: 'none'
            });
          } else if (res.cancel) {
            uni.showToast({
              title: '点击了取消',
              icon: 'none'
            });
          }
        }
      });
    },
    testNavigation() {
      console.log('测试导航跳转');
      uni.showActionSheet({
        itemList: ['跳转到服务页面', '跳转到个人页面', '跳转到套餐列表'],
        success: (res) => {
          console.log('选择了索引:', res.tapIndex);
          if (res.tapIndex === 0) {
            uni.switchTab({
              url: '/pages/service/service',
              success: () => console.log('跳转到服务页面成功'),
              fail: (err) => console.error('跳转失败:', err)
            });
          } else if (res.tapIndex === 1) {
            uni.switchTab({
              url: '/pages/profile/profile',
              success: () => console.log('跳转到个人页面成功'),
              fail: (err) => console.error('跳转失败:', err)
            });
          } else if (res.tapIndex === 2) {
            uni.navigateTo({
              url: '/pages/service/packageList',
              success: () => console.log('跳转到套餐列表成功'),
              fail: (err) => {
                console.error('跳转失败:', err);
                // 尝试使用 redirectTo
                uni.redirectTo({
                  url: '/pages/service/packageList',
                  success: () => console.log('redirectTo 成功'),
                  fail: (err2) => console.error('redirectTo 也失败:', err2)
                });
              }
            });
          }
        }
      });
    }
  }
};
</script>

<style scoped>
.home-page {
  min-height: 100vh;
  background-color: #F8F9FA;
  padding-bottom: 30rpx;
}

/* 轮播图样式 */
.banner-section {
  padding: 30rpx;
}

.banner-swiper {
  height: 360rpx;
  border-radius: 20rpx;
  overflow: hidden;
  box-shadow: 0 4rpx 20rpx rgba(76, 175, 80, 0.15);
}

.banner-item {
  position: relative;
  width: 100%;
  height: 100%;
}

.banner-click-area {
  width: 100%;
  height: 100%;
  position: relative;
}

.banner-image {
  width: 100%;
  height: 100%;
}

.banner-info {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 30rpx;
  background: linear-gradient(transparent, rgba(0, 0, 0, 0.6));
  color: #FFFFFF;
}

.banner-title {
  display: block;
  font-size: 32rpx;
  font-weight: 600;
  margin-bottom: 8rpx;
}

.banner-desc {
  display: block;
  font-size: 24rpx;
  opacity: 0.9;
}

/* 快捷入口样式 */
.quick-entry {
  margin: 0 30rpx 30rpx;
}

.card {
  background-color: #FFFFFF;
  border-radius: 16rpx;
  overflow: hidden;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.grid {
  padding: 20rpx 0;
}

.grid-item {
  width: 25%;
  padding: 20rpx 10rpx;
  box-sizing: border-box;
  text-align: center;
  position: relative;
  z-index: 10;
}

.grid-item-icon {
  width: 96rpx;
  height: 96rpx;
  background-color: #E8F5E9;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 16rpx;
  position: relative;
  z-index: 10;
}

.icon-text {
  font-size: 44rpx;
}

.grid-item-text {
  font-size: 24rpx;
  color: #333333;
}

/* 案例展示样式 */
.case-section {
  padding: 0 30rpx;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24rpx;
}

.section-title {
  font-size: 32rpx;
  font-weight: 600;
  color: #333333;
}

.section-more {
  font-size: 24rpx;
  color: #4CAF50;
  position: relative;
  z-index: 10;
}

.case-list {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
}

.case-item {
  width: 48%;
  background-color: #FFFFFF;
  border-radius: 16rpx;
  overflow: hidden;
  margin-bottom: 24rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
  position: relative;
  z-index: 10;
}

.case-image {
  width: 100%;
  height: 240rpx;
}

.case-info {
  padding: 20rpx;
}

.case-title {
  display: block;
  font-size: 26rpx;
  font-weight: 500;
  color: #333333;
  margin-bottom: 12rpx;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.case-desc {
  display: block;
  font-size: 22rpx;
  color: #999999;
  margin-bottom: 16rpx;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.case-tags {
  display: flex;
  flex-wrap: wrap;
}

.tag {
  display: inline-block;
  padding: 4rpx 12rpx;
  border-radius: 12rpx;
  font-size: 20rpx;
  margin-right: 8rpx;
  margin-bottom: 8rpx;
  background-color: #E8F5E9;
  color: #4CAF50;
}

/* 测试区域样式 */
.test-section {
  margin: 30rpx;
}

.test-header {
  padding: 20rpx 30rpx;
  border-bottom: 1rpx solid #E0E0E0;
}

.test-title {
  font-size: 28rpx;
  font-weight: 600;
  color: #333333;
}

.test-buttons {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-around;
  padding: 20rpx;
}

.test-btn {
  padding: 16rpx 32rpx;
  margin: 10rpx;
  background-color: #4CAF50;
  color: #FFFFFF;
  border-radius: 8rpx;
  font-size: 26rpx;
  position: relative;
  z-index: 100;
}
</style>
