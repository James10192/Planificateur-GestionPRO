package com.projectmanagement.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Entity representing users (tbutil table).
 */
@Entity
@Table(name = "tbutil")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class User extends BaseEntity {
    
    @Column(name = "nom", length = 50)
    private String lastName;
    
    @Column(name = "prenom", length = 50)
    private String firstName;
    
    @Column(name = "email", length = 100, unique = true)
    private String email;
    
    @Column(name = "tel", length = 20)
    private String phone;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "iddir")
    private Direction direction;
    
    @Column(name = "fonc", length = 50)
    private String function;
    
    @OneToMany(mappedBy = "user")
    @Builder.Default
    private List<TeamMember> teamMemberships = new ArrayList<>();
    
    @OneToMany(mappedBy = "responsable")
    @Builder.Default
    private List<Action> responsibleActions = new ArrayList<>();
    
    @OneToMany(mappedBy = "uploadedBy")
    @Builder.Default
    private List<Document> uploadedDocuments = new ArrayList<>();
    
    @OneToMany(mappedBy = "modifiedBy")
    @Builder.Default
    private List<AuditLog> auditLogs = new ArrayList<>();
    
    // Convenience methods
    
    public String getFullName() {
        return firstName + " " + lastName;
    }
} 