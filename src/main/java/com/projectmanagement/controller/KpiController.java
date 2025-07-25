package com.projectmanagement.controller;

import com.projectmanagement.dto.KpiMetricDTO;
import com.projectmanagement.dto.KpiValueDTO;
import com.projectmanagement.entity.KpiMetric;
import com.projectmanagement.export.ExportFormat;
import com.projectmanagement.service.KpiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for KPI operations.
 */
@RestController
@RequestMapping("/api/kpis")
public class KpiController {

    private final KpiService kpiService;

    @Autowired
    public KpiController(KpiService kpiService) {
        this.kpiService = kpiService;
    }

    /**
     * Get all KPI metrics.
     *
     * @return list of KPI metrics
     */
    @GetMapping("/metrics")
    public ResponseEntity<List<KpiMetricDTO>> getAllMetrics() {
        List<KpiMetric> metrics = kpiService.findAll();
        List<KpiMetricDTO> dtos = metrics.stream()
                .map(kpiService::entityToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /**
     * Get all active KPI metrics.
     *
     * @return list of active KPI metrics
     */
    @GetMapping("/metrics/active")
    public ResponseEntity<List<KpiMetricDTO>> getAllActiveMetrics() {
        List<KpiMetric> metrics = kpiService.findAllActive();
        List<KpiMetricDTO> dtos = metrics.stream()
                .map(kpiService::entityToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /**
     * Get KPI metrics by portfolio phase.
     *
     * @param portfolioPhaseId the ID of the portfolio phase
     * @return list of KPI metrics for the portfolio phase
     */
    @GetMapping("/metrics/portfolio-phase/{portfolioPhaseId}")
    public ResponseEntity<List<KpiMetricDTO>> getMetricsByPortfolioPhase(@PathVariable Long portfolioPhaseId) {
        return ResponseEntity.ok(kpiService.findMetricsByPortfolioPhaseId(portfolioPhaseId));
    }

    /**
     * Get active KPI metrics by portfolio phase.
     *
     * @param portfolioPhaseId the ID of the portfolio phase
     * @return list of active KPI metrics for the portfolio phase
     */
    @GetMapping("/metrics/portfolio-phase/{portfolioPhaseId}/active")
    public ResponseEntity<List<KpiMetricDTO>> getActiveMetricsByPortfolioPhase(@PathVariable Long portfolioPhaseId) {
        return ResponseEntity.ok(kpiService.findActiveMetricsByPortfolioPhaseId(portfolioPhaseId));
    }

    /**
     * Get a KPI metric by ID.
     *
     * @param id the ID of the KPI metric
     * @return the KPI metric
     */
    @GetMapping("/metrics/{id}")
    public ResponseEntity<KpiMetricDTO> getMetricById(@PathVariable Long id) {
        Optional<KpiMetric> kpiMetric = kpiService.findById(id);
        return kpiMetric.map(metric -> ResponseEntity.ok(kpiService.entityToDto(metric)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Create a new KPI metric.
     *
     * @param metricDTO the KPI metric to create
     * @return the created KPI metric
     */
    @PostMapping("/metrics")
    public ResponseEntity<KpiMetricDTO> createMetric(@RequestBody KpiMetricDTO metricDTO) {
        KpiMetric entity = kpiService.dtoToEntity(metricDTO);
        KpiMetric savedEntity = kpiService.save(entity);
        return ResponseEntity.ok(kpiService.entityToDto(savedEntity));
    }

    /**
     * Update a KPI metric.
     *
     * @param id the ID of the KPI metric
     * @param metricDTO the updated KPI metric
     * @return the updated KPI metric
     */
    @PutMapping("/metrics/{id}")
    public ResponseEntity<KpiMetricDTO> updateMetric(@PathVariable Long id, @RequestBody KpiMetricDTO metricDTO) {
        metricDTO.setId(id);
        KpiMetric entity = kpiService.dtoToEntity(metricDTO);
        KpiMetric updatedEntity = kpiService.update(id, entity);
        return ResponseEntity.ok(kpiService.entityToDto(updatedEntity));
    }

    /**
     * Delete a KPI metric.
     *
     * @param id the ID of the KPI metric to delete
     * @return no content response
     */
    @DeleteMapping("/metrics/{id}")
    public ResponseEntity<Void> deleteMetric(@PathVariable Long id) {
        kpiService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get the latest KPI values for a project.
     *
     * @param projectId the ID of the project
     * @return list of latest KPI values
     */
    @GetMapping("/values/project/{projectId}")
    public ResponseEntity<List<KpiValueDTO>> getLatestKpiValues(@PathVariable Long projectId) {
        return ResponseEntity.ok(kpiService.findLatestKpiValuesForProject(projectId));
    }

    /**
     * Record a new KPI value.
     *
     * @param metricId the ID of the KPI metric
     * @param projectId the ID of the project
     * @param value the KPI value
     * @param comment optional comment about the measurement
     * @return the recorded KPI value
     */
    @PostMapping("/values")
    public ResponseEntity<KpiValueDTO> recordKpiValue(
            @RequestParam Long metricId,
            @RequestParam Long projectId,
            @RequestParam Double value,
            @RequestParam(required = false) String comment) {
        return ResponseEntity.ok(kpiService.recordKpiValue(metricId, projectId, value, comment));
    }

    /**
     * Get KPI values for a project within a date range.
     *
     * @param projectId the ID of the project
     * @param startDate the start date
     * @param endDate the end date
     * @return list of KPI values
     */
    @GetMapping("/values/project/{projectId}/daterange")
    public ResponseEntity<List<KpiValueDTO>> getKpiValuesInDateRange(
            @PathVariable Long projectId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(kpiService.findKpiValuesForProjectInDateRange(projectId, startDate, endDate));
    }

    /**
     * Manually trigger KPI updates.
     *
     * @return success message
     */
    @PostMapping("/update")
    public ResponseEntity<String> updateKpis() {
        kpiService.updateKpisAutomatically();
        return ResponseEntity.ok("KPI update triggered successfully");
    }

    /**
     * Manually check thresholds and send notifications.
     *
     * @return success message
     */
    @PostMapping("/check-thresholds")
    public ResponseEntity<String> checkThresholds() {
        kpiService.checkThresholdsAndNotify();
        return ResponseEntity.ok("Threshold check triggered successfully");
    }

    /**
     * Export KPI data for a project.
     *
     * @param projectId the ID of the project
     * @param format the export format (excel, pdf, csv)
     * @return the exported file
     */
    @GetMapping("/export/project/{projectId}")
    public ResponseEntity<byte[]> exportKpiData(
            @PathVariable Long projectId,
            @RequestParam(defaultValue = "excel") String format) {
        
        byte[] reportContent = kpiService.exportKpiData(projectId, format);
        
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
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=kpi_report." + extension)
                .body(reportContent);
    }
} 