package com.projectmanagement.controller;

import com.projectmanagement.dto.ActionDTO;
import com.projectmanagement.entity.Action;
import com.projectmanagement.export.ExportFormat;
import com.projectmanagement.service.ActionService;
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
 * REST controller for action operations.
 */
@RestController
@RequestMapping("/api/actions")
public class ActionController {

    private final ActionService actionService;

    @Autowired
    public ActionController(ActionService actionService) {
        this.actionService = actionService;
    }

    /**
     * Get all actions.
     *
     * @return list of all actions
     */
    @GetMapping
    public ResponseEntity<List<ActionDTO>> getAll() {
        List<ActionDTO> actions = actionService.findAll().stream()
                .map(actionService::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(actions);
    }

    /**
     * Get an action by ID.
     *
     * @param id the action ID
     * @return the action
     */
    @GetMapping("/{id}")
    public ResponseEntity<ActionDTO> getById(@PathVariable Long id) {
        return actionService.findById(id)
                .map(actionService::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Create a new action.
     *
     * @param actionDTO the action to create
     * @return the created action
     */
    @PostMapping
    public ResponseEntity<ActionDTO> create(@RequestBody ActionDTO actionDTO) {
        Action action = actionService.toEntity(actionDTO);
        action = actionService.save(action);
        return ResponseEntity.ok(actionService.toDTO(action));
    }

    /**
     * Update an action.
     *
     * @param id the action ID
     * @param actionDTO the updated action
     * @return the updated action
     */
    @PutMapping("/{id}")
    public ResponseEntity<ActionDTO> update(@PathVariable Long id, @RequestBody ActionDTO actionDTO) {
        actionDTO.setId(id);
        Action action = actionService.toEntity(actionDTO);
        action = actionService.update(id, action);
        return ResponseEntity.ok(actionService.toDTO(action));
    }

    /**
     * Delete an action.
     *
     * @param id the action ID
     * @return no content response
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        actionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Find actions by planning.
     *
     * @param planningId the planning ID
     * @return list of actions for the given planning
     */
    @GetMapping("/planning/{planningId}")
    public ResponseEntity<List<ActionDTO>> findByPlanning(@PathVariable Long planningId) {
        return ResponseEntity.ok(actionService.findByPlanningId(planningId));
    }

    /**
     * Find actions by status.
     *
     * @param statusId the status ID
     * @return list of actions with the given status
     */
    @GetMapping("/status/{statusId}")
    public ResponseEntity<List<ActionDTO>> findByStatus(@PathVariable Long statusId) {
        return ResponseEntity.ok(actionService.findByStatusId(statusId));
    }

    /**
     * Find actions by user.
     *
     * @param userId the user ID
     * @return list of actions assigned to the given user
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ActionDTO>> findByUser(@PathVariable Long userId) {
        Pageable pageable = PageRequest.of(0, 100, Sort.by("endDate"));
        Page<ActionDTO> page = actionService.findByResponsableId(userId, pageable);
        return ResponseEntity.ok(page.getContent());
    }

    /**
     * Find upcoming actions due in the specified number of days.
     *
     * @param days number of days
     * @return list of upcoming actions
     */
    @GetMapping("/upcoming/{days}")
    public ResponseEntity<List<ActionDTO>> findUpcomingActions(@PathVariable int days) {
        Pageable pageable = PageRequest.of(0, 100, Sort.by("endDate"));
        Page<ActionDTO> page = actionService.findUpcomingDeadlines((long) days, pageable);
        return ResponseEntity.ok(page.getContent());
    }

    /**
     * Find overdue actions.
     *
     * @return list of overdue actions
     */
    @GetMapping("/overdue")
    public ResponseEntity<List<ActionDTO>> findOverdueActions() {
        Pageable pageable = PageRequest.of(0, 100, Sort.by("endDate"));
        Page<ActionDTO> page = actionService.findOverdueActions(pageable);
        return ResponseEntity.ok(page.getContent());
    }

    /**
     * Find actions with start date in a given range.
     *
     * @param startDate the start of the date range
     * @param endDate the end of the date range
     * @return list of actions starting in the given date range
     */
    @GetMapping("/start-date-range")
    public ResponseEntity<List<ActionDTO>> findByStartDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Pageable pageable = PageRequest.of(0, 100, Sort.by("startDate"));
        Page<ActionDTO> page = actionService.searchActions(null, null, null, startDate, null, null, pageable);
        return ResponseEntity.ok(page.getContent());
    }

    /**
     * Find actions with end date in a given range.
     *
     * @param startDate the start of the date range
     * @param endDate the end of the date range
     * @return list of actions ending in the given date range
     */
    @GetMapping("/end-date-range")
    public ResponseEntity<List<ActionDTO>> findByEndDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Pageable pageable = PageRequest.of(0, 100, Sort.by("endDate"));
        Page<ActionDTO> page = actionService.searchActions(null, null, null, null, endDate, null, pageable);
        return ResponseEntity.ok(page.getContent());
    }

    /**
     * Update the progress of an action.
     *
     * @param id the action ID
     * @param progress the new progress value (0-100)
     * @return the updated action
     */
    @PatchMapping("/{id}/progress")
    public ResponseEntity<ActionDTO> updateProgress(
            @PathVariable Long id,
            @RequestParam int progress) {
        return ResponseEntity.ok(actionService.updateProgress(id));
    }

    /**
     * Get all dependencies for an action.
     *
     * @param id the action ID
     * @return list of dependencies
     */
    @GetMapping("/{id}/dependencies")
    public ResponseEntity<List<ActionDTO>> getDependencies(@PathVariable Long id) {
        // This endpoint requires additional implementation in the service layer
        return ResponseEntity.ok(List.of());
    }

    /**
     * Add a dependency to an action.
     *
     * @param id the action ID
     * @param dependencyId the dependency action ID
     * @return success message
     */
    @PostMapping("/{id}/dependencies/{dependencyId}")
    public ResponseEntity<String> addDependency(
            @PathVariable Long id,
            @PathVariable Long dependencyId) {
        actionService.addDependency(id, dependencyId);
        return ResponseEntity.ok("Dependency added successfully");
    }

    /**
     * Remove a dependency from an action.
     *
     * @param id the action ID
     * @param dependencyId the dependency action ID to remove
     * @return success message
     */
    @DeleteMapping("/{id}/dependencies/{dependencyId}")
    public ResponseEntity<String> removeDependency(
            @PathVariable Long id,
            @PathVariable Long dependencyId) {
        actionService.removeDependency(id, dependencyId);
        return ResponseEntity.ok("Dependency removed successfully");
    }

    /**
     * Export action data.
     *
     * @param id the action ID
     * @param format the export format (excel, pdf, csv)
     * @return the exported file
     */
    @GetMapping("/{id}/export")
    public ResponseEntity<byte[]> exportAction(
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
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=action_" + id + "." + extension)
                .body(reportContent);
    }
} 