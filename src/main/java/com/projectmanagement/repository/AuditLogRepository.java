package com.projectmanagement.repository;

import com.projectmanagement.entity.AuditLog;
import com.projectmanagement.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for audit logs.
 */
@Repository
public interface AuditLogRepository extends BaseRepository<AuditLog, Long> {
    /**
     * Find all audit logs by table name.
     * 
     * @param tableName the table name to search for
     * @return the list of audit logs
     */
    List<AuditLog> findByTableName(String tableName);
    
    /**
     * Find all audit logs by record ID.
     * 
     * @param recordId the record ID to search for
     * @return the list of audit logs
     */
    List<AuditLog> findByRecordId(String recordId);
    
    /**
     * Find all audit logs by operation type.
     * 
     * @param operationType the operation type to search for
     * @return the list of audit logs
     */
    List<AuditLog> findByOperationType(String operationType);
    
    /**
     * Find all audit logs by user.
     * 
     * @param modifiedBy the user to search for
     * @return the list of audit logs
     */
    List<AuditLog> findByModifiedBy(User modifiedBy);
    
    /**
     * Find all audit logs by modification date.
     * 
     * @param modificationDate the modification date to search for
     * @return the list of audit logs
     */
    List<AuditLog> findByModificationDate(LocalDateTime modificationDate);
    
    /**
     * Find all audit logs by modification date after a specific date.
     * 
     * @param modificationDate the modification date to compare with
     * @param pageable the pagination information
     * @return a page of audit logs
     */
    Page<AuditLog> findByModificationDateAfter(LocalDateTime modificationDate, Pageable pageable);
    
    /**
     * Find all audit logs by table name and record ID.
     * 
     * @param tableName the table name to search for
     * @param recordId the record ID to search for
     * @return the list of audit logs
     */
    List<AuditLog> findByTableNameAndRecordId(String tableName, String recordId);
    
    /**
     * Find all audit logs by table name, record ID, and operation type.
     * 
     * @param tableName the table name to search for
     * @param recordId the record ID to search for
     * @param operationType the operation type to search for
     * @return the list of audit logs
     */
    List<AuditLog> findByTableNameAndRecordIdAndOperationType(String tableName, String recordId, String operationType);
} 