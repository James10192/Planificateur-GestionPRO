package com.projectmanagement.export;

/**
 * Enumeration of supported export formats.
 */
public enum ExportFormat {
    
    EXCEL("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "xlsx", "Excel"),
    PDF("application/pdf", "pdf", "PDF"),
    CSV("text/csv", "csv", "CSV");
    
    private final String contentType;
    private final String extension;
    private final String displayName;
    
    ExportFormat(String contentType, String extension, String displayName) {
        this.contentType = contentType;
        this.extension = extension;
        this.displayName = displayName;
    }
    
    /**
     * Get the MIME content type for this format.
     * 
     * @return the content type
     */
    public String getContentType() {
        return contentType;
    }
    
    /**
     * Get the file extension for this format (without the dot).
     * 
     * @return the file extension
     */
    public String getExtension() {
        return extension;
    }
    
    /**
     * Get the display name for this format.
     * 
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }
} 