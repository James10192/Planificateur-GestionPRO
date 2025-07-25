package com.projectmanagement.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing portfolio phases (pkpi table).
 */
@Entity
@Table(name = "pkpi")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class PortfolioPhase extends BaseEntity {
    
    @Column(name = "lib", nullable = false, length = 100)
    private String name;
    
    @Column(name = "pct")
    private Double percentage;
    
    @OneToMany(mappedBy = "phase")
    @Builder.Default
    private List<Planning> plannings = new ArrayList<>();
} 