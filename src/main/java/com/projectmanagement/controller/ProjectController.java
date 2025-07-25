package com.projectmanagement.controller;

import com.projectmanagement.dto.ProjectDTO;
import com.projectmanagement.entity.Project;
import com.projectmanagement.export.ExportFormat;
import com.projectmanagement.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for project operations.
 */
@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    /**
     * Get all projects.
     *
     * @return list of all projects
     */
    @GetMapping
    public ResponseEntity<List<ProjectDTO>> getAll() {
        List<ProjectDTO> projects = projectService.findAll().stream()
                .map(projectService::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(projects);
    }

    /**
     * Get a project by ID.
     *
     * @param id the project ID
     * @return the project
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getById(@PathVariable Long id) {
        return projectService.findById(id)
                .map(projectService::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Create a new project.
     *
     * @param projectDTO the project to create
     * @return the created project
     */
    @PostMapping
    public ResponseEntity<ProjectDTO> create(@RequestBody ProjectDTO projectDTO) {
        Project project = projectService.toEntity(projectDTO);
        project = projectService.save(project);
        return ResponseEntity.ok(projectService.toDTO(project));
    }

    /**
     * Update a project.
     *
     * @param id the project ID
     * @param projectDTO the updated project
     * @return the updated project
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProjectDTO> update(@PathVariable Long id, @RequestBody ProjectDTO projectDTO) {
        projectDTO.setId(id);
        Project project = projectService.toEntity(projectDTO);
        project = projectService.update(id, project);
        return ResponseEntity.ok(projectService.toDTO(project));
    }

    /**
     * Delete a project.
     *
     * @param id the project ID
     * @return no content response
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        projectService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Find projects by status.
     *
     * @param statusId the status ID
     * @return list of projects with the given status
     */
    @GetMapping("/status/{statusId}")
    public ResponseEntity<List<ProjectDTO>> findByStatus(@PathVariable Long statusId) {
        Pageable pageable = PageRequest.of(0, 100, Sort.by("name"));
        Page<ProjectDTO> page = projectService.findByStatusId(statusId, pageable);
        return ResponseEntity.ok(page.getContent());
    }

    /**
     * Find projects by priority.
     *
     * @param priorityId the priority ID
     * @return list of projects with the given priority
     */
    @GetMapping("/priority/{priorityId}")
    public ResponseEntity<List<ProjectDTO>> findByPriority(@PathVariable Long priorityId) {
        Pageable pageable = PageRequest.of(0, 100, Sort.by("name"));
        Page<ProjectDTO> page = projectService.findByPriorityId(priorityId, pageable);
        return ResponseEntity.ok(page.getContent());
    }

    /**
     * Find projects by type.
     *
     * @param typeId the project type ID
     * @return list of projects with the given type
     */
    @GetMapping("/type/{typeId}")
    public ResponseEntity<List<ProjectDTO>> findByType(@PathVariable Long typeId) {
        Pageable pageable = PageRequest.of(0, 100, Sort.by("name"));
        Page<ProjectDTO> page = projectService.findByTypeId(typeId, pageable);
        return ResponseEntity.ok(page.getContent());
    }

    /**
     * Find projects by direction.
     *
     * @param directionId the direction ID
     * @return list of projects for the given direction
     */
    @GetMapping("/direction/{directionId}")
    public ResponseEntity<List<ProjectDTO>> findByDirection(@PathVariable Long directionId) {
        Pageable pageable = PageRequest.of(0, 100, Sort.by("name"));
        Page<ProjectDTO> page = projectService.findByDirectionId(directionId, pageable);
        return ResponseEntity.ok(page.getContent());
    }

    /**
     * Find projects by team.
     *
     * @param teamId the team ID
     * @return list of projects assigned to the given team
     */
    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<ProjectDTO>> findByTeam(@PathVariable Long teamId) {
        Pageable pageable = PageRequest.of(0, 100, Sort.by("name"));
        Page<ProjectDTO> page = projectService.findByTeamId(teamId, pageable);
        return ResponseEntity.ok(page.getContent());
    }

    /**
     * Find projects with start date in a given range.
     *
     * @param startDate the start of the date range
     * @param endDate the end of the date range
     * @return list of projects starting in the given date range
     */
    @GetMapping("/start-date-range")
    public ResponseEntity<List<ProjectDTO>> findByStartDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Pageable pageable = PageRequest.of(0, 100, Sort.by("startDate"));
        Page<ProjectDTO> page = projectService.searchProjects(null, null, null, null, null, startDate, null, pageable);
        return ResponseEntity.ok(page.getContent());
    }

    /**
     * Find projects with end date in a given range.
     *
     * @param startDate the start of the date range
     * @param endDate the end of the date range
     * @return list of projects ending in the given date range
     */
    @GetMapping("/end-date-range")
    public ResponseEntity<List<ProjectDTO>> findByEndDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Pageable pageable = PageRequest.of(0, 100, Sort.by("endDate"));
        Page<ProjectDTO> page = projectService.searchProjects(null, null, null, null, null, null, endDate, pageable);
        return ResponseEntity.ok(page.getContent());
    }

    /**
     * Find projects that will end in the specified number of days.
     *
     * @param days number of days
     * @return list of projects ending in the specified number of days
     */
    @GetMapping("/ending-in-days/{days}")
    public ResponseEntity<List<ProjectDTO>> findProjectsEndingInDays(@PathVariable int days) {
        LocalDate endDate = LocalDate.now().plusDays(days);
        Pageable pageable = PageRequest.of(0, 100, Sort.by("endDate"));
        Page<ProjectDTO> page = projectService.searchProjects(null, null, null, null, null, null, endDate, pageable);
        return ResponseEntity.ok(page.getContent());
    }

    /**
     * Export project data.
     *
     * @param id the project ID
     * @param format the export format (excel, pdf, csv)
     * @return the exported file
     */
    @GetMapping("/{id}/export")
    public ResponseEntity<byte[]> exportProject(
            @PathVariable Long id,
            @RequestParam(defaultValue = "excel") String format) {
        
        // This is a mock implementation since we don't have actual export functionality
        byte[] reportContent = new byte[0]; // Service method should be implemented
        
        String contentType;
        String extension;
        
        try {
            ExportFormat exportFormat = ExportFormat.valueOf(format.toUpperCase());
            contentType = exportFormat.getContentType();
            extension = exportFormat.getExtension();
        } catch (IllegalArgumentException e) {
            // Default to Excel if format is invalid
            contentType = ExportFormat.EXCEL.getContentType();
            extension = ExportFormat.EXCEL.getExtension();
        }
        
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=project_" + id + "." + extension)
                .body(reportContent);
    }
} 