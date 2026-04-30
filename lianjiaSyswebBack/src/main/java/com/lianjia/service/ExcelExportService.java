package com.lianjia.service;

import com.lianjia.entity.Customer;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ExcelExportService {
    
    private final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,##0.00");
    
    public byte[] exportCustomers(List<Customer> customers) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("客户列表");
            
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dataStyle = createDataStyle(workbook);
            
            String[] headers = {"客户ID", "客户姓名", "手机号", "性别", "年龄", "地址", 
                                "家庭标签", "会员等级", "累计消费(元)", "订单数", "状态", "创建时间"};
            
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }
            
            int rowNum = 1;
            for (Customer customer : customers) {
                Row row = sheet.createRow(rowNum++);
                
                createCell(row, 0, customer.getId(), dataStyle);
                createCell(row, 1, customer.getName(), dataStyle);
                createCell(row, 2, customer.getPhone(), dataStyle);
                createCell(row, 3, customer.getGender(), dataStyle);
                createCell(row, 4, customer.getAge(), dataStyle);
                createCell(row, 5, customer.getAddress(), dataStyle);
                createCell(row, 6, customer.getFamilyTag(), dataStyle);
                createCell(row, 7, customer.getMemberLevel(), dataStyle);
                createCell(row, 8, formatAmount(customer.getTotalConsumption()), dataStyle);
                createCell(row, 9, customer.getOrderCount(), dataStyle);
                createCell(row, 10, formatStatus(customer.getStatus()), dataStyle);
                createCell(row, 11, customer.getCreateTime() != null ? 
                        customer.getCreateTime().format(DATE_FORMATTER) : "", dataStyle);
            }
            
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                workbook.write(outputStream);
                return outputStream.toByteArray();
            }
        }
    }
    
    private void createCell(Row row, int column, Object value, CellStyle style) {
        Cell cell = row.createCell(column);
        if (value != null) {
            if (value instanceof Long) {
                cell.setCellValue((Long) value);
            } else if (value instanceof Integer) {
                cell.setCellValue((Integer) value);
            } else if (value instanceof Double) {
                cell.setCellValue((Double) value);
            } else {
                cell.setCellValue(value.toString());
            }
        } else {
            cell.setCellValue("");
        }
        cell.setCellStyle(style);
    }
    
    private String formatAmount(Integer amount) {
        if (amount == null) return "0.00";
        return DECIMAL_FORMAT.format(amount / 100.0);
    }
    
    private String formatStatus(Integer status) {
        if (status == null) return "";
        return status == 1 ? "正常" : "禁用";
    }
    
    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        
        return style;
    }
    
    private CellStyle createDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }
}
