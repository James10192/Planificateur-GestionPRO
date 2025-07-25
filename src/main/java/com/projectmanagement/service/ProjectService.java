package com.projectmanagement.service;

import com.projectmanagement.dto.ProjectDTO;
import com.projectmanagement.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

/**
 * Service interface for Project management operations.
 */
public interface ProjectService extends BaseService<Project, Long> {

    /**
     * Convert a Project entity to a ProjectDTO.
     * 
     * @param project the entity to convert
     * @return the DTO representation of the entity
     */
    ProjectDTO toDTO(Project project);
    
    /**
     * Convert a ProjectDTO to a Project entity.
     * 
     * @param projectDTO the DTO to convert
     * @return the entity representation of the DTO
     */
    Project toEntity(ProjectDTO projectDTO);
    
    /**
     * Find projects by status ID.
     * 
     * @param statusId the status ID to search for
     * @param pageable the pagination information
     * @return a page of projects
     */
    Page<ProjectDTO> findByStatusId(Long statusId, Pageable pageable);
    
    /**
     * Find projects by direction ID.
     * 
     * @param directionId the direction ID to search for
     * @param pageable the pagination information
     * @return a page of projects
     */
    Page<ProjectDTO> findByDirectionId(Long directionId, Pageable pageable);
    
    /**
     * Find projects by type ID.
     * 
     * @param typeId the type ID to search for
     * @param pageable the pagination information
     * @return a page of projects
     */
    Page<ProjectDTO> findByTypeId(Long typeId, Pageable pageable);
    
    /**
     * Find projects by priority ID.
     * 
     * @param priorityId the priority ID to search for
     * @param pageable the pagination information
     * @return a page of projects
     */
    Page<ProjectDTO> findByPriorityId(Long priorityId, Pageable pageable);
    
    /**
     * Find projects by team ID.
     * 
     * @param teamId the team ID to search for
     * @param pageable the pagination information
     * @return a page of projects
     */
    Page<ProjectDTO> findByTeamId(Long teamId, Pageable pageable);
    
    /**
     * Search for projects with multiple criteria.
     * 
     * @param name the name to search for (optional)
     * @param statusId the status ID to search for (optional)
     * @param directionId the direction ID to search for (optional)
     * @param typeId the type ID to search for (optional)
     * @param priorityId the priority ID to search for (optional)
     * @param startDate the start date to search for (optional)
     * @param endDate the end date to search for (optional)
     * @param pageable the pagination information
     * @return a page of projects
     */
    Page<ProjectDTO> searchProjects(
            String name,
            Long statusId,
            Long directionId,
            Long typeId,
            Long priorityId,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable);
    
    /**
     * Calculate and update the progress of a project.
     * 
     * @param projectId the ID of the project to update
     * @return the updated project DTO
     */
    ProjectDTO updateProgress(Long projectId);
} 