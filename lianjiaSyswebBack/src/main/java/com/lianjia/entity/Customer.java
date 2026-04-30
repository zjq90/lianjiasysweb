package com.lianjia.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    private String phone;
    
    private String idCard;
    
    private String gender;  // 男/女
    
    private Integer age;
    
    private String address;
    
    private String familyTag;  // 家庭标签：普通家庭、VIP家庭、特殊需求家庭等
    
    private String memberLevel;  // 会员等级：普通会员、银卡会员、金卡会员、钻石会员
    
    private Integer totalConsumption;  // 累计消费金额（分）
    
    private Integer orderCount;  // 订单数量
    
    private String remark;
    
    private Integer status;  // 1:正常 0:禁用
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
    
    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        if (status == null) {
            status = 1;
        }
        if (totalConsumption == null) {
            totalConsumption = 0;
        }
        if (orderCount == null) {
            orderCount = 0;
        }
        if (memberLevel == null) {
            memberLevel = "普通会员";
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
