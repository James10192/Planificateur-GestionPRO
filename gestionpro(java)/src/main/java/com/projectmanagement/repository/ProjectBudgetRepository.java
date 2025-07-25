package com.projectmanagement.repository;

import com.projectmanagement.entity.ProjectBudget;
import com.projectmanagement.entity.Project;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Repository for project budgets.
 */
@Repository
public interface ProjectBudgetRepository extends BaseRepository<ProjectBudget, Long> {
    /**
     * Find all budgets by project.
     * 
     * @param project the project to search for
     * @return the list of budgets
     */
    List<ProjectBudget> findByProject(Project project);
    
    /**
     * Find all active budgets by project.
     * 
     * @param project the project to search for
     * @return the list of budgets
     */
    List<ProjectBudget> findByProjectAndActifTrue(Project project);
    
    /**
     * Find the most recent budget for a project.
     * 
     * @param project the project to search for
     * @return the most recent budget if found, empty otherwise
     */
    @Query("SELECT b FROM ProjectBudget b WHERE b.project = :project AND b.actif = true ORDER BY b.dateCreation DESC")
    Optional<ProjectBudget> findMostRecentByProject(@Param("project") Project project);
    
    /**
     * Find all budgets with an initial budget greater than a certain amount.
     * 
     * @param amount the amount to compare with
     * @return the list of budgets
     */
    List<ProjectBudget> findByInitialBudgetGreaterThan(BigDecimal amount);
    
    /**
     * Find all budgets with a consumed budget less than a certain amount.
     * 
     * @param amount the amount to compare with
     * @return the list of budgets
     */
    List<ProjectBudget> findByConsumedBudgetLessThan(BigDecimal amount);
    
    /**
     * Calculate the total initial budget for all active projects.
     * 
     * @return the total initial budget
     */
    @Query("SELECT SUM(b.initialBudget) FROM ProjectBudget b WHERE b.actif = true")
    BigDecimal calculateTotalInitialBudget();
    
    /**
     * Calculate the total consumed budget for all active projects.
     * 
     * @return the total consumed budget
     */
    @Query("SELECT SUM(b.consumedBudget) FROM ProjectBudget b WHERE b.actif = true")
    BigDecimal calculateTotalConsumedBudget();
} 