package com.projectmanagement.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing team roles (pequipro table).
 */
@Entity
@Table(name = "pequipro")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class TeamRole extends BaseEntity {
    
    @Column(name = "lib", nullable = false, length = 100)
    private String name;
    
    @OneToMany(mappedBy = "role")
    @Builder.Default
    private List<TeamMember> teamMembers = new ArrayList<>();
} 