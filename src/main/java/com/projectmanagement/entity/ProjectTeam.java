package com.projectmanagement.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing project teams (tbequipro table).
 */
@Entity
@Table(name = "tbequipro")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ProjectTeam extends BaseEntity {
    
    @Column(name = "lib", nullable = false, length = 100)
    private String name;
    
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<TeamMember> members = new ArrayList<>();
    
    @OneToMany(mappedBy = "team")
    @Builder.Default
    private List<Project> projects = new ArrayList<>();
    
    // Helper methods
    
    public void addMember(TeamMember member) {
        members.add(member);
        member.setTeam(this);
    }
    
    public void removeMember(TeamMember member) {
        members.remove(member);
        member.setTeam(null);
    }
} 