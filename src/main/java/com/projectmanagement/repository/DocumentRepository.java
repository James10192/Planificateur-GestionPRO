package com.projectmanagement.repository;

import com.projectmanagement.entity.Document;
import com.projectmanagement.entity.Project;
import com.projectmanagement.entity.Status;
import com.projectmanagement.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for documents.
 */
@Repository
public interface DocumentRepository extends BaseRepository<Document, Long> {
    /**
     * Find all documents by project.
     * 
     * @param project the project to search for
     * @return the list of documents
     */
    List<Document> findByProject(Project project);
    
    /**
     * Find all active documents by project.
     * 
     * @param project the project to search for
     * @param pageable the pagination information
     * @return a page of documents
     */
    Page<Document> findByProjectAndActifTrue(Project project, Pageable pageable);
    
    /**
     * Find all documents by status.
     * 
     * @param status the status to search for
     * @return the list of documents
     */
    List<Document> findByStatus(Status status);
    
    /**
     * Find all documents uploaded by a user.
     * 
     * @param uploadedBy the user who uploaded the documents
     * @return the list of documents
     */
    List<Document> findByUploadedBy(User uploadedBy);
    
    /**
     * Find all active documents uploaded by a user.
     * 
     * @param uploadedBy the user who uploaded the documents
     * @param pageable the pagination information
     * @return a page of documents
     */
    Page<Document> findByUploadedByAndActifTrue(User uploadedBy, Pageable pageable);
    
    /**
     * Find all documents uploaded after a date.
     * 
     * @param uploadDate the date to compare with
     * @return the list of documents
     */
    List<Document> findByUploadDateAfter(LocalDateTime uploadDate);
    
    /**
     * Search documents with multiple criteria.
     * 
     * @param title the title to search for
     * @param version the version to search for
     * @param statusId the status ID to search for
     * @param projectId the project ID to search for
     * @param uploadedById the user ID who uploaded the documents
     * @param pageable the pagination information
     * @return a page of documents
     */
    @Query("SELECT d FROM Document d WHERE " +
           "(:title IS NULL OR LOWER(d.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
           "(:version IS NULL OR d.version = :version) AND " +
           "(:statusId IS NULL OR d.status.id = :statusId) AND " +
           "(:projectId IS NULL OR d.project.id = :projectId) AND " +
           "(:uploadedById IS NULL OR d.uploadedBy.id = :uploadedById) AND " +
           "d.actif = true")
    Page<Document> searchDocuments(
            @Param("title") String title,
            @Param("version") String version,
            @Param("statusId") Long statusId,
            @Param("projectId") Long projectId,
            @Param("uploadedById") Long uploadedById,
            Pageable pageable);
} 