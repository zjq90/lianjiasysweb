package com.lianjia.mapper;

import com.lianjia.entity.ServiceItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ServiceItemMapper {

    ServiceItem selectById(@Param("id") Long id);

    List<ServiceItem> selectByCategoryId(@Param("categoryId") Long categoryId);

    List<ServiceItem> selectByPackageId(@Param("packageId") Long packageId);

    List<ServiceItem> selectList(ServiceItem service);

    List<ServiceItem> selectPage(@Param("service") ServiceItem service,
                                  @Param("offset") int offset,
                                  @Param("pageSize") int pageSize);

    long selectCount(ServiceItem service);

    int insert(ServiceItem service);

    int update(ServiceItem service);

    int deleteById(@Param("id") Long id);

    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    int incrementSales(@Param("id") Long id, @Param("count") int count);
}
