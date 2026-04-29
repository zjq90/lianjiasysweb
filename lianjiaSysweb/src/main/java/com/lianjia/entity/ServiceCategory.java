package com.lianjia.entity;

import com.lianjia.common.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "服务分类实体")
public class ServiceCategory extends BaseEntity {

    @ApiModelProperty(value = "父分类ID, 0表示一级分类")
    private Long parentId;

    @ApiModelProperty(value = "分类名称")
    private String categoryName;

    @ApiModelProperty(value = "分类图标")
    private String categoryIcon;

    @ApiModelProperty(value = "排序")
    private Integer sort;
}
