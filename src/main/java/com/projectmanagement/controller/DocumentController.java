package com.projectmanagement.controller;

import com.projectmanagement.dto.DocumentDTO;
import com.projectmanagement.entity.Document;
import com.projectmanagement.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for document operations.
 */
@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;

    @Autowired
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    /**
     * Get all documents.
     *
     * @return list of all documents
     */
    @GetMapping
    public ResponseEntity<List<DocumentDTO>> getAll() {
        List<DocumentDTO> documents = documentService.findAll().stream()
                .map(documentService::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(documents);
    }

    /**
     * Get a document by ID.
     *
     * @param id the document ID
     * @return the document
     */
    @GetMapping("/{id}")
    public ResponseEntity<DocumentDTO> getById(@PathVariable Long id) {
        return documentService.findById(id)
                .map(documentService::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Create a new document.
     *
     * @param documentDTO the document to create
     * @return the created document
     */
    @PostMapping
    public ResponseEntity<DocumentDTO> create(@RequestBody DocumentDTO documentDTO) {
        Document document = documentService.toEntity(documentDTO);
        document = documentService.save(document);
        return ResponseEntity.ok(documentService.toDTO(document));
    }

    /**
     * Update a document.
     *
     * @param id the document ID
     * @param documentDTO the updated document
     * @return the updated document
     */
    @PutMapping("/{id}")
    public ResponseEntity<DocumentDTO> update(@PathVariable Long id, @RequestBody DocumentDTO documentDTO) {
        documentDTO.setId(id);
        Document document = documentService.toEntity(documentDTO);
        document = documentService.update(id, document);
        return ResponseEntity.ok(documentService.toDTO(document));
    }

    /**
     * Delete a document.
     *
     * @param id the document ID
     * @return no content response
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        documentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Find documents by project.
     *
     * @param projectId the project ID
     * @return list of documents for the given project
     */
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<DocumentDTO>> findByProject(@PathVariable Long projectId) {
        Pageable pageable = PageRequest.of(0, 100, Sort.by("uploadDate").descending());
        Page<DocumentDTO> page = documentService.findByProjectId(projectId, pageable);
        return ResponseEntity.ok(page.getContent());
    }

    /**
     * Find documents by status.
     *
     * @param statusId the status ID
     * @return list of documents with the given status
     */
    @GetMapping("/status/{statusId}")
    public ResponseEntity<List<DocumentDTO>> findByStatus(@PathVariable Long statusId) {
        return ResponseEntity.ok(documentService.findByStatusId(statusId));
    }

    /**
     * Find documents by uploader.
     *
     * @param userId the user ID
     * @return list of documents uploaded by the given user
     */
    @GetMapping("/uploader/{userId}")
    public ResponseEntity<List<DocumentDTO>> findByUploader(@PathVariable Long userId) {
        Pageable pageable = PageRequest.of(0, 100, Sort.by("uploadDate").descending());
        Page<DocumentDTO> page = documentService.findByUploadedById(userId, pageable);
        return ResponseEntity.ok(page.getContent());
    }

    /**
     * Search documents by title.
     *
     * @param query the search query
     * @return list of documents matching the search query
     */
    @GetMapping("/search")
    public ResponseEntity<List<DocumentDTO>> searchDocuments(@RequestParam String query) {
        Pageable pageable = PageRequest.of(0, 100, Sort.by("uploadDate").descending());
        Page<DocumentDTO> page = documentService.searchDocuments(query, null, null, null, null, pageable);
        return ResponseEntity.ok(page.getContent());
    }

    /**
     * Upload a new document.
     *
     * @param file the document file
     * @param title the document title
     * @param version the document version
     * @param projectId the project ID
     * @param statusId the status ID
     * @param uploaderId the uploader ID
     * @return the uploaded document
     */
    @PostMapping("/upload")
    public ResponseEntity<DocumentDTO> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("version") String version,
            @RequestParam("projectId") Long projectId,
            @RequestParam("statusId") Long statusId,
            @RequestParam("uploaderId") Long uploaderId) {
        
        // This is a mock implementation since the method is not defined in DocumentService
        DocumentDTO documentDTO = new DocumentDTO();
        documentDTO.setTitle(title);
        documentDTO.setVersion(version);
        documentDTO.setProjectId(projectId);
        documentDTO.setStatusId(statusId);
        documentDTO.setUploadedById(uploaderId);
        
        Document document = documentService.toEntity(documentDTO);
        document = documentService.save(document);
        
        return ResponseEntity.ok(documentService.toDTO(document));
    }

    /**
     * Download a document.
     *
     * @param id the document ID
     * @return the document file
     */
    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable Long id) {
        DocumentDTO documentDTO = documentService.findById(id)
                .map(documentService::toDTO)
                .orElseThrow(() -> new RuntimeException("Document not found"));
        
        // This is a mock implementation since the method is not defined in DocumentService
        byte[] documentContent = new byte[0]; // Service method should be implemented
        
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + documentDTO.getTitle() + "\"")
                .body(documentContent);
    }

    /**
     * Update a document's status.
     *
     * @param id the document ID
     * @param statusId the new status ID
     * @return the updated document
     */
    @PatchMapping("/{id}/status/{statusId}")
    public ResponseEntity<DocumentDTO> updateStatus(
            @PathVariable Long id,
            @PathVariable Long statusId) {
        
        // This is a mock implementation since the method is not defined in DocumentService
        return documentService.findById(id)
                .map(document -> {
                    // In a real implementation, you would set the status ID and save
                    document = documentService.update(id, document);
                    return ResponseEntity.ok(documentService.toDTO(document));
                })
                .orElse(ResponseEntity.notFound().build());
    }
} 