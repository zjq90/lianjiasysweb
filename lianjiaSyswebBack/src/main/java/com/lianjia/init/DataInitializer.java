package com.lianjia.init;

import com.lianjia.entity.*;
import com.lianjia.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private MerchantRepository merchantRepository;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private FamilyArchiveRepository familyArchiveRepository;
    
    @Autowired
    private ConsumptionRecordRepository consumptionRecordRepository;
    
    @Autowired
    private ServiceStaffRepository serviceStaffRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    private final Random random = new Random();
    
    private final String[] familyTags = {"普通家庭", "VIP家庭", "特殊需求家庭", "环保家庭", "母婴家庭"};
    private final String[] memberLevels = {"普通会员", "银卡会员", "金卡会员", "钻石会员"};
    private final String[] serviceTypes = {"甲醛检测", "甲醛治理", "深度清洁", "空气净化", "消毒杀菌"};
    private final String[] paymentMethods = {"微信支付", "支付宝", "现金", "银行卡"};
    private final String[] statuses = {"待支付", "已支付", "已完成", "已取消"};
    private final String[] skills = {"甲醛检测", "甲醛治理", "深度清洁", "空气净化", "消毒杀菌"};
    private final String[] staffStatuses = {"空闲", "忙碌", "休假"};
    private final String[] genders = {"男", "女"};
    private final String[] housingTypes = {"普通住宅", "公寓", "别墅", "商住两用"};
    private final String[] decorationStyles = {"现代简约", "欧式风格", "中式风格", "美式风格", "北欧风格"};
    
    @Override
    public void run(String... args) {
        initMerchant();
        initServiceStaff();
        initCustomers();
    }
    
    private void initMerchant() {
        if (merchantRepository.count() == 0) {
            Merchant merchant = new Merchant();
            merchant.setUsername("admin");
            merchant.setPassword(passwordEncoder.encode("123456"));
            merchant.setMerchantName("恋家环保服务中心");
            merchant.setPhone("13800138000");
            merchant.setEmail("admin@lianjia.com");
            merchant.setAddress("北京市朝阳区建国路88号");
            merchant.setStatus(1);
            merchantRepository.save(merchant);
            System.out.println("=== 商户账户已创建 ===");
            System.out.println("用户名: admin");
            System.out.println("密码: 123456");
        }
    }
    
    private void initServiceStaff() {
        if (serviceStaffRepository.count() == 0) {
            String[] staffNames = {"张三", "李四", "王五", "赵六", "钱七", "孙八"};
            
            for (int i = 0; i < 6; i++) {
                ServiceStaff staff = new ServiceStaff();
                staff.setName(staffNames[i]);
                staff.setPhone("1390000" + String.format("%04d", 1000 + i));
                staff.setIdCard("110101199" + i + "0101" + String.format("%04d", 1000 + i));
                staff.setGender(genders[random.nextInt(genders.length)]);
                staff.setAge(25 + random.nextInt(20));
                staff.setSkill(skills[random.nextInt(skills.length)] + "," + skills[random.nextInt(skills.length)]);
                staff.setWorkArea("朝阳区" + (i + 1) + "号区域");
                staff.setStatus(staffStatuses[random.nextInt(2)]);
                staff.setOrderCount(random.nextInt(50));
                staff.setRating(4.0 + random.nextDouble());
                serviceStaffRepository.save(staff);
            }
            System.out.println("=== 服务人员数据已创建 ===");
        }
    }
    
    private void initCustomers() {
        if (customerRepository.count() == 0) {
            String[] customerNames = {"王小明", "李小红", "张伟", "刘芳", "陈强", 
                    "杨丽", "赵刚", "周敏", "吴杰", "郑艳",
                    "孙浩", "朱婷", "马超", "胡娟", "林阳"};
            
            for (int i = 0; i < 15; i++) {
                Customer customer = new Customer();
                customer.setName(customerNames[i]);
                customer.setPhone("1360000" + String.format("%04d", 1000 + i));
                customer.setIdCard("110102199" + (i % 10) + "0202" + String.format("%04d", 2000 + i));
                customer.setGender(genders[random.nextInt(genders.length)]);
                customer.setAge(25 + random.nextInt(40));
                customer.setAddress("北京市" + getRandomDistrict() + getRandomAddress());
                customer.setFamilyTag(familyTags[random.nextInt(familyTags.length)]);
                customer.setMemberLevel(memberLevels[random.nextInt(memberLevels.length)]);
                customer.setTotalConsumption(random.nextInt(500000));
                customer.setOrderCount(random.nextInt(20));
                customer.setStatus(1);
                
                customer = customerRepository.save(customer);
                
                createFamilyArchive(customer);
                createConsumptionRecords(customer);
            }
            System.out.println("=== 客户数据已创建 ===");
        }
    }
    
    private void createFamilyArchive(Customer customer) {
        FamilyArchive archive = new FamilyArchive();
        archive.setCustomerId(customer.getId());
        archive.setFamilyMemberCount(2 + random.nextInt(4));
        archive.setHasElderly(random.nextBoolean() ? "是" : "否");
        archive.setHasChildren(random.nextBoolean() ? "是" : "否");
        archive.setHasPets(random.nextBoolean() ? "是" : "否");
        archive.setHousingType(housingTypes[random.nextInt(housingTypes.length)]);
        archive.setHousingArea(60.0 + random.nextDouble() * 200.0);
        archive.setDecorationStyle(decorationStyles[random.nextInt(decorationStyles.length)]);
        archive.setSpecialNeeds(random.nextBoolean() ? "需要特殊清洁服务" : null);
        archive.setAllergyInfo(random.nextBoolean() ? "对花粉过敏" : null);
        familyArchiveRepository.save(archive);
    }
    
    private void createConsumptionRecords(Customer customer) {
        int recordCount = 1 + random.nextInt(5);
        
        for (int i = 0; i < recordCount; i++) {
            ConsumptionRecord record = new ConsumptionRecord();
            record.setCustomerId(customer.getId());
            record.setOrderNo("CR" + System.currentTimeMillis() + i);
            record.setServiceType(serviceTypes[random.nextInt(serviceTypes.length)]);
            
            int amount = 5000 + random.nextInt(50000);
            record.setAmount(new BigDecimal(amount));
            record.setDiscountAmount(new BigDecimal(random.nextInt(amount / 10)));
            record.setActualAmount(record.getAmount().subtract(record.getDiscountAmount()));
            
            record.setPaymentMethod(paymentMethods[random.nextInt(paymentMethods.length)]);
            record.setStatus(statuses[random.nextInt(statuses.length - 1)]);
            record.setConsumptionTime(LocalDateTime.now().minusDays(random.nextInt(365)));
            consumptionRecordRepository.save(record);
        }
    }
    
    private String getRandomDistrict() {
        String[] districts = {"朝阳区", "海淀区", "东城区", "西城区", "丰台区", "石景山区"};
        return districts[random.nextInt(districts.length)];
    }
    
    private String getRandomAddress() {
        String[] roads = {"建国路", "长安街", "中关村大街", "三里河路", "朝阳路", "平安大街"};
        return roads[random.nextInt(roads.length)] + (random.nextInt(100) + 1) + "号";
    }
}
