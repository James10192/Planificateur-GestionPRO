package com.projectmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

/**
 * DTO for project budgets.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ProjectBudgetDTO extends BaseDTO {
    
    private Long projectId;
    private String projectName;
    
    private BigDecimal initialBudget;
    private BigDecimal consumedBudget;
    
    private BigDecimal remainingBudget;
    private Double consumptionPercentage;
} 