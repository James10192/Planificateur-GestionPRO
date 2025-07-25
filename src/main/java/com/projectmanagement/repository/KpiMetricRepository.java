package com.projectmanagement.repository;

import com.projectmanagement.entity.KpiMetric;
import com.projectmanagement.entity.PortfolioPhase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for KPI metrics.
 */
@Repository
public interface KpiMetricRepository extends JpaRepository<KpiMetric, Long> {
    
    /**
     * Find a KPI metric by its code.
     *
     * @param code the code to search for
     * @return the matching KPI metric, if found
     */
    Optional<KpiMetric> findByCode(String code);
    
    /**
     * Find KPI metrics that require automatic updates.
     *
     * @return list of KPI metrics with update frequency defined
     */
    @Query("SELECT k FROM KpiMetric k WHERE k.updateFrequencyMinutes IS NOT NULL AND k.updateFrequencyMinutes > 0")
    List<KpiMetric> findMetricsRequiringUpdates();
    
    /**
     * Find KPI metrics with notification enabled.
     *
     * @return list of KPI metrics with notifications enabled
     */
    List<KpiMetric> findByEnableNotificationsTrue();
    
    /**
     * Find all active KPI metrics.
     *
     * @return list of active KPI metrics
     */
    List<KpiMetric> findByActifTrue();
    
    /**
     * Find KPI metrics by portfolio phase.
     *
     * @param portfolioPhase the portfolio phase to search for
     * @return list of KPI metrics associated with the provided portfolio phase
     */
    List<KpiMetric> findByPortfolioPhase(PortfolioPhase portfolioPhase);
    
    /**
     * Find KPI metrics by portfolio phase ID.
     *
     * @param portfolioPhaseId the ID of the portfolio phase to search for
     * @return list of KPI metrics associated with the provided portfolio phase ID
     */
    @Query("SELECT k FROM KpiMetric k WHERE k.portfolioPhase.id = :portfolioPhaseId")
    List<KpiMetric> findByPortfolioPhaseId(@Param("portfolioPhaseId") Long portfolioPhaseId);
    
    /**
     * Find active KPI metrics by portfolio phase.
     *
     * @param portfolioPhase the portfolio phase to search for
     * @return list of active KPI metrics associated with the provided portfolio phase
     */
    List<KpiMetric> findByPortfolioPhaseAndActifTrue(PortfolioPhase portfolioPhase);
} 