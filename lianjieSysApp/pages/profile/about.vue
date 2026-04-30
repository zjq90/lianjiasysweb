<template>
  <view class="about-page">
    <!-- Logo区域 -->
    <view class="logo-section card">
      <view class="logo-content">
        <view class="logo-icon">
          <text class="logo-text">🏠</text>
        </view>
        <text class="app-name">恋家环保师傅端</text>
        <text class="app-version">版本 1.0.0</text>
      </view>
    </view>

    <!-- 功能介绍 -->
    <view class="feature-section card">
      <view class="section-header">
        <text class="header-title">软件介绍</text>
      </view>
      <view class="feature-content">
        <text class="feature-text">
          恋家环保师傅端是一款专为家庭清洁服务师傅打造的专业服务管理平台，帮助师傅高效管理服务订单、客户信息、套餐卡等业务。
        </text>
        <view class="feature-list">
          <view class="feature-item">
            <text class="feature-dot">•</text>
            <text class="feature-desc">订单管理：轻松接单、派单、完成服务</text>
          </view>
          <view class="feature-item">
            <text class="feature-dot">•</text>
            <text class="feature-desc">套餐卡管理：管理客户套餐卡，扣减次数</text>
          </view>
          <view class="feature-item">
            <text class="feature-dot">•</text>
            <text class="feature-desc">家庭档案：管理客户家庭信息，提供个性化服务</text>
          </view>
          <view class="feature-item">
            <text class="feature-dot">•</text>
            <text class="feature-desc">智能推荐：根据场景标签智能推荐服务套餐</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 联系我们 -->
    <view class="contact-section card">
      <view class="section-header">
        <text class="header-title">联系我们</text>
      </view>
      <view class="contact-list">
        <view class="contact-item" @click="callPhone('400-123-4567')">
          <view class="contact-icon">
            <text class="icon-text">📞</text>
          </view>
          <view class="contact-info">
            <text class="contact-title">客服电话</text>
            <text class="contact-value">400-123-4567</text>
          </view>
          <text class="contact-arrow">›</text>
        </view>
        <view class="contact-item" @click="callPhone('service@lianjia.com')">
          <view class="contact-icon">
            <text class="icon-text">📧</text>
          </view>
          <view class="contact-info">
            <text class="contact-title">客服邮箱</text>
            <text class="contact-value">service@lianjia.com</text>
          </view>
          <text class="contact-arrow">›</text>
        </view>
        <view class="contact-item" @click="openWorkTime">
          <view class="contact-icon">
            <text class="icon-text">🕐</text>
          </view>
          <view class="contact-info">
            <text class="contact-title">工作时间</text>
            <text class="contact-value">周一至周日 8:00-22:00</text>
          </view>
          <text class="contact-arrow">›</text>
        </view>
      </view>
    </view>

    <!-- 公司信息 -->
    <view class="company-section card">
      <view class="section-header">
        <text class="header-title">公司信息</text>
      </view>
      <view class="company-content">
        <view class="company-item">
          <text class="company-label">公司名称：</text>
          <text class="company-value">恋家环保科技有限公司</text>
        </view>
        <view class="company-item">
          <text class="company-label">公司地址：</text>
          <text class="company-value">北京市朝阳区望京SOHO T1</text>
        </view>
        <view class="company-item">
          <text class="company-label">统一社会信用代码：</text>
          <text class="company-value">91110105MA00000000</text>
        </view>
      </view>
    </view>

    <!-- 版本信息 -->
    <view class="version-section card">
      <view class="section-header">
        <text class="header-title">版本信息</text>
      </view>
      <view class="version-content">
        <view class="version-item">
          <text class="version-label">当前版本：</text>
          <text class="version-value">1.0.0</text>
        </view>
        <view class="version-item">
          <text class="version-label">更新时间：</text>
          <text class="version-value">2024-04-30</text>
        </view>
        <view class="version-item">
          <text class="version-label">适用平台：</text>
          <text class="version-value">iOS / Android</text>
        </view>
      </view>
      <view class="check-update" @click="checkUpdate">
        <text class="check-text">检查更新</text>
      </view>
    </view>

    <!-- 底部版权 -->
    <view class="copyright-section">
      <text class="copyright-text">© 2024 恋家环保科技有限公司</text>
      <text class="copyright-text">保留所有权利</text>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {};
  },
  methods: {
    callPhone(phone) {
      if (phone.includes('@')) {
        uni.showToast({
          title: `发送邮件到：${phone}`,
          icon: 'none'
        });
      } else {
        uni.showModal({
          title: '拨打电话',
          content: `确定拨打 ${phone} 吗？`,
          success: (res) => {
            if (res.confirm) {
              uni.makePhoneCall({
                phoneNumber: phone,
                fail: () => {
                  uni.showToast({
                    title: '拨打电话功能需要真机测试',
                    icon: 'none'
                  });
                }
              });
            }
          }
        });
      }
    },
    openWorkTime() {
      uni.showToast({
        title: '工作时间：周一至周日 8:00-22:00',
        icon: 'none'
      });
    },
    checkUpdate() {
      uni.showLoading({
        title: '检查中...'
      });
      setTimeout(() => {
        uni.hideLoading();
        uni.showModal({
          title: '版本检查',
          content: '当前已是最新版本',
          showCancel: false,
          confirmText: '知道了'
        });
      }, 1500);
    }
  }
};
</script>

<style scoped>
.about-page {
  min-height: 100vh;
  background-color: #F8F9FA;
  padding: 30rpx;
  padding-bottom: 60rpx;
}

/* 卡片样式 */
.card {
  background-color: #FFFFFF;
  border-radius: 16rpx;
  margin-bottom: 24rpx;
  overflow: hidden;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

/* Logo区域 */
.logo-section {
  padding: 40rpx 30rpx;
}

.logo-content {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.logo-icon {
  width: 140rpx;
  height: 140rpx;
  background: linear-gradient(135deg, #4CAF50 0%, #81C784 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 20rpx;
}

.logo-text {
  font-size: 72rpx;
}

.app-name {
  font-size: 36rpx;
  font-weight: 600;
  color: #333333;
  margin-bottom: 8rpx;
}

.app-version {
  font-size: 26rpx;
  color: #999999;
}

/* 通用区域头部 */
.section-header {
  padding: 20rpx 30rpx;
  border-bottom: 1rpx solid #F0F0F0;
}

.header-title {
  font-size: 28rpx;
  font-weight: 600;
  color: #333333;
}

/* 功能介绍 */
.feature-content {
  padding: 24rpx 30rpx;
}

.feature-text {
  font-size: 26rpx;
  color: #666666;
  line-height: 1.8;
  margin-bottom: 20rpx;
}

.feature-list {
  display: flex;
  flex-direction: column;
}

.feature-item {
  display: flex;
  align-items: flex-start;
  margin-bottom: 12rpx;
}

.feature-dot {
  font-size: 26rpx;
  color: #4CAF50;
  margin-right: 12rpx;
  flex-shrink: 0;
}

.feature-desc {
  font-size: 26rpx;
  color: #666666;
  line-height: 1.6;
}

/* 联系我们 */
.contact-list {
  display: flex;
  flex-direction: column;
}

.contact-item {
  display: flex;
  align-items: center;
  padding: 24rpx 30rpx;
  border-bottom: 1rpx solid #F0F0F0;
}

.contact-item:last-child {
  border-bottom: none;
}

.contact-icon {
  width: 64rpx;
  height: 64rpx;
  background-color: #E8F5E9;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20rpx;
  flex-shrink: 0;
}

.icon-text {
  font-size: 32rpx;
}

.contact-info {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.contact-title {
  font-size: 28rpx;
  color: #333333;
  margin-bottom: 4rpx;
}

.contact-value {
  font-size: 24rpx;
  color: #999999;
}

.contact-arrow {
  font-size: 36rpx;
  color: #CCCCCC;
}

/* 公司信息 */
.company-content {
  padding: 20rpx 30rpx;
}

.company-item {
  display: flex;
  margin-bottom: 16rpx;
}

.company-item:last-child {
  margin-bottom: 0;
}

.company-label {
  font-size: 26rpx;
  color: #999999;
  flex-shrink: 0;
}

.company-value {
  font-size: 26rpx;
  color: #333333;
  flex: 1;
}

/* 版本信息 */
.version-content {
  padding: 20rpx 30rpx;
}

.version-item {
  display: flex;
  margin-bottom: 16rpx;
}

.version-item:last-child {
  margin-bottom: 0;
}

.version-label {
  font-size: 26rpx;
  color: #999999;
  flex-shrink: 0;
}

.version-value {
  font-size: 26rpx;
  color: #333333;
  flex: 1;
}

.check-update {
  padding: 20rpx 30rpx;
  border-top: 1rpx solid #F0F0F0;
  text-align: center;
}

.check-text {
  font-size: 28rpx;
  color: #4CAF50;
}

/* 版权信息 */
.copyright-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 40rpx 0 20rpx;
}

.copyright-text {
  font-size: 24rpx;
  color: #999999;
  margin-bottom: 8rpx;
}
</style>
