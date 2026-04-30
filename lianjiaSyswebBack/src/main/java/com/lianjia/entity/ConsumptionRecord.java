package com.lianjia.entity;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "consumption_record")
public class ConsumptionRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long customerId;
    
    @Transient
    private String customerName;
    
    private String orderNo;  // 订单号
    
    private String serviceType;  // 服务类型：甲醛检测、甲醛治理、深度清洁等
    
    private BigDecimal amount;  // 消费金额
    
    private BigDecimal discountAmount;  // 优惠金额
    
    private BigDecimal actualAmount;  // 实付金额
    
    private String paymentMethod;  // 支付方式：微信、支付宝、现金、银行卡等
    
    private String status;  // 订单状态：待支付、已支付、已取消、已退款
    
    private LocalDateTime consumptionTime;  // 消费时间
    
    private String remark;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
    
    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        if (consumptionTime == null) {
            consumptionTime = LocalDateTime.now();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
