package com.projectmanagement.repository;

import com.projectmanagement.entity.Planning;
import com.projectmanagement.entity.Project;
import com.projectmanagement.entity.PortfolioPhase;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for plannings.
 */
@Repository
public interface PlanningRepository extends BaseRepository<Planning, Long> {
    /**
     * Find all plannings by project.
     * 
     * @param project the project to search for
     * @return the list of plannings
     */
    List<Planning> findByProject(Project project);
    
    /**
     * Find all active plannings by project.
     * 
     * @param project the project to search for
     * @return the list of plannings
     */
    List<Planning> findByProjectAndActifTrue(Project project);
    
    /**
     * Find all plannings by phase.
     * 
     * @param phase the phase to search for
     * @return the list of plannings
     */
    List<Planning> findByPhase(PortfolioPhase phase);
    
    /**
     * Find a planning by project and phase.
     * 
     * @param project the project to search for
     * @param phase the phase to search for
     * @return the planning if found, empty otherwise
     */
    Optional<Planning> findByProjectAndPhase(Project project, PortfolioPhase phase);
} 