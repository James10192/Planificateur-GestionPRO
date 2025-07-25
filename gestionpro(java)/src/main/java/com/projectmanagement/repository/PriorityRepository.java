package com.projectmanagement.repository;

import com.projectmanagement.entity.Priority;
import org.springframework.stereotype.Repository;

/**
 * Repository for priorities.
 */
@Repository
public interface PriorityRepository extends BaseRepository<Priority, Long> {
    // Custom methods can be added here
} 