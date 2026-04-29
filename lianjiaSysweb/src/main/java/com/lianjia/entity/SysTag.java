package com.lianjia.entity;

import com.lianjia.common.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "标签实体")
public class SysTag extends BaseEntity {

    @ApiModelProperty(value = "标签名称")
    private String tagName;

    @ApiModelProperty(value = "标签编码")
    private String tagCode;

    @ApiModelProperty(value = "标签分类: user-用户, family-家庭, service-服务")
    private String category;

    @ApiModelProperty(value = "排序")
    private Integer sort;
}
