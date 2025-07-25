package com.projectmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * DTO for action dependencies.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ActionDependencyDTO extends BaseDTO {
    
    private Long actionId;
    private String actionName;
    
    private Long dependsOnId;
    private String dependsOnName;
    
    private Boolean fulfilled;
}