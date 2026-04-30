package com.lianjia.entity;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "service_order")
public class ServiceOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String orderNo;  // 订单号
    
    @Column(nullable = false)
    private Long customerId;
    
    @Transient
    private String customerName;
    
    @Transient
    private String customerPhone;
    
    @Transient
    private String customerAddress;
    
    private Long staffId;  // 指派的服务人员ID
    
    @Transient
    private String staffName;
    
    @Transient
    private String staffPhone;
    
    private String serviceType;  // 服务类型：甲醛检测、甲醛治理、深度清洁等
    
    private String serviceContent;  // 服务内容描述
    
    private LocalDateTime scheduledTime;  // 预约上门时间
    
    private LocalDateTime actualStartTime;  // 实际开始时间
    
    private LocalDateTime actualEndTime;  // 实际结束时间
    
    private String serviceAddress;  // 服务地址
    
    private BigDecimal serviceFee;  // 服务费用
    
    private String status;  // 订单状态：待派单、待服务、服务中、已完成、已取消
    
    private String progress;  // 服务进度描述
    
    private String customerFeedback;  // 客户反馈
    
    private Double rating;  // 客户评分
    
    private String remark;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
    
    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        if (status == null) {
            status = "待派单";
        }
        if (orderNo == null) {
            orderNo = generateOrderNo();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
    
    private String generateOrderNo() {
        return "SO" + System.currentTimeMillis();
    }
}
