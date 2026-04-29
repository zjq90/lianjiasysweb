package com.lianjia.service;

import com.lianjia.common.PageResult;
import com.lianjia.common.Result;
import com.lianjia.entity.WorkerInfo;

import java.util.List;

public interface WorkerInfoService {

    Result<WorkerInfo> getById(Long id);

    Result<WorkerInfo> getByPhone(String phone);

    Result<WorkerInfo> getByUserId(Long userId);

    Result<List<WorkerInfo>> getList(WorkerInfo worker);

    Result<PageResult<WorkerInfo>> getPage(WorkerInfo worker, Integer pageNum, Integer pageSize);

    Result<Long> add(WorkerInfo worker);

    Result<Boolean> update(WorkerInfo worker);

    Result<Boolean> delete(Long id);

    Result<Boolean> updateStatus(Long id, Integer status);
}
