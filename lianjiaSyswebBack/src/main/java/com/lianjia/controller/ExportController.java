package com.lianjia.controller;

import com.lianjia.entity.Customer;
import com.lianjia.service.CustomerService;
import com.lianjia.service.ExcelExportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Api(tags = "数据导出接口")
@RestController
@RequestMapping("/api/export")
public class ExportController {
    
    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private ExcelExportService excelExportService;
    
    @ApiOperation("导出客户数据到Excel")
    @GetMapping("/customers")
    public ResponseEntity<byte[]> exportCustomers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String familyTag,
            @RequestParam(required = false) String memberLevel,
            @RequestParam(required = false) Integer status) {
        try {
            List<Customer> customers = customerService.findByFiltersForExport(
                    name, phone, familyTag, memberLevel, status);
            
            byte[] excelBytes = excelExportService.exportCustomers(customers);
            
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            String filename = "客户数据_" + timestamp + ".xlsx";
            String encodedFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8.toString()).replaceAll("\\+", "%20");
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFilename)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(excelBytes);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
