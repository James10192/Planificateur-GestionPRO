package com.projectmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO for plannings.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class PlanningDTO extends BaseDTO {
    
    private Long projectId;
    private String projectName;
    
    private Long phaseId;
    private String phaseName;
    private Double phasePercentage;
    
    private Double progress;
    
    private List<ActionDTO> actions = new ArrayList<>();
} 