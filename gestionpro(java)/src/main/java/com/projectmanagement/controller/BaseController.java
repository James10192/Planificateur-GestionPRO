package com.projectmanagement.controller;

import com.projectmanagement.dto.BaseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Base controller interface providing common CRUD operations for all entities.
 *
 * @param <D> The DTO type
 * @param <ID> The ID type
 */
public interface BaseController<D extends BaseDTO, ID> {

    /**
     * Get all entities.
     *
     * @return list of all entities
     */
    @GetMapping
    ResponseEntity<List<D>> getAll();

    /**
     * Get an entity by ID.
     *
     * @param id the entity ID
     * @return the entity
     */
    @GetMapping("/{id}")
    ResponseEntity<D> getById(@PathVariable ID id);

    /**
     * Create a new entity.
     *
     * @param dto the entity to create
     * @return the created entity
     */
    @PostMapping
    ResponseEntity<D> create(@RequestBody D dto);

    /**
     * Update an entity.
     *
     * @param id the entity ID
     * @param dto the updated entity
     * @return the updated entity
     */
    @PutMapping("/{id}")
    ResponseEntity<D> update(@PathVariable ID id, @RequestBody D dto);

    /**
     * Delete an entity.
     *
     * @param id the entity ID
     * @return no content response
     */
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable ID id);
} 