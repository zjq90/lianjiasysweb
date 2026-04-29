package com.lianjia.mapper;

import com.lianjia.entity.UserPackageCard;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserPackageCardMapper {

    UserPackageCard selectById(@Param("id") Long id);

    List<UserPackageCard> selectByUserId(@Param("userId") Long userId);

    List<UserPackageCard> selectValidByUserId(@Param("userId") Long userId);

    int insert(UserPackageCard card);

    int update(UserPackageCard card);

    int deleteById(@Param("id") Long id);

    int incrementUsedCount(@Param("id") Long id);
}
