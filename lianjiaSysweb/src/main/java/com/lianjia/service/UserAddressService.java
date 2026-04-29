package com.lianjia.service;

import com.lianjia.common.Result;
import com.lianjia.entity.UserAddress;

import java.util.List;

public interface UserAddressService {

    Result<UserAddress> getById(Long id);

    Result<List<UserAddress>> getByUserId(Long userId);

    Result<UserAddress> getDefaultAddress(Long userId);

    Result<Long> add(UserAddress address);

    Result<Boolean> update(UserAddress address);

    Result<Boolean> delete(Long id);

    Result<Boolean> setDefault(Long id, Long userId);
}
