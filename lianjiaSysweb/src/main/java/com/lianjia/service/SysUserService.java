package com.lianjia.service;

import com.lianjia.common.PageResult;
import com.lianjia.common.Result;
import com.lianjia.entity.SysUser;

import java.util.List;

public interface SysUserService {

    Result<SysUser> getById(Long id);

    Result<SysUser> getByOpenId(String openId);

    Result<SysUser> getByPhone(String phone);

    Result<List<SysUser>> getList(SysUser user);

    Result<PageResult<SysUser>> getPage(SysUser user, Integer pageNum, Integer pageSize);

    Result<Long> add(SysUser user);

    Result<Boolean> update(SysUser user);

    Result<Boolean> delete(Long id);

    Result<Boolean> updateStatus(Long id, Integer status);
}
