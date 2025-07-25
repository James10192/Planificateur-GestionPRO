package com.projectmanagement.config;

import com.projectmanagement.service.KpiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Configuration for scheduled tasks.
 */
@Configuration
@EnableScheduling
@Slf4j
public class SchedulingConfig {

    private final KpiService kpiService;

    @Autowired
    public SchedulingConfig(KpiService kpiService) {
        this.kpiService = kpiService;
    }

    /**
     * Scheduled task to update KPIs automatically.
     * Runs every hour (3600000 ms).
     */
    @Scheduled(fixedRate = 3600000)
    public void scheduledKpiUpdate() {
        log.info("Running scheduled KPI update task");
        kpiService.updateKpisAutomatically();
    }

    /**
     * Scheduled task to check KPI thresholds and send notifications.
     * Runs every 15 minutes (900000 ms).
     */
    @Scheduled(fixedRate = 900000)
    public void scheduledThresholdCheck() {
        log.info("Running scheduled KPI threshold check");
        kpiService.checkThresholdsAndNotify();
    }
    
    /**
     * Scheduled task that runs at midnight every day.
     * Can be used for daily reports or data exports.
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void scheduledDailyTask() {
        log.info("Running scheduled daily task");
        // Implement daily tasks here if needed
    }
} 