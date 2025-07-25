package com.projectmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * Base DTO class for common fields.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class BaseDTO {
    
    private Long id;
    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private Boolean actif;
} 