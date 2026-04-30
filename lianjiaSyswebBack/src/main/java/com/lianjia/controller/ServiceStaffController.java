package com.lianjia.controller;

import com.lianjia.common.Result;
import com.lianjia.entity.ServiceStaff;
import com.lianjia.service.ServiceStaffService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Api(tags = "服务人员接口")
@RestController
@RequestMapping("/api/service-staff")
public class ServiceStaffController {
    
    @Autowired
    private ServiceStaffService serviceStaffService;
    
    @ApiOperation("查询所有服务人员")
    @GetMapping
    public Result<List<ServiceStaff>> list(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String skill) {
        List<ServiceStaff> staffList = serviceStaffService.findByFilters(name, status, skill);
        return Result.success(staffList);
    }
    
    @ApiOperation("查询空闲的服务人员")
    @GetMapping("/available")
    public Result<List<ServiceStaff>> getAvailable() {
        List<ServiceStaff> staffList = serviceStaffService.findByStatus("空闲");
        return Result.success(staffList);
    }
    
    @ApiOperation("根据ID查询服务人员")
    @GetMapping("/{id}")
    public Result<ServiceStaff> getById(@PathVariable Long id) {
        Optional<ServiceStaff> staff = serviceStaffService.findById(id);
        return staff.map(Result::success)
                .orElse(Result.error("服务人员不存在"));
    }
    
    @ApiOperation("新增服务人员")
    @PostMapping
    public Result<ServiceStaff> create(@RequestBody ServiceStaff serviceStaff) {
        ServiceStaff saved = serviceStaffService.save(serviceStaff);
        return Result.success("创建成功", saved);
    }
    
    @ApiOperation("更新服务人员")
    @PutMapping("/{id}")
    public Result<ServiceStaff> update(@PathVariable Long id, @RequestBody ServiceStaff serviceStaff) {
        Optional<ServiceStaff> existing = serviceStaffService.findById(id);
        if (!existing.isPresent()) {
            return Result.error("服务人员不存在");
        }
        serviceStaff.setId(id);
        ServiceStaff saved = serviceStaffService.save(serviceStaff);
        return Result.success("更新成功", saved);
    }
    
    @ApiOperation("更新服务人员状态")
    @PutMapping("/{id}/status")
    public Result<ServiceStaff> updateStatus(@PathVariable Long id, @RequestParam String status) {
        ServiceStaff updated = serviceStaffService.updateStatus(id, status);
        if (updated == null) {
            return Result.error("服务人员不存在");
        }
        return Result.success("状态更新成功", updated);
    }
    
    @ApiOperation("删除服务人员")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        Optional<ServiceStaff> existing = serviceStaffService.findById(id);
        if (!existing.isPresent()) {
            return Result.error("服务人员不存在");
        }
        serviceStaffService.deleteById(id);
        return Result.success();
    }
}
