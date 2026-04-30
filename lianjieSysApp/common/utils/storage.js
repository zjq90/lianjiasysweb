// 本地存储工具函数
// 用于持久化用户数据、家庭档案、地址、套餐卡等信息

// 存储 Key 定义
const STORAGE_KEYS = {
  USER_LIST: 'LIANJIA_USER_LIST',
  FAMILY_LIST: 'LIANJIA_FAMILY_LIST',
  ADDRESS_LIST: 'LIANJIA_ADDRESS_LIST',
  PACKAGE_CARD_LIST: 'LIANJIA_PACKAGE_CARD_LIST',
  CURRENT_USER: 'LIANJIA_CURRENT_USER',
  ORDER_LIST: 'LIANJIA_ORDER_LIST'
};

// 生成唯一 ID
function generateId(prefix = 'ID') {
  const timestamp = Date.now();
  const random = Math.floor(Math.random() * 10000);
  return `${prefix}_${timestamp}_${random}`;
}

// 获取当前时间字符串
function getCurrentTime() {
  const now = new Date();
  const year = now.getFullYear();
  const month = String(now.getMonth() + 1).padStart(2, '0');
  const day = String(now.getDate()).padStart(2, '0');
  const hours = String(now.getHours()).padStart(2, '0');
  const minutes = String(now.getMinutes()).padStart(2, '0');
  const seconds = String(now.getSeconds()).padStart(2, '0');
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}

// 从本地存储获取数据
function getData(key, defaultValue = []) {
  try {
    const data = uni.getStorageSync(key);
    if (data && data !== '') {
      return JSON.parse(data);
    }
    return defaultValue;
  } catch (error) {
    console.error('读取本地存储失败:', error);
    return defaultValue;
  }
}

// 保存数据到本地存储
function setData(key, data) {
  try {
    uni.setStorageSync(key, JSON.stringify(data));
    return true;
  } catch (error) {
    console.error('保存本地存储失败:', error);
    uni.showToast({
      title: '数据保存失败',
      icon: 'none'
    });
    return false;
  }
}

// 删除本地存储数据
function removeData(key) {
  try {
    uni.removeStorageSync(key);
    return true;
  } catch (error) {
    console.error('删除本地存储失败:', error);
    return false;
  }
}

// 用户管理
const UserStorage = {
  // 获取用户列表
  getList() {
    return getData(STORAGE_KEYS.USER_LIST, []);
  },
  
  // 获取单个用户
  getById(id) {
    const list = this.getList();
    return list.find(item => item.id === id) || null;
  },
  
  // 根据 openId 获取用户
  getByOpenId(openId) {
    const list = this.getList();
    return list.find(item => item.openId === openId) || null;
  },
  
  // 根据手机号获取用户
  getByPhone(phone) {
    const list = this.getList();
    return list.find(item => item.phone === phone) || null;
  },
  
  // 新增用户
  add(userData) {
    const list = this.getList();
    const newUser = {
      id: generateId('USER'),
      openId: userData.openId || generateId('OPEN'),
      name: userData.name || '',
      phone: userData.phone || '',
      avatar: userData.avatar || 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=default%20avatar%20user%20icon&image_size=square_hd',
      status: userData.status || 1,
      createTime: getCurrentTime()
    };
    list.unshift(newUser);
    setData(STORAGE_KEYS.USER_LIST, list);
    return newUser;
  },
  
  // 更新用户
  update(id, updateData) {
    const list = this.getList();
    const index = list.findIndex(item => item.id === id);
    if (index > -1) {
      list[index] = {
        ...list[index],
        ...updateData,
        id: id
      };
      setData(STORAGE_KEYS.USER_LIST, list);
      return list[index];
    }
    return null;
  },
  
  // 删除用户
  delete(id) {
    const list = this.getList();
    const index = list.findIndex(item => item.id === id);
    if (index > -1) {
      list.splice(index, 1);
      setData(STORAGE_KEYS.USER_LIST, list);
      return true;
    }
    return false;
  },
  
  // 更新用户状态
  updateStatus(id, status) {
    return this.update(id, { status });
  },
  
  // 初始化默认数据
  initDefaultData(defaultList) {
    const existing = this.getList();
    if (existing.length === 0 && defaultList && defaultList.length > 0) {
      setData(STORAGE_KEYS.USER_LIST, defaultList);
      return defaultList;
    }
    return existing;
  }
};

// 家庭档案管理
const FamilyStorage = {
  // 获取家庭档案列表
  getList() {
    return getData(STORAGE_KEYS.FAMILY_LIST, []);
  },
  
  // 获取单个家庭档案
  getById(id) {
    const list = this.getList();
    return list.find(item => item.id === id) || null;
  },
  
  // 获取用户的家庭档案列表
  getByUserId(userId) {
    const list = this.getList();
    return list.filter(item => item.userId === userId);
  },
  
  // 获取默认家庭档案
  getDefault(userId) {
    const list = this.getByUserId(userId);
    return list.find(item => item.isDefault === 1) || list[0] || null;
  },
  
  // 新增家庭档案
  add(familyData) {
    const list = this.getList();
    const newFamily = {
      id: generateId('FAMILY'),
      userId: familyData.userId || 1,
      name: familyData.name || '',
      isDefault: familyData.isDefault || 0,
      area: familyData.area || 0,
      roomType: familyData.roomType || '',
      address: familyData.address || '',
      memberCount: familyData.memberCount || 0,
      hasChildren: familyData.hasChildren || 0,
      hasPets: familyData.hasPets || 0,
      members: familyData.members || [],
      reminderEnabled: familyData.reminderEnabled || 0,
      createTime: getCurrentTime()
    };
    
    if (newFamily.isDefault === 1) {
      list.forEach(item => {
        if (item.userId === newFamily.userId) {
          item.isDefault = 0;
        }
      });
    }
    
    list.unshift(newFamily);
    setData(STORAGE_KEYS.FAMILY_LIST, list);
    return newFamily;
  },
  
  // 更新家庭档案
  update(id, updateData) {
    const list = this.getList();
    const index = list.findIndex(item => item.id === id);
    if (index > -1) {
      if (updateData.isDefault === 1) {
        const currentFamily = list[index];
        list.forEach(item => {
          if (item.userId === currentFamily.userId) {
            item.isDefault = 0;
          }
        });
      }
      
      list[index] = {
        ...list[index],
        ...updateData,
        id: id
      };
      setData(STORAGE_KEYS.FAMILY_LIST, list);
      return list[index];
    }
    return null;
  },
  
  // 删除家庭档案
  delete(id) {
    const list = this.getList();
    const index = list.findIndex(item => item.id === id);
    if (index > -1) {
      list.splice(index, 1);
      setData(STORAGE_KEYS.FAMILY_LIST, list);
      return true;
    }
    return false;
  },
  
  // 设置默认家庭档案
  setDefault(id, userId) {
    const list = this.getList();
    list.forEach(item => {
      if (item.userId === userId) {
        item.isDefault = item.id === id ? 1 : 0;
      }
    });
    setData(STORAGE_KEYS.FAMILY_LIST, list);
    return true;
  },
  
  // 切换提醒状态
  toggleReminder(id) {
    const list = this.getList();
    const index = list.findIndex(item => item.id === id);
    if (index > -1) {
      list[index].reminderEnabled = list[index].reminderEnabled === 1 ? 0 : 1;
      setData(STORAGE_KEYS.FAMILY_LIST, list);
      return list[index];
    }
    return null;
  },
  
  // 新增家庭成员
  addMember(familyId, memberData) {
    const list = this.getList();
    const index = list.findIndex(item => item.id === familyId);
    if (index > -1) {
      const newMember = {
        id: generateId('MEMBER'),
        name: memberData.name || '',
        relation: memberData.relation || '',
        phone: memberData.phone || '',
        createTime: getCurrentTime()
      };
      
      if (!list[index].members) {
        list[index].members = [];
      }
      list[index].members.push(newMember);
      list[index].memberCount = list[index].members.length;
      
      setData(STORAGE_KEYS.FAMILY_LIST, list);
      return newMember;
    }
    return null;
  },
  
  // 更新家庭成员
  updateMember(familyId, memberId, updateData) {
    const list = this.getList();
    const index = list.findIndex(item => item.id === familyId);
    if (index > -1 && list[index].members) {
      const memberIndex = list[index].members.findIndex(m => m.id === memberId);
      if (memberIndex > -1) {
        list[index].members[memberIndex] = {
          ...list[index].members[memberIndex],
          ...updateData,
          id: memberId
        };
        setData(STORAGE_KEYS.FAMILY_LIST, list);
        return list[index].members[memberIndex];
      }
    }
    return null;
  },
  
  // 删除家庭成员
  deleteMember(familyId, memberId) {
    const list = this.getList();
    const index = list.findIndex(item => item.id === familyId);
    if (index > -1 && list[index].members) {
      const memberIndex = list[index].members.findIndex(m => m.id === memberId);
      if (memberIndex > -1) {
        list[index].members.splice(memberIndex, 1);
        list[index].memberCount = list[index].members.length;
        setData(STORAGE_KEYS.FAMILY_LIST, list);
        return true;
      }
    }
    return false;
  },
  
  // 初始化默认数据
  initDefaultData(defaultList) {
    const existing = this.getList();
    if (existing.length === 0 && defaultList && defaultList.length > 0) {
      setData(STORAGE_KEYS.FAMILY_LIST, defaultList);
      return defaultList;
    }
    return existing;
  }
};

// 地址管理
const AddressStorage = {
  // 获取地址列表
  getList() {
    return getData(STORAGE_KEYS.ADDRESS_LIST, []);
  },
  
  // 获取单个地址
  getById(id) {
    const list = this.getList();
    return list.find(item => item.id === id) || null;
  },
  
  // 获取用户的地址列表
  getByUserId(userId) {
    const list = this.getList();
    return list.filter(item => item.userId === userId);
  },
  
  // 获取默认地址
  getDefault(userId) {
    const list = this.getByUserId(userId);
    return list.find(item => item.isDefault === 1) || list[0] || null;
  },
  
  // 新增地址
  add(addressData) {
    const list = this.getList();
    const newAddress = {
      id: generateId('ADDR'),
      userId: addressData.userId || 1,
      name: addressData.name || '',
      phone: addressData.phone || '',
      province: addressData.province || '',
      city: addressData.city || '',
      district: addressData.district || '',
      address: addressData.address || '',
      isDefault: addressData.isDefault || 0,
      createTime: getCurrentTime()
    };
    
    if (newAddress.isDefault === 1) {
      list.forEach(item => {
        if (item.userId === newAddress.userId) {
          item.isDefault = 0;
        }
      });
    }
    
    list.unshift(newAddress);
    setData(STORAGE_KEYS.ADDRESS_LIST, list);
    return newAddress;
  },
  
  // 更新地址
  update(id, updateData) {
    const list = this.getList();
    const index = list.findIndex(item => item.id === id);
    if (index > -1) {
      if (updateData.isDefault === 1) {
        const currentAddress = list[index];
        list.forEach(item => {
          if (item.userId === currentAddress.userId) {
            item.isDefault = 0;
          }
        });
      }
      
      list[index] = {
        ...list[index],
        ...updateData,
        id: id
      };
      setData(STORAGE_KEYS.ADDRESS_LIST, list);
      return list[index];
    }
    return null;
  },
  
  // 删除地址
  delete(id) {
    const list = this.getList();
    const index = list.findIndex(item => item.id === id);
    if (index > -1) {
      list.splice(index, 1);
      setData(STORAGE_KEYS.ADDRESS_LIST, list);
      return true;
    }
    return false;
  },
  
  // 设置默认地址
  setDefault(id, userId) {
    const list = this.getList();
    list.forEach(item => {
      if (item.userId === userId) {
        item.isDefault = item.id === id ? 1 : 0;
      }
    });
    setData(STORAGE_KEYS.ADDRESS_LIST, list);
    return true;
  },
  
  // 初始化默认数据
  initDefaultData(defaultList) {
    const existing = this.getList();
    if (existing.length === 0 && defaultList && defaultList.length > 0) {
      setData(STORAGE_KEYS.ADDRESS_LIST, defaultList);
      return defaultList;
    }
    return existing;
  }
};

// 套餐卡管理
const PackageCardStorage = {
  // 获取套餐卡列表
  getList() {
    return getData(STORAGE_KEYS.PACKAGE_CARD_LIST, []);
  },
  
  // 获取单个套餐卡
  getById(id) {
    const list = this.getList();
    return list.find(item => item.id === id) || null;
  },
  
  // 获取用户的套餐卡列表
  getByUserId(userId) {
    const list = this.getList();
    return list.filter(item => true);
  },
  
  // 获取用户有效的套餐卡列表
  getValidByUserId(userId) {
    const list = this.getByUserId(userId);
    return list.filter(item => item.status === 1 && item.remainingTimes > 0);
  },
  
  // 生成卡号
  generateCardNo() {
    const now = new Date();
    const year = now.getFullYear();
    const random = Math.floor(Math.random() * 100000).toString().padStart(5, '0');
    return `LJ${year}${random}`;
  },
  
  // 新增套餐卡
  add(cardData) {
    const list = this.getList();
    const newCard = {
      id: generateId('CARD'),
      cardNo: cardData.cardNo || this.generateCardNo(),
      name: cardData.name || '',
      packageId: cardData.packageId || null,
      packageName: cardData.packageName || '',
      totalTimes: cardData.totalTimes || 1,
      usedTimes: cardData.usedTimes || 0,
      remainingTimes: cardData.remainingTimes || cardData.totalTimes || 1,
      expireDate: cardData.expireDate || '',
      status: cardData.status || 1,
      createTime: getCurrentTime()
    };
    
    list.unshift(newCard);
    setData(STORAGE_KEYS.PACKAGE_CARD_LIST, list);
    return newCard;
  },
  
  // 更新套餐卡
  update(id, updateData) {
    const list = this.getList();
    const index = list.findIndex(item => item.id === id);
    if (index > -1) {
      list[index] = {
        ...list[index],
        ...updateData,
        id: id
      };
      setData(STORAGE_KEYS.PACKAGE_CARD_LIST, list);
      return list[index];
    }
    return null;
  },
  
  // 删除套餐卡
  delete(id) {
    const list = this.getList();
    const index = list.findIndex(item => item.id === id);
    if (index > -1) {
      list.splice(index, 1);
      setData(STORAGE_KEYS.PACKAGE_CARD_LIST, list);
      return true;
    }
    return false;
  },
  
  // 使用套餐卡（扣减次数）
  useCard(id) {
    const list = this.getList();
    const index = list.findIndex(item => item.id === id);
    if (index > -1 && list[index].remainingTimes > 0) {
      list[index].usedTimes++;
      list[index].remainingTimes--;
      
      if (list[index].remainingTimes <= 0) {
        list[index].status = 2;
      }
      
      setData(STORAGE_KEYS.PACKAGE_CARD_LIST, list);
      return list[index];
    }
    return null;
  },
  
  // 初始化默认数据
  initDefaultData(defaultList) {
    const existing = this.getList();
    if (existing.length === 0 && defaultList && defaultList.length > 0) {
      setData(STORAGE_KEYS.PACKAGE_CARD_LIST, defaultList);
      return defaultList;
    }
    return existing;
  }
};

// 服务单管理
const OrderStorage = {
  // 获取服务单列表
  getList() {
    return getData(STORAGE_KEYS.ORDER_LIST, []);
  },
  
  // 获取单个服务单
  getById(id) {
    const list = this.getList();
    return list.find(item => item.id === id) || null;
  },
  
  // 根据状态获取服务单列表
  getByStatus(status) {
    const list = this.getList();
    if (status === 'all') {
      return list;
    }
    return list.filter(item => item.status === status);
  },
  
  // 更新服务单状态
  updateStatus(id, status, statusText) {
    const list = this.getList();
    const index = list.findIndex(item => item.id === id);
    if (index > -1) {
      list[index].status = status;
      if (statusText) {
        list[index].statusText = statusText;
      }
      setData(STORAGE_KEYS.ORDER_LIST, list);
      return list[index];
    }
    return null;
  },
  
  // 初始化默认数据
  initDefaultData(defaultList) {
    const existing = this.getList();
    if (existing.length === 0 && defaultList && defaultList.length > 0) {
      setData(STORAGE_KEYS.ORDER_LIST, defaultList);
      return defaultList;
    }
    return existing;
  }
};

// 初始化所有数据（从模拟数据初始化到本地存储）
function initAllData(mockData) {
  console.log('初始化本地存储数据...');
  
  UserStorage.initDefaultData(mockData.userList || []);
  FamilyStorage.initDefaultData(mockData.familyList || []);
  AddressStorage.initDefaultData(mockData.addressList || []);
  PackageCardStorage.initDefaultData(mockData.packageCardList || []);
  OrderStorage.initDefaultData(mockData.orderList || []);
  
  console.log('数据初始化完成');
}

export default {
  generateId,
  getCurrentTime,
  getData,
  setData,
  removeData,
  
  UserStorage,
  FamilyStorage,
  AddressStorage,
  PackageCardStorage,
  OrderStorage,
  
  initAllData,
  STORAGE_KEYS
};
