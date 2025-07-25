package com.projectmanagement.repository;

import com.projectmanagement.entity.Status;
import org.springframework.stereotype.Repository;

/**
 * Repository for statuses.
 */
@Repository
public interface StatusRepository extends BaseRepository<Status, Long> {
    // Custom methods can be added here
} 