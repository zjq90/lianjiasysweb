package com.lianjia.controller;

import com.lianjia.common.Result;
import com.lianjia.entity.ConsumptionRecord;
import com.lianjia.service.ConsumptionRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Api(tags = "消费记录接口")
@RestController
@RequestMapping("/api/consumption-records")
public class ConsumptionRecordController {
    
    @Autowired
    private ConsumptionRecordService consumptionRecordService;
    
    @ApiOperation("分页查询消费记录")
    @GetMapping
    public Result<Page<ConsumptionRecord>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) String serviceType,
            @RequestParam(required = false) String status) {
        
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "consumptionTime"));
        Page<ConsumptionRecord> records = consumptionRecordService.findByFilters(customerId, serviceType, status, pageRequest);
        return Result.success(records);
    }
    
    @ApiOperation("根据客户ID查询消费记录列表")
    @GetMapping("/customer/{customerId}")
    public Result<List<ConsumptionRecord>> getByCustomerId(@PathVariable Long customerId) {
        List<ConsumptionRecord> records = consumptionRecordService.findByCustomerIdOrderByConsumptionTimeDesc(customerId);
        return Result.success(records);
    }
    
    @ApiOperation("根据ID查询消费记录")
    @GetMapping("/{id}")
    public Result<ConsumptionRecord> getById(@PathVariable Long id) {
        Optional<ConsumptionRecord> record = consumptionRecordService.findById(id);
        return record.map(Result::success)
                .orElse(Result.error("消费记录不存在"));
    }
    
    @ApiOperation("新增消费记录")
    @PostMapping
    public Result<ConsumptionRecord> create(@RequestBody ConsumptionRecord record) {
        ConsumptionRecord saved = consumptionRecordService.save(record);
        return Result.success("创建成功", saved);
    }
    
    @ApiOperation("更新消费记录")
    @PutMapping("/{id}")
    public Result<ConsumptionRecord> update(@PathVariable Long id, @RequestBody ConsumptionRecord record) {
        Optional<ConsumptionRecord> existing = consumptionRecordService.findById(id);
        if (!existing.isPresent()) {
            return Result.error("消费记录不存在");
        }
        record.setId(id);
        ConsumptionRecord saved = consumptionRecordService.save(record);
        return Result.success("更新成功", saved);
    }
    
    @ApiOperation("删除消费记录")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        Optional<ConsumptionRecord> existing = consumptionRecordService.findById(id);
        if (!existing.isPresent()) {
            return Result.error("消费记录不存在");
        }
        consumptionRecordService.deleteById(id);
        return Result.success();
    }
}
