package com.lianjia.entity;

import com.lianjia.common.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "优惠券实体")
public class Coupon extends BaseEntity {

    @ApiModelProperty(value = "优惠券名称")
    private String couponName;

    @ApiModelProperty(value = "优惠券编码")
    private String couponCode;

    @ApiModelProperty(value = "优惠券类型: discount-折扣券, cash-现金券, full_reduction-满减券")
    private String couponType;

    @ApiModelProperty(value = "折扣值: 折扣券是折扣率(0.8表示8折), 现金券是金额, 满减券是减免金额")
    private BigDecimal discountValue;

    @ApiModelProperty(value = "最低消费金额(满减券专用)")
    private BigDecimal minAmount;

    @ApiModelProperty(value = "最大优惠金额(折扣券专用)")
    private BigDecimal maxDiscount;

    @ApiModelProperty(value = "有效期开始")
    private LocalDate validStartDate;

    @ApiModelProperty(value = "有效期结束")
    private LocalDate validEndDate;

    @ApiModelProperty(value = "领取后有效天数")
    private Integer validDays;

    @ApiModelProperty(value = "发放总量(-1表示不限制)")
    private Integer totalCount;

    @ApiModelProperty(value = "已使用数量")
    private Integer usedCount;

    @ApiModelProperty(value = "每人限领数量")
    private Integer limitPerUser;

    @ApiModelProperty(value = "适用类型: all-全场通用, category-指定分类, service-指定服务, package-指定套餐")
    private String applyType;

    @ApiModelProperty(value = "适用ID列表(逗号分隔)")
    private String applyIds;

    @ApiModelProperty(value = "排除ID列表(逗号分隔)")
    private String excludeIds;
}
