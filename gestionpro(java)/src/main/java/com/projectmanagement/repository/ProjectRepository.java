package com.projectmanagement.repository;

import com.projectmanagement.entity.Project;
import com.projectmanagement.entity.Direction;
import com.projectmanagement.entity.Status;
import com.projectmanagement.entity.ProjectType;
import com.projectmanagement.entity.Priority;
import com.projectmanagement.entity.ProjectTeam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository for projects.
 */
@Repository
public interface ProjectRepository extends BaseRepository<Project, Long> {
    /**
     * Find all active projects by status.
     * 
     * @param status the status to search for
     * @param pageable the pagination information
     * @return a page of projects
     */
    Page<Project> findByStatusAndActifTrue(Status status, Pageable pageable);
    
    /**
     * Find all active projects by direction.
     * 
     * @param direction the direction to search for
     * @param pageable the pagination information
     * @return a page of projects
     */
    Page<Project> findByDirectionAndActifTrue(Direction direction, Pageable pageable);
    
    /**
     * Find all active projects by type.
     * 
     * @param type the type to search for
     * @param pageable the pagination information
     * @return a page of projects
     */
    Page<Project> findByTypeAndActifTrue(ProjectType type, Pageable pageable);
    
    /**
     * Find all active projects by priority.
     * 
     * @param priority the priority to search for
     * @param pageable the pagination information
     * @return a page of projects
     */
    Page<Project> findByPriorityAndActifTrue(Priority priority, Pageable pageable);
    
    /**
     * Find all active projects by team.
     * 
     * @param team the team to search for
     * @param pageable the pagination information
     * @return a page of projects
     */
    Page<Project> findByTeamAndActifTrue(ProjectTeam team, Pageable pageable);
    
    /**
     * Find all active projects that start after a date.
     * 
     * @param date the date to compare with
     * @param pageable the pagination information
     * @return a page of projects
     */
    Page<Project> findByStartDateGreaterThanEqualAndActifTrue(LocalDate date, Pageable pageable);
    
    /**
     * Find all active projects that end before a date.
     * 
     * @param date the date to compare with
     * @param pageable the pagination information
     * @return a page of projects
     */
    Page<Project> findByPlannedEndDateLessThanEqualAndActifTrue(LocalDate date, Pageable pageable);
    
    /**
     * Find all active projects by name (containing the search term).
     * 
     * @param name the name to search for
     * @param pageable the pagination information
     * @return a page of projects
     */
    Page<Project> findByNameContainingIgnoreCaseAndActifTrue(String name, Pageable pageable);
    
    /**
     * Find all active projects with a custom query.
     * 
     * @param name the name to search for
     * @param statusId the status ID to search for
     * @param directionId the direction ID to search for
     * @param typeId the type ID to search for
     * @param priorityId the priority ID to search for
     * @param startDate the start date to search for
     * @param endDate the end date to search for
     * @param pageable the pagination information
     * @return a page of projects
     */
    @Query("SELECT p FROM Project p WHERE " +
           "(:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:statusId IS NULL OR p.status.id = :statusId) AND " +
           "(:directionId IS NULL OR p.direction.id = :directionId) AND " +
           "(:typeId IS NULL OR p.type.id = :typeId) AND " +
           "(:priorityId IS NULL OR p.priority.id = :priorityId) AND " +
           "(:startDate IS NULL OR p.startDate >= :startDate) AND " +
           "(:endDate IS NULL OR p.plannedEndDate <= :endDate) AND " +
           "p.actif = true")
    Page<Project> searchProjects(
            @Param("name") String name,
            @Param("statusId") Long statusId,
            @Param("directionId") Long directionId,
            @Param("typeId") Long typeId,
            @Param("priorityId") Long priorityId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable);
} 