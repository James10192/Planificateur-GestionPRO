package com.projectmanagement.service;

import com.projectmanagement.entity.BaseEntity;
import com.projectmanagement.repository.BaseRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Base implementation of the BaseService interface.
 * This provides common CRUD functionality for all services.
 *
 * @param <T> The entity type
 * @param <ID> The type of the entity's primary key
 * @param <R> The repository type
 */
@Slf4j
@RequiredArgsConstructor
public abstract class BaseServiceImpl<T extends BaseEntity, ID, R extends BaseRepository<T, ID>> implements BaseService<T, ID> {

    protected final R repository;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public T save(T entity) {
        log.debug("Saving entity: {}", entity);
        entity.setActif(true);
        return repository.save(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public T update(ID id, T entity) {
        log.debug("Updating entity with id: {}", id);
        
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Entity not found with id: " + id);
        }
        
        // Set the ID on the entity to ensure we're updating the correct record
        entity.setId((Long) id);
        
        return repository.save(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<T> findById(ID id) {
        log.debug("Finding entity by id: {}", id);
        return repository.findById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<T> findAll() {
        log.debug("Finding all entities");
        return repository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<T> findAllActive() {
        log.debug("Finding all active entities");
        return repository.findAll().stream()
                .filter(BaseEntity::getActif)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteById(ID id) {
        log.debug("Deleting entity with id: {}", id);
        
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Entity not found with id: " + id);
        }
        
        repository.deleteById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public T deactivate(ID id) {
        log.debug("Deactivating entity with id: {}", id);
        
        T entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entity not found with id: " + id));
        
        entity.setActif(false);
        return repository.save(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public T reactivate(ID id) {
        log.debug("Reactivating entity with id: {}", id);
        
        T entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entity not found with id: " + id));
        
        entity.setActif(true);
        return repository.save(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public boolean existsById(ID id) {
        return repository.existsById(id);
    }
} 