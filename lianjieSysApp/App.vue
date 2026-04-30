<script>
import { userList, familyList, addressList, packageCardList, orderList } from '@/common/data/mockData.js';
import storage from '@/common/utils/storage.js';

export default {
  onLaunch: function() {
    console.log('App Launch - 恋家环保师傅端启动');
    
    // 初始化本地存储数据
    try {
      storage.initAllData({
        userList: userList,
        familyList: familyList,
        addressList: addressList,
        packageCardList: packageCardList,
        orderList: orderList
      });
      
      console.log('数据初始化成功');
    } catch (error) {
      console.error('数据初始化失败:', error);
    }
    
    setTimeout(() => {
      uni.showToast({
        title: '欢迎使用恋家环保',
        icon: 'none',
        duration: 2000
      });
    }, 500);
  },
  onShow: function() {
    console.log('App Show');
  },
  onHide: function() {
    console.log('App Hide');
  }
};
</script>

<style>
/* 全局样式 */
@import './common/styles/global.css';

/* 确保页面背景色 */
page {
  background-color: #F8F9FA;
}

/* 修复 tabBar 样式 */
.uni-tabbar {
  background-color: #FFFFFF !important;
  border-top: 1rpx solid #E0E0E0 !important;
}

/* 确保按钮可点击 */
button {
  position: relative;
  z-index: 10;
}

/* 确保点击区域可点击 */
view, text, image {
  position: relative;
}
</style>
