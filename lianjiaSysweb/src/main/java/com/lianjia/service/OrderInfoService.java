package com.lianjia.service;

import com.lianjia.common.PageResult;
import com.lianjia.common.Result;
import com.lianjia.entity.OrderInfo;

import java.util.List;

public interface OrderInfoService {

    Result<OrderInfo> getById(Long id);

    Result<OrderInfo> getByOrderNo(String orderNo);

    Result<List<OrderInfo>> getByUserId(Long userId);

    Result<List<OrderInfo>> getByUserIdAndStatus(Long userId, String status);

    Result<PageResult<OrderInfo>> getPage(OrderInfo order, Integer pageNum, Integer pageSize);

    Result<String> createOrder(OrderInfo order);

    Result<Boolean> update(OrderInfo order);

    Result<Boolean> cancelOrder(Long id, String cancelReason);

    Result<Boolean> payOrder(Long id, String payType);

    Result<Boolean> updateStatus(Long id, String status);
}
