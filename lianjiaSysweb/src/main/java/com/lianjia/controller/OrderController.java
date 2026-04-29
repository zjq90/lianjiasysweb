package com.lianjia.controller;

import com.lianjia.common.PageResult;
import com.lianjia.common.Result;
import com.lianjia.entity.OrderInfo;
import com.lianjia.service.OrderInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "订单管理")
@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderInfoService orderInfoService;

    @ApiOperation("根据ID获取订单详情")
    @GetMapping("/{id}")
    public Result<OrderInfo> getById(@ApiParam("订单ID") @PathVariable Long id) {
        return orderInfoService.getById(id);
    }

    @ApiOperation("根据订单号获取订单详情")
    @GetMapping("/no/{orderNo}")
    public Result<OrderInfo> getByOrderNo(@ApiParam("订单编号") @PathVariable String orderNo) {
        return orderInfoService.getByOrderNo(orderNo);
    }

    @ApiOperation("根据用户ID获取订单列表")
    @GetMapping("/user/{userId}")
    public Result<List<OrderInfo>> getByUserId(@ApiParam("用户ID") @PathVariable Long userId) {
        return orderInfoService.getByUserId(userId);
    }

    @ApiOperation("根据用户ID和状态获取订单列表")
    @GetMapping("/user/{userId}/status/{status}")
    public Result<List<OrderInfo>> getByUserIdAndStatus(
            @ApiParam("用户ID") @PathVariable Long userId,
            @ApiParam("订单状态") @PathVariable String status) {
        return orderInfoService.getByUserIdAndStatus(userId, status);
    }

    @ApiOperation("分页获取订单列表")
    @GetMapping("/page")
    public Result<PageResult<OrderInfo>> getPage(
            @ModelAttribute OrderInfo order,
            @ApiParam("页码") @RequestParam(required = false) Integer pageNum,
            @ApiParam("每页大小") @RequestParam(required = false) Integer pageSize) {
        return orderInfoService.getPage(order, pageNum, pageSize);
    }

    @ApiOperation("创建订单")
    @PostMapping
    public Result<String> createOrder(@RequestBody OrderInfo order) {
        return orderInfoService.createOrder(order);
    }

    @ApiOperation("更新订单")
    @PutMapping
    public Result<Boolean> update(@RequestBody OrderInfo order) {
        return orderInfoService.update(order);
    }

    @ApiOperation("取消订单")
    @PutMapping("/cancel/{id}")
    public Result<Boolean> cancelOrder(
            @ApiParam("订单ID") @PathVariable Long id,
            @ApiParam("取消原因") @RequestParam(required = false) String cancelReason) {
        return orderInfoService.cancelOrder(id, cancelReason);
    }

    @ApiOperation("支付订单")
    @PutMapping("/pay/{id}")
    public Result<Boolean> payOrder(
            @ApiParam("订单ID") @PathVariable Long id,
            @ApiParam("支付方式: wechat-微信, balance-余额") @RequestParam String payType) {
        return orderInfoService.payOrder(id, payType);
    }

    @ApiOperation("更新订单状态")
    @PutMapping("/status/{id}")
    public Result<Boolean> updateStatus(
            @ApiParam("订单ID") @PathVariable Long id,
            @ApiParam("订单状态") @RequestParam String status) {
        return orderInfoService.updateStatus(id, status);
    }
}
