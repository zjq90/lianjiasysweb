package com.lianjia.service;

import com.lianjia.common.Result;
import com.lianjia.entity.UserPackageCard;

import java.util.List;

public interface UserPackageCardService {

    Result<UserPackageCard> getById(Long id);

    Result<List<UserPackageCard>> getByUserId(Long userId);

    Result<List<UserPackageCard>> getValidByUserId(Long userId);

    Result<Long> add(UserPackageCard card);

    Result<Boolean> update(UserPackageCard card);

    Result<Boolean> useCard(Long id);

    Result<Boolean> delete(Long id);
}
