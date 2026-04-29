package com.lianjia.service.impl;

import com.lianjia.common.Constants;
import com.lianjia.common.Result;
import com.lianjia.entity.UserAddress;
import com.lianjia.exception.BusinessException;
import com.lianjia.mapper.UserAddressMapper;
import com.lianjia.service.UserAddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class UserAddressServiceImpl implements UserAddressService {

    @Autowired
    private UserAddressMapper userAddressMapper;

    @Override
    public Result<UserAddress> getById(Long id) {
        if (id == null) {
            throw new BusinessException("地址ID不能为空");
        }
        UserAddress address = userAddressMapper.selectById(id);
        return Result.success(address);
    }

    @Override
    public Result<List<UserAddress>> getByUserId(Long userId) {
        if (userId == null) {
            throw new BusinessException("用户ID不能为空");
        }
        List<UserAddress> list = userAddressMapper.selectByUserId(userId);
        return Result.success(list);
    }

    @Override
    public Result<UserAddress> getDefaultAddress(Long userId) {
        if (userId == null) {
            throw new BusinessException("用户ID不能为空");
        }
        UserAddress address = userAddressMapper.selectDefaultAddress(userId);
        return Result.success(address);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Long> add(UserAddress address) {
        if (address.getUserId() == null) {
            throw new BusinessException("用户ID不能为空");
        }
        if (address.getName() == null || address.getName().isEmpty()) {
            throw new BusinessException("收货人姓名不能为空");
        }
        if (address.getPhone() == null || address.getPhone().isEmpty()) {
            throw new BusinessException("收货人电话不能为空");
        }
        if (address.getDetailAddress() == null || address.getDetailAddress().isEmpty()) {
            throw new BusinessException("详细地址不能为空");
        }

        address.setStatus(Constants.NORMAL);
        
        if (address.getIsDefault() == null) {
            List<UserAddress> existList = userAddressMapper.selectByUserId(address.getUserId());
            address.setIsDefault(existList == null || existList.isEmpty() ? 1 : 0);
        }

        if (address.getIsDefault() != null && address.getIsDefault() == 1) {
            userAddressMapper.updateNonDefault(address.getUserId());
        }

        int rows = userAddressMapper.insert(address);
        if (rows > 0) {
            return Result.success(address.getId());
        }
        throw new BusinessException("添加地址失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> update(UserAddress address) {
        if (address.getId() == null) {
            throw new BusinessException("地址ID不能为空");
        }
        if (address.getIsDefault() != null && address.getIsDefault() == 1) {
            UserAddress exist = userAddressMapper.selectById(address.getId());
            if (exist != null) {
                userAddressMapper.updateNonDefault(exist.getUserId());
            }
        }
        int rows = userAddressMapper.update(address);
        return Result.success(rows > 0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> delete(Long id) {
        if (id == null) {
            throw new BusinessException("地址ID不能为空");
        }
        int rows = userAddressMapper.deleteById(id);
        return Result.success(rows > 0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> setDefault(Long id, Long userId) {
        if (id == null) {
            throw new BusinessException("地址ID不能为空");
        }
        if (userId == null) {
            throw new BusinessException("用户ID不能为空");
        }
        userAddressMapper.updateNonDefault(userId);
        int rows = userAddressMapper.setDefault(id);
        return Result.success(rows > 0);
    }
}
