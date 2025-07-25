package com.projectmanagement.repository;

import com.projectmanagement.entity.Action;
import com.projectmanagement.entity.Planning;
import com.projectmanagement.entity.Status;
import com.projectmanagement.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository for actions.
 */
@Repository
public interface ActionRepository extends BaseRepository<Action, Long> {
    /**
     * Find all actions by planning.
     * 
     * @param planning the planning to search for
     * @return the list of actions
     */
    List<Action> findByPlanning(Planning planning);
    
    /**
     * Find all active actions by planning.
     * 
     * @param planning the planning to search for
     * @return the list of actions
     */
    List<Action> findByPlanningAndActifTrue(Planning planning);
    
    /**
     * Find all actions by status.
     * 
     * @param status the status to search for
     * @return the list of actions
     */
    List<Action> findByStatus(Status status);
    
    /**
     * Find all actions assigned to a user.
     * 
     * @param responsable the user to search for
     * @return the list of actions
     */
    List<Action> findByResponsable(User responsable);
    
    /**
     * Find all active actions assigned to a user.
     * 
     * @param responsable the user to search for
     * @param pageable the pagination information
     * @return a page of actions
     */
    Page<Action> findByResponsableAndActifTrue(User responsable, Pageable pageable);
    
    /**
     * Find all active actions with a deadline approaching within X days.
     * 
     * @param currentDate the current date
     * @param daysThreshold the number of days to consider as approaching
     * @param pageable the pagination information
     * @return a page of actions
     */
    @Query("SELECT a FROM Action a WHERE " +
           "a.plannedEndDate IS NOT NULL AND " +
           "a.actualEndDate IS NULL AND " +
           "a.plannedEndDate BETWEEN :currentDate AND :currentDate + :daysThreshold AND " +
           "a.actif = true")
    Page<Action> findUpcomingDeadlines(
            @Param("currentDate") LocalDate currentDate,
            @Param("daysThreshold") Long daysThreshold,
            Pageable pageable);
    
    /**
     * Find all active overdue actions.
     * 
     * @param currentDate the current date
     * @param pageable the pagination information
     * @return a page of actions
     */
    @Query("SELECT a FROM Action a WHERE " +
           "a.plannedEndDate IS NOT NULL AND " +
           "a.actualEndDate IS NULL AND " +
           "a.plannedEndDate < :currentDate AND " +
           "a.actif = true")
    Page<Action> findOverdueActions(
            @Param("currentDate") LocalDate currentDate,
            Pageable pageable);
    
    /**
     * Search actions with multiple criteria.
     * 
     * @param name the name to search for
     * @param statusId the status ID to search for
     * @param responsableId the user ID to search for
     * @param startDate the start date to search for
     * @param endDate the end date to search for
     * @param planningId the planning ID to search for
     * @param pageable the pagination information
     * @return a page of actions
     */
    @Query("SELECT a FROM Action a WHERE " +
           "(:name IS NULL OR LOWER(a.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:statusId IS NULL OR a.status.id = :statusId) AND " +
           "(:responsableId IS NULL OR a.responsable.id = :responsableId) AND " +
           "(:startDate IS NULL OR a.startDate >= :startDate) AND " +
           "(:endDate IS NULL OR a.plannedEndDate <= :endDate) AND " +
           "(:planningId IS NULL OR a.planning.id = :planningId) AND " +
           "a.actif = true")
    Page<Action> searchActions(
            @Param("name") String name,
            @Param("statusId") Long statusId,
            @Param("responsableId") Long responsableId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("planningId") Long planningId,
            Pageable pageable);
} 