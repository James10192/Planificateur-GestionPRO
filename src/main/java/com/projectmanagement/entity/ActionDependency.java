package com.projectmanagement.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Entity representing action dependencies (tbadep table).
 */
@Entity
@Table(name = "tbadep")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ActionDependency extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idact")
    private Action action;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dep_on")
    private Action dependsOn;
    
    /**
     * Checks if the dependency is fulfilled (the dependent action is completed).
     * @return true if the dependency is fulfilled, false otherwise
     */
    public boolean isDependencyFulfilled() {
        return dependsOn != null && dependsOn.getActualEndDate() != null;
    }
} 