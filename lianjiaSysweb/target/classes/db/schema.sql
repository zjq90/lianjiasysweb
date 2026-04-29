-- =============================================
-- 恋家环保小程序数据库表结构
-- 数据库: lianjia_env
-- 字符集: utf8mb4
-- =============================================

CREATE DATABASE IF NOT EXISTS lianjia_env DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE lianjia_env;

-- =============================================
-- 1. 基础字典表
-- =============================================

-- 标签表(用于场景化推荐和家庭标签)
CREATE TABLE IF NOT EXISTS sys_tag (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '标签ID',
    tag_name VARCHAR(50) NOT NULL COMMENT '标签名称',
    tag_code VARCHAR(50) NOT NULL COMMENT '标签编码',
    category VARCHAR(50) NOT NULL COMMENT '标签分类: user-用户, family-家庭, service-服务',
    sort INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_tag_code (tag_code),
    KEY idx_category (category)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='标签表';

-- =============================================
-- 2. 用户管理模块
-- =============================================

-- 用户表
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    open_id VARCHAR(64) COMMENT '微信openid',
    union_id VARCHAR(64) COMMENT '微信unionid',
    phone VARCHAR(20) COMMENT '手机号',
    nickname VARCHAR(50) COMMENT '昵称',
    avatar VARCHAR(255) COMMENT '头像',
    gender TINYINT DEFAULT 0 COMMENT '性别: 0-未知, 1-男, 2-女',
    balance DECIMAL(10,2) DEFAULT 0.00 COMMENT '账户余额',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-禁用, 1-正常',
    last_login_time DATETIME COMMENT '最后登录时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_open_id (open_id),
    UNIQUE KEY uk_phone (phone),
    KEY idx_union_id (union_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 用户地址表
CREATE TABLE IF NOT EXISTS user_address (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '地址ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    name VARCHAR(50) NOT NULL COMMENT '收货人姓名',
    phone VARCHAR(20) NOT NULL COMMENT '收货人电话',
    province VARCHAR(50) COMMENT '省份',
    city VARCHAR(50) COMMENT '城市',
    district VARCHAR(50) COMMENT '区县',
    detail_address VARCHAR(255) NOT NULL COMMENT '详细地址',
    house_area DECIMAL(10,2) COMMENT '房屋面积(平方米)',
    house_type VARCHAR(50) COMMENT '房屋类型: 新房, 二手房, 出租房等',
    is_default TINYINT DEFAULT 0 COMMENT '是否默认: 0-否, 1-是',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-删除, 1-正常',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    KEY idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户地址表';

-- 用户标签关联表
CREATE TABLE IF NOT EXISTS user_tag_rel (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    tag_id BIGINT NOT NULL COMMENT '标签ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_user_tag (user_id, tag_id),
    KEY idx_user_id (user_id),
    KEY idx_tag_id (tag_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户标签关联表';

-- 师傅/服务人员表
CREATE TABLE IF NOT EXISTS worker_info (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '师傅ID',
    user_id BIGINT COMMENT '关联用户ID(如果有小程序账号)',
    name VARCHAR(50) NOT NULL COMMENT '师傅姓名',
    phone VARCHAR(20) NOT NULL COMMENT '手机号',
    id_card VARCHAR(20) COMMENT '身份证号',
    avatar VARCHAR(255) COMMENT '头像',
    skill VARCHAR(255) COMMENT '技能',
    level TINYINT DEFAULT 1 COMMENT '等级: 1-初级, 2-中级, 3-高级, 4-专家',
    work_years INT DEFAULT 0 COMMENT '工作年限',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-禁用, 1-正常',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    KEY idx_user_id (user_id),
    KEY idx_phone (phone)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='师傅/服务人员表';

-- =============================================
-- 3. 服务/套餐管理模块
-- =============================================

-- 服务分类表
CREATE TABLE IF NOT EXISTS service_category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '分类ID',
    parent_id BIGINT DEFAULT 0 COMMENT '父分类ID, 0表示一级分类',
    category_name VARCHAR(50) NOT NULL COMMENT '分类名称',
    category_icon VARCHAR(255) COMMENT '分类图标',
    sort INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    KEY idx_parent_id (parent_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='服务分类表';

-- 服务项目表
CREATE TABLE IF NOT EXISTS service_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '服务ID',
    category_id BIGINT NOT NULL COMMENT '分类ID',
    service_name VARCHAR(100) NOT NULL COMMENT '服务名称',
    service_code VARCHAR(50) COMMENT '服务编码',
    service_icon VARCHAR(255) COMMENT '服务图标',
    service_images TEXT COMMENT '服务图片(多张用逗号分隔)',
    service_desc TEXT COMMENT '服务描述',
    service_detail TEXT COMMENT '服务详情(富文本)',
    price DECIMAL(10,2) NOT NULL COMMENT '服务价格',
    market_price DECIMAL(10,2) COMMENT '市场价格',
    unit VARCHAR(20) DEFAULT '次' COMMENT '单位: 次/小时/平方米等',
    duration INT COMMENT '服务时长(分钟)',
    sort INT DEFAULT 0 COMMENT '排序',
    sales_count INT DEFAULT 0 COMMENT '销量',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-下架, 1-上架',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    KEY idx_category_id (category_id),
    KEY idx_service_code (service_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='服务项目表';

-- 套餐表
CREATE TABLE IF NOT EXISTS service_package (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '套餐ID',
    package_name VARCHAR(100) NOT NULL COMMENT '套餐名称',
    package_code VARCHAR(50) COMMENT '套餐编码',
    package_icon VARCHAR(255) COMMENT '套餐图标',
    package_images TEXT COMMENT '套餐图片(多张用逗号分隔)',
    package_desc TEXT COMMENT '套餐描述',
    package_detail TEXT COMMENT '套餐详情(富文本)',
    original_price DECIMAL(10,2) COMMENT '原价',
    sale_price DECIMAL(10,2) NOT NULL COMMENT '售价',
    total_count INT DEFAULT 1 COMMENT '总次数',
    valid_days INT COMMENT '有效天数(从购买日开始计算)',
    valid_start_date DATE COMMENT '有效期开始日期',
    valid_end_date DATE COMMENT '有效期结束日期',
    sort INT DEFAULT 0 COMMENT '排序',
    sales_count INT DEFAULT 0 COMMENT '销量',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-下架, 1-上架',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    KEY idx_package_code (package_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='套餐表';

-- 套餐服务关联表
CREATE TABLE IF NOT EXISTS package_service_rel (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    package_id BIGINT NOT NULL COMMENT '套餐ID',
    service_id BIGINT NOT NULL COMMENT '服务ID',
    service_count INT DEFAULT 1 COMMENT '该服务在套餐中的数量',
    sort INT DEFAULT 0 COMMENT '排序',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_package_service (package_id, service_id),
    KEY idx_package_id (package_id),
    KEY idx_service_id (service_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='套餐服务关联表';

-- 套餐标签关联表(用于场景化推荐)
CREATE TABLE IF NOT EXISTS package_tag_rel (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    package_id BIGINT NOT NULL COMMENT '套餐ID',
    tag_id BIGINT NOT NULL COMMENT '标签ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_package_tag (package_id, tag_id),
    KEY idx_package_id (package_id),
    KEY idx_tag_id (tag_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='套餐标签关联表';

-- =============================================
-- 4. 家庭档案管理模块
-- =============================================

-- 家庭档案主表
CREATE TABLE IF NOT EXISTS family_profile (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '家庭档案ID',
    user_id BIGINT NOT NULL COMMENT '用户ID(户主)',
    family_name VARCHAR(50) COMMENT '家庭名称',
    address_id BIGINT COMMENT '关联地址ID',
    house_area DECIMAL(10,2) COMMENT '房屋面积',
    house_type VARCHAR(50) COMMENT '房屋类型',
    decorate_date DATE COMMENT '装修日期',
    last_service_date DATE COMMENT '上次服务日期',
    next_service_date DATE COMMENT '下次服务日期',
    has_reminder TINYINT DEFAULT 0 COMMENT '是否已提醒: 0-否, 1-是',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-删除, 1-正常',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    KEY idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='家庭档案主表';

-- 家庭成员表
CREATE TABLE IF NOT EXISTS family_member (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '成员ID',
    family_id BIGINT NOT NULL COMMENT '家庭档案ID',
    member_type VARCHAR(20) NOT NULL COMMENT '成员类型: adult-成人, child-儿童, elder-老人, pet-宠物',
    member_name VARCHAR(50) COMMENT '成员姓名/昵称',
    gender TINYINT DEFAULT 0 COMMENT '性别: 0-未知, 1-男, 2-女',
    birthday DATE COMMENT '出生日期',
    health_status VARCHAR(255) COMMENT '健康状况',
    note TEXT COMMENT '备注',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-删除, 1-正常',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    KEY idx_family_id (family_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='家庭成员表';

-- 家庭标签关联表
CREATE TABLE IF NOT EXISTS family_tag_rel (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    family_id BIGINT NOT NULL COMMENT '家庭档案ID',
    tag_id BIGINT NOT NULL COMMENT '标签ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_family_tag (family_id, tag_id),
    KEY idx_family_id (family_id),
    KEY idx_tag_id (tag_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='家庭标签关联表';

-- =============================================
-- 5. 预约订单管理模块
-- =============================================

-- 订单主表
CREATE TABLE IF NOT EXISTS order_info (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订单ID',
    order_no VARCHAR(32) NOT NULL COMMENT '订单编号',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    order_type VARCHAR(20) NOT NULL COMMENT '订单类型: service-单次服务, package-套餐购买',
    service_id BIGINT COMMENT '服务ID(单次服务)',
    package_id BIGINT COMMENT '套餐ID',
    address_id BIGINT NOT NULL COMMENT '地址ID',
    contact_name VARCHAR(50) NOT NULL COMMENT '联系人姓名',
    contact_phone VARCHAR(20) NOT NULL COMMENT '联系人电话',
    service_date DATE NOT NULL COMMENT '服务日期',
    service_time VARCHAR(20) NOT NULL COMMENT '服务时间段',
    quantity INT DEFAULT 1 COMMENT '数量',
    original_amount DECIMAL(10,2) DEFAULT 0.00 COMMENT '原价',
    discount_amount DECIMAL(10,2) DEFAULT 0.00 COMMENT '优惠金额',
    coupon_amount DECIMAL(10,2) DEFAULT 0.00 COMMENT '优惠券金额',
    pay_amount DECIMAL(10,2) NOT NULL COMMENT '实付金额',
    pay_type VARCHAR(20) COMMENT '支付方式: wechat-微信, balance-余额',
    pay_time DATETIME COMMENT '支付时间',
    order_status VARCHAR(20) DEFAULT 'pending_pay' COMMENT '订单状态: pending_pay-待支付, pending_confirm-待确认, pending_service-待服务, in_service-服务中, pending_sign-待签字, completed-已完成, cancelled-已取消, refunded-已退款',
    cancel_reason VARCHAR(255) COMMENT '取消原因',
    cancel_time DATETIME COMMENT '取消时间',
    remark TEXT COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_order_no (order_no),
    KEY idx_user_id (user_id),
    KEY idx_order_status (order_status),
    KEY idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单主表';

-- 订单服务明细表(套餐订单使用的服务明细)
CREATE TABLE IF NOT EXISTS order_service_detail (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    service_id BIGINT NOT NULL COMMENT '服务ID',
    service_name VARCHAR(100) COMMENT '服务名称(冗余)',
    unit_price DECIMAL(10,2) COMMENT '单价',
    quantity INT DEFAULT 1 COMMENT '数量',
    total_amount DECIMAL(10,2) COMMENT '小计',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    KEY idx_order_id (order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单服务明细表';

-- =============================================
-- 6. 服务履约模块
-- =============================================

-- 任务单表(师傅视角)
CREATE TABLE IF NOT EXISTS task_order (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '任务单ID',
    task_no VARCHAR(32) NOT NULL COMMENT '任务编号',
    order_id BIGINT NOT NULL COMMENT '关联订单ID',
    worker_id BIGINT NOT NULL COMMENT '师傅ID',
    task_status VARCHAR(20) DEFAULT 'pending' COMMENT '任务状态: pending-待接单, accepted-已接单, on_way-已出发, arrived-已到达, in_service-服务中, completed-已完成, cancelled-已取消',
    accept_time DATETIME COMMENT '接单时间',
    start_time DATETIME COMMENT '开始服务时间',
    complete_time DATETIME COMMENT '完成服务时间',
    estimated_arrival_time DATETIME COMMENT '预计到达时间',
    actual_arrival_time DATETIME COMMENT '实际到达时间',
    note TEXT COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_task_no (task_no),
    KEY idx_order_id (order_id),
    KEY idx_worker_id (worker_id),
    KEY idx_task_status (task_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务单表';

-- 签到记录表
CREATE TABLE IF NOT EXISTS service_checkin (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '签到ID',
    task_id BIGINT NOT NULL COMMENT '任务单ID',
    checkin_type VARCHAR(20) NOT NULL COMMENT '签到类型: arrive-到达, start-开始, complete-完成',
    checkin_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '签到时间',
    longitude DECIMAL(10,7) COMMENT '经度',
    latitude DECIMAL(10,7) COMMENT '纬度',
    address VARCHAR(255) COMMENT '签到地址',
    photo VARCHAR(255) COMMENT '签到照片',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    KEY idx_task_id (task_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='签到记录表';

-- 服务报告表
CREATE TABLE IF NOT EXISTS service_report (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '报告ID',
    task_id BIGINT NOT NULL COMMENT '任务单ID',
    report_type VARCHAR(20) NOT NULL COMMENT '报告类型: detection-检测报告, service-服务日志',
    report_title VARCHAR(100) COMMENT '报告标题',
    report_content TEXT COMMENT '报告内容',
    report_file VARCHAR(255) COMMENT '报告文件(可选)',
    worker_sign VARCHAR(255) COMMENT '师傅签字',
    worker_sign_time DATETIME COMMENT '师傅签字时间',
    customer_sign VARCHAR(255) COMMENT '客户签字',
    customer_sign_time DATETIME COMMENT '客户签字时间',
    is_signed TINYINT DEFAULT 0 COMMENT '是否已签字: 0-否, 1-是',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-删除, 1-正常',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    KEY idx_task_id (task_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='服务报告表';

-- 服务照片表(施工前后对比照)
CREATE TABLE IF NOT EXISTS service_photo (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '照片ID',
    task_id BIGINT NOT NULL COMMENT '任务单ID',
    photo_type VARCHAR(20) NOT NULL COMMENT '照片类型: before-施工前, after-施工后',
    photo_url VARCHAR(255) NOT NULL COMMENT '照片URL',
    photo_desc VARCHAR(255) COMMENT '照片描述',
    sort INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-删除, 1-正常',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    KEY idx_task_id (task_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='服务照片表';

-- =============================================
-- 7. 会员营销模块
-- =============================================

-- 会员卡类型表
CREATE TABLE IF NOT EXISTS member_card_type (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '卡类型ID',
    card_name VARCHAR(50) NOT NULL COMMENT '卡名称',
    card_code VARCHAR(50) NOT NULL COMMENT '卡编码',
    card_level INT DEFAULT 1 COMMENT '卡等级: 1-银卡, 2-金卡, 3-钻石卡',
    card_icon VARCHAR(255) COMMENT '卡图标',
    card_color VARCHAR(20) COMMENT '卡颜色',
    price DECIMAL(10,2) COMMENT '售价',
    valid_days INT COMMENT '有效天数',
    discount_rate DECIMAL(3,2) DEFAULT 1.00 COMMENT '折扣率(1.00表示不打折)',
    benefit_desc TEXT COMMENT '权益描述',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_card_code (card_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会员卡类型表';

-- 用户会员卡表
CREATE TABLE IF NOT EXISTS user_member_card (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    card_type_id BIGINT NOT NULL COMMENT '卡类型ID',
    card_no VARCHAR(32) NOT NULL COMMENT '卡号',
    total_count INT DEFAULT 0 COMMENT '总次数(如果是次数卡)',
    used_count INT DEFAULT 0 COMMENT '已用次数',
    balance DECIMAL(10,2) DEFAULT 0.00 COMMENT '余额(如果是储值卡)',
    valid_start_date DATE COMMENT '有效期开始',
    valid_end_date DATE COMMENT '有效期结束',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-禁用, 1-正常, 2-已过期, 3-已用完',
    source VARCHAR(20) COMMENT '来源: purchase-购买, gift-赠送, activity-活动',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_card_no (card_no),
    KEY idx_user_id (user_id),
    KEY idx_card_type_id (card_type_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户会员卡表';

-- 用户套餐卡表(购买的套餐)
CREATE TABLE IF NOT EXISTS user_package_card (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    package_id BIGINT NOT NULL COMMENT '套餐ID',
    order_id BIGINT COMMENT '关联订单ID',
    total_count INT NOT NULL COMMENT '总次数',
    used_count INT DEFAULT 0 COMMENT '已用次数',
    valid_start_date DATE COMMENT '有效期开始',
    valid_end_date DATE COMMENT '有效期结束',
    status TINYINT DEFAULT 1 COMMENT '状态: 1-正常, 2-已过期, 3-已用完',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    KEY idx_user_id (user_id),
    KEY idx_package_id (package_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户套餐卡表';

-- 优惠券表
CREATE TABLE IF NOT EXISTS coupon (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '优惠券ID',
    coupon_name VARCHAR(100) NOT NULL COMMENT '优惠券名称',
    coupon_code VARCHAR(50) NOT NULL COMMENT '优惠券编码',
    coupon_type VARCHAR(20) NOT NULL COMMENT '优惠券类型: discount-折扣券, cash-现金券, full_reduction-满减券',
    discount_value DECIMAL(10,2) COMMENT '折扣值: 折扣券是折扣率(0.8表示8折), 现金券是金额, 满减券是减免金额',
    min_amount DECIMAL(10,2) COMMENT '最低消费金额(满减券专用)',
    max_discount DECIMAL(10,2) COMMENT '最大优惠金额(折扣券专用)',
    valid_start_date DATE COMMENT '有效期开始',
    valid_end_date DATE COMMENT '有效期结束',
    valid_days INT COMMENT '领取后有效天数',
    total_count INT DEFAULT -1 COMMENT '发放总量(-1表示不限制)',
    used_count INT DEFAULT 0 COMMENT '已使用数量',
    limit_per_user INT DEFAULT 1 COMMENT '每人限领数量',
    apply_type VARCHAR(20) DEFAULT 'all' COMMENT '适用类型: all-全场通用, category-指定分类, service-指定服务, package-指定套餐',
    apply_ids TEXT COMMENT '适用ID列表(逗号分隔)',
    exclude_ids TEXT COMMENT '排除ID列表(逗号分隔)',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_coupon_code (coupon_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='优惠券表';

-- 用户优惠券表
CREATE TABLE IF NOT EXISTS user_coupon (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    coupon_id BIGINT NOT NULL COMMENT '优惠券ID',
    coupon_code VARCHAR(50) COMMENT '优惠券编码(冗余)',
    receive_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '领取时间',
    valid_start_date DATE COMMENT '有效期开始',
    valid_end_date DATE COMMENT '有效期结束',
    is_used TINYINT DEFAULT 0 COMMENT '是否使用: 0-否, 1-是',
    use_time DATETIME COMMENT '使用时间',
    order_id BIGINT COMMENT '使用的订单ID',
    source VARCHAR(20) COMMENT '来源: receive-主动领取, gift-系统赠送, promotion-推广奖励',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-失效, 1-正常',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    KEY idx_user_id (user_id),
    KEY idx_coupon_id (coupon_id),
    KEY idx_is_used (is_used)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户优惠券表';

-- 优惠券标签关联表(用于精准推送)
CREATE TABLE IF NOT EXISTS coupon_tag_rel (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    coupon_id BIGINT NOT NULL COMMENT '优惠券ID',
    tag_id BIGINT NOT NULL COMMENT '标签ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_coupon_tag (coupon_id, tag_id),
    KEY idx_coupon_id (coupon_id),
    KEY idx_tag_id (tag_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='优惠券标签关联表';

-- 推广活动表
CREATE TABLE IF NOT EXISTS promotion_activity (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '活动ID',
    activity_name VARCHAR(100) NOT NULL COMMENT '活动名称',
    activity_code VARCHAR(50) NOT NULL COMMENT '活动编码',
    activity_desc TEXT COMMENT '活动描述',
    share_title VARCHAR(100) COMMENT '分享标题',
    share_desc VARCHAR(255) COMMENT '分享描述',
    share_image VARCHAR(255) COMMENT '分享图片',
    valid_start_date DATE COMMENT '有效期开始',
    valid_end_date DATE COMMENT '有效期结束',
    inviter_reward_type VARCHAR(20) COMMENT '邀请人奖励类型: coupon-优惠券, balance-余额',
    inviter_reward_value DECIMAL(10,2) COMMENT '邀请人奖励值',
    inviter_coupon_id BIGINT COMMENT '邀请人优惠券ID(如果是优惠券奖励)',
    invitee_reward_type VARCHAR(20) COMMENT '被邀请人奖励类型: coupon-优惠券, balance-余额',
    invitee_reward_value DECIMAL(10,2) COMMENT '被邀请人奖励值',
    invitee_coupon_id BIGINT COMMENT '被邀请人优惠券ID(如果是优惠券奖励)',
    min_consume_amount DECIMAL(10,2) COMMENT '最低消费金额(被邀请人需要消费此金额才发放奖励)',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_activity_code (activity_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='推广活动表';

-- 推广记录表
CREATE TABLE IF NOT EXISTS promotion_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '记录ID',
    activity_id BIGINT NOT NULL COMMENT '活动ID',
    inviter_user_id BIGINT NOT NULL COMMENT '邀请人用户ID',
    invitee_user_id BIGINT COMMENT '被邀请人用户ID(注册后)',
    invitee_phone VARCHAR(20) COMMENT '被邀请人手机号',
    share_channel VARCHAR(20) COMMENT '分享渠道: moments-朋友圈, wechat-微信好友, qq-QQ, weibo-微博, qrcode-二维码',
    is_registered TINYINT DEFAULT 0 COMMENT '是否已注册: 0-否, 1-是',
    register_time DATETIME COMMENT '注册时间',
    is_consumed TINYINT DEFAULT 0 COMMENT '是否已消费: 0-否, 1-是',
    consume_time DATETIME COMMENT '消费时间',
    consume_order_id BIGINT COMMENT '消费订单ID',
    is_rewarded TINYINT DEFAULT 0 COMMENT '是否已发放奖励: 0-否, 1-是',
    reward_time DATETIME COMMENT '奖励发放时间',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-失效, 1-正常',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    KEY idx_inviter_user_id (inviter_user_id),
    KEY idx_invitee_user_id (invitee_user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='推广记录表';

-- =============================================
-- 8. 操作日志表
-- =============================================

-- 系统操作日志
CREATE TABLE IF NOT EXISTS sys_oper_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '日志ID',
    user_id BIGINT COMMENT '操作用户ID',
    module VARCHAR(50) COMMENT '操作模块',
    operation VARCHAR(100) COMMENT '操作内容',
    method VARCHAR(255) COMMENT '请求方法',
    request_url VARCHAR(255) COMMENT '请求URL',
    request_param TEXT COMMENT '请求参数',
    response_param TEXT COMMENT '响应参数',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-失败, 1-成功',
    error_msg TEXT COMMENT '错误信息',
    execute_time BIGINT COMMENT '执行耗时(毫秒)',
    ip VARCHAR(50) COMMENT 'IP地址',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    KEY idx_user_id (user_id),
    KEY idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统操作日志表';
