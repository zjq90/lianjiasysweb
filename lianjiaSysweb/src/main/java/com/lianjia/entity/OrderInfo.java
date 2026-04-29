package com.lianjia.entity;

import com.lianjia.common.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "订单实体")
public class OrderInfo extends BaseEntity {

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "订单类型: service-单次服务, package-套餐购买")
    private String orderType;

    @ApiModelProperty(value = "服务ID(单次服务)")
    private Long serviceId;

    @ApiModelProperty(value = "套餐ID")
    private Long packageId;

    @ApiModelProperty(value = "地址ID")
    private Long addressId;

    @ApiModelProperty(value = "联系人姓名")
    private String contactName;

    @ApiModelProperty(value = "联系人电话")
    private String contactPhone;

    @ApiModelProperty(value = "服务日期")
    private LocalDate serviceDate;

    @ApiModelProperty(value = "服务时间段")
    private String serviceTime;

    @ApiModelProperty(value = "数量")
    private Integer quantity;

    @ApiModelProperty(value = "原价")
    private BigDecimal originalAmount;

    @ApiModelProperty(value = "优惠金额")
    private BigDecimal discountAmount;

    @ApiModelProperty(value = "优惠券金额")
    private BigDecimal couponAmount;

    @ApiModelProperty(value = "实付金额")
    private BigDecimal payAmount;

    @ApiModelProperty(value = "支付方式: wechat-微信, balance-余额")
    private String payType;

    @ApiModelProperty(value = "支付时间")
    private LocalDateTime payTime;

    @ApiModelProperty(value = "订单状态: pending_pay-待支付, pending_confirm-待确认, pending_service-待服务, in_service-服务中, pending_sign-待签字, completed-已完成, cancelled-已取消, refunded-已退款")
    private String orderStatus;

    @ApiModelProperty(value = "取消原因")
    private String cancelReason;

    @ApiModelProperty(value = "取消时间")
    private LocalDateTime cancelTime;

    @ApiModelProperty(value = "备注")
    private String remark;
}
