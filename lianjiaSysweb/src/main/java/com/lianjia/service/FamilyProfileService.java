package com.lianjia.service;

import com.lianjia.common.PageResult;
import com.lianjia.common.Result;
import com.lianjia.entity.FamilyMember;
import com.lianjia.entity.FamilyProfile;

import java.util.List;

public interface FamilyProfileService {

    Result<FamilyProfile> getById(Long id);

    Result<List<FamilyProfile>> getByUserId(Long userId);

    Result<FamilyProfile> getDefaultByUserId(Long userId);

    Result<PageResult<FamilyProfile>> getPage(FamilyProfile profile, Integer pageNum, Integer pageSize);

    Result<Long> add(FamilyProfile profile);

    Result<Boolean> update(FamilyProfile profile);

    Result<Boolean> delete(Long id);

    Result<Boolean> addMember(FamilyMember member);

    Result<Boolean> updateMember(FamilyMember member);

    Result<Boolean> deleteMember(Long id);

    Result<List<FamilyMember>> getMembersByFamilyId(Long familyId);

    Result<List<FamilyProfile>> getNeedReminder();

    Result<Boolean> updateHasReminder(Long id, Integer hasReminder);
}
