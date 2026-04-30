package com.lianjia.controller;

import com.lianjia.common.Result;
import com.lianjia.entity.ServiceOrder;
import com.lianjia.service.ServiceOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Api(tags = "服务订单接口")
@RestController
@RequestMapping("/api/service-orders")
public class ServiceOrderController {
    
    @Autowired
    private ServiceOrderService serviceOrderService;
    
    @ApiOperation("分页查询服务订单")
    @GetMapping
    public Result<Page<ServiceOrder>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String orderNo,
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) Long staffId,
            @RequestParam(required = false) String serviceType,
            @RequestParam(required = false) String status) {
        
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        Page<ServiceOrder> orders = serviceOrderService.findByFilters(
                orderNo, customerName, staffId, serviceType, status, pageRequest);
        return Result.success(orders);
    }
    
    @ApiOperation("根据ID查询服务订单")
    @GetMapping("/{id}")
    public Result<ServiceOrder> getById(@PathVariable Long id) {
        Optional<ServiceOrder> order = serviceOrderService.findById(id);
        return order.map(Result::success)
                .orElse(Result.error("服务订单不存在"));
    }
    
    @ApiOperation("根据客户ID查询服务订单")
    @GetMapping("/customer/{customerId}")
    public Result<List<ServiceOrder>> getByCustomerId(@PathVariable Long customerId) {
        List<ServiceOrder> orders = serviceOrderService.findByCustomerId(customerId);
        return Result.success(orders);
    }
    
    @ApiOperation("根据服务人员ID查询服务订单")
    @GetMapping("/staff/{staffId}")
    public Result<List<ServiceOrder>> getByStaffId(@PathVariable Long staffId) {
        List<ServiceOrder> orders = serviceOrderService.findByStaffId(staffId);
        return Result.success(orders);
    }
    
    @ApiOperation("新增服务订单")
    @PostMapping
    public Result<ServiceOrder> create(@RequestBody ServiceOrder serviceOrder) {
        ServiceOrder saved = serviceOrderService.save(serviceOrder);
        return Result.success("创建成功", saved);
    }
    
    @ApiOperation("更新服务订单")
    @PutMapping("/{id}")
    public Result<ServiceOrder> update(@PathVariable Long id, @RequestBody ServiceOrder serviceOrder) {
        Optional<ServiceOrder> existing = serviceOrderService.findById(id);
        if (!existing.isPresent()) {
            return Result.error("服务订单不存在");
        }
        serviceOrder.setId(id);
        ServiceOrder saved = serviceOrderService.save(serviceOrder);
        return Result.success("更新成功", saved);
    }
    
    @ApiOperation("派单 - 指派服务人员")
    @PostMapping("/{id}/assign")
    public Result<ServiceOrder> assignStaff(@PathVariable Long id, @RequestParam Long staffId) {
        try {
            ServiceOrder order = serviceOrderService.assignStaff(id, staffId);
            if (order == null) {
                return Result.error("订单或服务人员不存在");
            }
            return Result.success("派单成功", order);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("开始服务")
    @PostMapping("/{id}/start")
    public Result<ServiceOrder> startService(@PathVariable Long id) {
        try {
            ServiceOrder order = serviceOrderService.startService(id);
            if (order == null) {
                return Result.error("订单不存在");
            }
            return Result.success("服务已开始", order);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("完成服务")
    @PostMapping("/{id}/complete")
    public Result<ServiceOrder> completeService(
            @PathVariable Long id,
            @RequestParam(required = false) String progress,
            @RequestParam(required = false) String feedback,
            @RequestParam(required = false) Double rating) {
        try {
            ServiceOrder order = serviceOrderService.completeService(id, progress, feedback, rating);
            if (order == null) {
                return Result.error("订单不存在");
            }
            return Result.success("服务已完成", order);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("取消订单")
    @PostMapping("/{id}/cancel")
    public Result<ServiceOrder> cancelOrder(
            @PathVariable Long id,
            @RequestParam(required = false) String remark) {
        try {
            ServiceOrder order = serviceOrderService.cancelOrder(id, remark);
            if (order == null) {
                return Result.error("订单不存在");
            }
            return Result.success("订单已取消", order);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("删除服务订单")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        Optional<ServiceOrder> existing = serviceOrderService.findById(id);
        if (!existing.isPresent()) {
            return Result.error("服务订单不存在");
        }
        serviceOrderService.deleteById(id);
        return Result.success();
    }
}
