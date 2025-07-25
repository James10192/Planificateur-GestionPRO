package com.projectmanagement.repository;

import com.projectmanagement.entity.TeamMember;
import com.projectmanagement.entity.ProjectTeam;
import com.projectmanagement.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for team members.
 */
@Repository
public interface TeamMemberRepository extends BaseRepository<TeamMember, Long> {
    /**
     * Find all team members by team.
     * 
     * @param team the team to search for
     * @return the list of team members
     */
    List<TeamMember> findByTeam(ProjectTeam team);
    
    /**
     * Find all team members by user.
     * 
     * @param user the user to search for
     * @return the list of team members
     */
    List<TeamMember> findByUser(User user);
    
    /**
     * Find a team member by team and user.
     * 
     * @param team the team to search for
     * @param user the user to search for
     * @return the team member if found, empty otherwise
     */
    Optional<TeamMember> findByTeamAndUser(ProjectTeam team, User user);
} 