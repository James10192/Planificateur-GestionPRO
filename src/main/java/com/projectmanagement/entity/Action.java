package com.projectmanagement.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing project actions (tbact table).
 */
@Entity
@Table(name = "tbact")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Action extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idplan")
    private Planning planning;
    
    @Column(name = "lib", nullable = false, length = 200)
    private String name;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idsta")
    private Status status;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idutil")
    private User responsable;
    
    @Column(name = "dd")
    private LocalDate startDate;
    
    @Column(name = "df")
    private LocalDate plannedEndDate;
    
    @Column(name = "dfr")
    private LocalDate actualEndDate;
    
    @Column(name = "prog")
    private Double progress;
    
    @OneToMany(mappedBy = "action", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<SubAction> subActions = new ArrayList<>();
    
    @OneToMany(mappedBy = "action", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ActionDependency> dependencies = new ArrayList<>();
    
    @OneToMany(mappedBy = "dependsOn", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ActionDependency> dependentActions = new ArrayList<>();
    
    // Helper methods
    
    public void addSubAction(SubAction subAction) {
        subActions.add(subAction);
        subAction.setAction(this);
    }
    
    public void removeSubAction(SubAction subAction) {
        subActions.remove(subAction);
        subAction.setAction(null);
    }
    
    public void addDependency(Action dependsOn) {
        ActionDependency dependency = new ActionDependency();
        dependency.setAction(this);
        dependency.setDependsOn(dependsOn);
        dependencies.add(dependency);
        dependsOn.getDependentActions().add(dependency);
    }
    
    public void removeDependency(ActionDependency dependency) {
        dependencies.remove(dependency);
        dependency.getDependsOn().getDependentActions().remove(dependency);
        dependency.setAction(null);
        dependency.setDependsOn(null);
    }
    
    /**
     * Calculates the progress percentage of the action based on the progress of sub-actions.
     * If there are no sub-actions, returns the action's own progress.
     * @return the progress percentage (0-100)
     */
    public double calculateProgress() {
        if (subActions.isEmpty()) {
            return progress != null ? progress : 0;
        }
        
        int totalSubActions = subActions.size();
        long completedSubActions = subActions.stream()
                .filter(sa -> sa.getActualEndDate() != null)
                .count();
        
        return ((double) completedSubActions / totalSubActions) * 100;
    }
} 