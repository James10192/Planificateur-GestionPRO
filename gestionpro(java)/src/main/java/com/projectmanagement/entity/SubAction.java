package com.projectmanagement.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

/**
 * Entity representing project sub-actions (tbiact table).
 */
@Entity
@Table(name = "tbiact")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SubAction extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idact")
    private Action action;
    
    @Column(name = "lib", nullable = false, length = 200)
    private String name;
    
    @Column(name = "desc", columnDefinition = "TEXT")
    private String description;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idsta")
    private Status status;
    
    @Column(name = "dd")
    private LocalDate startDate;
    
    @Column(name = "df")
    private LocalDate plannedEndDate;
    
    @Column(name = "dfr")
    private LocalDate actualEndDate;
    
    /**
     * Determines if the sub-action is completed based on its actual end date.
     * @return true if the sub-action is completed, false otherwise
     */
    public boolean isCompleted() {
        return actualEndDate != null;
    }
} 