package com.projectmanagement.service.impl;

import com.projectmanagement.dto.ActionDTO;
import com.projectmanagement.dto.PlanningDTO;
import com.projectmanagement.entity.Action;
import com.projectmanagement.entity.Planning;
import com.projectmanagement.entity.PortfolioPhase;
import com.projectmanagement.entity.Project;
import com.projectmanagement.repository.ActionRepository;
import com.projectmanagement.repository.PlanningRepository;
import com.projectmanagement.repository.PortfolioPhaseRepository;
import com.projectmanagement.repository.ProjectRepository;
import com.projectmanagement.service.ActionService;
import com.projectmanagement.service.BaseServiceImpl;
import com.projectmanagement.service.PlanningService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of the PlanningService interface.
 */
@Service
@Slf4j
public class PlanningServiceImpl extends BaseServiceImpl<Planning, Long, PlanningRepository> implements PlanningService {

    private final ProjectRepository projectRepository;
    private final PortfolioPhaseRepository phaseRepository;
    private final ActionRepository actionRepository;
    private final ActionService actionService;

    @Autowired
    public PlanningServiceImpl(
            PlanningRepository repository,
            ProjectRepository projectRepository,
            PortfolioPhaseRepository phaseRepository,
            ActionRepository actionRepository,
            ActionService actionService) {
        super(repository);
        this.projectRepository = projectRepository;
        this.phaseRepository = phaseRepository;
        this.actionRepository = actionRepository;
        this.actionService = actionService;
    }

    @Override
    public PlanningDTO toDTO(Planning planning) {
        if (planning == null) {
            return null;
        }

        PlanningDTO dto = PlanningDTO.builder()
                .progress(planning.calculateProgress())
                .actions(new ArrayList<>())
                .build();

        // Set the base fields
        dto.setId(planning.getId());
        dto.setActif(planning.getActif());
        dto.setDateCreation(planning.getDateCreation());
        dto.setDateModification(planning.getDateModification());

        // Set the reference fields with their IDs and names
        if (planning.getProject() != null) {
            dto.setProjectId(planning.getProject().getId());
            dto.setProjectName(planning.getProject().getName());
        }

        if (planning.getPhase() != null) {
            dto.setPhaseId(planning.getPhase().getId());
            dto.setPhaseName(planning.getPhase().getName());
        }

        // Convert actions to DTOs (only include basic info to avoid circular references)
        if (planning.getActions() != null) {
            planning.getActions().forEach(action -> {
                ActionDTO actionDTO = actionService.toDTO(action);
                dto.getActions().add(actionDTO);
            });
        }

        return dto;
    }

    @Override
    public Planning toEntity(PlanningDTO planningDTO) {
        if (planningDTO == null) {
            return null;
        }

        Planning planning = Planning.builder()
                .actions(new ArrayList<>())
                .build();

        // Set the ID if it exists (for updates)
        if (planningDTO.getId() != null) {
            planning.setId(planningDTO.getId());
        }

        // Set the reference entities if their IDs exist
        if (planningDTO.getProjectId() != null) {
            planning.setProject(projectRepository.findById(planningDTO.getProjectId())
                    .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + planningDTO.getProjectId())));
        }

        if (planningDTO.getPhaseId() != null) {
            planning.setPhase(phaseRepository.findById(planningDTO.getPhaseId())
                    .orElseThrow(() -> new EntityNotFoundException("PortfolioPhase not found with id: " + planningDTO.getPhaseId())));
        }

        return planning;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlanningDTO> findByProjectId(Long projectId) {
        log.debug("Finding plannings by project ID: {}", projectId);
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + projectId));
        
        return repository.findByProject(project).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlanningDTO> findActiveByProjectId(Long projectId) {
        log.debug("Finding active plannings by project ID: {}", projectId);
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + projectId));
        
        return repository.findByProjectAndActifTrue(project).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlanningDTO> findByPhaseId(Long phaseId) {
        log.debug("Finding plannings by phase ID: {}", phaseId);
        PortfolioPhase phase = phaseRepository.findById(phaseId)
                .orElseThrow(() -> new EntityNotFoundException("PortfolioPhase not found with id: " + phaseId));
        
        return repository.findByPhase(phase).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PlanningDTO> findByProjectIdAndPhaseId(Long projectId, Long phaseId) {
        log.debug("Finding planning by project ID: {} and phase ID: {}", projectId, phaseId);
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + projectId));
        
        PortfolioPhase phase = phaseRepository.findById(phaseId)
                .orElseThrow(() -> new EntityNotFoundException("PortfolioPhase not found with id: " + phaseId));
        
        return repository.findByProjectAndPhase(project, phase)
                .map(this::toDTO);
    }

    @Override
    @Transactional
    public PlanningDTO updateProgress(Long planningId) {
        log.debug("Updating progress for planning with ID: {}", planningId);
        
        Planning planning = repository.findById(planningId)
                .orElseThrow(() -> new EntityNotFoundException("Planning not found with id: " + planningId));
        
        // Update the progress of all actions in the planning
        planning.getActions().forEach(action -> {
            actionService.updateProgress(action.getId());
        });
        
        // The progress is calculated by the entity's calculateProgress method
        // which aggregates the progress of all actions
        Planning updatedPlanning = repository.save(planning);
        
        return toDTO(updatedPlanning);
    }

    @Override
    @Transactional
    public PlanningDTO addAction(Long planningId, Long actionId) {
        log.debug("Adding action with ID: {} to planning with ID: {}", actionId, planningId);
        
        Planning planning = repository.findById(planningId)
                .orElseThrow(() -> new EntityNotFoundException("Planning not found with id: " + planningId));
        
        Action action = actionRepository.findById(actionId)
                .orElseThrow(() -> new EntityNotFoundException("Action not found with id: " + actionId));
        
        // Check if action is already in this planning
        if (action.getPlanning() != null && action.getPlanning().getId().equals(planningId)) {
            throw new IllegalArgumentException("Action is already in this planning");
        }
        
        // If action is already in another planning, remove it from there
        if (action.getPlanning() != null) {
            action.getPlanning().removeAction(action);
        }
        
        // Add the action to the planning
        planning.addAction(action);
        
        // Save the planning
        Planning updatedPlanning = repository.save(planning);
        
        return toDTO(updatedPlanning);
    }

    @Override
    @Transactional
    public PlanningDTO removeAction(Long planningId, Long actionId) {
        log.debug("Removing action with ID: {} from planning with ID: {}", actionId, planningId);
        
        Planning planning = repository.findById(planningId)
                .orElseThrow(() -> new EntityNotFoundException("Planning not found with id: " + planningId));
        
        Action action = actionRepository.findById(actionId)
                .orElseThrow(() -> new EntityNotFoundException("Action not found with id: " + actionId));
        
        // Check if action is in this planning
        if (action.getPlanning() == null || !action.getPlanning().getId().equals(planningId)) {
            throw new IllegalArgumentException("Action is not in this planning");
        }
        
        // Remove the action from the planning
        planning.removeAction(action);
        
        // Save the planning
        Planning updatedPlanning = repository.save(planning);
        
        return toDTO(updatedPlanning);
    }
} 