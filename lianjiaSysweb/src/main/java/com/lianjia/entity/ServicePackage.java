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
@ApiModel(description = "套餐实体")
public class ServicePackage extends BaseEntity {

    @ApiModelProperty(value = "套餐名称")
    private String packageName;

    @ApiModelProperty(value = "套餐编码")
    private String packageCode;

    @ApiModelProperty(value = "套餐图标")
    private String packageIcon;

    @ApiModelProperty(value = "套餐图片(多张用逗号分隔)")
    private String packageImages;

    @ApiModelProperty(value = "套餐描述")
    private String packageDesc;

    @ApiModelProperty(value = "套餐详情(富文本)")
    private String packageDetail;

    @ApiModelProperty(value = "原价")
    private BigDecimal originalPrice;

    @ApiModelProperty(value = "售价")
    private BigDecimal salePrice;

    @ApiModelProperty(value = "总次数")
    private Integer totalCount;

    @ApiModelProperty(value = "有效天数(从购买日开始计算)")
    private Integer validDays;

    @ApiModelProperty(value = "有效期开始日期")
    private LocalDate validStartDate;

    @ApiModelProperty(value = "有效期结束日期")
    private LocalDate validEndDate;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "销量")
    private Integer salesCount;

    @ApiModelProperty(value = "关联标签列表")
    private transient List<SysTag> tags;

    @ApiModelProperty(value = "关联服务列表")
    private transient List<ServiceItem> services;
}
