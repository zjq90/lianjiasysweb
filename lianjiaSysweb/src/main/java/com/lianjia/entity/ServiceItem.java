package com.lianjia.entity;

import com.lianjia.common.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "服务项目实体")
public class ServiceItem extends BaseEntity {

    @ApiModelProperty(value = "分类ID")
    private Long categoryId;

    @ApiModelProperty(value = "服务名称")
    private String serviceName;

    @ApiModelProperty(value = "服务编码")
    private String serviceCode;

    @ApiModelProperty(value = "服务图标")
    private String serviceIcon;

    @ApiModelProperty(value = "服务图片(多张用逗号分隔)")
    private String serviceImages;

    @ApiModelProperty(value = "服务描述")
    private String serviceDesc;

    @ApiModelProperty(value = "服务详情(富文本)")
    private String serviceDetail;

    @ApiModelProperty(value = "服务价格")
    private BigDecimal price;

    @ApiModelProperty(value = "市场价格")
    private BigDecimal marketPrice;

    @ApiModelProperty(value = "单位: 次/小时/平方米等")
    private String unit;

    @ApiModelProperty(value = "服务时长(分钟)")
    private Integer duration;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "销量")
    private Integer salesCount;
}
