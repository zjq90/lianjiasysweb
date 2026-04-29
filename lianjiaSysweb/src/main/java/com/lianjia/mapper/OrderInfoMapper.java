package com.lianjia.mapper;

import com.lianjia.entity.OrderInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderInfoMapper {

    OrderInfo selectById(@Param("id") Long id);

    OrderInfo selectByOrderNo(@Param("orderNo") String orderNo);

    List<OrderInfo> selectByUserId(@Param("userId") Long userId);

    List<OrderInfo> selectByUserIdAndStatus(@Param("userId") Long userId, @Param("status") String status);

    List<OrderInfo> selectList(OrderInfo order);

    List<OrderInfo> selectPage(@Param("order") OrderInfo order,
                                @Param("offset") int offset,
                                @Param("pageSize") int pageSize);

    long selectCount(OrderInfo order);

    int insert(OrderInfo order);

    int update(OrderInfo order);

    int updateStatus(@Param("id") Long id, @Param("status") String status);

    int deleteById(@Param("id") Long id);
}
