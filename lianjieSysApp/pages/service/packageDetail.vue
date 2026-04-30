<template>
  <view class="package-detail-page">
    <!-- 套餐图片 -->
    <view class="image-section">
      <image :src="packageInfo.image" class="package-image" mode="aspectFill" />
      <view class="back-btn" @tap="goBack">
        <text class="back-icon">‹</text>
      </view>
    </view>

    <!-- 基础信息 -->
    <view class="info-card card">
      <view class="card-body">
        <view class="name-section">
          <text class="package-name">{{ packageInfo.name || '套餐详情' }}</text>
          <view class="package-tags">
            <text class="tag" v-for="(tag, index) in packageInfo.tags" :key="index">{{ tag }}</text>
          </view>
        </view>
        
        <view class="price-section">
          <text class="price-label">¥</text>
          <text class="price-value">{{ packageInfo.price || 0 }}</text>
          <text class="price-original">原价 ¥{{ packageInfo.originalPrice || 0 }}</text>
        </view>
        
        <view class="sales-section">
          <text class="sales-text">已售 {{ packageInfo.sales || 0 }} 份</text>
        </view>
      </view>
    </view>

    <!-- 服务规格 -->
    <view class="spec-card card">
      <view class="card-header">
        <text class="card-title">📋 服务规格</text>
      </view>
      <view class="card-body">
        <view class="spec-list">
          <view class="spec-item">
            <text class="spec-icon">⏱️</text>
            <view class="spec-info">
              <text class="spec-label">服务时长</text>
              <text class="spec-value">{{ packageInfo.duration || '未设置' }}</text>
            </view>
          </view>
          <view class="spec-item">
            <text class="spec-icon">📐</text>
            <view class="spec-info">
              <text class="spec-label">适用面积</text>
              <text class="spec-value">{{ packageInfo.area || '未设置' }}</text>
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 套餐描述 -->
    <view class="desc-card card">
      <view class="card-header">
        <text class="card-title">📝 套餐介绍</text>
      </view>
      <view class="card-body">
        <text class="desc-text">{{ packageInfo.description || '暂无介绍' }}</text>
      </view>
    </view>

    <!-- 服务内容 -->
    <view class="content-card card">
      <view class="card-header">
        <text class="card-title">✅ 服务内容</text>
      </view>
      <view class="card-body">
        <view class="content-list">
          <view class="content-item" v-for="(item, index) in serviceContents" :key="index">
            <view class="check-icon">✓</view>
            <text class="content-text">{{ item }}</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 注意事项 -->
    <view class="notice-card card">
      <view class="card-header">
        <text class="card-title">⚠️ 注意事项</text>
      </view>
      <view class="card-body">
        <view class="notice-list">
          <view class="notice-item">
            <text class="notice-dot">•</text>
            <text class="notice-text">请提前预约服务时间</text>
          </view>
          <view class="notice-item">
            <text class="notice-dot">•</text>
            <text class="notice-text">服务当日请确保家中有人</text>
          </view>
          <view class="notice-item">
            <text class="notice-dot">•</text>
            <text class="notice-text">如需取消，请提前24小时联系</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 底部操作栏 -->
    <view class="bottom-action safe-area-bottom">
      <view class="action-left">
        <view class="action-item" @tap="goBack">
          <text class="action-icon">🏠</text>
          <text class="action-text">返回</text>
        </view>
      </view>
      <view class="action-right">
        <button class="btn btn-primary btn-block" @tap="purchasePackage">
          立即选购 ¥{{ packageInfo.price || 0 }}
        </button>
      </view>
    </view>
  </view>
</template>

<script>
import { packageList } from '@/common/data/mockData.js';
import navigator from '@/common/utils/navigator.js';

export default {
  data() {
    return {
      packageId: null,
      packageInfo: {},
      serviceContents: []
    };
  },
  onLoad(options) {
    console.log('套餐详情页面 onLoad, options:', options);
    
    if (options && options.id) {
      this.packageId = parseInt(options.id);
      this.loadPackageInfo();
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
  methods: {
    loadPackageInfo() {
      console.log('加载套餐信息, packageId:', this.packageId);
      
      // 从 mockData 中查找套餐信息
      const found = packageList.find(p => p.id === this.packageId);
      console.log('获取到的套餐信息:', found);
      
      if (found) {
        this.packageInfo = found;
        
        // 根据套餐名称生成服务内容
        this.generateServiceContents();
      } else {
        // 默认套餐信息
        this.packageInfo = {
          id: this.packageId,
          name: '清洁套餐',
          price: 199,
          originalPrice: 299,
          description: '专业清洁服务，让您的家焕然一新。采用环保清洁产品，专业团队服务。',
          duration: '4小时',
          area: '100㎡以内',
          sales: 128,
          tags: ['热销', '推荐'],
          image: ''
        };
        this.generateServiceContents();
      }
    },
    
    generateServiceContents() {
      const name = this.packageInfo.name || '';
      
      if (name.includes('清洁') || name.includes('保洁')) {
        this.serviceContents = [
          '全屋表面清洁除尘',
          '厨房台面清洁',
          '卫生间深度清洁',
          '地面清洁拖地',
          '玻璃清洁',
          '垃圾清理'
        ];
      } else if (name.includes('消毒') || name.includes('杀菌')) {
        this.serviceContents = [
          '全屋空气消毒',
          '表面消毒杀菌',
          '卫生间深度消毒',
          '厨房餐具消毒',
          '儿童玩具消毒',
          '消毒报告反馈'
        ];
      } else if (name.includes('甲醛') || name.includes('除味')) {
        this.serviceContents = [
          '甲醛浓度检测',
          '污染源定位分析',
          '专业除醛药剂喷涂',
          '光触媒治理',
          '异味消除处理',
          '复检达标保障'
        ];
      } else {
        this.serviceContents = [
          '专业团队上门服务',
          '标准服务流程',
          '环保材料使用',
          '质量保证承诺',
          '售后无忧服务',
          '客户满意度保障'
        ];
      }
    },
    
    goBack() {
      console.log('点击返回');
      navigator.navigateBack();
    },
    
    purchasePackage() {
      console.log('点击选购套餐:', this.packageInfo);
      uni.showModal({
        title: '确认选购',
        content: `确定选购「${this.packageInfo.name || '套餐'}」吗？价格：¥${this.packageInfo.price || 0}`,
        success: (res) => {
          if (res.confirm) {
            uni.showToast({
              title: '选购成功',
              icon: 'success'
            });
            
            setTimeout(() => {
              // 跳转到套餐卡管理或其他相关页面
              navigator.navigateTo({
                pageKey: 'packageCardList'
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
.package-detail-page {
  min-height: 100vh;
  background-color: #F8F9FA;
  padding-bottom: 180rpx;
}

/* 图片区域 */
.image-section {
  position: relative;
  width: 100%;
  height: 400rpx;
}

.package-image {
  width: 100%;
  height: 100%;
}

.back-btn {
  position: absolute;
  top: 30rpx;
  left: 30rpx;
  width: 64rpx;
  height: 64rpx;
  background-color: rgba(0, 0, 0, 0.3);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.back-icon {
  font-size: 40rpx;
  color: #FFFFFF;
}

/* 卡片样式 */
.card {
  background-color: #FFFFFF;
  margin: 24rpx 30rpx;
  border-radius: 16rpx;
  overflow: hidden;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.card-header {
  padding: 20rpx 30rpx;
  border-bottom: 1rpx solid #E0E0E0;
}

.card-title {
  font-size: 30rpx;
  font-weight: 600;
  color: #333333;
}

.card-body {
  padding: 24rpx 30rpx;
}

/* 基础信息卡片 */
.name-section {
  margin-bottom: 20rpx;
}

.package-name {
  display: block;
  font-size: 36rpx;
  font-weight: 600;
  color: #333333;
  margin-bottom: 12rpx;
}

.package-tags {
  display: flex;
  flex-wrap: wrap;
}

.tag {
  padding: 6rpx 16rpx;
  background-color: #E8F5E9;
  color: #4CAF50;
  font-size: 22rpx;
  border-radius: 12rpx;
  margin-right: 12rpx;
  margin-bottom: 8rpx;
}

.price-section {
  display: flex;
  align-items: baseline;
  margin-bottom: 12rpx;
}

.price-label {
  font-size: 28rpx;
  color: #F44336;
  font-weight: 500;
}

.price-value {
  font-size: 48rpx;
  color: #F44336;
  font-weight: 600;
}

.price-original {
  font-size: 26rpx;
  color: #999999;
  text-decoration: line-through;
  margin-left: 16rpx;
}

.sales-section {
  padding-top: 16rpx;
  border-top: 1rpx solid #F0F0F0;
}

.sales-text {
  font-size: 24rpx;
  color: #999999;
}

/* 规格卡片 */
.spec-list {
  display: flex;
  flex-direction: column;
}

.spec-item {
  display: flex;
  align-items: center;
  padding: 16rpx 0;
  border-bottom: 1rpx solid #F0F0F0;
}

.spec-item:last-child {
  border-bottom: none;
}

.spec-icon {
  font-size: 32rpx;
  margin-right: 16rpx;
}

.spec-info {
  flex: 1;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.spec-label {
  font-size: 28rpx;
  color: #666666;
}

.spec-value {
  font-size: 28rpx;
  color: #333333;
  font-weight: 500;
}

/* 描述卡片 */
.desc-text {
  font-size: 28rpx;
  color: #666666;
  line-height: 1.8;
}

/* 服务内容卡片 */
.content-list {
  display: flex;
  flex-direction: column;
}

.content-item {
  display: flex;
  align-items: center;
  padding: 12rpx 0;
}

.check-icon {
  width: 40rpx;
  height: 40rpx;
  background-color: #4CAF50;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #FFFFFF;
  font-size: 24rpx;
  margin-right: 16rpx;
  flex-shrink: 0;
}

.content-text {
  font-size: 28rpx;
  color: #333333;
}

/* 注意事项卡片 */
.notice-list {
  display: flex;
  flex-direction: column;
}

.notice-item {
  display: flex;
  align-items: flex-start;
  padding: 8rpx 0;
}

.notice-dot {
  font-size: 28rpx;
  color: #4CAF50;
  margin-right: 12rpx;
  flex-shrink: 0;
}

.notice-text {
  font-size: 26rpx;
  color: #666666;
  line-height: 1.6;
}

/* 底部操作栏 */
.bottom-action {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  display: flex;
  align-items: center;
  padding: 20rpx 30rpx;
  background-color: #FFFFFF;
  box-shadow: 0 -2rpx 10rpx rgba(0, 0, 0, 0.05);
}

.action-left {
  display: flex;
  align-items: center;
  margin-right: 24rpx;
}

.action-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 8rpx 16rpx;
}

.action-icon {
  font-size: 36rpx;
  margin-bottom: 4rpx;
}

.action-text {
  font-size: 22rpx;
  color: #666666;
}

.action-right {
  flex: 1;
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
