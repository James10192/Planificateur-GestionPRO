package com.projectmanagement.export;

import lombok.Builder;
import lombok.Data;

/**
 * Configuration options for export operations.
 */
@Data
@Builder
public class ExportOptions {
    
    /**
     * Title for the export document
     */
    private String title;
    
    /**
     * Author of the export document
     */
    private String author;
    
    /**
     * Description of the export document
     */
    private String description;
    
    /**
     * Whether to include headers in the export
     */
    @Builder.Default
    private boolean includeHeaders = true;
    
    /**
     * Whether to format dates in a localized manner
     */
    @Builder.Default
    private boolean formatDates = true;
    
    /**
     * Whether to format numbers in a localized manner
     */
    @Builder.Default
    private boolean formatNumbers = true;
    
    /**
     * Password to protect the document (if supported by the format)
     */
    private String password;
    
    /**
     * Number format pattern (e.g., "#,##0.00")
     */
    private String numberPattern;
    
    /**
     * Date format pattern (e.g., "yyyy-MM-dd")
     */
    private String datePattern;
} 