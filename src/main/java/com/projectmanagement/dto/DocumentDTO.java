package com.projectmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * DTO for documents.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class DocumentDTO extends BaseDTO {
    
    private Long projectId;
    private String projectName;
    
    private String title;
    private String version;
    
    private Long statusId;
    private String statusName;
    
    private String path;
    
    private Long uploadedById;
    private String uploadedByName;
    
    private LocalDateTime uploadDate;
} 