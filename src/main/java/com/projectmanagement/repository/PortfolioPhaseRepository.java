package com.projectmanagement.repository;

import com.projectmanagement.entity.PortfolioPhase;
import org.springframework.stereotype.Repository;

/**
 * Repository for portfolio phases.
 */
@Repository
public interface PortfolioPhaseRepository extends BaseRepository<PortfolioPhase, Long> {
    // Custom methods can be added here
} 