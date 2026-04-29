package com.lianjia.service;

import com.lianjia.common.PageResult;
import com.lianjia.common.Result;
import com.lianjia.entity.TaskOrder;

import java.util.List;

public interface TaskOrderService {

    Result<TaskOrder> getById(Long id);

    Result<TaskOrder> getByTaskNo(String taskNo);

    Result<TaskOrder> getByOrderId(Long orderId);

    Result<List<TaskOrder>> getByWorkerId(Long workerId);

    Result<List<TaskOrder>> getByWorkerIdAndStatus(Long workerId, String status);

    Result<PageResult<TaskOrder>> getPage(TaskOrder task, Integer pageNum, Integer pageSize);

    Result<Long> createTask(TaskOrder task);

    Result<Boolean> update(TaskOrder task);

    Result<Boolean> acceptTask(Long id, Long workerId);

    Result<Boolean> startTask(Long id);

    Result<Boolean> completeTask(Long id);

    Result<Boolean> updateStatus(Long id, String status);
}
