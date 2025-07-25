package com.projectmanagement.repository;

import com.projectmanagement.entity.KpiMetric;
import com.projectmanagement.entity.KpiValue;
import com.projectmanagement.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for KPI values.
 */
@Repository
public interface KpiValueRepository extends JpaRepository<KpiValue, Long> {
    
    /**
     * Find KPI values for a specific project.
     *
     * @param project the project to search for
     * @return list of KPI values for the project
     */
    List<KpiValue> findByProject(Project project);
    
    /**
     * Find KPI values for a specific metric.
     *
     * @param metric the metric to search for
     * @return list of KPI values for the metric
     */
    List<KpiValue> findByMetric(KpiMetric metric);
    
    /**
     * Find KPI values for a specific project and metric.
     *
     * @param project the project to search for
     * @param metric the metric to search for
     * @return list of KPI values for the project and metric
     */
    List<KpiValue> findByProjectAndMetric(Project project, KpiMetric metric);
    
    /**
     * Find the most recent KPI value for a specific project and metric.
     *
     * @param project the project to search for
     * @param metric the metric to search for
     * @return the most recent KPI value, if any
     */
    @Query("SELECT kv FROM KpiValue kv WHERE kv.project = :project AND kv.metric = :metric ORDER BY kv.measurementDate DESC")
    List<KpiValue> findLatestForProjectAndMetric(@Param("project") Project project, @Param("metric") KpiMetric metric);
    
    /**
     * Find KPI values measured between two dates.
     *
     * @param startDate the start date
     * @param endDate the end date
     * @return list of KPI values measured between the dates
     */
    List<KpiValue> findByMeasurementDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Find KPI values that have breached thresholds but haven't sent notifications.
     *
     * @return list of KPI values with breached thresholds and no notifications
     */
    @Query("SELECT kv FROM KpiValue kv WHERE (kv.warningThresholdBreached = true OR kv.criticalThresholdBreached = true) AND kv.notificationSent = false")
    List<KpiValue> findBreachedThresholdsWithoutNotifications();
} 