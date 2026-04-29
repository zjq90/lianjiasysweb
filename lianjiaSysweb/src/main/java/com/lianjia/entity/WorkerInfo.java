package com.lianjia.entity;

import com.lianjia.common.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "师傅/服务人员实体")
public class WorkerInfo extends BaseEntity {

    @ApiModelProperty(value = "关联用户ID")
    private Long userId;

    @ApiModelProperty(value = "师傅姓名")
    private String name;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "身份证号")
    private String idCard;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "技能")
    private String skill;

    @ApiModelProperty(value = "等级: 1-初级, 2-中级, 3-高级, 4-专家")
    private Integer level;

    @ApiModelProperty(value = "工作年限")
    private Integer workYears;
}
