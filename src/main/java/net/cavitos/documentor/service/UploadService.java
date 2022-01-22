package net.cavitos.documentor.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.vavr.control.Either;
import io.vavr.control.Try;
import net.cavitos.documentor.client.ObjectStorageClient;
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

    public Either<String, ImageDocument> storeDocument(final String tenantId, 
                              final String documentId,
                              final List<MultipartFile> files) {

        try {

            var imageDocumentHolder = documentRepository.findById(documentId);

            if (imageDocumentHolder.isEmpty()) {

                var message = "No document: %s was found for tenant: %s";
                return Either.left(String.format(message, documentId, tenantId));
            }

            var uploads = new ArrayList<Upload>();

            for (final MultipartFile multipartFile : files) {
                
                final var documentHolder = objectStorageClient.storeDocument(multipartFile, tenantId);
    
                if (documentHolder.isPresent()) {
    
                    final var fileName = documentHolder.get();
    
                    final var upload = buildUpload(tenantId, fileName, multipartFile.getName(), true);
                    uploads.add(upload);

                    LOGGER.info("store document file: {} for tenantId: {}", multipartFile, tenantId);

                    break;
                }

                final var upload = buildUpload(tenantId, "", multipartFile.getName(), false);
                uploads.add(upload);
                LOGGER.warn("unable to store document file: {} for tenantId: {}", multipartFile, tenantId);
            }

            LOGGER.info("document: {} was stored for tenant: {}", documentId, tenantId);
            var storedDocument = documentRepository.save(imageDocumentHolder.get());

            return Either.right(storedDocument);

        } catch (Exception exception) {

            LOGGER.error("unable to store document: {} for tenant: {} - ", documentId, tenantId, exception);
            var message = "Unable to store document: %s for tenant: %s";
            return Either.left(String.format(message, documentId, tenantId));
        }
    }

    // ------------------------------------------------------------------------------------------

    private Upload buildUpload(final String tenantId, 
                               final String fileName, 
                               final String uploadName,
                               final boolean stored) {

        var upload = new Upload();
        upload.setStored(stored);
        upload.setTenantId(tenantId);
        upload.setFile(fileName);
        upload.setUploadName(uploadName);

        return upload;
    }
}
