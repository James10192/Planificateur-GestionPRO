package com.projectmanagement.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Base entity class for all entities in the application.
 * Provides common fields and audit functionality.
 * Configured for MySQL database with AUTO_INCREMENT generation.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @CreatedDate
    @Column(name = "dtcrea", nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @LastModifiedDate
    @Column(name = "dtmod")
    private LocalDateTime dateModification;

    @Column(name = "actif", nullable = false)
    @Builder.Default
    private Boolean actif = true;
    
    /**
     * Checks if the entity is active.
     * @return true if the entity is active, false otherwise
     */
    public boolean isActive() {
        return Boolean.TRUE.equals(actif);
    }
    
    /**
     * Sets the entity as active or inactive.
     * @param active the active status to set
     */
    public void setActive(boolean active) {
        this.actif = active;
    }
} 