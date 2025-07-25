package com.projectmanagement.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing project types (ptyp table).
 */
@Entity
@Table(name = "ptyp")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ProjectType extends BaseEntity {
    
    @Column(name = "lib", nullable = false, length = 100)
    private String name;
    
    @OneToMany(mappedBy = "type")
    @Builder.Default
    private List<Project> projects = new ArrayList<>();
} 