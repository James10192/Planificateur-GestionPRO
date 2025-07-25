package com.projectmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * DTO for KPI metrics.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class KpiMetricDTO extends BaseDTO {
    
    private String code;
    private String name;
    private String description;
    private String unit;
    private Double thresholdWarning;
    private Double thresholdCritical;
    private Boolean higherIsBetter;
    private String calculationFormula;
    private Integer updateFrequencyMinutes;
    private Boolean enableNotifications;
} 