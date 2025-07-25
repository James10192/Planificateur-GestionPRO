package com.projectmanagement.service;

import com.projectmanagement.dto.UserDTO;
import com.projectmanagement.entity.User;

import java.util.Optional;

/**
 * Service interface for User management operations.
 */
public interface UserService extends BaseService<User, Long> {

    /**
     * Convert a User entity to a UserDTO.
     * 
     * @param user the entity to convert
     * @return the DTO representation of the entity
     */
    UserDTO toDTO(User user);
    
    /**
     * Convert a UserDTO to a User entity.
     * 
     * @param userDTO the DTO to convert
     * @return the entity representation of the DTO
     */
    User toEntity(UserDTO userDTO);
    
    /**
     * Find a user by email.
     * 
     * @param email the email to search for
     * @return the user DTO if found, empty otherwise
     */
    Optional<UserDTO> findByEmail(String email);
    
    /**
     * Find an active user by email.
     * 
     * @param email the email to search for
     * @return the user DTO if found, empty otherwise
     */
    Optional<UserDTO> findActiveByEmail(String email);
    
    /**
     * Check if a user exists with the given email.
     * 
     * @param email the email to check
     * @return true if a user exists with the email, false otherwise
     */
    boolean existsByEmail(String email);
    
    /**
     * Update a user's contact information.
     * 
     * @param userId the ID of the user to update
     * @param email the new email (optional, pass null to keep current)
     * @param phone the new phone (optional, pass null to keep current)
     * @return the updated user DTO
     */
    UserDTO updateContactInfo(Long userId, String email, String phone);
    
    /**
     * Update a user's direction and function.
     * 
     * @param userId the ID of the user to update
     * @param directionId the new direction ID (optional, pass null to keep current)
     * @param function the new function (optional, pass null to keep current)
     * @return the updated user DTO
     */
    UserDTO updateDirectionAndFunction(Long userId, Long directionId, String function);
} 