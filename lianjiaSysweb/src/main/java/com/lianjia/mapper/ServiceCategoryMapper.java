package com.lianjia.mapper;

import com.lianjia.entity.ServiceCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ServiceCategoryMapper {

    ServiceCategory selectById(@Param("id") Long id);

    List<ServiceCategory> selectAll();

    List<ServiceCategory> selectByParentId(@Param("parentId") Long parentId);

    List<ServiceCategory> selectList(ServiceCategory category);

    int insert(ServiceCategory category);

    int update(ServiceCategory category);

    int deleteById(@Param("id") Long id);
}
