package com.projectmanagement.export;

import com.projectmanagement.export.impl.CsvExportService;
import com.projectmanagement.export.impl.ExcelExportService;
import com.projectmanagement.export.impl.PdfExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Factory class for obtaining the appropriate export service based on format.
 */
@Component
public class ExportServiceFactory {
    
    private final ExcelExportService excelExportService;
    private final PdfExportService pdfExportService;
    private final CsvExportService csvExportService;
    
    @Autowired
    public ExportServiceFactory(ExcelExportService excelExportService,
                               PdfExportService pdfExportService,
                               CsvExportService csvExportService) {
        this.excelExportService = excelExportService;
        this.pdfExportService = pdfExportService;
        this.csvExportService = csvExportService;
    }
    
    /**
     * Get the export service for the specified format.
     *
     * @param format the export format
     * @return the appropriate export service
     * @throws IllegalArgumentException if the format is not supported
     */
    public ExportService getExportService(ExportFormat format) {
        switch (format) {
            case EXCEL:
                return excelExportService;
            case PDF:
                return pdfExportService;
            case CSV:
                return csvExportService;
            default:
                throw new IllegalArgumentException("Unsupported export format: " + format);
        }
    }
} 