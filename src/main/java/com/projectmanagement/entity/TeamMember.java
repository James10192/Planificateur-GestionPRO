package com.projectmanagement.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Entity representing team members (tbmem table).
 */
@Entity
@Table(name = "tbmem")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class TeamMember extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idequipro")
    private ProjectTeam team;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idutil")
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idrole")
    private TeamRole role;
    
    // Convenience methods
    
    public String getMemberName() {
        return user != null ? user.getFullName() : "";
    }
    
    public String getRoleName() {
        return role != null ? role.getName() : "";
    }
} 