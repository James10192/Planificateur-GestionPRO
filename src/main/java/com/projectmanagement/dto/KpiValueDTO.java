package com.projectmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for KPI values.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class KpiValueDTO extends BaseDTO {
    
    private Long metricId;
    private String metricCode;
    private String metricName;
    private Long projectId;
    private String projectName;
    private Double value;
    private LocalDateTime measurementDate;
    private String comment;
    private Boolean warningThresholdBreached;
    private Boolean criticalThresholdBreached;
    private Boolean notificationSent;
} 