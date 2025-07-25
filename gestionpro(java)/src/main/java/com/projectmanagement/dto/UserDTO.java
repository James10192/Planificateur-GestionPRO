package com.projectmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * DTO for users.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UserDTO extends BaseDTO {
    
    private String lastName;
    private String firstName;
    private String email;
    private String phone;
    private Long directionId;
    private String directionName;
    private String function;
    
    // Convenience methods
    
    public String getFullName() {
        return firstName + " " + lastName;
    }
} 