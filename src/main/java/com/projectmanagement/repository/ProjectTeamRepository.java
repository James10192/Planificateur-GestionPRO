package com.projectmanagement.repository;

import com.projectmanagement.entity.ProjectTeam;
import org.springframework.stereotype.Repository;

/**
 * Repository for project teams.
 */
@Repository
public interface ProjectTeamRepository extends BaseRepository<ProjectTeam, Long> {
    // Custom methods can be added here
} 