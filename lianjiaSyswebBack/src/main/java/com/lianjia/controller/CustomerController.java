package com.lianjia.controller;

import com.lianjia.common.Result;
import com.lianjia.entity.Customer;
import com.lianjia.service.CustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Api(tags = "客户管理接口")
@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    
    @Autowired
    private CustomerService customerService;
    
    @ApiOperation("分页查询客户列表")
    @GetMapping
    public Result<Page<Customer>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String familyTag,
            @RequestParam(required = false) String memberLevel,
            @RequestParam(required = false) Integer status) {
        
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        Page<Customer> customers = customerService.findByFilters(name, phone, familyTag, memberLevel, status, pageRequest);
        return Result.success(customers);
    }
    
    @ApiOperation("根据ID查询客户")
    @GetMapping("/{id}")
    public Result<Customer> getById(@PathVariable Long id) {
        Optional<Customer> customer = customerService.findById(id);
        return customer.map(Result::success)
                .orElse(Result.error("客户不存在"));
    }
    
    @ApiOperation("新增客户")
    @PostMapping
    public Result<Customer> create(@RequestBody Customer customer) {
        Customer saved = customerService.save(customer);
        return Result.success("创建成功", saved);
    }
    
    @ApiOperation("更新客户")
    @PutMapping("/{id}")
    public Result<Customer> update(@PathVariable Long id, @RequestBody Customer customer) {
        Optional<Customer> existing = customerService.findById(id);
        if (!existing.isPresent()) {
            return Result.error("客户不存在");
        }
        customer.setId(id);
        Customer saved = customerService.save(customer);
        return Result.success("更新成功", saved);
    }
    
    @ApiOperation("删除客户")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        Optional<Customer> existing = customerService.findById(id);
        if (!existing.isPresent()) {
            return Result.error("客户不存在");
        }
        customerService.deleteById(id);
        return Result.success();
    }
    
    @ApiOperation("获取所有家庭标签")
    @GetMapping("/family-tags")
    public Result<List<String>> getFamilyTags() {
        List<String> tags = customerService.findAllFamilyTags();
        return Result.success(tags);
    }
    
    @ApiOperation("获取所有会员等级")
    @GetMapping("/member-levels")
    public Result<List<String>> getMemberLevels() {
        List<String> levels = customerService.findAllMemberLevels();
        return Result.success(levels);
    }
}
