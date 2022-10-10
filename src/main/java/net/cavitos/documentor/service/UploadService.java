package net.cavitos.documentor.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import net.cavitos.documentor.builder.BusinessExceptionBuilder;
import net.cavitos.documentor.client.ObjectStorageClient;
import net.cavitos.documentor.client.domain.StoredDocument;
import net.cavitos.documentor.domain.model.ImageDocument;
import net.cavitos.documentor.domain.model.Upload;
import net.cavitos.documentor.repository.DocumentRepository;

@Service
public class UploadService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UploadService.class);

    private final ObjectStorageClient objectStorageClient;
    private final DocumentRepository documentRepository;

    @Autowired
    public UploadService(final ObjectStorageClient objectStorageClient,
                         final DocumentRepository documentRepository) {

        this.objectStorageClient = objectStorageClient;
        this.documentRepository = documentRepository;
    }

    public ImageDocument storeDocument(final String tenantId, 
                              final String documentId,
                              final List<MultipartFile> files) {

        final var imageDocumentHolder = documentRepository.findById(documentId);

        if (imageDocumentHolder.isEmpty()) {

            throw BusinessExceptionBuilder.notFoundException("Document: %s was not found for tenant: %s", documentId, tenantId);
        }

        final var uploads = imageDocumentHolder.map(ImageDocument::getUploads)
            .orElse(Lists.newArrayList());

        for (final MultipartFile multipartFile : files) {

            final var storedDocumentHolder = objectStorageClient.storeDocument(multipartFile, tenantId);

            if (storedDocumentHolder.isPresent()) {
    
                var storedDocument = storedDocumentHolder.get();

                final var upload = buildUpload(tenantId, storedDocument, multipartFile.getOriginalFilename(), true);
                uploads.add(upload);

                LOGGER.info("store document file: {} for tenantId: {}", multipartFile, tenantId);

                break;
            }
        }

        LOGGER.info("document: {} was stored for tenant: {}", documentId, tenantId);
        
        var imageDocument = imageDocumentHolder.get();
        imageDocument.setUploads(uploads);
        documentRepository.save(imageDocument);

        LOGGER.info("document: {} was updated for tenant: {} for files: {}", documentId, tenantId, files);
        
        return imageDocument;
    }

    public void deleteUpload(String tenantId, String documentId, String uploadId) {

        LOGGER.info("about to delete upload: {} for document: {} - tenant: {}", uploadId, documentId, tenantId);

        final var imageDocumentHolder = documentRepository.findById(documentId);

        if (imageDocumentHolder.isEmpty()) {

            throw BusinessExceptionBuilder.notFoundException("Document: %s was not found for tenant: %s", documentId, tenantId);
        }

        LOGGER.info("retrieve upload list for document: {} - tenant: {}", documentId, tenantId);

        final var uploads = imageDocumentHolder.map(ImageDocument::getUploads)
            .orElse(Lists.newArrayList());

        final var uploadHolder = uploads.stream()
            .filter(upload -> upload.getId().equals(uploadId))
            .findFirst();

        uploadHolder.ifPresent(upload -> {

            LOGGER.info("upload: {} found for document: {} - tenant: {}", uploadId, documentId, tenantId);

            final var updatedUploads = uploads.stream()
                .filter(removeUpload -> !removeUpload.getId().equals(uploadId))
                .collect(Collectors.toList());

            var imageDocument = imageDocumentHolder.get();

            imageDocument.setUploads(updatedUploads);
            documentRepository.save(imageDocument);

            LOGGER.info("successfully deleted upload: {} found for document: {} - tenant: {}", uploadId, documentId, tenantId);
        });
    }

    public List<Upload> getUploads(String tenantId, String documentId) {

        final var imageDocumentHolder = documentRepository.findById(documentId);

        if (imageDocumentHolder.isEmpty()) {

            throw BusinessExceptionBuilder.notFoundException("Document: %s was not found for tenant: %s", documentId, tenantId);
        }

        return imageDocumentHolder.map(ImageDocument::getUploads)
            .orElse(Lists.newArrayList());
    }

    // ------------------------------------------------------------------------------------------

    private Upload buildUpload(final String tenantId, 
                               final StoredDocument storedDocument,
                               final String uploadName,
                               final boolean stored) {

        var upload = new Upload();

        upload.setId(UUID.randomUUID().toString());
        upload.setStored(stored);
        upload.setTenantId(tenantId);
        upload.setFileName(storedDocument.getFileName());
        upload.setUrl(storedDocument.getUrl());
        upload.setUploadName(uploadName);
        upload.setCreated(Instant.now());

        return upload;
    }
}
