package com.lianjia.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@ApiModel(description = "用户套餐卡实体")
public class UserPackageCard implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "套餐ID")
    private Long packageId;

    @ApiModelProperty(value = "关联订单ID")
    private Long orderId;

    @ApiModelProperty(value = "总次数")
    private Integer totalCount;

    @ApiModelProperty(value = "已用次数")
    private Integer usedCount;

    @ApiModelProperty(value = "有效期开始")
    private LocalDate validStartDate;

    @ApiModelProperty(value = "有效期结束")
    private LocalDate validEndDate;

    @ApiModelProperty(value = "状态: 1-正常, 2-已过期, 3-已用完")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private java.time.LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private java.time.LocalDateTime updateTime;

    @ApiModelProperty(value = "剩余次数")
    public Integer getRemainingCount() {
        if (totalCount != null && usedCount != null) {
            return totalCount - usedCount;
        }
        return 0;
    }
}
