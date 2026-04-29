package com.lianjia.service.impl;

import com.lianjia.common.Constants;
import com.lianjia.common.PageResult;
import com.lianjia.common.Result;
import com.lianjia.entity.WorkerInfo;
import com.lianjia.exception.BusinessException;
import com.lianjia.mapper.WorkerInfoMapper;
import com.lianjia.service.WorkerInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class WorkerInfoServiceImpl implements WorkerInfoService {

    @Autowired
    private WorkerInfoMapper workerInfoMapper;

    @Override
    public Result<WorkerInfo> getById(Long id) {
        if (id == null) {
            throw new BusinessException("师傅ID不能为空");
        }
        WorkerInfo worker = workerInfoMapper.selectById(id);
        return Result.success(worker);
    }

    @Override
    public Result<WorkerInfo> getByPhone(String phone) {
        if (phone == null || phone.isEmpty()) {
            throw new BusinessException("手机号不能为空");
        }
        WorkerInfo worker = workerInfoMapper.selectByPhone(phone);
        return Result.success(worker);
    }

    @Override
    public Result<WorkerInfo> getByUserId(Long userId) {
        if (userId == null) {
            throw new BusinessException("用户ID不能为空");
        }
        WorkerInfo worker = workerInfoMapper.selectByUserId(userId);
        return Result.success(worker);
    }

    @Override
    public Result<List<WorkerInfo>> getList(WorkerInfo worker) {
        List<WorkerInfo> list = workerInfoMapper.selectList(worker);
        return Result.success(list);
    }

    @Override
    public Result<PageResult<WorkerInfo>> getPage(WorkerInfo worker, Integer pageNum, Integer pageSize) {
        pageNum = pageNum == null ? Constants.DEFAULT_PAGE_NUM : pageNum;
        pageSize = pageSize == null ? Constants.DEFAULT_PAGE_SIZE : pageSize;
        int offset = (pageNum - 1) * pageSize;
        List<WorkerInfo> list = workerInfoMapper.selectPage(worker, offset, pageSize);
        long total = workerInfoMapper.selectCount(worker);
        return Result.success(PageResult.of(pageNum, pageSize, total, list));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Long> add(WorkerInfo worker) {
        if (worker.getName() == null || worker.getName().isEmpty()) {
            throw new BusinessException("师傅姓名不能为空");
        }
        if (worker.getPhone() == null || worker.getPhone().isEmpty()) {
            throw new BusinessException("手机号不能为空");
        }
        WorkerInfo exist = workerInfoMapper.selectByPhone(worker.getPhone());
        if (exist != null) {
            throw new BusinessException("手机号已存在");
        }
        worker.setStatus(Constants.NORMAL);
        worker.setLevel(worker.getLevel() == null ? 1 : worker.getLevel());
        worker.setWorkYears(worker.getWorkYears() == null ? 0 : worker.getWorkYears());
        int rows = workerInfoMapper.insert(worker);
        if (rows > 0) {
            return Result.success(worker.getId());
        }
        throw new BusinessException("添加师傅失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> update(WorkerInfo worker) {
        if (worker.getId() == null) {
            throw new BusinessException("师傅ID不能为空");
        }
        int rows = workerInfoMapper.update(worker);
        return Result.success(rows > 0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> delete(Long id) {
        if (id == null) {
            throw new BusinessException("师傅ID不能为空");
        }
        int rows = workerInfoMapper.deleteById(id);
        return Result.success(rows > 0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> updateStatus(Long id, Integer status) {
        if (id == null) {
            throw new BusinessException("师傅ID不能为空");
        }
        int rows = workerInfoMapper.updateStatus(id, status);
        return Result.success(rows > 0);
    }
}
