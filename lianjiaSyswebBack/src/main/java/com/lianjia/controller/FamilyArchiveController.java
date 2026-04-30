package com.lianjia.controller;

import com.lianjia.common.Result;
import com.lianjia.entity.FamilyArchive;
import com.lianjia.service.FamilyArchiveService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Api(tags = "家庭档案接口")
@RestController
@RequestMapping("/api/family-archives")
public class FamilyArchiveController {
    
    @Autowired
    private FamilyArchiveService familyArchiveService;
    
    @ApiOperation("根据客户ID查询家庭档案")
    @GetMapping("/customer/{customerId}")
    public Result<FamilyArchive> getByCustomerId(@PathVariable Long customerId) {
        Optional<FamilyArchive> archive = familyArchiveService.findByCustomerId(customerId);
        return archive.map(Result::success)
                .orElse(Result.error("家庭档案不存在"));
    }
    
    @ApiOperation("根据ID查询家庭档案")
    @GetMapping("/{id}")
    public Result<FamilyArchive> getById(@PathVariable Long id) {
        Optional<FamilyArchive> archive = familyArchiveService.findById(id);
        return archive.map(Result::success)
                .orElse(Result.error("家庭档案不存在"));
    }
    
    @ApiOperation("新增家庭档案")
    @PostMapping
    public Result<FamilyArchive> create(@RequestBody FamilyArchive familyArchive) {
        if (familyArchiveService.existsByCustomerId(familyArchive.getCustomerId())) {
            return Result.error("该客户已有家庭档案");
        }
        FamilyArchive saved = familyArchiveService.save(familyArchive);
        return Result.success("创建成功", saved);
    }
    
    @ApiOperation("更新家庭档案")
    @PutMapping("/{id}")
    public Result<FamilyArchive> update(@PathVariable Long id, @RequestBody FamilyArchive familyArchive) {
        Optional<FamilyArchive> existing = familyArchiveService.findById(id);
        if (!existing.isPresent()) {
            return Result.error("家庭档案不存在");
        }
        familyArchive.setId(id);
        FamilyArchive saved = familyArchiveService.save(familyArchive);
        return Result.success("更新成功", saved);
    }
    
    @ApiOperation("删除家庭档案")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        Optional<FamilyArchive> existing = familyArchiveService.findById(id);
        if (!existing.isPresent()) {
            return Result.error("家庭档案不存在");
        }
        familyArchiveService.deleteById(id);
        return Result.success();
    }
}
