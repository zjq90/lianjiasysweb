// tabBar 页面路径（必须使用 switchTab）
const tabBarPages = [
  '/pages/index/index',
  '/pages/service/service',
  '/pages/profile/profile'
];

// 页面路径映射
const pagePaths = {
  // tabBar 页面
  'home': '/pages/index/index',
  'service': '/pages/service/service',
  'profile': '/pages/profile/profile',
  
  // 服务模块子页面
  'packageList': '/pages/service/packageList',
  'packageCardList': '/pages/service/packageCardList',
  'familyList': '/pages/service/familyList',
  'userList': '/pages/service/userList',
  
  // 用户管理相关页面
  'userForm': '/pages/service/userForm',
  'userDetail': '/pages/service/userDetail',
  
  // 家庭档案相关页面
  'familyForm': '/pages/service/familyForm',
  'familyDetail': '/pages/service/familyDetail',
  
  // 套餐卡相关页面
  'packageCardForm': '/pages/service/packageCardForm',
  'packageCardDetail': '/pages/service/packageCardDetail',
  
  // 套餐推荐相关页面
  'packageDetail': '/pages/service/packageDetail',
  
  // 地址管理相关页面
  'addressForm': '/pages/profile/addressForm',
  'addressDetail': '/pages/profile/addressDetail',
  
  // 个人模块子页面
  'userInfo': '/pages/profile/userInfo',
  'address': '/pages/profile/address',
  'orders': '/pages/profile/orders',
  'about': '/pages/profile/about'
};

/**
 * 智能导航函数
 * 支持两种调用方式：
 * 1. navigateTo('pageKey', { param: 'value' })
 * 2. navigateTo({ pageKey: 'pageKey', params: { param: 'value' } })
 * @param {string|object} options - 页面键名（字符串）或配置对象（包含 pageKey 和 params）
 * @param {object} params - 页面参数（当第一个参数是字符串时使用）
 */
function navigateTo(options, params = {}) {
  let pageKey = '';
  let queryParams = {};
  
  // 解析参数
  if (typeof options === 'string') {
    // 方式1：navigateTo('pageKey', { param: 'value' })
    pageKey = options;
    queryParams = params;
    console.log('[Navigator] 方式1调用 - pageKey:', pageKey, 'params:', queryParams);
  } else if (typeof options === 'object' && options !== null) {
    // 方式2：navigateTo({ pageKey: 'pageKey', params: { param: 'value' } })
    pageKey = options.pageKey || options.path || '';
    queryParams = options.params || {};
    console.log('[Navigator] 方式2调用 - pageKey:', pageKey, 'params:', queryParams);
  } else {
    console.error('[Navigator] 无效的参数类型:', typeof options, options);
    uni.showToast({
      title: '参数错误',
      icon: 'none',
      duration: 2000
    });
    return;
  }
  
  // 验证 pageKey
  if (!pageKey) {
    console.error('[Navigator] pageKey 为空');
    uni.showToast({
      title: '页面标识不能为空',
      icon: 'none',
      duration: 2000
    });
    return;
  }
  
  // 获取完整路径
  let url = pagePaths[pageKey];
  
  // 如果在 pagePaths 中找不到，直接使用 pageKey 作为路径
  if (!url) {
    console.warn('[Navigator] pageKey 未在映射中找到，直接作为路径使用:', pageKey);
    url = pageKey;
  }
  
  console.log('[Navigator] 解析后的 URL:', url);
  
  // 确保 url 是字符串
  if (typeof url !== 'string') {
    console.error('[Navigator] URL 不是字符串:', url);
    uni.showToast({
      title: '页面路径无效',
      icon: 'none',
      duration: 2000
    });
    return;
  }
  
  // 确保路径以 / 开头
  if (!url.startsWith('/')) {
    url = '/' + url;
    console.log('[Navigator] 补全路径前缀:', url);
  }
  
  // 拼接参数
  if (queryParams && Object.keys(queryParams).length > 0) {
    const queryString = Object.keys(queryParams)
      .map(key => `${key}=${encodeURIComponent(queryParams[key])}`)
      .join('&');
    url += `?${queryString}`;
    console.log('[Navigator] 拼接参数后 URL:', url);
  }
  
  // 判断是否为 tabBar 页面
  const isTabBar = tabBarPages.some(tabPage => url.startsWith(tabPage));
  console.log('[Navigator] 是否为 TabBar 页面:', isTabBar);
  
  if (isTabBar) {
    // tabBar 页面必须使用 switchTab
    const tabBarUrl = url.split('?')[0];
    console.log('[Navigator] 使用 switchTab 跳转到:', tabBarUrl);
    
    uni.switchTab({
      url: tabBarUrl,
      success: () => {
        console.log('[Navigator] switchTab 成功:', tabBarUrl);
      },
      fail: (err) => {
        console.error('[Navigator] switchTab 失败:', err);
        uni.showToast({
          title: '页面跳转失败: ' + (err.errMsg || '未知错误'),
          icon: 'none',
          duration: 3000
        });
      }
    });
  } else {
    // 普通页面使用 navigateTo
    console.log('[Navigator] 使用 navigateTo 跳转到:', url);
    
    uni.navigateTo({
      url: url,
      success: () => {
        console.log('[Navigator] navigateTo 成功:', url);
      },
      fail: (err) => {
        console.error('[Navigator] navigateTo 失败:', err);
        
        // 如果 navigateTo 失败，可能是页面未注册或路径错误
        let errorMsg = '页面跳转失败';
        if (err.errMsg && err.errMsg.includes('fail')) {
          if (err.errMsg.includes('not found')) {
            errorMsg = '页面不存在，请检查路径';
          } else if (err.errMsg.includes('tabBar')) {
            errorMsg = 'TabBar 页面请使用 switchTab';
          } else {
            errorMsg = '跳转失败: ' + err.errMsg;
          }
        }
        
        uni.showToast({
          title: errorMsg,
          icon: 'none',
          duration: 3000
        });
      }
    });
  }
}

/**
 * 返回上一页
 * @param {number} delta - 返回层数，默认 1
 */
function navigateBack(delta = 1) {
  const pages = getCurrentPages();
  console.log('当前页面栈长度:', pages.length);
  
  if (pages.length > 1) {
    uni.navigateBack({
      delta: delta,
      success: () => {
        console.log('navigateBack 成功');
      },
      fail: (err) => {
        console.error('navigateBack 失败:', err);
        uni.showToast({
          title: '返回失败',
          icon: 'none'
        });
      }
    });
  } else {
    console.log('没有上一页，跳转到首页');
    // 如果没有上一页，跳转到首页
    uni.switchTab({
      url: '/pages/index/index'
    });
  }
}

/**
 * 重定向到页面（关闭当前页面，跳转到应用内的某个页面）
 * 支持两种调用方式：
 * 1. redirectTo('pageKey')
 * 2. redirectTo({ pageKey: 'pageKey', params: { param: 'value' } })
 * @param {string|object} options - 页面键名（字符串）或配置对象
 */
function redirectTo(options) {
  let pageKey = '';
  let queryParams = {};
  
  // 解析参数
  if (typeof options === 'string') {
    // 方式1：redirectTo('pageKey')
    pageKey = options;
  } else if (typeof options === 'object' && options !== null) {
    // 方式2：redirectTo({ pageKey: 'pageKey', params: {} })
    pageKey = options.pageKey || options.path || '';
    queryParams = options.params || {};
  }
  
  console.log('redirectTo pageKey:', pageKey, 'params:', queryParams);
  
  // 获取完整路径
  let url = pagePaths[pageKey] || pageKey;
  
  // 确保 url 是字符串
  if (typeof url !== 'string') {
    console.error('无效的页面路径:', url);
    uni.showToast({
      title: '页面路径无效',
      icon: 'none'
    });
    return;
  }
  
  // 确保路径以 / 开头
  if (!url.startsWith('/')) {
    url = '/' + url;
  }
  
  // 拼接参数
  if (queryParams && Object.keys(queryParams).length > 0) {
    const queryString = Object.keys(queryParams)
      .map(key => `${key}=${encodeURIComponent(queryParams[key])}`)
      .join('&');
    url += `?${queryString}`;
  }
  
  console.log('redirectTo final url:', url);
  
  const isTabBar = tabBarPages.some(tabPage => url.startsWith(tabPage));
  
  if (isTabBar) {
    uni.switchTab({
      url: url.split('?')[0],
      fail: (err) => {
        console.error('switchTab 失败:', err);
      }
    });
  } else {
    uni.redirectTo({
      url: url,
      fail: (err) => {
        console.error('redirectTo 失败:', err);
        uni.showToast({
          title: '页面跳转失败',
          icon: 'none'
        });
      }
    });
  }
}

export default {
  navigateTo,
  navigateBack,
  redirectTo,
  tabBarPages,
  pagePaths
};
