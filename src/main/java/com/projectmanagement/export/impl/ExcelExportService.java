package com.projectmanagement.export.impl;

import com.projectmanagement.export.ExportException;
import com.projectmanagement.export.ExportFormat;
import com.projectmanagement.export.ExportOptions;
import com.projectmanagement.export.ExportService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * Implementation of ExportService for Excel exports using Apache POI.
 */
@Service
public class ExcelExportService implements ExportService {

    @Override
    public <T> void export(List<T> data, Map<String, String> headers, OutputStream outputStream, 
                          ExportOptions options) throws ExportException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(options.getTitle() != null ? options.getTitle() : "Export");
            
            // Create header style
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            
            // Create date style
            CellStyle dateStyle = workbook.createCellStyle();
            String datePattern = options.getDatePattern() != null ? options.getDatePattern() : "yyyy-MM-dd";
            CreationHelper createHelper = workbook.getCreationHelper();
            dateStyle.setDataFormat(createHelper.createDataFormat().getFormat(datePattern));
            
            // Create number style
            CellStyle numberStyle = workbook.createCellStyle();
            String numberPattern = options.getNumberPattern() != null ? options.getNumberPattern() : "#,##0.00";
            numberStyle.setDataFormat(createHelper.createDataFormat().getFormat(numberPattern));
            
            int rowNum = 0;
            
            // Add headers if requested
            if (options.isIncludeHeaders() && headers != null && !headers.isEmpty()) {
                Row headerRow = sheet.createRow(rowNum++);
                int colNum = 0;
                
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    Cell cell = headerRow.createCell(colNum++);
                    cell.setCellValue(entry.getValue());
                    cell.setCellStyle(headerStyle);
                }
            }
            
            // Add data rows
            if (data != null && !data.isEmpty()) {
                Class<?> clazz = data.get(0).getClass();
                Field[] fields = clazz.getDeclaredFields();
                
                for (T item : data) {
                    Row row = sheet.createRow(rowNum++);
                    int colNum = 0;
                    
                    for (Map.Entry<String, String> entry : headers.entrySet()) {
                        String fieldName = entry.getKey();
                        try {
                            Field field = clazz.getDeclaredField(fieldName);
                            field.setAccessible(true);
                            Object value = field.get(item);
                            
                            Cell cell = row.createCell(colNum++);
                            setCellValue(cell, value, dateStyle, numberStyle, options);
                        } catch (NoSuchFieldException | IllegalAccessException e) {
                            row.createCell(colNum++).setCellValue("");
                        }
                    }
                }
            }
            
            // Auto-size columns
            for (int i = 0; i < headers.size(); i++) {
                sheet.autoSizeColumn(i);
            }
            
            // Add metadata if available - commented out because getProperties() is not available
            /*
            if (options.getTitle() != null || options.getAuthor() != null || options.getDescription() != null) {
                workbook.getProperties().getCoreProperties().setTitle(options.getTitle());
                workbook.getProperties().getCoreProperties().setCreator(options.getAuthor());
                workbook.getProperties().getCoreProperties().setDescription(options.getDescription());
            }
            */
            
            // Write to output stream
            workbook.write(outputStream);
        } catch (IOException e) {
            throw new ExportException("Error creating Excel file", e);
        }
    }

    @Override
    public String getContentType() {
        return ExportFormat.EXCEL.getContentType();
    }

    @Override
    public String getFileExtension() {
        return ExportFormat.EXCEL.getExtension();
    }
    
    /**
     * Sets the cell value based on the value's type.
     */
    private void setCellValue(Cell cell, Object value, CellStyle dateStyle, CellStyle numberStyle, ExportOptions options) {
        if (value == null) {
            cell.setCellValue("");
            return;
        }

        if (value instanceof Number) {
            Number number = (Number) value;
            cell.setCellValue(number.doubleValue());
            if (options.isFormatNumbers()) {
                cell.setCellStyle(numberStyle);
            }
        } else if (value instanceof LocalDate) {
            Date date = Date.from(((LocalDate) value).atStartOfDay(ZoneId.systemDefault()).toInstant());
            cell.setCellValue(date);
            if (options.isFormatDates()) {
                cell.setCellStyle(dateStyle);
            }
        } else if (value instanceof LocalDateTime) {
            Date date = Date.from(((LocalDateTime) value).atZone(ZoneId.systemDefault()).toInstant());
            cell.setCellValue(date);
            if (options.isFormatDates()) {
                cell.setCellStyle(dateStyle);
            }
        } else if (value instanceof Date) {
            cell.setCellValue((Date) value);
            if (options.isFormatDates()) {
                cell.setCellStyle(dateStyle);
            }
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else {
            cell.setCellValue(value.toString());
        }
    }
} 