package com.projectmanagement.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing statuses (psta table).
 */
@Entity
@Table(name = "psta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Status extends BaseEntity {
    
    @Column(name = "lib", nullable = false, length = 100)
    private String name;
    
    @OneToMany(mappedBy = "status")
    @Builder.Default
    private List<Project> projects = new ArrayList<>();
    
    @OneToMany(mappedBy = "status")
    @Builder.Default
    private List<Action> actions = new ArrayList<>();
    
    @OneToMany(mappedBy = "status")
    @Builder.Default
    private List<SubAction> subActions = new ArrayList<>();
    
    @OneToMany(mappedBy = "status")
    @Builder.Default
    private List<Document> documents = new ArrayList<>();
} 