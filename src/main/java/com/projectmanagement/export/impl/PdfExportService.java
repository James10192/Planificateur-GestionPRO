package com.projectmanagement.export.impl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.projectmanagement.export.ExportException;
import com.projectmanagement.export.ExportFormat;
import com.projectmanagement.export.ExportOptions;
import com.projectmanagement.export.ExportService;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Implementation of ExportService for PDF exports using iText.
 */
@Service
public class PdfExportService implements ExportService {

    @Override
    public <T> void export(List<T> data, Map<String, String> headers, OutputStream outputStream, 
                          ExportOptions options) throws ExportException {
        try {
            // Create document
            Document document = new Document(PageSize.A4.rotate(), 36, 36, 36, 36);
            PdfWriter.getInstance(document, outputStream);
            document.open();
            
            // Add document metadata
            if (options.getTitle() != null) {
                document.addTitle(options.getTitle());
            }
            if (options.getAuthor() != null) {
                document.addAuthor(options.getAuthor());
            }
            if (options.getDescription() != null) {
                document.addSubject(options.getDescription());
            }
            
            // Add title if available
            if (options.getTitle() != null) {
                Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
                Paragraph title = new Paragraph(options.getTitle(), titleFont);
                title.setAlignment(Element.ALIGN_CENTER);
                title.setSpacingAfter(15);
                document.add(title);
            }
            
            // Create table
            PdfPTable table = new PdfPTable(headers.size());
            table.setWidthPercentage(100);
            
            // Set up table headers
            if (options.isIncludeHeaders() && headers != null && !headers.isEmpty()) {
                // Header style
                Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
                BaseColor headerBg = new BaseColor(79, 129, 189); // Blue header
                
                for (String headerText : headers.values()) {
                    PdfPCell cell = new PdfPCell(new Phrase(headerText, headerFont));
                    cell.setBackgroundColor(headerBg);
                    cell.setPadding(5);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                }
                table.setHeaderRows(1);
            }
            
            // Add data rows
            if (data != null && !data.isEmpty()) {
                Class<?> clazz = data.get(0).getClass();
                BaseColor altColor = new BaseColor(242, 242, 242); // Light gray for alternate rows
                boolean alternate = false;
                
                for (T item : data) {
                    alternate = !alternate;
                    
                    for (String fieldName : headers.keySet()) {
                        try {
                            Field field = clazz.getDeclaredField(fieldName);
                            field.setAccessible(true);
                            Object value = field.get(item);
                            
                            String formattedValue = formatValue(value, options);
                            PdfPCell cell = new PdfPCell(new Phrase(formattedValue));
                            cell.setPadding(5);
                            
                            if (alternate) {
                                cell.setBackgroundColor(altColor);
                            }
                            
                            table.addCell(cell);
                        } catch (NoSuchFieldException | IllegalAccessException e) {
                            PdfPCell cell = new PdfPCell(new Phrase(""));
                            cell.setPadding(5);
                            
                            if (alternate) {
                                cell.setBackgroundColor(altColor);
                            }
                            
                            table.addCell(cell);
                        }
                    }
                }
            }
            
            document.add(table);
            
            // Add footer with timestamp
            Font footerFont = FontFactory.getFont(FontFactory.HELVETICA, 8, BaseColor.GRAY);
            Paragraph footer = new Paragraph("Exported on: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), footerFont);
            footer.setAlignment(Element.ALIGN_RIGHT);
            footer.setSpacingBefore(10);
            document.add(footer);
            
            document.close();
        } catch (DocumentException e) {
            throw new ExportException("Error creating PDF file", e);
        }
    }

    @Override
    public String getContentType() {
        return ExportFormat.PDF.getContentType();
    }

    @Override
    public String getFileExtension() {
        return ExportFormat.PDF.getExtension();
    }
    
    /**
     * Formats a value for PDF output based on its type.
     */
    private String formatValue(Object value, ExportOptions options) {
        if (value == null) {
            return "";
        }

        if (value instanceof Number) {
            if (options.isFormatNumbers() && options.getNumberPattern() != null) {
                return String.format(options.getNumberPattern(), value);
            }
            return value.toString();
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