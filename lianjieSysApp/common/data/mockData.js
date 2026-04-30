// 模拟数据

// 首页案例广告数据
export const bannerList = [
  {
    id: 1,
    image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=home%20cleaning%20service%20advertisement%20banner%20green%20theme&image_size=landscape_16_9',
    title: '春季清洁特惠',
    desc: '全屋清洁低至199元起'
  },
  {
    id: 2,
    image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=environmental%20protection%20service%20banner%20fresh%20green%20style&image_size=landscape_16_9',
    title: '环保服务升级',
    desc: '使用天然环保清洁剂'
  },
  {
    id: 3,
    image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=professional%20cleaning%20team%20service%20banner%20green%20color&image_size=landscape_16_9',
    title: '专业团队服务',
    desc: '持证上岗，专业可靠'
  }
];

// 案例展示数据
export const caseList = [
  {
    id: 1,
    image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=clean%20and%20tidy%20living%20room%20after%20professional%20cleaning%20service&image_size=square_hd',
    title: '客厅深度清洁案例',
    desc: '120㎡三居室，恢复光亮如新',
    tags: ['深度清洁', '客厅']
  },
  {
    id: 2,
    image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=modern%20kitchen%20professional%20cleaning%20service%20before%20after&image_size=square_hd',
    title: '厨房油污清洁案例',
    desc: '重油污厨房焕然一新',
    tags: ['厨房清洁', '去油污']
  },
  {
    id: 3,
    image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=clean%20bathroom%20toilet%20professional%20cleaning%20service&image_size=square_hd',
    title: '卫生间杀菌消毒案例',
    desc: '专业消毒，守护家人健康',
    tags: ['杀菌消毒', '卫生间']
  },
  {
    id: 4,
    image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=office%20space%20commercial%20cleaning%20service%20professional&image_size=square_hd',
    title: '办公场所清洁案例',
    desc: '500㎡办公室全面清洁',
    tags: ['商业清洁', '办公室']
  }
];

// 套餐标签数据
export const packageTags = [
  { id: 1, name: '日常清洁', icon: '🧹' },
  { id: 2, name: '深度清洁', icon: '✨' },
  { id: 3, name: '厨房清洁', icon: '🍳' },
  { id: 4, name: '卫生间清洁', icon: '🚽' },
  { id: 5, name: '玻璃清洁', icon: '🪟' },
  { id: 6, name: '窗帘清洁', icon: '🪟' },
  { id: 7, name: '地毯清洁', icon: '🧶' },
  { id: 8, name: '消毒杀菌', icon: '🧴' },
  { id: 9, name: '新房开荒', icon: '🏠' }
];

// 套餐数据
export const packageList = [
  {
    id: 1,
    name: '日常清洁套餐',
    price: 99,
    originalPrice: 159,
    image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=daily%20home%20cleaning%20service%20package%20green%20theme&image_size=square_hd',
    tags: ['日常清洁'],
    duration: '2小时',
    area: '80㎡以内',
    description: '包含客厅、卧室、厨房、卫生间等基础区域清洁',
    sales: 1256
  },
  {
    id: 2,
    name: '深度清洁套餐',
    price: 299,
    originalPrice: 399,
    image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=deep%20house%20cleaning%20service%20package%20professional&image_size=square_hd',
    tags: ['深度清洁', '消毒杀菌'],
    duration: '4小时',
    area: '120㎡以内',
    description: '深度清洁+全面消毒，适合换季清洁或入住前清洁',
    sales: 892
  },
  {
    id: 3,
    name: '厨房专项清洁',
    price: 199,
    originalPrice: 299,
    image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=kitchen%20deep%20cleaning%20service%20removing%20grease&image_size=square_hd',
    tags: ['厨房清洁', '去油污'],
    duration: '2-3小时',
    area: '厨房区域',
    description: '油烟机、灶台、橱柜全方位清洁，重油污区域专项处理',
    sales: 678
  },
  {
    id: 4,
    name: '卫生间清洁套餐',
    price: 159,
    originalPrice: 239,
    image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=bathroom%20disinfection%20cleaning%20service%20sanitary&image_size=square_hd',
    tags: ['卫生间清洁', '消毒杀菌'],
    duration: '1-2小时',
    area: '卫生间区域',
    description: '马桶、浴缸、淋浴房全面清洁消毒，去除异味',
    sales: 534
  },
  {
    id: 5,
    name: '玻璃清洁套餐',
    price: 129,
    originalPrice: 199,
    image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=window%20glass%20cleaning%20service%20professional%20tools&image_size=square_hd',
    tags: ['玻璃清洁'],
    duration: '1-2小时',
    area: '窗户区域',
    description: '室内外玻璃清洁，不留水渍',
    sales: 456
  },
  {
    id: 6,
    name: '新房开荒套餐',
    price: 499,
    originalPrice: 699,
    image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=new%20house%20renovation%20cleaning%20service%20post%20construction&image_size=square_hd',
    tags: ['新房开荒', '深度清洁'],
    duration: '6-8小时',
    area: '全屋120㎡以内',
    description: '装修后全方位清洁，去除水泥、涂料等装修残留',
    sales: 234
  }
];

// 套餐卡数据
export const packageCardList = [
  {
    id: 1,
    cardNo: 'LJ2024001',
    name: '黄金会员卡',
    packageId: 1,
    packageName: '日常清洁套餐',
    totalTimes: 12,
    usedTimes: 3,
    remainingTimes: 9,
    expireDate: '2025-12-31',
    status: 1,
    createTime: '2024-01-15 10:30:00'
  },
  {
    id: 2,
    cardNo: 'LJ2024002',
    name: '钻石会员卡',
    packageId: 2,
    packageName: '深度清洁套餐',
    totalTimes: 6,
    usedTimes: 2,
    remainingTimes: 4,
    expireDate: '2025-06-30',
    status: 1,
    createTime: '2024-02-20 14:20:00'
  },
  {
    id: 3,
    cardNo: 'LJ2024003',
    name: '厨房清洁年卡',
    packageId: 3,
    packageName: '厨房专项清洁',
    totalTimes: 12,
    usedTimes: 12,
    remainingTimes: 0,
    expireDate: '2024-12-31',
    status: 2,
    createTime: '2024-01-01 09:00:00'
  }
];

// 家庭档案数据
export const familyList = [
  {
    id: 1,
    userId: 1,
    name: '张先生家',
    isDefault: 1,
    area: 120,
    roomType: '三室一厅',
    address: '北京市朝阳区望京SOHO T1-1001',
    memberCount: 4,
    hasChildren: 1,
    hasPets: 0,
    createTime: '2024-01-10 08:00:00',
    members: [
      { id: 1, name: '张先生', relation: '本人', phone: '13800138001' },
      { id: 2, name: '张太太', relation: '配偶', phone: '13800138002' },
      { id: 3, name: '张小宝', relation: '儿子', phone: '13800138003' },
      { id: 4, name: '张奶奶', relation: '母亲', phone: '13800138004' }
    ]
  },
  {
    id: 2,
    userId: 1,
    name: '李女士家',
    isDefault: 0,
    area: 90,
    roomType: '两室一厅',
    address: '北京市海淀区中关村大街1号',
    memberCount: 2,
    hasChildren: 0,
    hasPets: 1,
    createTime: '2024-02-15 10:30:00',
    members: [
      { id: 5, name: '李女士', relation: '本人', phone: '13900139001' },
      { id: 6, name: '李先生', relation: '配偶', phone: '13900139002' }
    ]
  }
];

// 用户数据
export const userList = [
  {
    id: 1,
    openId: 'o123456789',
    name: '张三',
    phone: '13800138001',
    avatar: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=professional%20avatar%20male%20cleaning%20service%20staff&image_size=square_hd',
    status: 1,
    createTime: '2024-01-10 08:00:00'
  },
  {
    id: 2,
    openId: 'o987654321',
    name: '李四',
    phone: '13900139001',
    avatar: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=professional%20avatar%20female%20cleaning%20service%20staff&image_size=square_hd',
    status: 1,
    createTime: '2024-02-15 10:30:00'
  },
  {
    id: 3,
    openId: 'o111111111',
    name: '王五',
    phone: '13700137001',
    avatar: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=professional%20avatar%20male%20service%20technician&image_size=square_hd',
    status: 2,
    createTime: '2024-03-20 14:00:00'
  }
];

// 当前登录用户数据
export const currentUser = {
  id: 1,
  openId: 'o123456789',
  name: '张师傅',
  phone: '13800138001',
  avatar: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=professional%20avatar%20male%20cleaning%20service%20worker%20wearing%20uniform&image_size=square_hd',
  gender: 1,
  age: 35,
  workYears: 5,
  serviceCount: 256,
  goodRate: 98.5,
  status: 1,
  createTime: '2024-01-10 08:00:00'
};

// 地址数据
export const addressList = [
  {
    id: 1,
    userId: 1,
    name: '张先生',
    phone: '13800138001',
    province: '北京市',
    city: '北京市',
    district: '朝阳区',
    address: '望京SOHO T1-1001',
    isDefault: 1,
    createTime: '2024-01-10 08:00:00'
  },
  {
    id: 2,
    userId: 1,
    name: '李女士',
    phone: '13900139001',
    province: '北京市',
    city: '北京市',
    district: '海淀区',
    address: '中关村大街1号 科技大厦A座',
    isDefault: 0,
    createTime: '2024-02-15 10:30:00'
  }
];

// 服务单数据
export const orderList = [
  {
    id: 1,
    orderNo: 'LJ20240430001',
    packageName: '日常清洁套餐',
    customerName: '王女士',
    customerPhone: '13600136001',
    address: '北京市朝阳区某某小区1号楼101室',
    appointmentTime: '2024-05-01 09:00:00',
    status: 1,
    statusText: '待接单',
    price: 99,
    createTime: '2024-04-30 14:30:00'
  },
  {
    id: 2,
    orderNo: 'LJ20240429002',
    packageName: '深度清洁套餐',
    customerName: '李先生',
    customerPhone: '13500135001',
    address: '北京市海淀区某某小区2号楼202室',
    appointmentTime: '2024-04-30 14:00:00',
    status: 2,
    statusText: '已接单',
    price: 299,
    createTime: '2024-04-29 10:20:00'
  },
  {
    id: 3,
    orderNo: 'LJ20240428003',
    packageName: '厨房专项清洁',
    customerName: '赵女士',
    customerPhone: '13400134001',
    address: '北京市西城区某某小区3号楼303室',
    appointmentTime: '2024-04-28 09:00:00',
    status: 3,
    statusText: '进行中',
    price: 199,
    createTime: '2024-04-27 16:45:00'
  },
  {
    id: 4,
    orderNo: 'LJ20240427004',
    packageName: '卫生间清洁套餐',
    customerName: '孙先生',
    customerPhone: '13300133001',
    address: '北京市丰台区某某小区4号楼404室',
    appointmentTime: '2024-04-27 14:00:00',
    status: 4,
    statusText: '已完成',
    price: 159,
    createTime: '2024-04-26 09:30:00'
  },
  {
    id: 5,
    orderNo: 'LJ20240426005',
    packageName: '玻璃清洁套餐',
    customerName: '周女士',
    customerPhone: '13200132001',
    address: '北京市通州区某某小区5号楼505室',
    appointmentTime: '2024-04-26 10:00:00',
    status: 5,
    statusText: '已取消',
    price: 129,
    createTime: '2024-04-25 15:20:00'
  }
];
