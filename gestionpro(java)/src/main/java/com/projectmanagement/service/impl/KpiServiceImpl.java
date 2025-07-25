package com.projectmanagement.service.impl;

import com.projectmanagement.dto.KpiMetricDTO;
import com.projectmanagement.dto.KpiValueDTO;
import com.projectmanagement.entity.Action;
import com.projectmanagement.entity.KpiMetric;
import com.projectmanagement.entity.KpiValue;
import com.projectmanagement.entity.Project;
import com.projectmanagement.entity.ProjectBudget;
import com.projectmanagement.export.ExportFormat;
import com.projectmanagement.export.ExportOptions;
import com.projectmanagement.export.ExportService;
import com.projectmanagement.export.ExportServiceFactory;
import com.projectmanagement.repository.KpiMetricRepository;
import com.projectmanagement.repository.KpiValueRepository;
import com.projectmanagement.repository.ProjectRepository;
import com.projectmanagement.service.KpiService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of the KPI service.
 */
@Service
@Slf4j
@Transactional
public class KpiServiceImpl implements KpiService {
    
    private final KpiMetricRepository kpiMetricRepository;
    private final KpiValueRepository kpiValueRepository;
    private final ProjectRepository projectRepository;
    private final ExportServiceFactory exportServiceFactory;
    
    @Autowired
    public KpiServiceImpl(KpiMetricRepository kpiMetricRepository, 
                         KpiValueRepository kpiValueRepository,
                         ProjectRepository projectRepository,
                         ExportServiceFactory exportServiceFactory) {
        this.kpiMetricRepository = kpiMetricRepository;
        this.kpiValueRepository = kpiValueRepository;
        this.projectRepository = projectRepository;
        this.exportServiceFactory = exportServiceFactory;
    }

    @Override
    public KpiMetricDTO entityToDto(KpiMetric entity) {
        if (entity == null) {
            return null;
        }
        
        KpiMetricDTO dto = new KpiMetricDTO();
        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setUnit(entity.getUnit());
        dto.setThresholdWarning(entity.getThresholdWarning());
        dto.setThresholdCritical(entity.getThresholdCritical());
        dto.setHigherIsBetter(entity.getHigherIsBetter());
        dto.setCalculationFormula(entity.getCalculationFormula());
        dto.setUpdateFrequencyMinutes(entity.getUpdateFrequencyMinutes());
        dto.setEnableNotifications(entity.getEnableNotifications());
        
        return dto;
    }

    @Override
    public KpiMetric dtoToEntity(KpiMetricDTO dto) {
        if (dto == null) {
            return null;
        }
        
        KpiMetric entity = new KpiMetric();
        entity.setId(dto.getId());
        entity.setCode(dto.getCode());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setUnit(dto.getUnit());
        entity.setThresholdWarning(dto.getThresholdWarning());
        entity.setThresholdCritical(dto.getThresholdCritical());
        entity.setHigherIsBetter(dto.getHigherIsBetter());
        entity.setCalculationFormula(dto.getCalculationFormula());
        entity.setUpdateFrequencyMinutes(dto.getUpdateFrequencyMinutes());
        entity.setEnableNotifications(dto.getEnableNotifications());
        
        return entity;
    }
    
    /**
     * Convert a KpiValue entity to a DTO.
     */
    private KpiValueDTO kpiValueToDto(KpiValue entity) {
        if (entity == null) {
            return null;
        }
        
        KpiValueDTO dto = new KpiValueDTO();
        dto.setId(entity.getId());
        
        if (entity.getMetric() != null) {
            dto.setMetricId(entity.getMetric().getId());
            dto.setMetricCode(entity.getMetric().getCode());
            dto.setMetricName(entity.getMetric().getName());
        }
        
        if (entity.getProject() != null) {
            dto.setProjectId(entity.getProject().getId());
            dto.setProjectName(entity.getProject().getName());
        }
        
        dto.setValue(entity.getValue());
        dto.setMeasurementDate(entity.getMeasurementDate());
        dto.setComment(entity.getComment());
        dto.setWarningThresholdBreached(entity.getWarningThresholdBreached());
        dto.setCriticalThresholdBreached(entity.getCriticalThresholdBreached());
        dto.setNotificationSent(entity.getNotificationSent());
        
        return dto;
    }

    // BaseService interface implementation
    @Override
    public KpiMetric save(KpiMetric entity) {
        return kpiMetricRepository.save(entity);
    }

    @Override
    public KpiMetric update(Long id, KpiMetric entity) {
        entity.setId(id);
        return kpiMetricRepository.save(entity);
    }

    @Override
    public Optional<KpiMetric> findById(Long id) {
        return kpiMetricRepository.findById(id);
    }

    @Override
    public List<KpiMetric> findAll() {
        return kpiMetricRepository.findAll();
    }

    @Override
    public List<KpiMetric> findAllActive() {
        return kpiMetricRepository.findByActifTrue();
    }

    @Override
    public void deleteById(Long id) {
        kpiMetricRepository.deleteById(id);
    }

    @Override
    public KpiMetric deactivate(Long id) {
        return kpiMetricRepository.findById(id)
                .map(metric -> {
                    metric.setActif(false);
                    return kpiMetricRepository.save(metric);
                })
                .orElseThrow(() -> new EntityNotFoundException("KPI metric not found with ID: " + id));
    }

    @Override
    public KpiMetric reactivate(Long id) {
        return kpiMetricRepository.findById(id)
                .map(metric -> {
                    metric.setActif(true);
                    return kpiMetricRepository.save(metric);
                })
                .orElseThrow(() -> new EntityNotFoundException("KPI metric not found with ID: " + id));
    }

    @Override
    public boolean existsById(Long id) {
        return kpiMetricRepository.existsById(id);
    }

    @Override
    public List<KpiMetricDTO> findMetricsRequiringUpdates() {
        return kpiMetricRepository.findMetricsRequiringUpdates().stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<KpiMetricDTO> findMetricsWithNotificationsEnabled() {
        return kpiMetricRepository.findByEnableNotificationsTrue().stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<KpiMetricDTO> findMetricsByPortfolioPhaseId(Long portfolioPhaseId) {
        log.debug("Finding KPI metrics for portfolio phase ID: {}", portfolioPhaseId);
        return kpiMetricRepository.findByPortfolioPhaseId(portfolioPhaseId).stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<KpiMetricDTO> findActiveMetricsByPortfolioPhaseId(Long portfolioPhaseId) {
        log.debug("Finding active KPI metrics for portfolio phase ID: {}", portfolioPhaseId);
        return kpiMetricRepository.findByPortfolioPhaseId(portfolioPhaseId).stream()
                .filter(metric -> Boolean.TRUE.equals(metric.getActif()))
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public KpiValueDTO recordKpiValue(Long metricId, Long projectId, Double value, String comment) {
        KpiMetric metric = kpiMetricRepository.findById(metricId)
                .orElseThrow(() -> new EntityNotFoundException("KPI metric not found with ID: " + metricId));
        
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with ID: " + projectId));
        
        KpiValue kpiValue = new KpiValue();
        kpiValue.setMetric(metric);
        kpiValue.setProject(project);
        kpiValue.setValue(value);
        kpiValue.setMeasurementDate(LocalDateTime.now());
        kpiValue.setComment(comment);
        
        // Set the portfolio phase based on the metric's phase (if it has one)
        // The database trigger will enforce this rule, but we also implement it here
        // for better application-level consistency
        if (metric.getPortfolioPhase() != null) {
            kpiValue.setPortfolioPhase(metric.getPortfolioPhase());
        }
        
        // Check thresholds
        if (metric.getThresholdWarning() != null) {
            boolean breached;
            if (Boolean.TRUE.equals(metric.getHigherIsBetter())) {
                breached = value < metric.getThresholdWarning();
            } else {
                breached = value > metric.getThresholdWarning();
            }
            kpiValue.setWarningThresholdBreached(breached);
        }
        
        if (metric.getThresholdCritical() != null) {
            boolean breached;
            if (Boolean.TRUE.equals(metric.getHigherIsBetter())) {
                breached = value < metric.getThresholdCritical();
            } else {
                breached = value > metric.getThresholdCritical();
            }
            kpiValue.setCriticalThresholdBreached(breached);
        }
        
        kpiValue = kpiValueRepository.save(kpiValue);
        
        return kpiValueToDto(kpiValue);
    }

    @Override
    public List<KpiValueDTO> findLatestKpiValuesForProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with ID: " + projectId));
        
        // Get all metrics
        List<KpiMetric> metrics = kpiMetricRepository.findAll();
        
        // For each metric, find the latest value for this project
        List<KpiValueDTO> result = new ArrayList<>();
        for (KpiMetric metric : metrics) {
            List<KpiValue> values = kpiValueRepository.findLatestForProjectAndMetric(project, metric);
            if (!values.isEmpty()) {
                result.add(kpiValueToDto(values.get(0)));
            }
        }
        
        return result;
    }

    @Override
    public List<KpiValueDTO> findKpiValuesForProjectInDateRange(Long projectId, LocalDateTime startDate, LocalDateTime endDate) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with ID: " + projectId));
        
        // Get values in the date range
        List<KpiValue> values = kpiValueRepository.findByMeasurementDateBetween(startDate, endDate).stream()
                .filter(value -> value.getProject().equals(project))
                .collect(Collectors.toList());
        
        return values.stream()
                .map(this::kpiValueToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void updateKpisAutomatically() {
        log.info("Starting automatic KPI updates");
        
        List<KpiMetric> metrics = kpiMetricRepository.findMetricsRequiringUpdates();
        List<Project> projects = projectRepository.findAll();
        
        for (KpiMetric metric : metrics) {
            log.info("Updating KPI: {}", metric.getCode());
            
            for (Project project : projects) {
                try {
                    Double value = calculateKpiValue(metric.getId(), project.getId());
                    if (value != null) {
                        recordKpiValue(metric.getId(), project.getId(), value, 
                                "Automatically calculated on " + LocalDateTime.now());
                    }
                } catch (Exception e) {
                    log.error("Error calculating KPI {} for project {}: {}", 
                            metric.getCode(), project.getName(), e.getMessage(), e);
                }
            }
        }
        
        log.info("Completed automatic KPI updates");
    }

    @Override
    public void checkThresholdsAndNotify() {
        log.info("Checking for KPI threshold breaches");
        
        List<KpiValue> breachedValues = kpiValueRepository.findBreachedThresholdsWithoutNotifications();
        
        for (KpiValue value : breachedValues) {
            try {
                // In a real implementation, this would send an email or other notification
                String level = value.getCriticalThresholdBreached() ? "CRITICAL" : "WARNING";
                log.info("KPI Threshold Breach Notification - Level: {}, Project: {}, Metric: {}, Value: {}", 
                        level, value.getProject().getName(), value.getMetric().getName(), value.getValue());
                
                // Mark as notified
                value.setNotificationSent(true);
                kpiValueRepository.save(value);
            } catch (Exception e) {
                log.error("Error sending notification for KPI threshold breach: {}", e.getMessage(), e);
            }
        }
        
        log.info("Completed threshold breach check");
    }

    @Override
    public Double calculateKpiValue(Long metricId, Long projectId) {
        KpiMetric metric = kpiMetricRepository.findById(metricId)
                .orElseThrow(() -> new EntityNotFoundException("KPI metric not found with ID: " + metricId));
        
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with ID: " + projectId));
        
        // Exit early if no calculation formula is defined
        if (metric.getCalculationFormula() == null || metric.getCalculationFormula().isEmpty()) {
            log.debug("No calculation formula defined for KPI: {}", metric.getCode());
            return null;
        }
        
        log.debug("Calculating KPI {} for project {}", metric.getCode(), project.getName());
        
        // Implement specific calculations based on KPI code
        switch (metric.getCode()) {
            case "COMPLETION_RATE":
                // Calculate completion rate based on project's actions progress
                return calculateCompletionRate(project);
                
            case "BUDGET_UTILIZATION":
                // Calculate budget utilization based on project's budget data
                return calculateBudgetUtilization(project);
                
            default:
                // For custom formulas, try to evaluate the formula string
                try {
                    return evaluateCustomFormula(metric.getCalculationFormula(), project);
                } catch (Exception e) {
                    log.warn("Error evaluating custom formula for KPI {}: {}", 
                            metric.getCode(), e.getMessage());
                    return null;
                }
        }
    }
    
    /**
     * Calculate the completion rate of a project based on actions progress.
     * 
     * @param project The project to calculate completion rate for
     * @return The completion rate as a percentage (0-100)
     */
    private Double calculateCompletionRate(Project project) {
        // Use the project's existing calculateProgress method
        return project.calculateProgress();
    }
    
    /**
     * Calculate the budget utilization of a project.
     * 
     * @param project The project to calculate budget utilization for
     * @return The budget utilization as a percentage (0-100)
     */
    private Double calculateBudgetUtilization(Project project) {
        if (project.getBudgets() == null || project.getBudgets().isEmpty()) {
            log.debug("No budget data available for project: {}", project.getName());
            return null;
        }
        
        // Get the most recent budget entry
        // In a real implementation, you might want to handle multiple budget entries differently
        ProjectBudget budget = project.getBudgets().stream()
                .filter(b -> b.getActif())
                .findFirst()
                .orElse(null);
        
        if (budget == null || budget.getInitialBudget() == null || 
                budget.getInitialBudget().doubleValue() == 0) {
            log.debug("Invalid budget data for project: {}", project.getName());
            return null;
        }
        
        return budget.getConsumptionPercentage();
    }
    
    /**
     * Evaluate a custom formula for KPI calculation.
     * 
     * @param formula The formula string to evaluate
     * @param project The project context for the formula
     * @return The calculated value
     */
    private Double evaluateCustomFormula(String formula, Project project) {
        // This is a placeholder for a real formula evaluation engine
        // In a real implementation, you would parse and evaluate the formula
        // using a library like MVEL, SpEL, or a custom parser
        
        log.debug("Formula evaluation not fully implemented, using placeholder");
        
        // For now, return a random value as placeholder
        // This should be replaced with actual formula evaluation logic
        return Math.random() * 100.0;
    }

    @Override
    public byte[] exportKpiData(Long projectId, String format) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with ID: " + projectId));
        
        // Get KPI data for the project
        List<KpiValueDTO> kpiValues = findLatestKpiValuesForProject(projectId);
        
        // Convert to export format
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ExportFormat exportFormat = ExportFormat.valueOf(format.toUpperCase());
            ExportService exportService = exportServiceFactory.getExportService(exportFormat);
            
            // Define headers
            Map<String, String> headers = new LinkedHashMap<>();
            headers.put("metricCode", "Metric Code");
            headers.put("metricName", "Metric Name");
            headers.put("value", "Value");
            headers.put("measurementDate", "Measurement Date");
            headers.put("warningThresholdBreached", "Warning Threshold Breached");
            headers.put("criticalThresholdBreached", "Critical Threshold Breached");
            
            // Configure export options
            ExportOptions options = ExportOptions.builder()
                    .title("KPI Report - " + project.getName())
                    .author("Project Management System")
                    .description("KPI values for project: " + project.getName())
                    .includeHeaders(true)
                    .formatDates(true)
                    .formatNumbers(true)
                    .datePattern("yyyy-MM-dd HH:mm:ss")
                    .build();
            
            // Perform export
            exportService.export(kpiValues, headers, outputStream, options);
            
            return outputStream.toByteArray();
        } catch (Exception e) {
            log.error("Error exporting KPI data: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to export KPI data: " + e.getMessage(), e);
        }
    }

    /**
     * Batch updates KPI values for multiple metrics on a project.
     *
     * @param projectId    the ID of the project
     * @param metricsIds   list of KPI metric IDs to update
     * @param currentDate  date to record for the KPI values (defaults to now if null)
     * @return list of updated KPI values
     */
    @Override
    public List<KpiValueDTO> batchUpdateKpiValues(Long projectId, List<Long> metricsIds, LocalDateTime currentDate) {
        log.debug("Batch updating KPI values for project ID: {} and metrics: {}", projectId, metricsIds);
        
        // Validate project
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with ID: " + projectId));
        
        LocalDateTime valueDate = currentDate != null ? currentDate : LocalDateTime.now();
        
        List<KpiValueDTO> updatedValues = new ArrayList<>();
        
        for (Long metricId : metricsIds) {
            try {
                // Calculate new value
                Double calculatedValue = calculateKpiValue(metricId, projectId);
                
                // Find or create KPI value
                KpiMetric metric = kpiMetricRepository.findById(metricId)
                        .orElseThrow(() -> new EntityNotFoundException("KPI metric not found with ID: " + metricId));
                
                KpiValue kpiValue = new KpiValue();
                kpiValue.setMetric(metric);
                kpiValue.setProject(project);
                kpiValue.setValue(calculatedValue);
                kpiValue.setMeasurementDate(valueDate);
                kpiValue.setComment("Auto-updated via batch process");
                
                // Save value
                KpiValue savedValue = kpiValueRepository.save(kpiValue);
                updatedValues.add(kpiValueToDto(savedValue));
                
                log.debug("Updated KPI value for metric ID: {}, new value: {}", metricId, calculatedValue);
            } catch (Exception e) {
                log.error("Error updating KPI value for metric ID: {}", metricId, e);
                // Continue with other metrics even if one fails
            }
        }
        
        return updatedValues;
    }

    @Override
    public String exportKpiData(Long projectId, ExportFormat format) {
        log.debug("Exporting KPI data for project ID: {} in format: {}", projectId, format);
        
        // Validate project
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with ID: " + projectId));
        
        // Get latest KPI values for project
        List<KpiValueDTO> kpiValues = findLatestKpiValuesForProject(projectId);
        
        if (kpiValues.isEmpty()) {
            return "No KPI data available for export";
        }
        
        // Get export service based on requested format
        ExportService exportService = exportServiceFactory.getExportService(format);
        
        // Set export options
        ExportOptions options = ExportOptions.builder()
                .title("KPI Report for Project: " + project.getName())
                .description("Generated on " + LocalDateTime.now())
                .build();
        
        // Convert KpiValueDTO list to a format appropriate for export
        List<Map<String, Object>> exportData = kpiValues.stream()
                .map(kpi -> {
                    Map<String, Object> data = new HashMap<>();
                    data.put("Metric Name", kpi.getMetricName());
                    data.put("Value", kpi.getValue());
                    data.put("Measurement Date", kpi.getMeasurementDate());
                    // These fields aren't in KpiValueDTO, so we'll remove them
                    // data.put("Target", kpi.getTarget());
                    // data.put("Status", calculateStatus(kpi.getValue(), kpi.getTarget()));
                    return data;
                })
                .collect(Collectors.toList());
        
        // Define headers for the export
        Map<String, String> headers = new LinkedHashMap<>();
        headers.put("Metric Name", "Metric Name");
        headers.put("Value", "Value");
        headers.put("Measurement Date", "Measurement Date");
        
        // Use outputStream for the export
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            // Export the data using the proper method signature
            exportService.export(exportData, headers, outputStream, options);
            
            // Return the result as a string
            return outputStream.toString();
        } catch (Exception e) {
            log.error("Error exporting KPI data: {}", e.getMessage(), e);
            return "Error exporting KPI data: " + e.getMessage();
        }
    }
} 