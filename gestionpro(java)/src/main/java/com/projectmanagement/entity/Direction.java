package com.projectmanagement.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing directions/centers (pdir table).
 */
@Entity
@Table(name = "pdir")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Direction extends BaseEntity {
    
    @Column(name = "code", length = 10)
    private String code;
    
    @Column(name = "lib", nullable = false, length = 100)
    private String name;
    
    @OneToMany(mappedBy = "direction")
    @Builder.Default
    private List<Project> projects = new ArrayList<>();
    
    @OneToMany(mappedBy = "direction")
    @Builder.Default
    private List<User> users = new ArrayList<>();
} 