package com.lianjia.controller;

import com.lianjia.common.Result;
import com.lianjia.entity.UserAddress;
import com.lianjia.service.UserAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "地址管理")
@RestController
@RequestMapping("/api/address")
public class AddressController {

    @Autowired
    private UserAddressService userAddressService;

    @ApiOperation("根据ID获取地址详情")
    @GetMapping("/{id}")
    public Result<UserAddress> getById(@ApiParam("地址ID") @PathVariable Long id) {
        return userAddressService.getById(id);
    }

    @ApiOperation("根据用户ID获取地址列表")
    @GetMapping("/user/{userId}")
    public Result<List<UserAddress>> getByUserId(@ApiParam("用户ID") @PathVariable Long userId) {
        return userAddressService.getByUserId(userId);
    }

    @ApiOperation("获取用户的默认地址")
    @GetMapping("/default/{userId}")
    public Result<UserAddress> getDefaultAddress(@ApiParam("用户ID") @PathVariable Long userId) {
        return userAddressService.getDefaultAddress(userId);
    }

    @ApiOperation("新增地址")
    @PostMapping
    public Result<Long> add(@RequestBody UserAddress address) {
        return userAddressService.add(address);
    }

    @ApiOperation("更新地址")
    @PutMapping
    public Result<Boolean> update(@RequestBody UserAddress address) {
        return userAddressService.update(address);
    }

    @ApiOperation("删除地址")
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@ApiParam("地址ID") @PathVariable Long id) {
        return userAddressService.delete(id);
    }

    @ApiOperation("设置默认地址")
    @PutMapping("/default/{id}")
    public Result<Boolean> setDefault(
            @ApiParam("地址ID") @PathVariable Long id,
            @ApiParam("用户ID") @RequestParam Long userId) {
        return userAddressService.setDefault(id, userId);
    }
}
