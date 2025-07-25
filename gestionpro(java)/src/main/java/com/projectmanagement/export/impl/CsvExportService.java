package com.projectmanagement.export.impl;

import com.projectmanagement.export.ExportException;
import com.projectmanagement.export.ExportFormat;
import com.projectmanagement.export.ExportOptions;
import com.projectmanagement.export.ExportService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Implementation of ExportService for CSV exports using Apache Commons CSV.
 */
@Service
public class CsvExportService implements ExportService {

    @Override
    public <T> void export(List<T> data, Map<String, String> headers, OutputStream outputStream, 
                          ExportOptions options) throws ExportException {
        try (OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {
            // Prepare CSV format
            CSVFormat.Builder formatBuilder = CSVFormat.DEFAULT.builder();
            
            // Add headers if requested
            if (options.isIncludeHeaders() && headers != null && !headers.isEmpty()) {
                formatBuilder.setHeader(headers.values().toArray(new String[0]));
            }
            
            // Configure the CSV format
            CSVFormat csvFormat = formatBuilder.build();
            
            // Create CSV printer
            try (CSVPrinter csvPrinter = new CSVPrinter(writer, csvFormat)) {
                // Add data rows
                if (data != null && !data.isEmpty()) {
                    Class<?> clazz = data.get(0).getClass();
                    
                    for (T item : data) {
                        List<Object> rowData = new ArrayList<>();
                        
                        for (String fieldName : headers.keySet()) {
                            try {
                                Field field = clazz.getDeclaredField(fieldName);
                                field.setAccessible(true);
                                Object value = field.get(item);
                                rowData.add(formatValue(value, options));
                            } catch (NoSuchFieldException | IllegalAccessException e) {
                                rowData.add("");
                            }
                        }
                        
                        csvPrinter.printRecord(rowData);
                    }
                }
                
                csvPrinter.flush();
            }
        } catch (IOException e) {
            throw new ExportException("Error creating CSV file", e);
        }
    }

    @Override
    public String getContentType() {
        return ExportFormat.CSV.getContentType();
    }

    @Override
    public String getFileExtension() {
        return ExportFormat.CSV.getExtension();
    }
    
    /**
     * Formats a value for CSV output based on its type.
     */
    private Object formatValue(Object value, ExportOptions options) {
        if (value == null) {
            return "";
        }

        if (value instanceof Number) {
            if (options.isFormatNumbers() && options.getNumberPattern() != null) {
                return String.format(options.getNumberPattern(), value);
            }
            return value;
        } else if (value instanceof LocalDate) {
            if (options.isFormatDates()) {
                String pattern = options.getDatePattern() != null ? options.getDatePattern() : "yyyy-MM-dd";
                return ((LocalDate) value).format(DateTimeFormatter.ofPattern(pattern));
            }
            return value.toString();
        } else if (value instanceof LocalDateTime) {
            if (options.isFormatDates()) {
                String pattern = options.getDatePattern() != null ? options.getDatePattern() : "yyyy-MM-dd HH:mm:ss";
                return ((LocalDateTime) value).format(DateTimeFormatter.ofPattern(pattern));
            }
            return value.toString();
        } else if (value instanceof Date) {
            if (options.isFormatDates()) {
                String pattern = options.getDatePattern() != null ? options.getDatePattern() : "yyyy-MM-dd";
                return new SimpleDateFormat(pattern).format((Date) value);
            }
            return value.toString();
        } else {
            return value.toString();
        }
    }
} 