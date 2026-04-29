package com.lianjia.service.impl;

import com.lianjia.common.Constants;
import com.lianjia.common.PageResult;
import com.lianjia.common.Result;
import com.lianjia.entity.TaskOrder;
import com.lianjia.exception.BusinessException;
import com.lianjia.mapper.TaskOrderMapper;
import com.lianjia.service.TaskOrderService;
import com.lianjia.util.OrderNoGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class TaskOrderServiceImpl implements TaskOrderService {

    @Autowired
    private TaskOrderMapper taskOrderMapper;

    @Override
    public Result<TaskOrder> getById(Long id) {
        if (id == null) {
            throw new BusinessException("任务单ID不能为空");
        }
        TaskOrder task = taskOrderMapper.selectById(id);
        return Result.success(task);
    }

    @Override
    public Result<TaskOrder> getByTaskNo(String taskNo) {
        if (taskNo == null || taskNo.isEmpty()) {
            throw new BusinessException("任务编号不能为空");
        }
        TaskOrder task = taskOrderMapper.selectByTaskNo(taskNo);
        return Result.success(task);
    }

    @Override
    public Result<TaskOrder> getByOrderId(Long orderId) {
        if (orderId == null) {
            throw new BusinessException("订单ID不能为空");
        }
        TaskOrder task = taskOrderMapper.selectByOrderId(orderId);
        return Result.success(task);
    }

    @Override
    public Result<List<TaskOrder>> getByWorkerId(Long workerId) {
        if (workerId == null) {
            throw new BusinessException("师傅ID不能为空");
        }
        List<TaskOrder> list = taskOrderMapper.selectByWorkerId(workerId);
        return Result.success(list);
    }

    @Override
    public Result<List<TaskOrder>> getByWorkerIdAndStatus(Long workerId, String status) {
        if (workerId == null) {
            throw new BusinessException("师傅ID不能为空");
        }
        if (status == null || status.isEmpty()) {
            throw new BusinessException("任务状态不能为空");
        }
        List<TaskOrder> list = taskOrderMapper.selectByWorkerIdAndStatus(workerId, status);
        return Result.success(list);
    }

    @Override
    public Result<PageResult<TaskOrder>> getPage(TaskOrder task, Integer pageNum, Integer pageSize) {
        pageNum = pageNum == null ? Constants.DEFAULT_PAGE_NUM : pageNum;
        pageSize = pageSize == null ? Constants.DEFAULT_PAGE_SIZE : pageSize;
        int offset = (pageNum - 1) * pageSize;
        List<TaskOrder> list = taskOrderMapper.selectPage(task, offset, pageSize);
        long total = taskOrderMapper.selectCount(task);
        return Result.success(PageResult.of(pageNum, pageSize, total, list));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Long> createTask(TaskOrder task) {
        if (task.getOrderId() == null) {
            throw new BusinessException("订单ID不能为空");
        }
        TaskOrder existTask = taskOrderMapper.selectByOrderId(task.getOrderId());
        if (existTask != null) {
            throw new BusinessException("该订单已创建任务单");
        }
        String taskNo = OrderNoGenerator.generateTaskNo();
        task.setTaskNo(taskNo);
        task.setTaskStatus(Constants.TASK_STATUS_PENDING);
        task.setStatus(Constants.NORMAL);
        int rows = taskOrderMapper.insert(task);
        if (rows > 0) {
            return Result.success(task.getId());
        }
        throw new BusinessException("创建任务单失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> update(TaskOrder task) {
        if (task.getId() == null) {
            throw new BusinessException("任务单ID不能为空");
        }
        int rows = taskOrderMapper.update(task);
        return Result.success(rows > 0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> acceptTask(Long id, Long workerId) {
        if (id == null) {
            throw new BusinessException("任务单ID不能为空");
        }
        if (workerId == null) {
            throw new BusinessException("师傅ID不能为空");
        }
        TaskOrder task = taskOrderMapper.selectById(id);
        if (task == null) {
            throw new BusinessException("任务单不存在");
        }
        if (!Constants.TASK_STATUS_PENDING.equals(task.getTaskStatus())) {
            throw new BusinessException("任务单状态不正确，无法接单");
        }
        TaskOrder update = new TaskOrder();
        update.setId(id);
        update.setWorkerId(workerId);
        update.setTaskStatus(Constants.TASK_STATUS_ACCEPTED);
        update.setAcceptTime(LocalDateTime.now());
        int rows = taskOrderMapper.update(update);
        return Result.success(rows > 0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> startTask(Long id) {
        if (id == null) {
            throw new BusinessException("任务单ID不能为空");
        }
        TaskOrder task = taskOrderMapper.selectById(id);
        if (task == null) {
            throw new BusinessException("任务单不存在");
        }
        if (!Constants.TASK_STATUS_ACCEPTED.equals(task.getTaskStatus()) 
            && !Constants.TASK_STATUS_ARRIVED.equals(task.getTaskStatus())) {
            throw new BusinessException("任务单状态不正确，无法开始服务");
        }
        TaskOrder update = new TaskOrder();
        update.setId(id);
        update.setTaskStatus(Constants.TASK_STATUS_IN_SERVICE);
        update.setStartTime(LocalDateTime.now());
        int rows = taskOrderMapper.update(update);
        return Result.success(rows > 0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> completeTask(Long id) {
        if (id == null) {
            throw new BusinessException("任务单ID不能为空");
        }
        TaskOrder task = taskOrderMapper.selectById(id);
        if (task == null) {
            throw new BusinessException("任务单不存在");
        }
        if (!Constants.TASK_STATUS_IN_SERVICE.equals(task.getTaskStatus())) {
            throw new BusinessException("任务单状态不正确，无法完成服务");
        }
        TaskOrder update = new TaskOrder();
        update.setId(id);
        update.setTaskStatus(Constants.TASK_STATUS_COMPLETED);
        update.setCompleteTime(LocalDateTime.now());
        int rows = taskOrderMapper.update(update);
        return Result.success(rows > 0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> updateStatus(Long id, String status) {
        if (id == null) {
            throw new BusinessException("任务单ID不能为空");
        }
        if (status == null || status.isEmpty()) {
            throw new BusinessException("任务状态不能为空");
        }
        int rows = taskOrderMapper.updateStatus(id, status);
        return Result.success(rows > 0);
    }
}
