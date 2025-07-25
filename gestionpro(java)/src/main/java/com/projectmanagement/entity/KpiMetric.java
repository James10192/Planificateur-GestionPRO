package com.projectmanagement.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Entity representing KPI metrics that can be tracked for projects and tasks.
 */
@Entity
@Table(name = "pkpim")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class KpiMetric extends BaseEntity {

    @Column(name = "code", length = 50, nullable = false, unique = true)
    private String code;

    @Column(name = "lib", length = 100, nullable = false)
    private String name;

    @Column(name = "descr", length = 500)
    private String description;

    @Column(name = "unit", length = 50)
    private String unit;

    @Column(name = "thwarn")
    private Double thresholdWarning;

    @Column(name = "thcrit")
    private Double thresholdCritical;

    /**
     * Flag indicating if higher values are better (true) or lower values are better (false).
     */
    @Column(name = "hibett")
    private Boolean higherIsBetter = true;

    /**
     * The formula used to calculate this KPI, if it's a computed KPI.
     */
    @Column(name = "calc", length = 1000)
    private String calculationFormula;

    /**
     * Frequency of KPI updates in minutes (null means manual update only).
     */
    @Column(name = "updfrq")
    private Integer updateFrequencyMinutes;
    
    /**
     * Flag indicating if notifications should be sent when thresholds are crossed.
     */
    @Column(name = "ennot")
    private Boolean enableNotifications = false;
    
    /**
     * Reference to the associated portfolio phase, if any.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idkpi")
    private PortfolioPhase portfolioPhase;
} 