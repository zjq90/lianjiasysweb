package com.lianjia.mapper;

import com.lianjia.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysUserMapper {

    SysUser selectById(@Param("id") Long id);

    SysUser selectByOpenId(@Param("openId") String openId);

    SysUser selectByPhone(@Param("phone") String phone);

    List<SysUser> selectList(SysUser user);

    List<SysUser> selectPage(@Param("user") SysUser user,
                              @Param("offset") int offset,
                              @Param("pageSize") int pageSize);

    long selectCount(SysUser user);

    int insert(SysUser user);

    int update(SysUser user);

    int deleteById(@Param("id") Long id);

    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
}
