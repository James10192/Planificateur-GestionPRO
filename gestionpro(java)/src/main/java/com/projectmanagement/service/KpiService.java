package com.projectmanagement.service;

import com.projectmanagement.dto.KpiMetricDTO;
import com.projectmanagement.dto.KpiValueDTO;
import com.projectmanagement.entity.KpiMetric;
import com.projectmanagement.entity.KpiValue;
import com.projectmanagement.entity.Project;
import com.projectmanagement.export.ExportFormat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service interface for KPI operations.
 */
public interface KpiService extends BaseService<KpiMetric, Long> {
    
    /**
     * Convert a KpiMetric entity to a KpiMetricDTO.
     * 
     * @param entity the entity to convert
     * @return the DTO
     */
    KpiMetricDTO entityToDto(KpiMetric entity);
    
    /**
     * Convert a KpiMetricDTO to a KpiMetric entity.
     * 
     * @param dto the DTO to convert
     * @return the entity
     */
    KpiMetric dtoToEntity(KpiMetricDTO dto);
    
    /**
     * Find KPI metrics requiring automatic updates.
     *
     * @return list of KPI metrics that need automatic updates
     */
    List<KpiMetricDTO> findMetricsRequiringUpdates();
    
    /**
     * Find KPI metrics with notifications enabled.
     *
     * @return list of KPI metrics with notifications enabled
     */
    List<KpiMetricDTO> findMetricsWithNotificationsEnabled();
    
    /**
     * Find KPI metrics by portfolio phase ID.
     *
     * @param portfolioPhaseId the ID of the portfolio phase to search for
     * @return list of KPI metrics associated with the given portfolio phase
     */
    List<KpiMetricDTO> findMetricsByPortfolioPhaseId(Long portfolioPhaseId);
    
    /**
     * Find active KPI metrics by portfolio phase ID.
     *
     * @param portfolioPhaseId the ID of the portfolio phase to search for
     * @return list of active KPI metrics associated with the given portfolio phase
     */
    List<KpiMetricDTO> findActiveMetricsByPortfolioPhaseId(Long portfolioPhaseId);
    
    /**
     * Record a new KPI value for a project.
     *
     * @param metricId the ID of the KPI metric
     * @param projectId the ID of the project
     * @param value the KPI value to record
     * @param comment optional comment about the measurement
     * @return the recorded KPI value DTO
     */
    KpiValueDTO recordKpiValue(Long metricId, Long projectId, Double value, String comment);
    
    /**
     * Find the latest KPI values for a project.
     *
     * @param projectId the ID of the project
     * @return list of the latest KPI values for the project
     */
    List<KpiValueDTO> findLatestKpiValuesForProject(Long projectId);
    
    /**
     * Find KPI values for a project within a date range.
     *
     * @param projectId the ID of the project
     * @param startDate the start date
     * @param endDate the end date
     * @return list of KPI values within the date range
     */
    List<KpiValueDTO> findKpiValuesForProjectInDateRange(Long projectId, LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Update KPIs automatically based on their calculation formulas and frequencies.
     */
    void updateKpisAutomatically();
    
    /**
     * Check for threshold breaches and send notifications if needed.
     */
    void checkThresholdsAndNotify();
    
    /**
     * Calculate the value of a computed KPI for a project.
     *
     * @param metricId the ID of the KPI metric to calculate
     * @param projectId the ID of the project
     * @return the calculated KPI value
     */
    Double calculateKpiValue(Long metricId, Long projectId);
    
    /**
     * Export KPI data for a project to a file.
     *
     * @param projectId the ID of the project
     * @param format the export format (e.g., "excel", "pdf", "csv")
     * @return byte array containing the exported data
     */
    byte[] exportKpiData(Long projectId, String format);
    
    /**
     * Batch updates KPI values for multiple metrics on a project.
     *
     * @param projectId    the ID of the project
     * @param metricsIds   list of KPI metric IDs to update
     * @param currentDate  date to record for the KPI values (defaults to now if null)
     * @return list of updated KPI values
     */
    List<KpiValueDTO> batchUpdateKpiValues(Long projectId, List<Long> metricsIds, LocalDateTime currentDate);
    
    /**
     * Export KPI data for a project in the specified format.
     *
     * @param projectId the ID of the project
     * @param format the export format
     * @return the exported data as a string
     */
    String exportKpiData(Long projectId, ExportFormat format);
} 