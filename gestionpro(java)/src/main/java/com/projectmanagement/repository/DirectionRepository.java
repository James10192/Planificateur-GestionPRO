package com.projectmanagement.repository;

import com.projectmanagement.entity.Direction;
import org.springframework.stereotype.Repository;

/**
 * Repository for directions/centers.
 */
@Repository
public interface DirectionRepository extends BaseRepository<Direction, Long> {
    // Custom methods can be added here
} 