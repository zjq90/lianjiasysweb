-- =============================================
-- 恋家环保小程序测试数据
-- =============================================

USE lianjia_env;

-- =============================================
-- 1. 标签数据
-- =============================================
INSERT INTO sys_tag (tag_name, tag_code, category, sort, status) VALUES
('有宠物', 'has_pet', 'family', 1, 1),
('有宝宝', 'has_baby', 'family', 2, 1),
('新装修', 'new_decorate', 'family', 3, 1),
('鼻炎咽炎过敏体质', 'allergy', 'family', 4, 1),
('有老人', 'has_elder', 'family', 5, 1),
('上班族', 'office_worker', 'user', 1, 1),
('宝妈', 'mom', 'user', 2, 1),
('养宠人士', 'pet_owner', 'user', 3, 1),
('新房业主', 'new_house_owner', 'user', 4, 1),
('甲醛检测', 'formaldehyde_detect', 'service', 1, 1),
('空气治理', 'air_treatment', 'service', 2, 1),
('深度清洁', 'deep_clean', 'service', 3, 1),
('消毒杀菌', 'disinfection', 'service', 4, 1);

-- =============================================
-- 2. 服务分类数据
-- =============================================
INSERT INTO service_category (parent_id, category_name, category_icon, sort, status) VALUES
(0, '空气检测', '/images/category/air_detect.png', 1, 1),
(0, '空气治理', '/images/category/air_treatment.png', 2, 1),
(0, '深度清洁', '/images/category/deep_clean.png', 3, 1),
(0, '消毒杀菌', '/images/category/disinfection.png', 4, 1),
(0, '除螨服务', '/images/category/dust_mite.png', 5, 1);

-- =============================================
-- 3. 服务项目数据
-- =============================================
INSERT INTO service_item (category_id, service_name, service_code, service_icon, service_images, service_desc, service_detail, price, market_price, unit, duration, sort, sales_count, status) VALUES
(1, '甲醛检测(上门)', 'formaldehyde_detect_onsite', '/images/service/甲醛检测.png', '/images/service/甲醛检测_1.png,/images/service/甲醛检测_2.png', '专业工程师上门采样，实验室精准分析，出具权威检测报告', '服务包含：上门采样1次，实验室分析，出具检测报告', 199.00, 299.00, '次', 60, 1, 1568, 1),
(1, 'TVOC全面检测', 'tvoc_full_detect', '/images/service/TVOC检测.png', '/images/service/TVOC检测_1.png', '检测甲醛、苯、甲苯、二甲苯、TVOC五项指标', '全面检测室内空气质量五项关键指标', 399.00, 599.00, '次', 90, 2, 892, 1),
(2, '甲醛治理(基础版)', 'formaldehyde_treatment_basic', '/images/service/除甲醛服务.png', '/images/service/除甲醛_1.png', '采用进口光触媒，有效分解甲醛，质保5年', '基础版甲醛治理服务，适合普通住宅', 2999.00, 3999.00, '套', 240, 1, 456, 1),
(2, '甲醛治理(豪华版)', 'formaldehyde_treatment_premium', '/images/service/豪华版除甲醛.png', '/images/service/豪华版_1.png', '豪华版甲醛治理，包含母婴级安全配方，质保10年', '豪华版甲醛治理服务，母婴级安全', 5999.00, 7999.00, '套', 360, 2, 234, 1),
(3, '全屋深度清洁', 'whole_house_deep_clean', '/images/service/深度清洁.png', '/images/service/深度清洁_1.png', '全屋深度清洁，包含厨房、卫生间、卧室、客厅等区域', '专业保洁团队，深度清洁每一个角落', 299.00, 399.00, '次', 180, 1, 2156, 1),
(4, '全屋消毒杀菌', 'whole_house_disinfection', '/images/service/消毒杀菌.png', '/images/service/消毒_1.png', '采用医用级消毒产品，全面杀灭细菌病毒', '专业消毒服务，守护家庭健康', 199.00, 299.00, '次', 120, 1, 1876, 1),
(5, '床垫除螨服务', 'mattress_dust_mite', '/images/service/除螨服务.png', '/images/service/除螨_1.png', '专业除螨设备，深层清除床垫螨虫', '床垫深度除螨，还您健康睡眠', 159.00, 259.00, '张', 60, 1, 3254, 1);

-- =============================================
-- 4. 套餐数据
-- =============================================
INSERT INTO service_package (package_name, package_code, package_icon, package_images, package_desc, package_detail, original_price, sale_price, total_count, valid_days, sort, sales_count, status) VALUES
('宠物家庭健康年卡', 'pet_family_health_year', '/images/package/宠物家庭年卡.png', '/images/package/宠物年卡_1.png', '专为养宠家庭设计，包含除螨、消毒、检测等服务，全年呵护家人健康', '宠物家庭专属健康年卡，包含：\n1. 全屋消毒杀菌 x 4次\n2. 床垫除螨服务 x 6张\n3. TVOC全面检测 x 1次', 2999.00, 1999.00, 11, 365, 1, 128, 1),
('婴幼儿呼吸成长卡', 'baby_respiratory_growth', '/images/package/婴幼儿成长卡.png', '/images/package/婴幼儿卡_1.png', '专为有婴幼儿的家庭设计，包含空气治理、定期检测，守护宝宝健康成长', '婴幼儿呼吸健康守护卡，包含：\n1. 甲醛治理(豪华版) x 1套\n2. TVOC全面检测 x 2次\n3. 全屋消毒杀菌 x 4次', 8999.00, 5999.00, 7, 365, 2, 86, 1),
('新房入住安心包', 'new_house_checkin_package', '/images/package/新房安心包.png', '/images/package/新房包_1.png', '新装修家庭必备，包含检测+治理+复检，放心入住', '新房入住安心包，包含：\n1. TVOC全面检测 x 2次\n2. 甲醛治理(基础版) x 1套\n3. 全屋深度清洁 x 1次', 4599.00, 2999.00, 4, 180, 3, 256, 1),
('过敏体质守护年卡', 'allergy_protection_year', '/images/package/过敏体质守护卡.png', '/images/package/过敏卡_1.png', '专为鼻炎咽炎过敏体质人群设计，定期清洁消毒，改善生活环境', '过敏体质守护年卡，包含：\n1. 全屋深度清洁 x 6次\n2. 全屋消毒杀菌 x 6次\n3. 床垫除螨服务 x 12张\n4. TVOC全面检测 x 2次', 5999.00, 3999.00, 26, 365, 4, 68, 1);

-- =============================================
-- 5. 套餐服务关联数据
-- =============================================
INSERT INTO package_service_rel (package_id, service_id, service_count, sort) VALUES
(1, 6, 4, 1),
(1, 7, 6, 2),
(1, 2, 1, 3),
(2, 4, 1, 1),
(2, 2, 2, 2),
(2, 6, 4, 3),
(3, 2, 2, 1),
(3, 3, 1, 2),
(3, 5, 1, 3),
(4, 5, 6, 1),
(4, 6, 6, 2),
(4, 7, 12, 3),
(4, 2, 2, 4);

-- =============================================
-- 6. 套餐标签关联数据(用于场景化推荐)
-- =============================================
INSERT INTO package_tag_rel (package_id, tag_id) VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 4);

-- =============================================
-- 7. 用户数据
-- =============================================
INSERT INTO sys_user (open_id, union_id, phone, nickname, avatar, gender, balance, status, last_login_time) VALUES
('o1234567890abcdef', 'u1234567890abcdef', '13800138001', '张三', '/images/avatar/avatar1.png', 1, 500.00, 1, NOW()),
('o1234567890abcdeg', 'u1234567890abcdeg', '13800138002', '李女士', '/images/avatar/avatar2.png', 2, 1200.50, 1, NOW()),
('o1234567890abcdeh', 'u1234567890abcdeh', '13800138003', '王先生', '/images/avatar/avatar3.png', 1, 0.00, 1, NOW()),
('o1234567890abcdei', 'u1234567890abcdei', '13800138004', '陈宝妈', '/images/avatar/avatar4.png', 2, 3500.00, 1, NOW()),
('o1234567890abcdej', 'u1234567890abcdej', '13800138005', '刘先生', '/images/avatar/avatar5.png', 1, 888.88, 1, NOW());

-- =============================================
-- 8. 用户地址数据
-- =============================================
INSERT INTO user_address (user_id, name, phone, province, city, district, detail_address, house_area, house_type, is_default, status) VALUES
(1, '张三', '13800138001', '广东省', '深圳市', '南山区', '科技园南区A栋1203室', 85.50, '新房', 1, 1),
(1, '张三', '13800138001', '广东省', '深圳市', '福田区', '中心广场B座2105室', 120.00, '二手房', 0, 1),
(2, '李女士', '13800138002', '广东省', '广州市', '天河区', '珠江新城富力中心1802室', 95.00, '新房', 1, 1),
(3, '王先生', '13800138003', '广东省', '深圳市', '宝安区', '新安街道创业一路1004号', 78.00, '出租房', 1, 1),
(4, '陈宝妈', '13800138004', '广东省', '广州市', '番禺区', '南村万博万达华府C座1503室', 140.00, '新房', 1, 1),
(5, '刘先生', '13800138005', '广东省', '深圳市', '龙华区', '民治街道红山地铁站旁', 105.00, '新房', 1, 1);

-- =============================================
-- 9. 师傅数据
-- =============================================
INSERT INTO worker_info (user_id, name, phone, id_card, avatar, skill, level, work_years, status) VALUES
(NULL, '王师傅', '13900139001', '440101198501011234', '/images/worker/worker1.png', '甲醛治理、空气检测', 3, 8, 1),
(NULL, '李师傅', '13900139002', '440101198802022345', '/images/worker/worker2.png', '深度清洁、消毒杀菌', 2, 5, 1),
(NULL, '张师傅', '13900139003', '440101199003033456', '/images/worker/worker3.png', '除螨服务、空气检测', 2, 6, 1),
(NULL, '刘师傅', '13900139004', '440101198204044567', '/images/worker/worker4.png', '甲醛治理、全屋清洁', 4, 12, 1),
(NULL, '陈师傅', '13900139005', '440101198705055678', '/images/worker/worker5.png', '消毒杀菌、除螨服务', 3, 7, 1);

-- =============================================
-- 10. 家庭档案数据
-- =============================================
INSERT INTO family_profile (user_id, family_name, address_id, house_area, house_type, decorate_date, last_service_date, next_service_date, has_reminder, status) VALUES
(1, '我的小家', 1, 85.50, '新房', '2023-06-15', '2024-01-15', '2024-07-15', 0, 1),
(2, '温馨小窝', 3, 95.00, '新房', '2023-08-20', '2024-02-20', '2024-04-20', 1, 1),
(3, '租房生活', 4, 78.00, '出租房', '2022-05-10', '2024-03-01', '2024-06-01', 0, 1),
(4, '宝宝的家', 5, 140.00, '新房', '2023-10-01', '2024-01-20', '2024-04-20', 1, 1),
(5, '幸福之家', 6, 105.00, '新房', '2023-09-15', '2024-02-15', '2024-05-15', 0, 1);

-- =============================================
-- 11. 家庭成员数据
-- =============================================
INSERT INTO family_member (family_id, member_type, member_name, gender, birthday, health_status, note, status) VALUES
(1, 'adult', '张三', 1, '1990-01-15', '健康', '户主', 1),
(1, 'adult', '张三老婆', 2, '1992-05-20', '健康', '', 1),
(1, 'pet', '旺财', 0, '2021-03-10', '健康', '金毛犬', 1),
(2, 'adult', '李女士', 2, '1988-08-08', '鼻炎', '有过敏性鼻炎', 1),
(2, 'child', '小宝', 1, '2022-02-14', '健康', '2岁男宝', 1),
(3, 'adult', '王先生', 1, '1991-11-11', '健康', '单身', 1),
(4, 'adult', '陈宝妈', 2, '1993-04-20', '健康', '', 1),
(4, 'child', '大宝', 1, '2020-06-01', '健康', '3岁', 1),
(4, 'child', '小宝', 2, '2023-01-15', '健康', '1岁', 1),
(5, 'adult', '刘先生', 1, '1987-09-09', '咽炎', '慢性咽炎', 1),
(5, 'adult', '刘太太', 2, '1989-12-12', '健康', '', 1),
(5, 'elder', '刘父', 1, '1958-03-05', '高血压', '66岁', 1),
(5, 'elder', '刘母', 2, '1960-07-18', '糖尿病', '64岁', 1);

-- =============================================
-- 12. 家庭标签关联数据
-- =============================================
INSERT INTO family_tag_rel (family_id, tag_id) VALUES
(1, 1),
(2, 4),
(2, 2),
(4, 2),
(4, 3),
(5, 4),
(5, 5);

-- =============================================
-- 13. 用户标签关联数据
-- =============================================
INSERT INTO user_tag_rel (user_id, tag_id) VALUES
(1, 3),
(2, 7),
(3, 6),
(4, 7),
(4, 9),
(5, 4);

-- =============================================
-- 14. 会员卡类型数据
-- =============================================
INSERT INTO member_card_type (card_name, card_code, card_level, card_icon, card_color, price, valid_days, discount_rate, benefit_desc, status) VALUES
('银卡会员', 'silver_card', 1, '/images/card/silver.png', '#C0C0C0', 99.00, 365, 0.95, '银卡会员权益：\n1. 全场服务95折\n2. 每月赠送50元优惠券\n3. 专属客服', 1),
('金卡会员', 'gold_card', 2, '/images/card/gold.png', '#FFD700', 299.00, 365, 0.90, '金卡会员权益：\n1. 全场服务9折\n2. 每月赠送100元优惠券\n3. 专属客服\n4. 生日礼包\n5. 优先预约', 1),
('钻石卡会员', 'diamond_card', 3, '/images/card/diamond.png', '#B9F2FF', 999.00, 365, 0.80, '钻石卡会员权益：\n1. 全场服务8折\n2. 每月赠送200元优惠券\n3. 专属VIP客服\n4. 生日大礼包\n5. 优先预约\n6. 免费上门检测1次/季度\n7. 专属定制服务方案', 1);

-- =============================================
-- 15. 用户会员卡数据
-- =============================================
INSERT INTO user_member_card (user_id, card_type_id, card_no, total_count, used_count, balance, valid_start_date, valid_end_date, status, source) VALUES
(1, 2, 'C202401010001', 0, 0, 0.00, '2024-01-01', '2025-01-01', 1, 'purchase'),
(2, 3, 'C202401150002', 0, 0, 0.00, '2024-01-15', '2025-01-15', 1, 'purchase'),
(4, 2, 'C202402010003', 0, 0, 0.00, '2024-02-01', '2025-02-01', 1, 'gift'),
(5, 1, 'C202401200004', 0, 0, 0.00, '2024-01-20', '2025-01-20', 1, 'purchase');

-- =============================================
-- 16. 用户套餐卡数据
-- =============================================
INSERT INTO user_package_card (user_id, package_id, order_id, total_count, used_count, valid_start_date, valid_end_date, status) VALUES
(1, 1, NULL, 11, 3, '2024-01-01', '2025-01-01', 1),
(4, 2, NULL, 7, 1, '2024-02-01', '2025-02-01', 1),
(5, 4, NULL, 26, 5, '2024-01-20', '2025-01-20', 1);

-- =============================================
-- 17. 优惠券数据
-- =============================================
INSERT INTO coupon (coupon_name, coupon_code, coupon_type, discount_value, min_amount, max_discount, valid_start_date, valid_end_date, valid_days, total_count, used_count, limit_per_user, apply_type, apply_ids, status) VALUES
('新用户专享券', 'new_user_2024', 'cash', 50.00, 199.00, NULL, '2024-01-01', '2024-12-31', NULL, 10000, 1256, 1, 'all', NULL, 1),
('满500减100券', 'full_500_100', 'full_reduction', 100.00, 500.00, NULL, '2024-01-01', '2024-06-30', NULL, 5000, 892, 2, 'all', NULL, 1),
('8折优惠券', 'discount_80', 'discount', 0.80, 299.00, 200.00, '2024-01-01', '2024-12-31', NULL, 8000, 2156, 3, 'all', NULL, 1),
('空气治理专享券', 'treatment_special', 'cash', 300.00, 2000.00, NULL, '2024-01-01', '2024-12-31', NULL, 2000, 456, 1, 'category', '2', 1),
('宠物家庭专属券', 'pet_family_special', 'cash', 100.00, 500.00, NULL, '2024-01-01', '2024-12-31', NULL, 3000, 628, 1, 'all', NULL, 1);

-- =============================================
-- 18. 优惠券标签关联数据
-- =============================================
INSERT INTO coupon_tag_rel (coupon_id, tag_id) VALUES
(5, 1);

-- =============================================
-- 19. 用户优惠券数据
-- =============================================
INSERT INTO user_coupon (user_id, coupon_id, coupon_code, receive_time, valid_start_date, valid_end_date, is_used, use_time, order_id, source, status) VALUES
(1, 1, 'new_user_2024_001', '2024-01-01 10:00:00', '2024-01-01', '2024-12-31', 1, '2024-01-15 14:30:00', 1, 'receive', 1),
(1, 2, 'full_500_100_001', '2024-01-01 10:00:00', '2024-01-01', '2024-06-30', 0, NULL, NULL, 'receive', 1),
(2, 3, 'discount_80_001', '2024-01-15 09:30:00', '2024-01-15', '2024-12-31', 0, NULL, NULL, 'gift', 1),
(2, 5, 'pet_family_special_001', '2024-01-15 09:30:00', '2024-01-15', '2024-12-31', 1, '2024-02-20 11:00:00', 2, 'promotion', 1),
(4, 1, 'new_user_2024_002', '2024-02-01 15:00:00', '2024-02-01', '2024-12-31', 0, NULL, NULL, 'receive', 1),
(4, 4, 'treatment_special_001', '2024-02-01 15:00:00', '2024-02-01', '2024-12-31', 1, '2024-02-15 16:00:00', 3, 'gift', 1),
(5, 3, 'discount_80_002', '2024-01-20 11:00:00', '2024-01-20', '2024-12-31', 0, NULL, NULL, 'receive', 1),
(5, 5, 'pet_family_special_002', '2024-01-20 11:00:00', '2024-01-20', '2024-12-31', 0, NULL, NULL, 'promotion', 1);

-- =============================================
-- 20. 推广活动数据
-- =============================================
INSERT INTO promotion_activity (activity_name, activity_code, activity_desc, share_title, share_desc, share_image, valid_start_date, valid_end_date, inviter_reward_type, inviter_reward_value, inviter_coupon_id, invitee_reward_type, invitee_reward_value, invitee_coupon_id, min_consume_amount, status) VALUES
('邀好友得好礼', 'invite_friend_2024', '邀请好友注册并消费，双方均可获得奖励', '邀请好友加入恋家环保，双方都有奖励！', '我在恋家环保体验了专业的空气治理服务，推荐给你，扫码注册即可领取新人优惠券！', '/images/promotion/invite_poster.png', '2024-01-01', '2024-12-31', 'coupon', 100.00, 2, 'coupon', 50.00, 1, 199.00, 1);

-- =============================================
-- 21. 订单数据
-- =============================================
INSERT INTO order_info (order_no, user_id, order_type, service_id, package_id, address_id, contact_name, contact_phone, service_date, service_time, quantity, original_amount, discount_amount, coupon_amount, pay_amount, pay_type, pay_time, order_status, remark, status) VALUES
('O202401150001', 1, 'package', NULL, 1, 1, '张三', '13800138001', '2024-01-20', '上午9:00-11:00', 1, 2999.00, 1000.00, 50.00, 1949.00, 'wechat', '2024-01-15 14:30:00', 'completed', '购买宠物家庭健康年卡', 1),
('O202402200002', 2, 'service', 6, NULL, 3, '李女士', '13800138002', '2024-02-25', '下午14:00-16:00', 1, 199.00, 0.00, 100.00, 99.00, 'balance', '2024-02-20 11:00:00', 'completed', '全屋消毒杀菌服务', 1),
('O202402150003', 4, 'package', NULL, 2, 5, '陈宝妈', '13800138004', '2024-02-20', '上午10:00-12:00', 1, 8999.00, 3000.00, 300.00, 5699.00, 'wechat', '2024-02-15 16:00:00', 'pending_service', '购买婴幼儿呼吸成长卡', 1),
('O202401200004', 5, 'service', 7, NULL, 6, '刘先生', '13800138005', '2024-01-25', '上午9:00-10:00', 2, 318.00, 0.00, 0.00, 318.00, 'wechat', '2024-01-20 15:30:00', 'completed', '床垫除螨服务2张', 1),
('O202403010005', 3, 'service', 1, NULL, 4, '王先生', '13800138003', '2024-03-05', '下午15:00-16:00', 1, 199.00, 0.00, 0.00, 199.00, NULL, NULL, 'pending_pay', '甲醛检测服务', 1);

-- =============================================
-- 22. 任务单数据
-- =============================================
INSERT INTO task_order (task_no, order_id, worker_id, task_status, accept_time, start_time, complete_time, estimated_arrival_time, actual_arrival_time, note, status) VALUES
('T202401150001', 1, 1, 'completed', '2024-01-15 15:00:00', '2024-01-20 09:30:00', '2024-01-20 12:00:00', '2024-01-20 09:00:00', '2024-01-20 08:55:00', '年卡激活服务', 1),
('T202402200002', 2, 5, 'completed', '2024-02-20 11:30:00', '2024-02-25 14:15:00', '2024-02-25 16:00:00', '2024-02-25 14:00:00', '2024-02-25 14:10:00', '全屋消毒杀菌', 1),
('T202402150003', 3, 4, 'pending', NULL, NULL, NULL, NULL, NULL, '等待分配师傅', 1),
('T202401200004', 4, 3, 'completed', '2024-01-20 16:00:00', '2024-01-25 09:15:00', '2024-01-25 11:00:00', '2024-01-25 09:00:00', '2024-01-25 09:10:00', '两张床垫除螨', 1);

-- =============================================
-- 23. 签到记录数据
-- =============================================
INSERT INTO service_checkin (task_id, checkin_type, checkin_time, longitude, latitude, address, photo, status) VALUES
(1, 'arrive', '2024-01-20 08:55:00', 113.945678, 22.543210, '广东省深圳市南山区科技园南区A栋1203室', '/images/checkin/arrive_1.png', 1),
(1, 'start', '2024-01-20 09:30:00', 113.945678, 22.543210, '广东省深圳市南山区科技园南区A栋1203室', '/images/checkin/start_1.png', 1),
(1, 'complete', '2024-01-20 12:00:00', 113.945678, 22.543210, '广东省深圳市南山区科技园南区A栋1203室', '/images/checkin/complete_1.png', 1),
(2, 'arrive', '2024-02-25 14:10:00', 113.321456, 23.123456, '广东省广州市天河区珠江新城富力中心1802室', '/images/checkin/arrive_2.png', 1),
(2, 'start', '2024-02-25 14:15:00', 113.321456, 23.123456, '广东省广州市天河区珠江新城富力中心1802室', '/images/checkin/start_2.png', 1),
(2, 'complete', '2024-02-25 16:00:00', 113.321456, 23.123456, '广东省广州市天河区珠江新城富力中心1802室', '/images/checkin/complete_2.png', 1),
(4, 'arrive', '2024-01-25 09:10:00', 114.023456, 22.634567, '广东省深圳市龙华区民治街道红山地铁站旁', '/images/checkin/arrive_4.png', 1),
(4, 'start', '2024-01-25 09:15:00', 114.023456, 22.634567, '广东省深圳市龙华区民治街道红山地铁站旁', '/images/checkin/start_4.png', 1),
(4, 'complete', '2024-01-25 11:00:00', 114.023456, 22.634567, '广东省深圳市龙华区民治街道红山地铁站旁', '/images/checkin/complete_4.png', 1);

-- =============================================
-- 24. 服务报告数据
-- =============================================
INSERT INTO service_report (task_id, report_type, report_title, report_content, report_file, worker_sign, worker_sign_time, customer_sign, customer_sign_time, is_signed, status) VALUES
(1, 'detection', '甲醛检测报告', '检测结果：甲醛浓度0.05mg/m³，TVOC浓度0.3mg/m³，各项指标均符合国家标准。', '/images/report/detection_1.pdf', '/images/sign/worker_1.png', '2024-01-20 12:00:00', '/images/sign/customer_1.png', '2024-01-20 12:15:00', 1, 1),
(1, 'service', '年卡激活服务日志', '服务内容：\n1. 宠物家庭健康年卡激活\n2. 套餐服务内容讲解\n3. 注意事项告知', '/images/report/service_1.pdf', '/images/sign/worker_1.png', '2024-01-20 12:05:00', '/images/sign/customer_1.png', '2024-01-20 12:20:00', 1, 1),
(2, 'service', '全屋消毒杀菌服务日志', '服务内容：\n1. 客厅消毒\n2. 卧室消毒\n3. 厨房消毒\n4. 卫生间消毒\n使用医用级消毒剂，对物表和空气进行全面消毒。', '/images/report/service_2.pdf', '/images/sign/worker_5.png', '2024-02-25 16:00:00', '/images/sign/customer_2.png', '2024-02-25 16:15:00', 1, 1),
(4, 'service', '床垫除螨服务日志', '服务内容：\n1. 主卧床垫除螨\n2. 次卧床垫除螨\n使用专业除螨设备，深层清除螨虫和过敏原。', '/images/report/service_4.pdf', '/images/sign/worker_3.png', '2024-01-25 11:00:00', '/images/sign/customer_5.png', '2024-01-25 11:20:00', 1, 1);

-- =============================================
-- 25. 服务照片数据
-- =============================================
INSERT INTO service_photo (task_id, photo_type, photo_url, photo_desc, sort, status) VALUES
(1, 'before', '/images/photo/task1_before_1.png', '客厅施工前', 1, 1),
(1, 'before', '/images/photo/task1_before_2.png', '卧室施工前', 2, 1),
(1, 'after', '/images/photo/task1_after_1.png', '客厅施工后', 1, 1),
(1, 'after', '/images/photo/task1_after_2.png', '卧室施工后', 2, 1),
(2, 'before', '/images/photo/task2_before_1.png', '客厅消毒前', 1, 1),
(2, 'after', '/images/photo/task2_after_1.png', '客厅消毒后', 1, 1),
(4, 'before', '/images/photo/task4_before_1.png', '主卧床垫除螨前', 1, 1),
(4, 'before', '/images/photo/task4_before_2.png', '次卧床垫除螨前', 2, 1),
(4, 'after', '/images/photo/task4_after_1.png', '主卧床垫除螨后', 1, 1),
(4, 'after', '/images/photo/task4_after_2.png', '次卧床垫除螨后', 2, 1);

-- =============================================
-- 26. 推广记录数据
-- =============================================
INSERT INTO promotion_record (activity_id, inviter_user_id, invitee_user_id, invitee_phone, share_channel, is_registered, register_time, is_consumed, consume_time, consume_order_id, is_rewarded, reward_time, status) VALUES
(1, 1, 3, '13800138003', 'wechat', 1, '2024-01-25 10:00:00', 1, '2024-03-01 15:00:00', 5, 1, '2024-03-02 09:00:00', 1),
(1, 2, NULL, '13800138100', 'moments', 0, NULL, 0, NULL, NULL, 0, NULL, 1),
(1, 5, NULL, '13800138101', 'qrcode', 0, NULL, 0, NULL, NULL, 0, NULL, 1);
