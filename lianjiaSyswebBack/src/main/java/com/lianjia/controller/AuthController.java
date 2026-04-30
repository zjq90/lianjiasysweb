package com.lianjia.controller;

import com.lianjia.common.Result;
import com.lianjia.dto.LoginRequest;
import com.lianjia.dto.LoginResponse;
import com.lianjia.service.MerchantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(tags = "认证接口")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private MerchantService merchantService;
    
    @ApiOperation("商户登录")
    @PostMapping("/login")
    public Result<LoginResponse> login(@Validated @RequestBody LoginRequest request) {
        try {
            LoginResponse response = merchantService.login(request);
            return Result.success("登录成功", response);
        } catch (BadCredentialsException e) {
            return Result.error("用户名或密码错误");
        } catch (Exception e) {
            return Result.error("登录失败: " + e.getMessage());
        }
    }
    
    @ApiOperation("测试接口")
    @GetMapping("/test")
    public Result<String> test() {
        return Result.success("服务正常运行");
    }
}
