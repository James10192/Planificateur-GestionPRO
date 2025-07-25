package com.projectmanagement.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity representing project documents (tbdoc table).
 */
@Entity
@Table(name = "tbdoc")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Document extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idpro")
    private Project project;
    
    @Column(name = "title", nullable = false, length = 200)
    private String title;
    
    @Column(name = "vers", nullable = false, length = 20)
    private String version;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idsta")
    private Status status;
    
    @Column(name = "path", length = 500)
    private String path;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ub")
    private User uploadedBy;
    
    @Column(name = "ud")
    private LocalDateTime uploadDate;
    
    @PrePersist
    public void prePersist() {
        if (uploadDate == null) {
            uploadDate = LocalDateTime.now();
        }
    }
} 