package com.projectmanagement.service.impl;

import com.projectmanagement.dto.DocumentDTO;
import com.projectmanagement.entity.Document;
import com.projectmanagement.entity.Project;
import com.projectmanagement.entity.Status;
import com.projectmanagement.entity.User;
import com.projectmanagement.repository.DocumentRepository;
import com.projectmanagement.repository.ProjectRepository;
import com.projectmanagement.repository.StatusRepository;
import com.projectmanagement.repository.UserRepository;
import com.projectmanagement.service.BaseServiceImpl;
import com.projectmanagement.service.DocumentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the DocumentService interface.
 */
@Service
@Slf4j
public class DocumentServiceImpl extends BaseServiceImpl<Document, Long, DocumentRepository> implements DocumentService {

    private final ProjectRepository projectRepository;
    private final StatusRepository statusRepository;
    private final UserRepository userRepository;

    @Autowired
    public DocumentServiceImpl(
            DocumentRepository repository,
            ProjectRepository projectRepository,
            StatusRepository statusRepository,
            UserRepository userRepository) {
        super(repository);
        this.projectRepository = projectRepository;
        this.statusRepository = statusRepository;
        this.userRepository = userRepository;
    }

    @Override
    public DocumentDTO toDTO(Document document) {
        if (document == null) {
            return null;
        }

        DocumentDTO dto = DocumentDTO.builder()
                .title(document.getTitle())
                .version(document.getVersion())
                .path(document.getPath())
                .uploadDate(document.getUploadDate())
                .build();

        // Set the base fields
        dto.setId(document.getId());
        dto.setActif(document.getActif());
        dto.setDateCreation(document.getDateCreation());
        dto.setDateModification(document.getDateModification());

        // Set the reference fields with their IDs and names
        if (document.getProject() != null) {
            dto.setProjectId(document.getProject().getId());
            dto.setProjectName(document.getProject().getName());
        }

        if (document.getStatus() != null) {
            dto.setStatusId(document.getStatus().getId());
            dto.setStatusName(document.getStatus().getName());
        }

        if (document.getUploadedBy() != null) {
            dto.setUploadedById(document.getUploadedBy().getId());
            dto.setUploadedByName(document.getUploadedBy().getFullName());
        }

        return dto;
    }

    @Override
    public Document toEntity(DocumentDTO documentDTO) {
        if (documentDTO == null) {
            return null;
        }

        Document document = Document.builder()
                .title(documentDTO.getTitle())
                .version(documentDTO.getVersion())
                .path(documentDTO.getPath())
                .uploadDate(documentDTO.getUploadDate())
                .build();

        // Set the ID if it exists (for updates)
        if (documentDTO.getId() != null) {
            document.setId(documentDTO.getId());
        }

        // Set the reference entities if their IDs exist
        if (documentDTO.getProjectId() != null) {
            document.setProject(projectRepository.findById(documentDTO.getProjectId())
                    .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + documentDTO.getProjectId())));
        }

        if (documentDTO.getStatusId() != null) {
            document.setStatus(statusRepository.findById(documentDTO.getStatusId())
                    .orElseThrow(() -> new EntityNotFoundException("Status not found with id: " + documentDTO.getStatusId())));
        }

        if (documentDTO.getUploadedById() != null) {
            document.setUploadedBy(userRepository.findById(documentDTO.getUploadedById())
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + documentDTO.getUploadedById())));
        }

        return document;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DocumentDTO> findByProjectId(Long projectId, Pageable pageable) {
        log.debug("Finding documents by project ID: {}", projectId);
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + projectId));
        
        return repository.findByProjectAndActifTrue(project, pageable)
                .map(this::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentDTO> findByStatusId(Long statusId) {
        log.debug("Finding documents by status ID: {}", statusId);
        Status status = statusRepository.findById(statusId)
                .orElseThrow(() -> new EntityNotFoundException("Status not found with id: " + statusId));
        
        return repository.findByStatus(status).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DocumentDTO> findByUploadedById(Long userId, Pageable pageable) {
        log.debug("Finding documents by uploaded user ID: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        
        return repository.findByUploadedByAndActifTrue(user, pageable)
                .map(this::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentDTO> findByUploadDateAfter(LocalDateTime uploadDate) {
        log.debug("Finding documents uploaded after: {}", uploadDate);
        
        return repository.findByUploadDateAfter(uploadDate).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DocumentDTO> searchDocuments(
            String title,
            String version,
            Long statusId,
            Long projectId,
            Long uploadedById,
            Pageable pageable) {
        log.debug("Searching documents with criteria: title={}, version={}, statusId={}, projectId={}, uploadedById={}",
                title, version, statusId, projectId, uploadedById);
        
        return repository.searchDocuments(title, version, statusId, projectId, uploadedById, pageable)
                .map(this::toDTO);
    }
} 