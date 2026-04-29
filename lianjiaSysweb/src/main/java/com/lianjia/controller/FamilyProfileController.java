package com.lianjia.controller;

import com.lianjia.common.Result;
import com.lianjia.entity.FamilyMember;
import com.lianjia.entity.FamilyProfile;
import com.lianjia.service.FamilyProfileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "家庭档案管理")
@RestController
@RequestMapping("/api/family")
public class FamilyProfileController {

    @Autowired
    private FamilyProfileService familyProfileService;

    @ApiOperation("根据ID获取家庭档案详情")
    @GetMapping("/{id}")
    public Result<FamilyProfile> getById(@ApiParam("家庭档案ID") @PathVariable Long id) {
        return familyProfileService.getById(id);
    }

    @ApiOperation("根据用户ID获取家庭档案列表")
    @GetMapping("/user/{userId}")
    public Result<List<FamilyProfile>> getByUserId(@ApiParam("用户ID") @PathVariable Long userId) {
        return familyProfileService.getByUserId(userId);
    }

    @ApiOperation("获取用户的默认家庭档案")
    @GetMapping("/default/{userId}")
    public Result<FamilyProfile> getDefaultByUserId(@ApiParam("用户ID") @PathVariable Long userId) {
        return familyProfileService.getDefaultByUserId(userId);
    }

    @ApiOperation("新增家庭档案")
    @PostMapping
    public Result<Long> add(@RequestBody FamilyProfile profile) {
        return familyProfileService.add(profile);
    }

    @ApiOperation("更新家庭档案")
    @PutMapping
    public Result<Boolean> update(@RequestBody FamilyProfile profile) {
        return familyProfileService.update(profile);
    }

    @ApiOperation("删除家庭档案")
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@ApiParam("家庭档案ID") @PathVariable Long id) {
        return familyProfileService.delete(id);
    }

    @ApiOperation("新增家庭成员")
    @PostMapping("/member")
    public Result<Boolean> addMember(@RequestBody FamilyMember member) {
        return familyProfileService.addMember(member);
    }

    @ApiOperation("更新家庭成员")
    @PutMapping("/member")
    public Result<Boolean> updateMember(@RequestBody FamilyMember member) {
        return familyProfileService.updateMember(member);
    }

    @ApiOperation("删除家庭成员")
    @DeleteMapping("/member/{id}")
    public Result<Boolean> deleteMember(@ApiParam("成员ID") @PathVariable Long id) {
        return familyProfileService.deleteMember(id);
    }

    @ApiOperation("获取家庭档案的成员列表")
    @GetMapping("/members/{familyId}")
    public Result<List<FamilyMember>> getMembersByFamilyId(@ApiParam("家庭档案ID") @PathVariable Long familyId) {
        return familyProfileService.getMembersByFamilyId(familyId);
    }

    @ApiOperation("更新提醒状态")
    @PutMapping("/reminder/{id}")
    public Result<Boolean> updateHasReminder(
            @ApiParam("家庭档案ID") @PathVariable Long id,
            @ApiParam("是否已提醒: 0-否, 1-是") @RequestParam Integer hasReminder) {
        return familyProfileService.updateHasReminder(id, hasReminder);
    }
}
