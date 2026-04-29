package com.lianjia.mapper;

import com.lianjia.entity.ServicePackage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ServicePackageMapper {

    ServicePackage selectById(@Param("id") Long id);

    List<ServicePackage> selectList(ServicePackage pkg);

    List<ServicePackage> selectPage(@Param("pkg") ServicePackage pkg,
                                     @Param("offset") int offset,
                                     @Param("pageSize") int pageSize);

    List<ServicePackage> selectByTagIds(@Param("tagIds") List<Long> tagIds);

    long selectCount(ServicePackage pkg);

    int insert(ServicePackage pkg);

    int update(ServicePackage pkg);

    int deleteById(@Param("id") Long id);

    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    int incrementSales(@Param("id") Long id, @Param("count") int count);
}
