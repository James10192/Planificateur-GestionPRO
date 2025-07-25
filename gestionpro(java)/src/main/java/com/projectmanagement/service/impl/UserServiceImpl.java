package com.projectmanagement.service.impl;

import com.projectmanagement.dto.UserDTO;
import com.projectmanagement.entity.Direction;
import com.projectmanagement.entity.User;
import com.projectmanagement.repository.DirectionRepository;
import com.projectmanagement.repository.UserRepository;
import com.projectmanagement.service.BaseServiceImpl;
import com.projectmanagement.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Implementation of the UserService interface.
 */
@Service
@Slf4j
public class UserServiceImpl extends BaseServiceImpl<User, Long, UserRepository> implements UserService {

    private final DirectionRepository directionRepository;

    @Autowired
    public UserServiceImpl(UserRepository repository, DirectionRepository directionRepository) {
        super(repository);
        this.directionRepository = directionRepository;
    }

    @Override
    public UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }

        UserDTO dto = UserDTO.builder()
                .lastName(user.getLastName())
                .firstName(user.getFirstName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .function(user.getFunction())
                .build();

        // Set the base fields
        dto.setId(user.getId());
        dto.setActif(user.getActif());
        dto.setDateCreation(user.getDateCreation());
        dto.setDateModification(user.getDateModification());

        // Set the reference fields with their IDs and names
        if (user.getDirection() != null) {
            dto.setDirectionId(user.getDirection().getId());
            dto.setDirectionName(user.getDirection().getName());
        }

        return dto;
    }

    @Override
    public User toEntity(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }

        User user = User.builder()
                .lastName(userDTO.getLastName())
                .firstName(userDTO.getFirstName())
                .email(userDTO.getEmail())
                .phone(userDTO.getPhone())
                .function(userDTO.getFunction())
                .build();

        // Set the ID if it exists (for updates)
        if (userDTO.getId() != null) {
            user.setId(userDTO.getId());
        }

        // Set the reference entities if their IDs exist
        if (userDTO.getDirectionId() != null) {
            user.setDirection(directionRepository.findById(userDTO.getDirectionId())
                    .orElseThrow(() -> new EntityNotFoundException("Direction not found with id: " + userDTO.getDirectionId())));
        }

        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDTO> findByEmail(String email) {
        log.debug("Finding user by email: {}", email);
        return repository.findByEmail(email)
                .map(this::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDTO> findActiveByEmail(String email) {
        log.debug("Finding active user by email: {}", email);
        return repository.findByEmailAndActif(email, true)
                .map(this::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        log.debug("Checking if user exists by email: {}", email);
        return repository.existsByEmail(email);
    }

    @Override
    @Transactional
    public UserDTO updateContactInfo(Long userId, String email, String phone) {
        log.debug("Updating contact info for user with ID: {}", userId);
        
        User user = repository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        
        // Only update fields that are not null
        if (email != null) {
            // Check if email is already in use by another user
            if (!email.equals(user.getEmail()) && repository.existsByEmail(email)) {
                throw new IllegalArgumentException("Email already in use: " + email);
            }
            user.setEmail(email);
        }
        
        if (phone != null) {
            user.setPhone(phone);
        }
        
        User updatedUser = repository.save(user);
        
        return toDTO(updatedUser);
    }

    @Override
    @Transactional
    public UserDTO updateDirectionAndFunction(Long userId, Long directionId, String function) {
        log.debug("Updating direction and function for user with ID: {}", userId);
        
        User user = repository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        
        // Only update fields that are not null
        if (directionId != null) {
            Direction direction = directionRepository.findById(directionId)
                    .orElseThrow(() -> new EntityNotFoundException("Direction not found with id: " + directionId));
            user.setDirection(direction);
        }
        
        if (function != null) {
            user.setFunction(function);
        }
        
        User updatedUser = repository.save(user);
        
        return toDTO(updatedUser);
    }
} 