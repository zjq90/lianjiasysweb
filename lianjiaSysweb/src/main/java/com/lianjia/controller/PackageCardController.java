package com.lianjia.controller;

import com.lianjia.common.Result;
import com.lianjia.entity.UserPackageCard;
import com.lianjia.service.UserPackageCardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "套餐卡管理")
@RestController
@RequestMapping("/api/packageCard")
public class PackageCardController {

    @Autowired
    private UserPackageCardService userPackageCardService;

    @ApiOperation("根据ID获取套餐卡详情")
    @GetMapping("/{id}")
    public Result<UserPackageCard> getById(@ApiParam("套餐卡ID") @PathVariable Long id) {
        return userPackageCardService.getById(id);
    }

    @ApiOperation("根据用户ID获取套餐卡列表")
    @GetMapping("/user/{userId}")
    public Result<List<UserPackageCard>> getByUserId(@ApiParam("用户ID") @PathVariable Long userId) {
        return userPackageCardService.getByUserId(userId);
    }

    @ApiOperation("获取用户有效的套餐卡列表")
    @GetMapping("/valid/{userId}")
    public Result<List<UserPackageCard>> getValidByUserId(@ApiParam("用户ID") @PathVariable Long userId) {
        return userPackageCardService.getValidByUserId(userId);
    }

    @ApiOperation("新增套餐卡")
    @PostMapping
    public Result<Long> add(@RequestBody UserPackageCard card) {
        return userPackageCardService.add(card);
    }

    @ApiOperation("更新套餐卡")
    @PutMapping
    public Result<Boolean> update(@RequestBody UserPackageCard card) {
        return userPackageCardService.update(card);
    }

    @ApiOperation("使用套餐卡(扣减次数)")
    @PutMapping("/use/{id}")
    public Result<Boolean> useCard(@ApiParam("套餐卡ID") @PathVariable Long id) {
        return userPackageCardService.useCard(id);
    }

    @ApiOperation("删除套餐卡")
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@ApiParam("套餐卡ID") @PathVariable Long id) {
        return userPackageCardService.delete(id);
    }
}
