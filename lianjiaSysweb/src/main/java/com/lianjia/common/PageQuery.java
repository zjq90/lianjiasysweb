package com.lianjia.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(description = "分页查询参数")
public class PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "当前页码", example = "1")
    private Integer pageNum = 1;

    @ApiModelProperty(value = "每页大小", example = "10")
    private Integer pageSize = 10;

    @ApiModelProperty(value = "排序字段", example = "create_time")
    private String orderBy = "create_time";

    @ApiModelProperty(value = "排序方向: asc-升序, desc-降序", example = "desc")
    private String orderDir = "desc";

    public int getOffset() {
        return (pageNum - 1) * pageSize;
    }
}
