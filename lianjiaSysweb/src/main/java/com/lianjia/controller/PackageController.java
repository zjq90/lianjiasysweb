package com.lianjia.controller;

import com.lianjia.common.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Api(tags = "场景化导购/套餐推荐")
@RestController
@RequestMapping("/api/package")
public class PackageController {

    @ApiOperation("根据用户选择的标签智能推荐套餐")
    @PostMapping("/recommend")
    public Result<List<Long>> recommendByTags(
            @ApiParam("标签ID列表") @RequestBody List<Long> tagIds) {
        return Result.success(Arrays.asList(1L, 2L, 3L, 4L));
    }

    @ApiOperation("获取所有标签(用于场景化选择)")
    @GetMapping("/tags")
    public Result<List<String>> getAllTags() {
        return Result.success(Arrays.asList("有宠物", "有宝宝", "新装修", "鼻炎咽炎过敏体质", "有老人"));
    }

    @ApiOperation("获取套餐详情")
    @GetMapping("/{id}")
    public Result<Long> getPackageDetail(@ApiParam("套餐ID") @PathVariable Long id) {
        return Result.success(id);
    }
}
