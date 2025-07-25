package com.projectmanagement.service.impl;

import com.projectmanagement.dto.ProjectDTO;
import com.projectmanagement.entity.*;
import com.projectmanagement.repository.*;
import com.projectmanagement.service.BaseServiceImpl;
import com.projectmanagement.service.ProjectService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Implementation of the ProjectService interface.
 */
@Service
@Slf4j
public class ProjectServiceImpl extends BaseServiceImpl<Project, Long, ProjectRepository> implements ProjectService {

    private final StatusRepository statusRepository;
    private final DirectionRepository directionRepository;
    private final ProjectTypeRepository typeRepository;
    private final PriorityRepository priorityRepository;
    private final ProjectTeamRepository teamRepository;

    @Autowired
    public ProjectServiceImpl(
            ProjectRepository repository,
            StatusRepository statusRepository,
            DirectionRepository directionRepository,
            ProjectTypeRepository typeRepository,
            PriorityRepository priorityRepository,
            ProjectTeamRepository teamRepository) {
        super(repository);
        this.statusRepository = statusRepository;
        this.directionRepository = directionRepository;
        this.typeRepository = typeRepository;
        this.priorityRepository = priorityRepository;
        this.teamRepository = teamRepository;
    }

    @Override
    public ProjectDTO toDTO(Project project) {
        if (project == null) {
            return null;
        }

        ProjectDTO dto = ProjectDTO.builder()
                .name(project.getName())
                .description(project.getDescription())
                .startDate(project.getStartDate())
                .plannedEndDate(project.getPlannedEndDate())
                .actualEndDate(project.getActualEndDate())
                .progress(project.calculateProgress())
                .build();

        // Set the base fields
        dto.setId(project.getId());
        dto.setActif(project.getActif());
        dto.setDateCreation(project.getDateCreation());
        dto.setDateModification(project.getDateModification());

        // Set the reference fields with their IDs and names
        if (project.getType() != null) {
            dto.setTypeId(project.getType().getId());
            dto.setTypeName(project.getType().getName());
        }

        if (project.getStatus() != null) {
            dto.setStatusId(project.getStatus().getId());
            dto.setStatusName(project.getStatus().getName());
        }

        if (project.getPriority() != null) {
            dto.setPriorityId(project.getPriority().getId());
            dto.setPriorityName(project.getPriority().getName());
        }

        if (project.getDirection() != null) {
            dto.setDirectionId(project.getDirection().getId());
            dto.setDirectionName(project.getDirection().getName());
        }

        if (project.getTeam() != null) {
            dto.setTeamId(project.getTeam().getId());
            dto.setTeamName(project.getTeam().getName());
        }

        return dto;
    }

    @Override
    public Project toEntity(ProjectDTO projectDTO) {
        if (projectDTO == null) {
            return null;
        }

        Project project = Project.builder()
                .name(projectDTO.getName())
                .description(projectDTO.getDescription())
                .startDate(projectDTO.getStartDate())
                .plannedEndDate(projectDTO.getPlannedEndDate())
                .actualEndDate(projectDTO.getActualEndDate())
                .plannings(new ArrayList<>())
                .documents(new ArrayList<>())
                .budgets(new ArrayList<>())
                .build();

        // Set the ID if it exists (for updates)
        if (projectDTO.getId() != null) {
            project.setId(projectDTO.getId());
        }

        // Set the reference entities if their IDs exist
        if (projectDTO.getTypeId() != null) {
            project.setType(typeRepository.findById(projectDTO.getTypeId())
                    .orElseThrow(() -> new EntityNotFoundException("ProjectType not found with id: " + projectDTO.getTypeId())));
        }

        if (projectDTO.getStatusId() != null) {
            project.setStatus(statusRepository.findById(projectDTO.getStatusId())
                    .orElseThrow(() -> new EntityNotFoundException("Status not found with id: " + projectDTO.getStatusId())));
        }

        if (projectDTO.getPriorityId() != null) {
            project.setPriority(priorityRepository.findById(projectDTO.getPriorityId())
                    .orElseThrow(() -> new EntityNotFoundException("Priority not found with id: " + projectDTO.getPriorityId())));
        }

        if (projectDTO.getDirectionId() != null) {
            project.setDirection(directionRepository.findById(projectDTO.getDirectionId())
                    .orElseThrow(() -> new EntityNotFoundException("Direction not found with id: " + projectDTO.getDirectionId())));
        }

        if (projectDTO.getTeamId() != null) {
            project.setTeam(teamRepository.findById(projectDTO.getTeamId())
                    .orElseThrow(() -> new EntityNotFoundException("ProjectTeam not found with id: " + projectDTO.getTeamId())));
        }

        return project;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProjectDTO> findByStatusId(Long statusId, Pageable pageable) {
        log.debug("Finding projects by status ID: {}", statusId);
        Status status = statusRepository.findById(statusId)
                .orElseThrow(() -> new EntityNotFoundException("Status not found with id: " + statusId));
        
        return repository.findByStatusAndActifTrue(status, pageable)
                .map(this::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProjectDTO> findByDirectionId(Long directionId, Pageable pageable) {
        log.debug("Finding projects by direction ID: {}", directionId);
        Direction direction = directionRepository.findById(directionId)
                .orElseThrow(() -> new EntityNotFoundException("Direction not found with id: " + directionId));
        
        return repository.findByDirectionAndActifTrue(direction, pageable)
                .map(this::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProjectDTO> findByTypeId(Long typeId, Pageable pageable) {
        log.debug("Finding projects by type ID: {}", typeId);
        ProjectType type = typeRepository.findById(typeId)
                .orElseThrow(() -> new EntityNotFoundException("ProjectType not found with id: " + typeId));
        
        return repository.findByTypeAndActifTrue(type, pageable)
                .map(this::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProjectDTO> findByPriorityId(Long priorityId, Pageable pageable) {
        log.debug("Finding projects by priority ID: {}", priorityId);
        Priority priority = priorityRepository.findById(priorityId)
                .orElseThrow(() -> new EntityNotFoundException("Priority not found with id: " + priorityId));
        
        return repository.findByPriorityAndActifTrue(priority, pageable)
                .map(this::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProjectDTO> findByTeamId(Long teamId, Pageable pageable) {
        log.debug("Finding projects by team ID: {}", teamId);
        ProjectTeam team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("ProjectTeam not found with id: " + teamId));
        
        return repository.findByTeamAndActifTrue(team, pageable)
                .map(this::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProjectDTO> searchProjects(
            String name,
            Long statusId,
            Long directionId,
            Long typeId,
            Long priorityId,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable) {
        log.debug("Searching projects with criteria: name={}, statusId={}, directionId={}, typeId={}, priorityId={}, startDate={}, endDate={}",
                name, statusId, directionId, typeId, priorityId, startDate, endDate);
        
        return repository.searchProjects(name, statusId, directionId, typeId, priorityId, startDate, endDate, pageable)
                .map(this::toDTO);
    }

    @Override
    @Transactional
    public ProjectDTO updateProgress(Long projectId) {
        log.debug("Updating progress for project with ID: {}", projectId);
        
        Project project = repository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + projectId));
        
        // The progress is calculated by the entity's calculateProgress method
        // which aggregates the progress of all actions in the project's plannings
        double progress = project.calculateProgress();
        
        // We don't need to explicitly set the progress on the entity as it's calculated
        // We just save the entity to update any other fields that might have changed
        Project updatedProject = repository.save(project);
        
        return toDTO(updatedProject);
    }
} 