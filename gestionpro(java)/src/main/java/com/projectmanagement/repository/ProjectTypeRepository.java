package com.projectmanagement.repository;

import com.projectmanagement.entity.ProjectType;
import org.springframework.stereotype.Repository;

/**
 * Repository for project types.
 */
@Repository
public interface ProjectTypeRepository extends BaseRepository<ProjectType, Long> {
    // Custom methods can be added here
} 