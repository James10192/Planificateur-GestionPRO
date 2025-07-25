package com.projectmanagement.repository;

import com.projectmanagement.entity.SubAction;
import com.projectmanagement.entity.Action;
import com.projectmanagement.entity.Status;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository for sub-actions.
 */
@Repository
public interface SubActionRepository extends BaseRepository<SubAction, Long> {
    /**
     * Find all sub-actions by parent action.
     * 
     * @param action the parent action to search for
     * @return the list of sub-actions
     */
    List<SubAction> findByAction(Action action);
    
    /**
     * Find all active sub-actions by parent action.
     * 
     * @param action the parent action to search for
     * @return the list of sub-actions
     */
    List<SubAction> findByActionAndActifTrue(Action action);
    
    /**
     * Find all sub-actions by status.
     * 
     * @param status the status to search for
     * @return the list of sub-actions
     */
    List<SubAction> findByStatus(Status status);
    
    /**
     * Find all sub-actions with a start date before a given date.
     * 
     * @param date the date to compare with
     * @return the list of sub-actions
     */
    List<SubAction> findByStartDateBefore(LocalDate date);
    
    /**
     * Find all sub-actions with a planned end date after a given date.
     * 
     * @param date the date to compare with
     * @return the list of sub-actions
     */
    List<SubAction> findByPlannedEndDateAfter(LocalDate date);
} 