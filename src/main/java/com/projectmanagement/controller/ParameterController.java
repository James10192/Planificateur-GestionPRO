package com.projectmanagement.controller;

import com.projectmanagement.dto.*;
import com.projectmanagement.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for parameter operations.
 * This controller provides endpoints for retrieving all parameter entities (project types, statuses, priorities, etc.).
 */
@RestController
@RequestMapping("/api/parameters")
public class ParameterController {

    private final ProjectTypeRepository projectTypeRepository;
    private final StatusRepository statusRepository;
    private final PriorityRepository priorityRepository;
    private final DirectionRepository directionRepository;
    private final PortfolioPhaseRepository portfolioPhaseRepository;
    private final TeamRoleRepository teamRoleRepository;

    @Autowired
    public ParameterController(
            ProjectTypeRepository projectTypeRepository,
            StatusRepository statusRepository,
            PriorityRepository priorityRepository,
            DirectionRepository directionRepository,
            PortfolioPhaseRepository portfolioPhaseRepository,
            TeamRoleRepository teamRoleRepository) {
        this.projectTypeRepository = projectTypeRepository;
        this.statusRepository = statusRepository;
        this.priorityRepository = priorityRepository;
        this.directionRepository = directionRepository;
        this.portfolioPhaseRepository = portfolioPhaseRepository;
        this.teamRoleRepository = teamRoleRepository;
    }

    /**
     * Get all project types.
     *
     * @return list of all project types
     */
    @GetMapping("/project-types")
    public ResponseEntity<List<ProjectTypeDTO>> getAllProjectTypes() {
        List<ProjectTypeDTO> projectTypes = projectTypeRepository.findAll().stream()
                .map(projectType -> {
                    ProjectTypeDTO dto = new ProjectTypeDTO();
                    dto.setId(projectType.getId());
                    dto.setName(projectType.getName());
                    return dto;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(projectTypes);
    }

    /**
     * Get all statuses.
     *
     * @return list of all statuses
     */
    @GetMapping("/statuses")
    public ResponseEntity<List<StatusDTO>> getAllStatuses() {
        List<StatusDTO> statuses = statusRepository.findAll().stream()
                .map(status -> {
                    StatusDTO dto = new StatusDTO();
                    dto.setId(status.getId());
                    dto.setName(status.getName());
                    return dto;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(statuses);
    }

    /**
     * Get all priorities.
     *
     * @return list of all priorities
     */
    @GetMapping("/priorities")
    public ResponseEntity<List<PriorityDTO>> getAllPriorities() {
        List<PriorityDTO> priorities = priorityRepository.findAll().stream()
                .map(priority -> {
                    PriorityDTO dto = new PriorityDTO();
                    dto.setId(priority.getId());
                    dto.setName(priority.getName());
                    return dto;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(priorities);
    }

    /**
     * Get all directions.
     *
     * @return list of all directions
     */
    @GetMapping("/directions")
    public ResponseEntity<List<DirectionDTO>> getAllDirections() {
        List<DirectionDTO> directions = directionRepository.findAll().stream()
                .map(direction -> {
                    DirectionDTO dto = new DirectionDTO();
                    dto.setId(direction.getId());
                    dto.setName(direction.getName());
                    dto.setCode(direction.getCode());
                    return dto;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(directions);
    }

    /**
     * Get all portfolio phases.
     *
     * @return list of all portfolio phases
     */
    @GetMapping("/portfolio-phases")
    public ResponseEntity<List<PortfolioPhaseDTO>> getAllPortfolioPhases() {
        List<PortfolioPhaseDTO> phases = portfolioPhaseRepository.findAll().stream()
                .map(phase -> {
                    PortfolioPhaseDTO dto = new PortfolioPhaseDTO();
                    dto.setId(phase.getId());
                    dto.setName(phase.getName());
                    dto.setPercentage(phase.getPercentage());
                    return dto;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(phases);
    }

    /**
     * Get all team roles.
     *
     * @return list of all team roles
     */
    @GetMapping("/team-roles")
    public ResponseEntity<List<TeamRoleDTO>> getAllTeamRoles() {
        List<TeamRoleDTO> roles = teamRoleRepository.findAll().stream()
                .map(role -> {
                    TeamRoleDTO dto = new TeamRoleDTO();
                    dto.setId(role.getId());
                    dto.setName(role.getName());
                    return dto;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(roles);
    }

    /**
     * Get all parameters in a single call.
     *
     * @return map containing all parameter lists
     */
    @GetMapping("/all")
    public ResponseEntity<ParametersDTO> getAllParameters() {
        ParametersDTO parametersDTO = new ParametersDTO();
        parametersDTO.setProjectTypes(getAllProjectTypes().getBody());
        parametersDTO.setStatuses(getAllStatuses().getBody());
        parametersDTO.setPriorities(getAllPriorities().getBody());
        parametersDTO.setDirections(getAllDirections().getBody());
        parametersDTO.setPortfolioPhases(getAllPortfolioPhases().getBody());
        parametersDTO.setTeamRoles(getAllTeamRoles().getBody());
        return ResponseEntity.ok(parametersDTO);
    }

    /**
     * DTO class to hold all parameter lists.
     */
    public static class ParametersDTO {
        private List<ProjectTypeDTO> projectTypes;
        private List<StatusDTO> statuses;
        private List<PriorityDTO> priorities;
        private List<DirectionDTO> directions;
        private List<PortfolioPhaseDTO> portfolioPhases;
        private List<TeamRoleDTO> teamRoles;

        public List<ProjectTypeDTO> getProjectTypes() {
            return projectTypes;
        }

        public void setProjectTypes(List<ProjectTypeDTO> projectTypes) {
            this.projectTypes = projectTypes;
        }

        public List<StatusDTO> getStatuses() {
            return statuses;
        }

        public void setStatuses(List<StatusDTO> statuses) {
            this.statuses = statuses;
        }

        public List<PriorityDTO> getPriorities() {
            return priorities;
        }

        public void setPriorities(List<PriorityDTO> priorities) {
            this.priorities = priorities;
        }

        public List<DirectionDTO> getDirections() {
            return directions;
        }

        public void setDirections(List<DirectionDTO> directions) {
            this.directions = directions;
        }

        public List<PortfolioPhaseDTO> getPortfolioPhases() {
            return portfolioPhases;
        }

        public void setPortfolioPhases(List<PortfolioPhaseDTO> portfolioPhases) {
            this.portfolioPhases = portfolioPhases;
        }

        public List<TeamRoleDTO> getTeamRoles() {
            return teamRoles;
        }

        public void setTeamRoles(List<TeamRoleDTO> teamRoles) {
            this.teamRoles = teamRoles;
        }
    }
} 