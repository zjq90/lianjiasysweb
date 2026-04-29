package com.lianjia.mapper;

import com.lianjia.entity.FamilyProfile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FamilyProfileMapper {

    FamilyProfile selectById(@Param("id") Long id);

    List<FamilyProfile> selectByUserId(@Param("userId") Long userId);

    FamilyProfile selectDefaultByUserId(@Param("userId") Long userId);

    List<FamilyProfile> selectList(FamilyProfile profile);

    List<FamilyProfile> selectNeedReminder();

    int insert(FamilyProfile profile);

    int update(FamilyProfile profile);

    int deleteById(@Param("id") Long id);

    int updateHasReminder(@Param("id") Long id, @Param("hasReminder") Integer hasReminder);
}
