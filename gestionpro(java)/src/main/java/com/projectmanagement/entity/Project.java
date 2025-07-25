package com.projectmanagement.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing projects (tbpro table).
 */
@Entity
@Table(name = "tbpro")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Project extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idpro")
    private Long id;
    
    @Column(name = "lib", nullable = false, length = 200)
    private String name;
    
    @Lob
    @Column(name = "descr")
    private String description;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idtyp")
    private ProjectType type;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idsta")
    private Status status;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idprio")
    private Priority priority;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "iddir")
    private Direction direction;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idequipro")
    private ProjectTeam team;
    
    @Column(name = "dd")
    private LocalDate startDate;
    
    @Column(name = "df")
    private LocalDate plannedEndDate;
    
    @Column(name = "dfr")
    private LocalDate actualEndDate;
    
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Planning> plannings = new ArrayList<>();
    
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Document> documents = new ArrayList<>();
    
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProjectBudget> budgets = new ArrayList<>();
    
    // Helper methods
    
    public void addPlanning(Planning planning) {
        plannings.add(planning);
        planning.setProject(this);
    }
    
    public void removePlanning(Planning planning) {
        plannings.remove(planning);
        planning.setProject(null);
    }
    
    public void addDocument(Document document) {
        documents.add(document);
        document.setProject(this);
    }
    
    public void removeDocument(Document document) {
        documents.remove(document);
        document.setProject(null);
    }
    
    public void addBudget(ProjectBudget budget) {
        budgets.add(budget);
        budget.setProject(this);
    }
    
    public void removeBudget(ProjectBudget budget) {
        budgets.remove(budget);
        budget.setProject(null);
    }
    
    /**
     * Calculates the progress percentage of the project based on the progress of actions.
     * @return the progress percentage (0-100)
     */
    public double calculateProgress() {
        if (plannings.isEmpty()) {
            return 0;
        }
        
        double totalProgress = 0;
        int totalActions = 0;
        
        for (Planning planning : plannings) {
            List<Action> actions = planning.getActions();
            totalActions += actions.size();
            
            for (Action action : actions) {
                totalProgress += action.getProgress();
            }
        }
        
        if (totalActions == 0) {
            return 0;
        }
        
        return totalProgress / totalActions;
    }
} 