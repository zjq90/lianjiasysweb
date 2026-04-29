package com.lianjia.service.impl;

import com.lianjia.common.Constants;
import com.lianjia.common.PageResult;
import com.lianjia.common.Result;
import com.lianjia.entity.OrderInfo;
import com.lianjia.exception.BusinessException;
import com.lianjia.mapper.OrderInfoMapper;
import com.lianjia.service.OrderInfoService;
import com.lianjia.util.OrderNoGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class OrderInfoServiceImpl implements OrderInfoService {

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Override
    public Result<OrderInfo> getById(Long id) {
        if (id == null) {
            throw new BusinessException("订单ID不能为空");
        }
        OrderInfo order = orderInfoMapper.selectById(id);
        return Result.success(order);
    }

    @Override
    public Result<OrderInfo> getByOrderNo(String orderNo) {
        if (orderNo == null || orderNo.isEmpty()) {
            throw new BusinessException("订单编号不能为空");
        }
        OrderInfo order = orderInfoMapper.selectByOrderNo(orderNo);
        return Result.success(order);
    }

    @Override
    public Result<List<OrderInfo>> getByUserId(Long userId) {
        if (userId == null) {
            throw new BusinessException("用户ID不能为空");
        }
        List<OrderInfo> list = orderInfoMapper.selectByUserId(userId);
        return Result.success(list);
    }

    @Override
    public Result<List<OrderInfo>> getByUserIdAndStatus(Long userId, String status) {
        if (userId == null) {
            throw new BusinessException("用户ID不能为空");
        }
        List<OrderInfo> list = orderInfoMapper.selectByUserIdAndStatus(userId, status);
        return Result.success(list);
    }

    @Override
    public Result<PageResult<OrderInfo>> getPage(OrderInfo order, Integer pageNum, Integer pageSize) {
        pageNum = pageNum == null ? Constants.DEFAULT_PAGE_NUM : pageNum;
        pageSize = pageSize == null ? Constants.DEFAULT_PAGE_SIZE : pageSize;
        int offset = (pageNum - 1) * pageSize;
        List<OrderInfo> list = orderInfoMapper.selectPage(order, offset, pageSize);
        long total = orderInfoMapper.selectCount(order);
        return Result.success(PageResult.of(pageNum, pageSize, total, list));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> createOrder(OrderInfo order) {
        if (order.getUserId() == null) {
            throw new BusinessException("用户ID不能为空");
        }
        if (order.getAddressId() == null) {
            throw new BusinessException("地址ID不能为空");
        }
        if (order.getServiceDate() == null) {
            throw new BusinessException("服务日期不能为空");
        }
        if (order.getServiceTime() == null || order.getServiceTime().isEmpty()) {
            throw new BusinessException("服务时间段不能为空");
        }
        if (order.getPayAmount() == null) {
            throw new BusinessException("支付金额不能为空");
        }

        String orderNo = OrderNoGenerator.generateOrderNo();
        order.setOrderNo(orderNo);
        order.setOrderStatus(Constants.ORDER_STATUS_PENDING_PAY);
        order.setStatus(Constants.NORMAL);
        order.setOriginalAmount(order.getOriginalAmount() == null ? order.getPayAmount() : order.getOriginalAmount());
        order.setDiscountAmount(order.getDiscountAmount() == null ? BigDecimal.ZERO : order.getDiscountAmount());
        order.setCouponAmount(order.getCouponAmount() == null ? BigDecimal.ZERO : order.getCouponAmount());
        order.setQuantity(order.getQuantity() == null ? 1 : order.getQuantity());

        int rows = orderInfoMapper.insert(order);
        if (rows > 0) {
            return Result.success(orderNo);
        }
        throw new BusinessException("创建订单失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> update(OrderInfo order) {
        if (order.getId() == null) {
            throw new BusinessException("订单ID不能为空");
        }
        int rows = orderInfoMapper.update(order);
        return Result.success(rows > 0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> cancelOrder(Long id, String cancelReason) {
        if (id == null) {
            throw new BusinessException("订单ID不能为空");
        }
        OrderInfo order = orderInfoMapper.selectById(id);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        if (Constants.ORDER_STATUS_PENDING_PAY.equals(order.getOrderStatus()) 
            || Constants.ORDER_STATUS_PENDING_CONFIRM.equals(order.getOrderStatus())) {
            OrderInfo update = new OrderInfo();
            update.setId(id);
            update.setOrderStatus(Constants.ORDER_STATUS_CANCELLED);
            update.setCancelReason(cancelReason);
            update.setCancelTime(LocalDateTime.now());
            int rows = orderInfoMapper.update(update);
            return Result.success(rows > 0);
        }
        throw new BusinessException("当前订单状态不可取消");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> payOrder(Long id, String payType) {
        if (id == null) {
            throw new BusinessException("订单ID不能为空");
        }
        OrderInfo order = orderInfoMapper.selectById(id);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        if (!Constants.ORDER_STATUS_PENDING_PAY.equals(order.getOrderStatus())) {
            throw new BusinessException("订单状态不正确，无法支付");
        }
        OrderInfo update = new OrderInfo();
        update.setId(id);
        update.setOrderStatus(Constants.ORDER_STATUS_PENDING_CONFIRM);
        update.setPayType(payType);
        update.setPayTime(LocalDateTime.now());
        int rows = orderInfoMapper.update(update);
        return Result.success(rows > 0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> updateStatus(Long id, String status) {
        if (id == null) {
            throw new BusinessException("订单ID不能为空");
        }
        if (status == null || status.isEmpty()) {
            throw new BusinessException("订单状态不能为空");
        }
        int rows = orderInfoMapper.updateStatus(id, status);
        return Result.success(rows > 0);
    }
}
