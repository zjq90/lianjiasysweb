package com.lianjia.entity;

import com.lianjia.common.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "家庭档案实体")
public class FamilyProfile extends BaseEntity {

    @ApiModelProperty(value = "用户ID(户主)")
    private Long userId;

    @ApiModelProperty(value = "家庭名称")
    private String familyName;

    @ApiModelProperty(value = "关联地址ID")
    private Long addressId;

    @ApiModelProperty(value = "房屋面积")
    private BigDecimal houseArea;

    @ApiModelProperty(value = "房屋类型")
    private String houseType;

    @ApiModelProperty(value = "装修日期")
    private LocalDate decorateDate;

    @ApiModelProperty(value = "上次服务日期")
    private LocalDate lastServiceDate;

    @ApiModelProperty(value = "下次服务日期")
    private LocalDate nextServiceDate;

    @ApiModelProperty(value = "是否已提醒: 0-否, 1-是")
    private Integer hasReminder;

    @ApiModelProperty(value = "家庭成员列表")
    private transient List<FamilyMember> members;

    @ApiModelProperty(value = "关联标签列表")
    private transient List<SysTag> tags;
}
