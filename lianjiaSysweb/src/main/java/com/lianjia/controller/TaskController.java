package com.lianjia.controller;

import com.lianjia.common.PageResult;
import com.lianjia.common.Result;
import com.lianjia.entity.TaskOrder;
import com.lianjia.service.TaskOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "任务单管理(师傅端)")
@RestController
@RequestMapping("/api/task")
public class TaskController {

    @Autowired
    private TaskOrderService taskOrderService;

    @ApiOperation("根据ID获取任务单详情")
    @GetMapping("/{id}")
    public Result<TaskOrder> getById(@ApiParam("任务单ID") @PathVariable Long id) {
        return taskOrderService.getById(id);
    }

    @ApiOperation("根据任务编号获取任务单详情")
    @GetMapping("/no/{taskNo}")
    public Result<TaskOrder> getByTaskNo(@ApiParam("任务编号") @PathVariable String taskNo) {
        return taskOrderService.getByTaskNo(taskNo);
    }

    @ApiOperation("根据订单ID获取任务单")
    @GetMapping("/order/{orderId}")
    public Result<TaskOrder> getByOrderId(@ApiParam("订单ID") @PathVariable Long orderId) {
        return taskOrderService.getByOrderId(orderId);
    }

    @ApiOperation("根据师傅ID获取任务单列表")
    @GetMapping("/worker/{workerId}")
    public Result<List<TaskOrder>> getByWorkerId(@ApiParam("师傅ID") @PathVariable Long workerId) {
        return taskOrderService.getByWorkerId(workerId);
    }

    @ApiOperation("根据师傅ID和状态获取任务单列表")
    @GetMapping("/worker/{workerId}/status/{status}")
    public Result<List<TaskOrder>> getByWorkerIdAndStatus(
            @ApiParam("师傅ID") @PathVariable Long workerId,
            @ApiParam("任务状态") @PathVariable String status) {
        return taskOrderService.getByWorkerIdAndStatus(workerId, status);
    }

    @ApiOperation("分页获取任务单列表")
    @GetMapping("/page")
    public Result<PageResult<TaskOrder>> getPage(
            @ModelAttribute TaskOrder task,
            @ApiParam("页码") @RequestParam(required = false) Integer pageNum,
            @ApiParam("每页大小") @RequestParam(required = false) Integer pageSize) {
        return taskOrderService.getPage(task, pageNum, pageSize);
    }

    @ApiOperation("创建任务单")
    @PostMapping
    public Result<Long> createTask(@RequestBody TaskOrder task) {
        return taskOrderService.createTask(task);
    }

    @ApiOperation("更新任务单")
    @PutMapping
    public Result<Boolean> update(@RequestBody TaskOrder task) {
        return taskOrderService.update(task);
    }

    @ApiOperation("师傅接单")
    @PutMapping("/accept/{id}")
    public Result<Boolean> acceptTask(
            @ApiParam("任务单ID") @PathVariable Long id,
            @ApiParam("师傅ID") @RequestParam Long workerId) {
        return taskOrderService.acceptTask(id, workerId);
    }

    @ApiOperation("开始服务")
    @PutMapping("/start/{id}")
    public Result<Boolean> startTask(@ApiParam("任务单ID") @PathVariable Long id) {
        return taskOrderService.startTask(id);
    }

    @ApiOperation("完成服务")
    @PutMapping("/complete/{id}")
    public Result<Boolean> completeTask(@ApiParam("任务单ID") @PathVariable Long id) {
        return taskOrderService.completeTask(id);
    }

    @ApiOperation("更新任务状态")
    @PutMapping("/status/{id}")
    public Result<Boolean> updateStatus(
            @ApiParam("任务单ID") @PathVariable Long id,
            @ApiParam("任务状态") @RequestParam String status) {
        return taskOrderService.updateStatus(id, status);
    }
}
