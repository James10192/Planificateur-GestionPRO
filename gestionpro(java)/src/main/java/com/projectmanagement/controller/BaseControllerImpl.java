package com.projectmanagement.controller;

import com.projectmanagement.dto.BaseDTO;
import com.projectmanagement.service.BaseService;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Base controller implementation providing common CRUD operations for all entities.
 *
 * @param <D> The DTO type
 * @param <ID> The ID type
 * @param <S> The service type
 */
public abstract class BaseControllerImpl<D extends BaseDTO, ID, S extends BaseService<D, ID>> 
        implements BaseController<D, ID> {

    protected final S service;

    protected BaseControllerImpl(S service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<List<D>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Override
    public ResponseEntity<D> getById(ID id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<D> create(D dto) {
        return ResponseEntity.ok(service.save(dto));
    }

    @Override
    public ResponseEntity<D> update(ID id, D dto) {
        if (dto instanceof BaseDTO) {
            ((BaseDTO) dto).setId((Long) id);
        }
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Override
    public ResponseEntity<Void> delete(ID id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
} 