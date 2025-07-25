package com.projectmanagement.repository;

import com.projectmanagement.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for users.
 */
@Repository
public interface UserRepository extends BaseRepository<User, Long> {
    /**
     * Find a user by email.
     * 
     * @param email the email to search for
     * @return the user if found, empty otherwise
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Find a user by email and active status.
     * 
     * @param email the email to search for
     * @param actif the active status
     * @return the user if found, empty otherwise
     */
    Optional<User> findByEmailAndActif(String email, Boolean actif);
    
    /**
     * Check if a user exists with the given email.
     * 
     * @param email the email to check
     * @return true if a user exists with the email, false otherwise
     */
    boolean existsByEmail(String email);
} 