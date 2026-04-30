package com.lianjia.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "family_archive")
public class FamilyArchive {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long customerId;
    
    @Transient
    private String customerName;
    
    private Integer familyMemberCount;  // 家庭成员数量
    
    private String hasElderly;  // 是否有老人
    
    private String hasChildren;  // 是否有小孩
    
    private String hasPets;  // 是否有宠物
    
    private String housingType;  // 房屋类型：公寓、别墅、普通住宅等
    
    private Double housingArea;  // 房屋面积（平方米）
    
    private String decorationStyle;  // 装修风格
    
    private String specialNeeds;  // 特殊需求
    
    private String allergyInfo;  // 过敏信息
    
    private String remark;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
    
    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
