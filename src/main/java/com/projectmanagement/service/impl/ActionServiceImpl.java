package com.projectmanagement.service.impl;

import com.projectmanagement.dto.ActionDTO;
import com.projectmanagement.dto.SubActionDTO;
import com.projectmanagement.entity.*;
import com.projectmanagement.repository.*;
import com.projectmanagement.service.ActionService;
import com.projectmanagement.service.BaseServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the ActionService interface.
 */
@Service
@Slf4j
public class ActionServiceImpl extends BaseServiceImpl<Action, Long, ActionRepository> implements ActionService {

    private final PlanningRepository planningRepository;
    private final StatusRepository statusRepository;
    private final UserRepository userRepository;
    private final SubActionRepository subActionRepository;
    private final ActionDependencyRepository dependencyRepository;

    @Autowired
    public ActionServiceImpl(
            ActionRepository repository,
            PlanningRepository planningRepository,
            StatusRepository statusRepository,
            UserRepository userRepository,
            SubActionRepository subActionRepository,
            ActionDependencyRepository dependencyRepository) {
        super(repository);
        this.planningRepository = planningRepository;
        this.statusRepository = statusRepository;
        this.userRepository = userRepository;
        this.subActionRepository = subActionRepository;
        this.dependencyRepository = dependencyRepository;
    }

    @Override
    public ActionDTO toDTO(Action action) {
        if (action == null) {
            return null;
        }

        ActionDTO dto = ActionDTO.builder()
                .name(action.getName())
                .startDate(action.getStartDate())
                .plannedEndDate(action.getPlannedEndDate())
                .actualEndDate(action.getActualEndDate())
                .progress(action.calculateProgress())
                .subActions(new ArrayList<>())
                .dependencies(new ArrayList<>())
                .dependentActions(new ArrayList<>())
                .build();

        // Set the base fields
        dto.setId(action.getId());
        dto.setActif(action.getActif());
        dto.setDateCreation(action.getDateCreation());
        dto.setDateModification(action.getDateModification());

        // Set the reference fields with their IDs and names
        if (action.getPlanning() != null) {
            dto.setPlanningId(action.getPlanning().getId());
            if (action.getPlanning().getProject() != null) {
                dto.setPlanningName(action.getPlanning().getProject().getName());
            } else {
                dto.setPlanningName("Planning " + action.getPlanning().getId());
            }
        }

        if (action.getStatus() != null) {
            dto.setStatusId(action.getStatus().getId());
            dto.setStatusName(action.getStatus().getName());
        }

        if (action.getResponsable() != null) {
            dto.setResponsableId(action.getResponsable().getId());
            dto.setResponsableName(action.getResponsable().getFullName());
        }

        // Convert sub-actions to DTOs
        if (action.getSubActions() != null) {
            action.getSubActions().forEach(subAction -> {
                SubActionDTO subActionDTO = SubActionDTO.builder()
                        .id(subAction.getId())
                        .name(subAction.getName() != null ? subAction.getName() : "Sub-action " + subAction.getId())
                        .description(subAction.getDescription() != null ? subAction.getDescription() : "")
                        .startDate(subAction.getStartDate())
                        .plannedEndDate(subAction.getPlannedEndDate())
                        .actualEndDate(subAction.getActualEndDate())
                        .actif(subAction.getActif())
                        .build();
                
                dto.getSubActions().add(subActionDTO);
            });
        }

        return dto;
    }

    @Override
    public Action toEntity(ActionDTO actionDTO) {
        if (actionDTO == null) {
            return null;
        }

        Action action = Action.builder()
                .name(actionDTO.getName())
                .startDate(actionDTO.getStartDate())
                .plannedEndDate(actionDTO.getPlannedEndDate())
                .actualEndDate(actionDTO.getActualEndDate())
                .progress(actionDTO.getProgress())
                .subActions(new ArrayList<>())
                .dependencies(new ArrayList<>())
                .dependentActions(new ArrayList<>())
                .build();

        // Set the ID if it exists (for updates)
        if (actionDTO.getId() != null) {
            action.setId(actionDTO.getId());
        }

        // Set the reference entities if their IDs exist
        if (actionDTO.getPlanningId() != null) {
            action.setPlanning(planningRepository.findById(actionDTO.getPlanningId())
                    .orElseThrow(() -> new EntityNotFoundException("Planning not found with id: " + actionDTO.getPlanningId())));
        }

        if (actionDTO.getStatusId() != null) {
            action.setStatus(statusRepository.findById(actionDTO.getStatusId())
                    .orElseThrow(() -> new EntityNotFoundException("Status not found with id: " + actionDTO.getStatusId())));
        }

        if (actionDTO.getResponsableId() != null) {
            action.setResponsable(userRepository.findById(actionDTO.getResponsableId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + actionDTO.getResponsableId())));
        }

        return action;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActionDTO> findByPlanningId(Long planningId) {
        log.debug("Finding actions by planning ID: {}", planningId);
        Planning planning = planningRepository.findById(planningId)
                .orElseThrow(() -> new EntityNotFoundException("Planning not found with id: " + planningId));
        
        return repository.findByPlanningAndActifTrue(planning).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActionDTO> findByStatusId(Long statusId) {
        log.debug("Finding actions by status ID: {}", statusId);
        Status status = statusRepository.findById(statusId)
                .orElseThrow(() -> new EntityNotFoundException("Status not found with id: " + statusId));
        
        return repository.findByStatus(status).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ActionDTO> findByResponsableId(Long userId, Pageable pageable) {
        log.debug("Finding actions by responsable ID: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        
        return repository.findByResponsableAndActifTrue(user, pageable)
                .map(this::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ActionDTO> findUpcomingDeadlines(Long daysThreshold, Pageable pageable) {
        log.debug("Finding actions with deadlines within the next {} days", daysThreshold);
        LocalDate currentDate = LocalDate.now();
        
        return repository.findUpcomingDeadlines(currentDate, daysThreshold, pageable)
                .map(this::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ActionDTO> findOverdueActions(Pageable pageable) {
        log.debug("Finding overdue actions");
        LocalDate currentDate = LocalDate.now();
        
        return repository.findOverdueActions(currentDate, pageable)
                .map(this::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ActionDTO> searchActions(
            String name,
            Long statusId,
            Long responsableId,
            LocalDate startDate,
            LocalDate endDate,
            Long planningId,
            Pageable pageable) {
        log.debug("Searching actions with criteria: name={}, statusId={}, responsableId={}, startDate={}, endDate={}, planningId={}",
                name, statusId, responsableId, startDate, endDate, planningId);
        
        return repository.searchActions(name, statusId, responsableId, startDate, endDate, planningId, pageable)
                .map(this::toDTO);
    }

    @Override
    @Transactional
    public ActionDTO updateProgress(Long actionId) {
        log.debug("Updating progress for action with ID: {}", actionId);
        
        Action action = repository.findById(actionId)
                .orElseThrow(() -> new EntityNotFoundException("Action not found with id: " + actionId));
        
        // The progress is calculated by the entity's calculateProgress method
        // which aggregates the progress of all sub-actions
        double progress = action.calculateProgress();
        action.setProgress(progress);
        
        Action updatedAction = repository.save(action);
        
        return toDTO(updatedAction);
    }

    @Override
    @Transactional
    public ActionDTO addDependency(Long actionId, Long dependsOnId) {
        log.debug("Adding dependency: action {} depends on action {}", actionId, dependsOnId);
        
        Action action = repository.findById(actionId)
                .orElseThrow(() -> new EntityNotFoundException("Action not found with id: " + actionId));
                
        Action dependsOn = repository.findById(dependsOnId)
                .orElseThrow(() -> new EntityNotFoundException("Action not found with id: " + dependsOnId));
        
        // Check for circular dependency
        if (actionId.equals(dependsOnId)) {
            throw new IllegalArgumentException("Action cannot depend on itself");
        }
        
        // Check if dependency already exists
        boolean dependencyExists = action.getDependencies().stream()
                .anyMatch(dep -> dep.getDependsOn().getId().equals(dependsOnId));
                
        if (dependencyExists) {
            throw new IllegalArgumentException("Dependency already exists");
        }
        
        // Add the dependency
        action.addDependency(dependsOn);
        
        // Save the action
        Action updatedAction = repository.save(action);
        
        return toDTO(updatedAction);
    }

    @Override
    @Transactional
    public ActionDTO removeDependency(Long actionId, Long dependsOnId) {
        log.debug("Removing dependency: action {} depends on action {}", actionId, dependsOnId);
        
        Action action = repository.findById(actionId)
                .orElseThrow(() -> new EntityNotFoundException("Action not found with id: " + actionId));
        
        // Find the dependency to remove
        ActionDependency dependencyToRemove = action.getDependencies().stream()
                .filter(dep -> dep.getDependsOn().getId().equals(dependsOnId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Dependency not found"));
        
        // Remove the dependency
        action.removeDependency(dependencyToRemove);
        
        // Save the action
        Action updatedAction = repository.save(action);
        
        return toDTO(updatedAction);
    }
} 