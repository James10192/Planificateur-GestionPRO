package com.projectmanagement.repository;

import com.projectmanagement.entity.ActionDependency;
import com.projectmanagement.entity.Action;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for action dependencies.
 */
@Repository
public interface ActionDependencyRepository extends BaseRepository<ActionDependency, Long> {
    /**
     * Find all dependencies for an action.
     * 
     * @param action the action to search for
     * @return the list of dependencies
     */
    List<ActionDependency> findByAction(Action action);
    
    /**
     * Find all active dependencies for an action.
     * 
     * @param action the action to search for
     * @return the list of dependencies
     */
    List<ActionDependency> findByActionAndActifTrue(Action action);
    
    /**
     * Find all dependencies where an action is a prerequisite.
     * 
     * @param dependsOn the prerequisite action
     * @return the list of dependencies
     */
    List<ActionDependency> findByDependsOn(Action dependsOn);
    
    /**
     * Find all active dependencies where an action is a prerequisite.
     * 
     * @param dependsOn the prerequisite action
     * @return the list of dependencies
     */
    List<ActionDependency> findByDependsOnAndActifTrue(Action dependsOn);
    
    /**
     * Find a dependency between two actions.
     * 
     * @param action the action
     * @param dependsOn the prerequisite action
     * @return the list of dependencies
     */
    List<ActionDependency> findByActionAndDependsOn(Action action, Action dependsOn);
} 