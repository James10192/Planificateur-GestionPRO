package com.projectmanagement.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing project planning (tbplan table).
 */
@Entity
@Table(name = "tbplan")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Planning extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idpro")
    private Project project;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idkpi")
    private PortfolioPhase phase;
    
    @OneToMany(mappedBy = "planning", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Action> actions = new ArrayList<>();
    
    // Helper methods
    
    public void addAction(Action action) {
        actions.add(action);
        action.setPlanning(this);
    }
    
    public void removeAction(Action action) {
        actions.remove(action);
        action.setPlanning(null);
    }
    
    /**
     * Calculates the progress percentage of the planning based on the progress of actions.
     * @return the progress percentage (0-100)
     */
    public double calculateProgress() {
        if (actions.isEmpty()) {
            return 0;
        }
        
        double totalProgress = actions.stream()
                .mapToDouble(Action::getProgress)
                .sum();
        
        return totalProgress / actions.size();
    }
} 