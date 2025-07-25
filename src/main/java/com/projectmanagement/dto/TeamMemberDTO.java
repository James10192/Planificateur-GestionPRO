package com.projectmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * DTO for team members.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class TeamMemberDTO extends BaseDTO {
    
    private Long teamId;
    private String teamName;
    private Long userId;
    private String userName;
    private Long roleId;
    private String roleName;
} 