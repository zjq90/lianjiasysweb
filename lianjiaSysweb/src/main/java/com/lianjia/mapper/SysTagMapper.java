package com.lianjia.mapper;

import com.lianjia.entity.SysTag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysTagMapper {

    SysTag selectById(@Param("id") Long id);

    SysTag selectByCode(@Param("tagCode") String tagCode);

    List<SysTag> selectByCategory(@Param("category") String category);

    List<SysTag> selectList(SysTag tag);

    List<SysTag> selectByPackageId(@Param("packageId") Long packageId);

    List<SysTag> selectByCouponId(@Param("couponId") Long couponId);

    List<SysTag> selectByUserId(@Param("userId") Long userId);

    List<SysTag> selectByFamilyId(@Param("familyId") Long familyId);

    int insert(SysTag tag);

    int update(SysTag tag);

    int deleteById(@Param("id") Long id);
}
