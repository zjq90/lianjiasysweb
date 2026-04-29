package com.lianjia.entity;

import com.lianjia.common.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "任务单实体")
public class TaskOrder extends BaseEntity {

    @ApiModelProperty(value = "任务编号")
    private String taskNo;

    @ApiModelProperty(value = "关联订单ID")
    private Long orderId;

    @ApiModelProperty(value = "师傅ID")
    private Long workerId;

    @ApiModelProperty(value = "任务状态: pending-待接单, accepted-已接单, on_way-已出发, arrived-已到达, in_service-服务中, completed-已完成, cancelled-已取消")
    private String taskStatus;

    @ApiModelProperty(value = "接单时间")
    private LocalDateTime acceptTime;

    @ApiModelProperty(value = "开始服务时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "完成服务时间")
    private LocalDateTime completeTime;

    @ApiModelProperty(value = "预计到达时间")
    private LocalDateTime estimatedArrivalTime;

    @ApiModelProperty(value = "实际到达时间")
    private LocalDateTime actualArrivalTime;

    @ApiModelProperty(value = "备注")
    private String note;
}
