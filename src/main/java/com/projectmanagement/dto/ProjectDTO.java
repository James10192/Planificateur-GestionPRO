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
 * DTO for projects.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ProjectDTO extends BaseDTO {
    
    private String name;
    private String description;
    
    private Long typeId;
    private String typeName;
    
    private Long statusId;
    private String statusName;
    
    private Long priorityId;
    private String priorityName;
    
    private Long directionId;
    private String directionName;
    
    private Long teamId;
    private String teamName;
    
    private LocalDate startDate;
    private LocalDate plannedEndDate;
    private LocalDate actualEndDate;
    
    private Double progress;
    
    private List<PlanningDTO> plannings = new ArrayList<>();
    private List<DocumentDTO> documents = new ArrayList<>();
    private List<ProjectBudgetDTO> budgets = new ArrayList<>();
} 