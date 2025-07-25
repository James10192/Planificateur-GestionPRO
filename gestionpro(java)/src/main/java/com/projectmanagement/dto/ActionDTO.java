package com.projectmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO for actions.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ActionDTO extends BaseDTO {
    
    private Long planningId;
    private String planningName;
    
    private String name;
    
    private Long statusId;
    private String statusName;
    
    private Long responsableId;
    private String responsableName;
    
    private LocalDate startDate;
    private LocalDate plannedEndDate;
    private LocalDate actualEndDate;
    
    private Double progress;
    
    private List<SubActionDTO> subActions = new ArrayList<>();
    private List<ActionDependencyDTO> dependencies = new ArrayList<>();
    private List<ActionDependencyDTO> dependentActions = new ArrayList<>();
}