package com.projectmanagement.service;

import com.projectmanagement.dto.DocumentDTO;
import com.projectmanagement.entity.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service interface for Document management operations.
 */
public interface DocumentService extends BaseService<Document, Long> {

    /**
     * Convert a Document entity to a DocumentDTO.
     * 
     * @param document the entity to convert
     * @return the DTO representation of the entity
     */
    DocumentDTO toDTO(Document document);
    
    /**
     * Convert a DocumentDTO to a Document entity.
     * 
     * @param documentDTO the DTO to convert
     * @return the entity representation of the DTO
     */
    Document toEntity(DocumentDTO documentDTO);
    
    /**
     * Find documents by project ID.
     * 
     * @param projectId the project ID to search for
     * @param pageable the pagination information
     * @return a page of document DTOs
     */
    Page<DocumentDTO> findByProjectId(Long projectId, Pageable pageable);
    
    /**
     * Find documents by status ID.
     * 
     * @param statusId the status ID to search for
     * @return a list of document DTOs
     */
    List<DocumentDTO> findByStatusId(Long statusId);
    
    /**
     * Find documents uploaded by a user.
     * 
     * @param userId the user ID to search for
     * @param pageable the pagination information
     * @return a page of document DTOs
     */
    Page<DocumentDTO> findByUploadedById(Long userId, Pageable pageable);
    
    /**
     * Find documents uploaded after a date.
     * 
     * @param uploadDate the date to compare with
     * @return a list of document DTOs
     */
    List<DocumentDTO> findByUploadDateAfter(LocalDateTime uploadDate);
    
    /**
     * Search documents with multiple criteria.
     * 
     * @param title the title to search for
     * @param version the version to search for
     * @param statusId the status ID to search for
     * @param projectId the project ID to search for
     * @param uploadedById the user ID who uploaded the documents
     * @param pageable the pagination information
     * @return a page of document DTOs
     */
    Page<DocumentDTO> searchDocuments(
            String title,
            String version,
            Long statusId,
            Long projectId,
            Long uploadedById,
            Pageable pageable);
} 