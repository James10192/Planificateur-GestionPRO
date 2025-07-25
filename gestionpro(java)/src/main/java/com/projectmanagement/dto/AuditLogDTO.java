package com.projectmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * DTO for audit logs.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class AuditLogDTO extends BaseDTO {
    
    private String tableName;
    private String recordId;
    private String operationType;
    
    private Long modifiedById;
    private String modifiedByName;
    
    private LocalDateTime modificationDate;
    private String details;
}