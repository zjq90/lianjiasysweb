package com.lianjia.entity;

import com.lianjia.common.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "用户地址实体")
public class UserAddress extends BaseEntity {

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "收货人姓名")
    private String name;

    @ApiModelProperty(value = "收货人电话")
    private String phone;

    @ApiModelProperty(value = "省份")
    private String province;

    @ApiModelProperty(value = "城市")
    private String city;

    @ApiModelProperty(value = "区县")
    private String district;

    @ApiModelProperty(value = "详细地址")
    private String detailAddress;

    @ApiModelProperty(value = "房屋面积(平方米)")
    private BigDecimal houseArea;

    @ApiModelProperty(value = "房屋类型")
    private String houseType;

    @ApiModelProperty(value = "是否默认: 0-否, 1-是")
    private Integer isDefault;
}
