package com.projectmanagement.export;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * Interface defining common export operations for different formats.
 */
public interface ExportService {

    /**
     * Exports data to a specific format.
     *
     * @param data          The data to export (list of objects)
     * @param headers       Map of header names and their display names
     * @param outputStream  The output stream to write the exported data to
     * @param options       Additional export options
     * @param <T>           The type of data to export
     * @throws ExportException if an error occurs during export
     */
    <T> void export(List<T> data, Map<String, String> headers, OutputStream outputStream, 
                   ExportOptions options) throws ExportException;
    
    /**
     * Get the content type of the export format.
     *
     * @return The MIME type of the export format
     */
    String getContentType();
    
    /**
     * Get the file extension for the export format.
     *
     * @return The file extension (without the dot)
     */
    String getFileExtension();
} 