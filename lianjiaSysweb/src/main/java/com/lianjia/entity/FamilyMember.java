package com.lianjia.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(description = "家庭档案成员实体")
public class FamilyMember implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "成员ID")
    private Long id;

    @ApiModelProperty(value = "家庭档案ID")
    private Long familyId;

    @ApiModelProperty(value = "成员类型: adult-成人, child-儿童, elder-老人, pet-宠物")
    private String memberType;

    @ApiModelProperty(value = "成员姓名/昵称")
    private String memberName;

    @ApiModelProperty(value = "性别: 0-未知, 1-男, 2-女")
    private Integer gender;

    @ApiModelProperty(value = "出生日期")
    private java.time.LocalDate birthday;

    @ApiModelProperty(value = "健康状况")
    private String healthStatus;

    @ApiModelProperty(value = "备注")
    private String note;

    @ApiModelProperty(value = "状态: 0-删除, 1-正常")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private java.time.LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private java.time.LocalDateTime updateTime;
}
