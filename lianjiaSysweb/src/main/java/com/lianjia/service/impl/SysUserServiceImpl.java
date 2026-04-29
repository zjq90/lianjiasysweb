package com.lianjia.service.impl;

import com.lianjia.common.Constants;
import com.lianjia.common.PageResult;
import com.lianjia.common.Result;
import com.lianjia.entity.SysUser;
import com.lianjia.exception.BusinessException;
import com.lianjia.mapper.SysUserMapper;
import com.lianjia.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public Result<SysUser> getById(Long id) {
        if (id == null) {
            throw new BusinessException("用户ID不能为空");
        }
        SysUser user = sysUserMapper.selectById(id);
        return Result.success(user);
    }

    @Override
    public Result<SysUser> getByOpenId(String openId) {
        if (openId == null || openId.isEmpty()) {
            throw new BusinessException("openId不能为空");
        }
        SysUser user = sysUserMapper.selectByOpenId(openId);
        return Result.success(user);
    }

    @Override
    public Result<SysUser> getByPhone(String phone) {
        if (phone == null || phone.isEmpty()) {
            throw new BusinessException("手机号不能为空");
        }
        SysUser user = sysUserMapper.selectByPhone(phone);
        return Result.success(user);
    }

    @Override
    public Result<List<SysUser>> getList(SysUser user) {
        List<SysUser> list = sysUserMapper.selectList(user);
        return Result.success(list);
    }

    @Override
    public Result<PageResult<SysUser>> getPage(SysUser user, Integer pageNum, Integer pageSize) {
        pageNum = pageNum == null ? Constants.DEFAULT_PAGE_NUM : pageNum;
        pageSize = pageSize == null ? Constants.DEFAULT_PAGE_SIZE : pageSize;
        int offset = (pageNum - 1) * pageSize;
        List<SysUser> list = sysUserMapper.selectPage(user, offset, pageSize);
        long total = sysUserMapper.selectCount(user);
        return Result.success(PageResult.of(pageNum, pageSize, total, list));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Long> add(SysUser user) {
        if (user.getPhone() == null || user.getPhone().isEmpty()) {
            throw new BusinessException("手机号不能为空");
        }
        SysUser existUser = sysUserMapper.selectByPhone(user.getPhone());
        if (existUser != null) {
            throw new BusinessException("手机号已存在");
        }
        if (user.getOpenId() != null && !user.getOpenId().isEmpty()) {
            SysUser existByOpenId = sysUserMapper.selectByOpenId(user.getOpenId());
            if (existByOpenId != null) {
                throw new BusinessException("openId已存在");
            }
        }
        user.setStatus(Constants.NORMAL);
        user.setBalance(BigDecimal.ZERO);
        int rows = sysUserMapper.insert(user);
        if (rows > 0) {
            return Result.success(user.getId());
        }
        throw new BusinessException("添加用户失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> update(SysUser user) {
        if (user.getId() == null) {
            throw new BusinessException("用户ID不能为空");
        }
        int rows = sysUserMapper.update(user);
        return Result.success(rows > 0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> delete(Long id) {
        if (id == null) {
            throw new BusinessException("用户ID不能为空");
        }
        int rows = sysUserMapper.deleteById(id);
        return Result.success(rows > 0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> updateStatus(Long id, Integer status) {
        if (id == null) {
            throw new BusinessException("用户ID不能为空");
        }
        int rows = sysUserMapper.updateStatus(id, status);
        return Result.success(rows > 0);
    }
}
