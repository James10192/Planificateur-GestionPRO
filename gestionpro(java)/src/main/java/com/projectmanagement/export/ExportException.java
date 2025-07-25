package com.projectmanagement.export;

/**
 * Exception thrown when an error occurs during export operations.
 */
public class ExportException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new export exception with the specified detail message.
     *
     * @param message the detail message
     */
    public ExportException(String message) {
        super(message);
    }

    /**
     * Constructs a new export exception with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause   the cause of the exception
     */
    public ExportException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new export exception with the specified cause.
     *
     * @param cause the cause of the exception
     */
    public ExportException(Throwable cause) {
        super(cause);
    }
} 