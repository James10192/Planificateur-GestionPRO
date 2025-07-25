package com.projectmanagement.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * Entity representing actual KPI values measured for projects.
 */
@Entity
@Table(name = "tbkpiv")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class KpiValue extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idkpim", nullable = false)
    private KpiMetric metric;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idpro")
    private Project project;
    
    /**
     * Reference to the associated portfolio phase, if any.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idkpi")
    private PortfolioPhase portfolioPhase;

    @Column(name = "val", nullable = false)
    private Double value;

    @Column(name = "mdate", nullable = false)
    private LocalDateTime measurementDate;

    @Column(name = "comm", length = 500)
    private String comment;

    /**
     * Flag indicating if this value represents a warning threshold breach.
     */
    @Column(name = "warnbr")
    private Boolean warningThresholdBreached = false;

    /**
     * Flag indicating if this value represents a critical threshold breach.
     */
    @Column(name = "critbr")
    private Boolean criticalThresholdBreached = false;

    /**
     * Flag indicating if a notification has been sent for this value.
     */
    @Column(name = "notsnt")
    private Boolean notificationSent = false;
} 