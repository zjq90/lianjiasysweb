package com.lianjia.controller;

import com.lianjia.common.PageResult;
import com.lianjia.common.Result;
import com.lianjia.entity.SysUser;
import com.lianjia.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "用户管理")
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private SysUserService sysUserService;

    @ApiOperation("根据ID获取用户信息")
    @GetMapping("/{id}")
    public Result<SysUser> getById(@ApiParam("用户ID") @PathVariable Long id) {
        return sysUserService.getById(id);
    }

    @ApiOperation("根据openId获取用户信息")
    @GetMapping("/openId/{openId}")
    public Result<SysUser> getByOpenId(@ApiParam("微信openId") @PathVariable String openId) {
        return sysUserService.getByOpenId(openId);
    }

    @ApiOperation("根据手机号获取用户信息")
    @GetMapping("/phone/{phone}")
    public Result<SysUser> getByPhone(@ApiParam("手机号") @PathVariable String phone) {
        return sysUserService.getByPhone(phone);
    }

    @ApiOperation("获取用户列表")
    @GetMapping("/list")
    public Result<List<SysUser>> getList(@ModelAttribute SysUser user) {
        return sysUserService.getList(user);
    }

    @ApiOperation("分页获取用户列表")
    @GetMapping("/page")
    public Result<PageResult<SysUser>> getPage(
            @ModelAttribute SysUser user,
            @ApiParam("页码") @RequestParam(required = false) Integer pageNum,
            @ApiParam("每页大小") @RequestParam(required = false) Integer pageSize) {
        return sysUserService.getPage(user, pageNum, pageSize);
    }

    @ApiOperation("新增用户")
    @PostMapping
    public Result<Long> add(@RequestBody SysUser user) {
        return sysUserService.add(user);
    }

    @ApiOperation("更新用户")
    @PutMapping
    public Result<Boolean> update(@RequestBody SysUser user) {
        return sysUserService.update(user);
    }

    @ApiOperation("删除用户")
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@ApiParam("用户ID") @PathVariable Long id) {
        return sysUserService.delete(id);
    }

    @ApiOperation("更新用户状态")
    @PutMapping("/status/{id}")
    public Result<Boolean> updateStatus(
            @ApiParam("用户ID") @PathVariable Long id,
            @ApiParam("状态") @RequestParam Integer status) {
        return sysUserService.updateStatus(id, status);
    }
}
