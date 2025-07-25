package com.projectmanagement.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

/**
 * Entity representing project budgets (tbprbudg table).
 */
@Entity
@Table(name = "tbprbudg")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ProjectBudget extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idpro")
    private Project project;
    
    @Column(name = "BI", precision = 18, scale = 2)
    private BigDecimal initialBudget;
    
    @Column(name = "BC", precision = 18, scale = 2)
    private BigDecimal consumedBudget;
    
    /**
     * Calculates the remaining budget.
     * @return the remaining budget
     */
    public BigDecimal getRemainingBudget() {
        if (initialBudget == null) {
            return BigDecimal.ZERO;
        }
        
        if (consumedBudget == null) {
            return initialBudget;
        }
        
        return initialBudget.subtract(consumedBudget);
    }
    
    /**
     * Calculates the budget consumption percentage.
     * @return the consumption percentage
     */
    public double getConsumptionPercentage() {
        if (initialBudget == null || initialBudget.compareTo(BigDecimal.ZERO) == 0) {
            return 0;
        }
        
        if (consumedBudget == null) {
            return 0;
        }
        
        return consumedBudget.divide(initialBudget, 4, BigDecimal.ROUND_HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .doubleValue();
    }
}