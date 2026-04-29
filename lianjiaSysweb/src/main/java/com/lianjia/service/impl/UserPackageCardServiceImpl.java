package com.lianjia.service.impl;

import com.lianjia.common.Constants;
import com.lianjia.common.Result;
import com.lianjia.entity.UserPackageCard;
import com.lianjia.exception.BusinessException;
import com.lianjia.mapper.UserPackageCardMapper;
import com.lianjia.service.UserPackageCardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class UserPackageCardServiceImpl implements UserPackageCardService {

    @Autowired
    private UserPackageCardMapper userPackageCardMapper;

    @Override
    public Result<UserPackageCard> getById(Long id) {
        if (id == null) {
            throw new BusinessException("套餐卡ID不能为空");
        }
        UserPackageCard card = userPackageCardMapper.selectById(id);
        return Result.success(card);
    }

    @Override
    public Result<List<UserPackageCard>> getByUserId(Long userId) {
        if (userId == null) {
            throw new BusinessException("用户ID不能为空");
        }
        List<UserPackageCard> list = userPackageCardMapper.selectByUserId(userId);
        return Result.success(list);
    }

    @Override
    public Result<List<UserPackageCard>> getValidByUserId(Long userId) {
        if (userId == null) {
            throw new BusinessException("用户ID不能为空");
        }
        List<UserPackageCard> list = userPackageCardMapper.selectValidByUserId(userId);
        return Result.success(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Long> add(UserPackageCard card) {
        if (card.getUserId() == null) {
            throw new BusinessException("用户ID不能为空");
        }
        if (card.getPackageId() == null) {
            throw new BusinessException("套餐ID不能为空");
        }
        card.setStatus(Constants.NORMAL);
        card.setUsedCount(card.getUsedCount() == null ? 0 : card.getUsedCount());
        int rows = userPackageCardMapper.insert(card);
        if (rows > 0) {
            return Result.success(card.getId());
        }
        throw new BusinessException("添加套餐卡失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> update(UserPackageCard card) {
        if (card.getId() == null) {
            throw new BusinessException("套餐卡ID不能为空");
        }
        int rows = userPackageCardMapper.update(card);
        return Result.success(rows > 0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> useCard(Long id) {
        if (id == null) {
            throw new BusinessException("套餐卡ID不能为空");
        }
        UserPackageCard card = userPackageCardMapper.selectById(id);
        if (card == null) {
            throw new BusinessException("套餐卡不存在");
        }
        if (card.getStatus() != Constants.NORMAL) {
            throw new BusinessException("套餐卡状态不正常");
        }
        if (card.getRemainingCount() <= 0) {
            throw new BusinessException("套餐卡次数已用完");
        }
        int rows = userPackageCardMapper.incrementUsedCount(id);
        return Result.success(rows > 0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> delete(Long id) {
        if (id == null) {
            throw new BusinessException("套餐卡ID不能为空");
        }
        int rows = userPackageCardMapper.deleteById(id);
        return Result.success(rows > 0);
    }
}
