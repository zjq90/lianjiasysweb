package com.lianjia.service.impl;

import com.lianjia.common.Constants;
import com.lianjia.common.PageResult;
import com.lianjia.common.Result;
import com.lianjia.entity.FamilyMember;
import com.lianjia.entity.FamilyProfile;
import com.lianjia.entity.SysTag;
import com.lianjia.exception.BusinessException;
import com.lianjia.mapper.FamilyMemberMapper;
import com.lianjia.mapper.FamilyProfileMapper;
import com.lianjia.mapper.SysTagMapper;
import com.lianjia.service.FamilyProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class FamilyProfileServiceImpl implements FamilyProfileService {

    @Autowired
    private FamilyProfileMapper familyProfileMapper;

    @Autowired
    private FamilyMemberMapper familyMemberMapper;

    @Autowired
    private SysTagMapper sysTagMapper;

    @Override
    public Result<FamilyProfile> getById(Long id) {
        if (id == null) {
            throw new BusinessException("家庭档案ID不能为空");
        }
        FamilyProfile profile = familyProfileMapper.selectById(id);
        if (profile != null) {
            List<FamilyMember> members = familyMemberMapper.selectByFamilyId(id);
            profile.setMembers(members);
            List<SysTag> tags = sysTagMapper.selectByFamilyId(id);
            profile.setTags(tags);
        }
        return Result.success(profile);
    }

    @Override
    public Result<List<FamilyProfile>> getByUserId(Long userId) {
        if (userId == null) {
            throw new BusinessException("用户ID不能为空");
        }
        List<FamilyProfile> list = familyProfileMapper.selectByUserId(userId);
        if (list != null && !list.isEmpty()) {
            for (FamilyProfile profile : list) {
                List<FamilyMember> members = familyMemberMapper.selectByFamilyId(profile.getId());
                profile.setMembers(members);
                List<SysTag> tags = sysTagMapper.selectByFamilyId(profile.getId());
                profile.setTags(tags);
            }
        }
        return Result.success(list);
    }

    @Override
    public Result<FamilyProfile> getDefaultByUserId(Long userId) {
        if (userId == null) {
            throw new BusinessException("用户ID不能为空");
        }
        FamilyProfile profile = familyProfileMapper.selectDefaultByUserId(userId);
        if (profile == null) {
            List<FamilyProfile> list = familyProfileMapper.selectByUserId(userId);
            if (list != null && !list.isEmpty()) {
                profile = list.get(0);
            }
        }
        if (profile != null) {
            List<FamilyMember> members = familyMemberMapper.selectByFamilyId(profile.getId());
            profile.setMembers(members);
            List<SysTag> tags = sysTagMapper.selectByFamilyId(profile.getId());
            profile.setTags(tags);
        }
        return Result.success(profile);
    }

    @Override
    public Result<PageResult<FamilyProfile>> getPage(FamilyProfile profile, Integer pageNum, Integer pageSize) {
        pageNum = pageNum == null ? Constants.DEFAULT_PAGE_NUM : pageNum;
        pageSize = pageSize == null ? Constants.DEFAULT_PAGE_SIZE : pageSize;
        int offset = (pageNum - 1) * pageSize;
        return Result.success(new PageResult<>());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Long> add(FamilyProfile profile) {
        if (profile.getUserId() == null) {
            throw new BusinessException("用户ID不能为空");
        }
        profile.setStatus(Constants.NORMAL);
        profile.setHasReminder(0);
        int rows = familyProfileMapper.insert(profile);
        if (rows > 0) {
            return Result.success(profile.getId());
        }
        throw new BusinessException("添加家庭档案失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> update(FamilyProfile profile) {
        if (profile.getId() == null) {
            throw new BusinessException("家庭档案ID不能为空");
        }
        int rows = familyProfileMapper.update(profile);
        return Result.success(rows > 0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> delete(Long id) {
        if (id == null) {
            throw new BusinessException("家庭档案ID不能为空");
        }
        familyMemberMapper.deleteByFamilyId(id);
        int rows = familyProfileMapper.deleteById(id);
        return Result.success(rows > 0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> addMember(FamilyMember member) {
        if (member.getFamilyId() == null) {
            throw new BusinessException("家庭档案ID不能为空");
        }
        member.setStatus(Constants.NORMAL);
        int rows = familyMemberMapper.insert(member);
        return Result.success(rows > 0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> updateMember(FamilyMember member) {
        if (member.getId() == null) {
            throw new BusinessException("成员ID不能为空");
        }
        int rows = familyMemberMapper.update(member);
        return Result.success(rows > 0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> deleteMember(Long id) {
        if (id == null) {
            throw new BusinessException("成员ID不能为空");
        }
        int rows = familyMemberMapper.deleteById(id);
        return Result.success(rows > 0);
    }

    @Override
    public Result<List<FamilyMember>> getMembersByFamilyId(Long familyId) {
        if (familyId == null) {
            throw new BusinessException("家庭档案ID不能为空");
        }
        List<FamilyMember> list = familyMemberMapper.selectByFamilyId(familyId);
        return Result.success(list);
    }

    @Override
    public Result<List<FamilyProfile>> getNeedReminder() {
        List<FamilyProfile> list = familyProfileMapper.selectNeedReminder();
        return Result.success(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> updateHasReminder(Long id, Integer hasReminder) {
        if (id == null) {
            throw new BusinessException("家庭档案ID不能为空");
        }
        int rows = familyProfileMapper.updateHasReminder(id, hasReminder);
        return Result.success(rows > 0);
    }
}
