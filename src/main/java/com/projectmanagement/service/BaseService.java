package com.projectmanagement.service;

import java.util.List;
import java.util.Optional;

/**
 * Base service interface that provides common CRUD operations for entities.
 * 
 * @param <T> The entity type
 * @param <ID> The type of the entity's primary key
 */
public interface BaseService<T, ID> {

    /**
     * Save a new entity.
     *
     * @param entity The entity to save
     * @return The saved entity
     */
    T save(T entity);

    /**
     * Update an existing entity.
     *
     * @param id The ID of the entity to update
     * @param entity The entity with updated values
     * @return The updated entity
     */
    T update(ID id, T entity);

    /**
     * Find an entity by its ID.
     *
     * @param id The ID of the entity to find
     * @return An Optional containing the found entity, or empty if not found
     */
    Optional<T> findById(ID id);

    /**
     * Find all entities.
     *
     * @return A list of all entities
     */
    List<T> findAll();

    /**
     * Find all active entities.
     *
     * @return A list of all active entities
     */
    List<T> findAllActive();

    /**
     * Delete an entity by its ID.
     *
     * @param id The ID of the entity to delete
     */
    void deleteById(ID id);

    /**
     * Deactivate an entity (soft delete).
     *
     * @param id The ID of the entity to deactivate
     * @return The deactivated entity
     */
    T deactivate(ID id);

    /**
     * Reactivate an entity that was previously deactivated.
     *
     * @param id The ID of the entity to reactivate
     * @return The reactivated entity
     */
    T reactivate(ID id);

    /**
     * Check if an entity exists by its ID.
     *
     * @param id The ID to check
     * @return true if the entity exists, false otherwise
     */
    boolean existsById(ID id);
} 