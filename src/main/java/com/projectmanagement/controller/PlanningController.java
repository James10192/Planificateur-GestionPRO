package com.projectmanagement.controller;

import com.projectmanagement.dto.ActionDTO;
import com.projectmanagement.dto.PlanningDTO;
import com.projectmanagement.entity.Planning;
import com.projectmanagement.export.ExportFormat;
import com.projectmanagement.service.PlanningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for planning operations.
 */
@RestController
@RequestMapping("/api/plannings")
public class PlanningController {

    private final PlanningService planningService;

    @Autowired
    public PlanningController(PlanningService planningService) {
        this.planningService = planningService;
    }

    /**
     * Get all plannings.
     *
     * @return list of all plannings
     */
    @GetMapping
    public ResponseEntity<List<PlanningDTO>> getAll() {
        List<PlanningDTO> plannings = planningService.findAll().stream()
                .map(planningService::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(plannings);
    }

    /**
     * Get a planning by ID.
     *
     * @param id the planning ID
     * @return the planning
     */
    @GetMapping("/{id}")
    public ResponseEntity<PlanningDTO> getById(@PathVariable Long id) {
        return planningService.findById(id)
                .map(planningService::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Create a new planning.
     *
     * @param planningDTO the planning to create
     * @return the created planning
     */
    @PostMapping
    public ResponseEntity<PlanningDTO> create(@RequestBody PlanningDTO planningDTO) {
        Planning planning = planningService.toEntity(planningDTO);
        planning = planningService.save(planning);
        return ResponseEntity.ok(planningService.toDTO(planning));
    }

    /**
     * Update a planning.
     *
     * @param id the planning ID
     * @param planningDTO the updated planning
     * @return the updated planning
     */
    @PutMapping("/{id}")
    public ResponseEntity<PlanningDTO> update(@PathVariable Long id, @RequestBody PlanningDTO planningDTO) {
        planningDTO.setId(id);
        Planning planning = planningService.toEntity(planningDTO);
        planning = planningService.update(id, planning);
        return ResponseEntity.ok(planningService.toDTO(planning));
    }

    /**
     * Delete a planning.
     *
     * @param id the planning ID
     * @return no content response
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        planningService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Find plannings by project.
     *
     * @param projectId the project ID
     * @return list of plannings for the given project
     */
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<PlanningDTO>> findByProject(@PathVariable Long projectId) {
        return ResponseEntity.ok(planningService.findByProjectId(projectId));
    }

    /**
     * Find plannings by phase.
     *
     * @param phaseId the phase ID
     * @return list of plannings for the given phase
     */
    @GetMapping("/phase/{phaseId}")
    public ResponseEntity<List<PlanningDTO>> findByPhase(@PathVariable Long phaseId) {
        return ResponseEntity.ok(planningService.findByPhaseId(phaseId));
    }

    /**
     * Find plannings by project and phase.
     *
     * @param projectId the project ID
     * @param phaseId the phase ID
     * @return list of plannings for the given project and phase
     */
    @GetMapping("/project/{projectId}/phase/{phaseId}")
    public ResponseEntity<PlanningDTO> findByProjectAndPhase(
            @PathVariable Long projectId,
            @PathVariable Long phaseId) {
        return planningService.findByProjectIdAndPhaseId(projectId, phaseId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get all actions for a planning.
     *
     * @param id the planning ID
     * @return list of actions for the planning
     */
    @GetMapping("/{id}/actions")
    public ResponseEntity<List<ActionDTO>> getPlanningActions(@PathVariable Long id) {
        // This is a mock implementation since the method is not defined in PlanningService
        return ResponseEntity.ok(List.of());
    }

    /**
     * Get overdue actions for a planning.
     *
     * @param id the planning ID
     * @return list of overdue actions for the planning
     */
    @GetMapping("/{id}/actions/overdue")
    public ResponseEntity<List<ActionDTO>> getOverdueActions(@PathVariable Long id) {
        // This is a mock implementation since the method is not defined in PlanningService
        return ResponseEntity.ok(List.of());
    }

    /**
     * Get upcoming actions for a planning.
     *
     * @param id the planning ID
     * @param days number of days to look ahead
     * @return list of upcoming actions for the planning
     */
    @GetMapping("/{id}/actions/upcoming/{days}")
    public ResponseEntity<List<ActionDTO>> getUpcomingActions(
            @PathVariable Long id,
            @PathVariable int days) {
        // This is a mock implementation since the method is not defined in PlanningService
        return ResponseEntity.ok(List.of());
    }

    /**
     * Recalculate the progress of a planning.
     *
     * @param id the planning ID
     * @return the updated planning with recalculated progress
     */
    @PostMapping("/{id}/recalculate-progress")
    public ResponseEntity<PlanningDTO> recalculateProgress(@PathVariable Long id) {
        return ResponseEntity.ok(planningService.updateProgress(id));
    }

    /**
     * Export planning data.
     *
     * @param id the planning ID
     * @param format the export format (excel, pdf, csv)
     * @return the exported file
     */
    @GetMapping("/{id}/export")
    public ResponseEntity<byte[]> exportPlanning(
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
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=planning_" + id + "." + extension)
                .body(reportContent);
    }
} 