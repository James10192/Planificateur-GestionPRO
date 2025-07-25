package com.projectmanagement.service;

import com.projectmanagement.dto.ActionDTO;
import com.projectmanagement.entity.Action;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

/**
 * Service interface for Action management operations.
 */
public interface ActionService extends BaseService<Action, Long> {

    /**
     * Convert an Action entity to an ActionDTO.
     * 
     * @param action the entity to convert
     * @return the DTO representation of the entity
     */
    ActionDTO toDTO(Action action);
    
    /**
     * Convert an ActionDTO to an Action entity.
     * 
     * @param actionDTO the DTO to convert
     * @return the entity representation of the DTO
     */
    Action toEntity(ActionDTO actionDTO);
    
    /**
     * Find actions by planning ID.
     * 
     * @param planningId the planning ID to search for
     * @return a list of action DTOs
     */
    List<ActionDTO> findByPlanningId(Long planningId);
    
    /**
     * Find actions by status ID.
     * 
     * @param statusId the status ID to search for
     * @return a list of action DTOs
     */
    List<ActionDTO> findByStatusId(Long statusId);
    
    /**
     * Find actions assigned to a user.
     * 
     * @param userId the user ID to search for
     * @param pageable the pagination information
     * @return a page of action DTOs
     */
    Page<ActionDTO> findByResponsableId(Long userId, Pageable pageable);
    
    /**
     * Find all active actions with a deadline approaching within X days.
     * 
     * @param daysThreshold the number of days to consider as approaching
     * @param pageable the pagination information
     * @return a page of action DTOs
     */
    Page<ActionDTO> findUpcomingDeadlines(Long daysThreshold, Pageable pageable);
    
    /**
     * Find all active overdue actions.
     * 
     * @param pageable the pagination information
     * @return a page of action DTOs
     */
    Page<ActionDTO> findOverdueActions(Pageable pageable);
    
    /**
     * Search actions with multiple criteria.
     * 
     * @param name the name to search for (optional)
     * @param statusId the status ID to search for (optional)
     * @param responsableId the user ID to search for (optional)
     * @param startDate the start date to search for (optional)
     * @param endDate the end date to search for (optional)
     * @param planningId the planning ID to search for (optional)
     * @param pageable the pagination information
     * @return a page of action DTOs
     */
    Page<ActionDTO> searchActions(
            String name,
            Long statusId,
            Long responsableId,
            LocalDate startDate,
            LocalDate endDate,
            Long planningId,
            Pageable pageable);
    
    /**
     * Calculate and update the progress of an action.
     * 
     * @param actionId the ID of the action to update
     * @return the updated action DTO
     */
    ActionDTO updateProgress(Long actionId);
    
    /**
     * Add a dependency between two actions.
     * 
     * @param actionId the ID of the action
     * @param dependsOnId the ID of the action it depends on
     * @return the updated action DTO
     */
    ActionDTO addDependency(Long actionId, Long dependsOnId);
    
    /**
     * Remove a dependency between two actions.
     * 
     * @param actionId the ID of the action
     * @param dependsOnId the ID of the action it depends on
     * @return the updated action DTO
     */
    ActionDTO removeDependency(Long actionId, Long dependsOnId);
} 