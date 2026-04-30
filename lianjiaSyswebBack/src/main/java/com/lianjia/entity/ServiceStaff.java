package com.lianjia.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "service_staff")
public class ServiceStaff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    private String phone;
    
    private String idCard;
    
    private String gender;  // 男/女
    
    private Integer age;
    
    private String skill;  // 技能：甲醛检测、甲醛治理、深度清洁等，多个用逗号分隔
    
    private String workArea;  // 工作区域
    
    private String status;  // 状态：空闲、忙碌、休假、离职
    
    private Integer orderCount;  // 已完成订单数
    
    private Double rating;  // 评分
    
    private String remark;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
    
    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        if (orderCount == null) {
            orderCount = 0;
        }
        if (rating == null) {
            rating = 5.0;
        }
        if (status == null) {
            status = "空闲";
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
