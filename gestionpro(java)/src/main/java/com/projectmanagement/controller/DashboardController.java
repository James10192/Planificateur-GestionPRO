package com.projectmanagement.controller;

import com.projectmanagement.dto.ActionDTO;
import com.projectmanagement.dto.ProjectDTO;
import com.projectmanagement.service.ActionService;
import com.projectmanagement.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * REST controller for dashboard operations.
 * This controller provides endpoints for retrieving summary data for the dashboard.
 */
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final ProjectService projectService;
    private final ActionService actionService;

    @Autowired
    public DashboardController(ProjectService projectService, ActionService actionService) {
        this.projectService = projectService;
        this.actionService = actionService;
    }

    /**
     * Get summary data for the dashboard.
     *
     * @return map containing summary data
     */
    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getDashboardSummary() {
        Map<String, Object> summary = new HashMap<>();
        
        // Project counts by status - mock implementation
        Map<String, Long> projectStatusCounts = new HashMap<>();
        projectStatusCounts.put("Not Started", 3L);
        projectStatusCounts.put("In Progress", 5L);
        projectStatusCounts.put("Completed", 2L);
        projectStatusCounts.put("On Hold", 1L);
        summary.put("projectStatusCounts", projectStatusCounts);
        
        // Project counts by priority - mock implementation
        Map<String, Long> projectPriorityCounts = new HashMap<>();
        projectPriorityCounts.put("Low", 2L);
        projectPriorityCounts.put("Medium", 4L);
        projectPriorityCounts.put("High", 3L);
        projectPriorityCounts.put("Critical", 2L);
        summary.put("projectPriorityCounts", projectPriorityCounts);
        
        // Action counts by status - mock implementation
        Map<String, Long> actionStatusCounts = new HashMap<>();
        actionStatusCounts.put("Not Started", 10L);
        actionStatusCounts.put("In Progress", 15L);
        actionStatusCounts.put("Completed", 8L);
        actionStatusCounts.put("Overdue", 3L);
        summary.put("actionStatusCounts", actionStatusCounts);
        
        // Total counts - mock implementation
        summary.put("totalProjects", 11L);
        summary.put("totalActions", 36L);
        summary.put("overdueActions", 3L);
        
        return ResponseEntity.ok(summary);
    }

    /**
     * Get recently updated projects.
     *
     * @param limit the maximum number of projects to return
     * @return list of recently updated projects
     */
    @GetMapping("/recent-projects")
    public ResponseEntity<List<ProjectDTO>> getRecentProjects(@RequestParam(defaultValue = "5") int limit) {
        // Mock implementation - would normally use a service method to get recently updated projects
        List<ProjectDTO> projects = projectService.findAll().stream()
                .map(projectService::toDTO)
                .sorted((p1, p2) -> p2.getDateModification().compareTo(p1.getDateModification()))
                .limit(limit)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(projects);
    }

    /**
     * Get upcoming actions.
     *
     * @param days the number of days to look ahead
     * @param limit the maximum number of actions to return
     * @return list of upcoming actions
     */
    @GetMapping("/upcoming-actions")
    public ResponseEntity<List<ActionDTO>> getUpcomingActions(
            @RequestParam(defaultValue = "7") int days,
            @RequestParam(defaultValue = "10") int limit) {
        
        Pageable pageable = PageRequest.of(0, limit, Sort.by("endDate"));
        Page<ActionDTO> upcomingActions = actionService.findUpcomingDeadlines((long) days, pageable);
        
        return ResponseEntity.ok(upcomingActions.getContent());
    }

    /**
     * Get overdue actions.
     *
     * @param limit the maximum number of actions to return
     * @return list of overdue actions
     */
    @GetMapping("/overdue-actions")
    public ResponseEntity<List<ActionDTO>> getOverdueActions(@RequestParam(defaultValue = "10") int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by("endDate"));
        Page<ActionDTO> overdueActions = actionService.findOverdueActions(pageable);
        
        return ResponseEntity.ok(overdueActions.getContent());
    }

    /**
     * Get project completion trend data.
     *
     * @param months the number of months to look back
     * @return map containing project completion trend data by month
     */
    @GetMapping("/project-completion-trend")
    public ResponseEntity<Map<String, Long>> getProjectCompletionTrend(@RequestParam(defaultValue = "12") int months) {
        // Mock implementation - would normally use a service method
        Map<String, Long> trend = new HashMap<>();
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy");
        
        for (int i = 0; i < months; i++) {
            LocalDate date = today.minusMonths(i);
            String month = date.format(formatter);
            trend.put(month, (long) (Math.random() * 5)); // Random number of completed projects
        }
        
        return ResponseEntity.ok(trend);
    }

    /**
     * Get user-specific dashboard data.
     *
     * @param userId the user ID
     * @return map containing user-specific dashboard data
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> getUserDashboard(@PathVariable Long userId) {
        Map<String, Object> userDashboard = new HashMap<>();
        
        // User's projects - mock implementation
        List<ProjectDTO> userProjects = projectService.findAll().stream()
                .map(projectService::toDTO)
                .filter(p -> {
                    // In a real implementation, this would check if the user is assigned to the project
                    // Since getTeamMembers() is not available, we'll mock the behavior
                    return Math.random() > 0.5; // Randomly include some projects
                })
                //.filter(p -> p.getTeamMembers() != null && p.getTeamMembers().contains(userId))
                .collect(Collectors.toList());
        userDashboard.put("userProjects", userProjects);
        
        // User's actions - mock implementation
        Pageable pageable = PageRequest.of(0, 10, Sort.by("endDate"));
        Page<ActionDTO> userActions = actionService.findByResponsableId(userId, pageable);
        userDashboard.put("userActions", userActions.getContent());
        
        // User's overdue actions - mock implementation
        Page<ActionDTO> overdueActions = actionService.findOverdueActions(pageable);
        List<ActionDTO> userOverdueActions = overdueActions.getContent().stream()
                .filter(a -> userId.equals(a.getResponsableId()))
                .collect(Collectors.toList());
        userDashboard.put("userOverdueActions", userOverdueActions);
        
        // User's upcoming actions - mock implementation
        Page<ActionDTO> upcomingActions = actionService.findUpcomingDeadlines(7L, pageable);
        List<ActionDTO> userUpcomingActions = upcomingActions.getContent().stream()
                .filter(a -> userId.equals(a.getResponsableId()))
                .collect(Collectors.toList());
        userDashboard.put("userUpcomingActions", userUpcomingActions);
        
        return ResponseEntity.ok(userDashboard);
    }
} 