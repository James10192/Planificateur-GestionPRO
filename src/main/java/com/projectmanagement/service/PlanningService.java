package com.projectmanagement.service;

import com.projectmanagement.dto.PlanningDTO;
import com.projectmanagement.entity.Planning;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for Planning management operations.
 */
public interface PlanningService extends BaseService<Planning, Long> {

    /**
     * Convert a Planning entity to a PlanningDTO.
     * 
     * @param planning the entity to convert
     * @return the DTO representation of the entity
     */
    PlanningDTO toDTO(Planning planning);
    
    /**
     * Convert a PlanningDTO to a Planning entity.
     * 
     * @param planningDTO the DTO to convert
     * @return the entity representation of the DTO
     */
    Planning toEntity(PlanningDTO planningDTO);
    
    /**
     * Find all plannings by project ID.
     * 
     * @param projectId the project ID to search for
     * @return a list of planning DTOs
     */
    List<PlanningDTO> findByProjectId(Long projectId);
    
    /**
     * Find all active plannings by project ID.
     * 
     * @param projectId the project ID to search for
     * @return a list of planning DTOs
     */
    List<PlanningDTO> findActiveByProjectId(Long projectId);
    
    /**
     * Find all plannings by phase ID.
     * 
     * @param phaseId the phase ID to search for
     * @return a list of planning DTOs
     */
    List<PlanningDTO> findByPhaseId(Long phaseId);
    
    /**
     * Find a planning by project ID and phase ID.
     * 
     * @param projectId the project ID to search for
     * @param phaseId the phase ID to search for
     * @return the planning DTO if found, empty otherwise
     */
    Optional<PlanningDTO> findByProjectIdAndPhaseId(Long projectId, Long phaseId);
    
    /**
     * Calculate and update the progress of a planning.
     * 
     * @param planningId the ID of the planning to update
     * @return the updated planning DTO
     */
    PlanningDTO updateProgress(Long planningId);
    
    /**
     * Add an action to a planning.
     * 
     * @param planningId the ID of the planning
     * @param actionId the ID of the action to add
     * @return the updated planning DTO
     */
    PlanningDTO addAction(Long planningId, Long actionId);
    
    /**
     * Remove an action from a planning.
     * 
     * @param planningId the ID of the planning
     * @param actionId the ID of the action to remove
     * @return the updated planning DTO
     */
    PlanningDTO removeAction(Long planningId, Long actionId);
} 