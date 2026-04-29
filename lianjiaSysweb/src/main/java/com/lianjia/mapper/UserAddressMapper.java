package com.lianjia.mapper;

import com.lianjia.entity.UserAddress;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserAddressMapper {

    UserAddress selectById(@Param("id") Long id);

    List<UserAddress> selectByUserId(@Param("userId") Long userId);

    UserAddress selectDefaultAddress(@Param("userId") Long userId);

    List<UserAddress> selectList(UserAddress address);

    int insert(UserAddress address);

    int update(UserAddress address);

    int deleteById(@Param("id") Long id);

    int updateNonDefault(@Param("userId") Long userId);

    int setDefault(@Param("id") Long id);
}
