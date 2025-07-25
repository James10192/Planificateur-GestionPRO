package com.projectmanagement.repository;

import com.projectmanagement.entity.TeamRole;
import org.springframework.stereotype.Repository;

/**
 * Repository for team roles.
 */
@Repository
public interface TeamRoleRepository extends BaseRepository<TeamRole, Long> {
    // Custom methods can be added here
} 