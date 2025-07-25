package com.projectmanagement.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * Entity representing audit logs (tbaulog table).
 */
@Entity
@Table(name = "tbaulog")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class AuditLog extends BaseEntity {
    
    @Column(name = "tbn", nullable = false, length = 30)
    private String tableName;
    
    @Column(name = "rid", nullable = false, length = 100)
    private String recordId;
    
    @Column(name = "opetyp", nullable = false, length = 6)
    private String operationType;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mp")
    private User modifiedBy;
    
    @Column(name = "dm")
    private LocalDateTime modificationDate;
    
    @Lob
    @Column(name = "details")
    private String details;
    
    @PrePersist
    public void prePersist() {
        if (modificationDate == null) {
            modificationDate = LocalDateTime.now();
        }
    }
    
    /**
     * Enum for the types of operations in the audit log.
     */
    public enum OperationType {
        INS("Insert"),
        UPD("Update"),
        DEL("Delete");
        
        private final String description;
        
        OperationType(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
} 