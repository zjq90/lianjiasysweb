package com.lianjia.mapper;

import com.lianjia.entity.FamilyMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FamilyMemberMapper {

    FamilyMember selectById(@Param("id") Long id);

    List<FamilyMember> selectByFamilyId(@Param("familyId") Long familyId);

    int insert(FamilyMember member);

    int update(FamilyMember member);

    int deleteById(@Param("id") Long id);

    int deleteByFamilyId(@Param("familyId") Long familyId);
}
