package net.cavitos.documentor.service;

import net.cavitos.documentor.builder.BusinessExceptionBuilder;
import net.cavitos.documentor.domain.model.ImageDocument;
import net.cavitos.documentor.domain.web.Document;
import net.cavitos.documentor.repository.DocumentRepository;
import net.cavitos.documentor.repository.TenantRepository;
import net.cavitos.documentor.transformer.DocumentTransformer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class DocumentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentService.class);

    private final DocumentRepository documentRepository;
    private final TenantRepository tenantRepository;

    @Autowired
    public DocumentService(final DocumentRepository documentRepository, 
                           final TenantRepository tenantRepository) {

        this.documentRepository = documentRepository;
        this.tenantRepository = tenantRepository;
    }

    public Page<ImageDocument> search(final String tenant, final String text, final int page, final int size) {

        LOGGER.info("find documents for tenant: {} and text: {}", tenant, text);
        verifyTenantIdExists(tenant);

        final var pageable = PageRequest.of(page, size);

        if (StringUtils.isBlank(text)) {

            return documentRepository.findByTenantId(tenant, pageable);
        }

        return documentRepository.findByTenantIdAndText(tenant, text, pageable);
    }

    public ImageDocument findById(final String tenant, final String documentId) {

        LOGGER.info("get document: {} for tenant: {}", documentId, tenant);

        final var imageDocument = documentRepository.findById(documentId)
                .orElseThrow(() -> BusinessExceptionBuilder.notFoundException("Document not found"));

        if (!imageDocument.getTenantId().equalsIgnoreCase(tenant)) {

            throw BusinessExceptionBuilder.notFoundException("Document not found");
        }

        return imageDocument;
    }

    public ImageDocument addDocument(final String tenantId, final Document document) {
        
        final var name = document.getName();
        LOGGER.info("add new document with name: {} for tenantId: {}", name, tenantId);

        final var documentHolder = documentRepository.findByTenantIdAndName(tenantId, name);

        if (documentHolder.isPresent()) {

            final var exception = BusinessExceptionBuilder.unprocessableException("a document with name: %s already exists for tenantId: %s",
                name, tenantId);

            LOGGER.error("a document_name={} already exists for tenantId={} - ", name, tenantId);

            throw exception;
        }

        return documentRepository.save(DocumentTransformer.toModel(tenantId, document));
    }

    public ImageDocument updateDocument(final String tenantId,
                                        final String id,
                                        final Document document) {

        LOGGER.info("update document for tenant: {} and document_name: {}", tenantId, document.getName());

        final var imageDocument = findById(tenantId, id);

        documentRepository.findByTenantIdAndName(tenantId, document.getName())
                        .ifPresent(existingDocument -> {

                            if (!existingDocument.getId().equalsIgnoreCase(imageDocument.getId())) {

                                throw BusinessExceptionBuilder.unprocessableException("Another document with same name already exists");
                            }
                        });

        imageDocument.setName(document.getName());
        imageDocument.setDescription(document.getDescription());
        imageDocument.setTags(document.getTags());

        return documentRepository.save(imageDocument);
    }

    public void deleteDocument(final String tenantId,
                               final String id) {

        final var imageDocumentHolder = documentRepository.findByTenantIdAndId(tenantId, id);

        if (imageDocumentHolder.isPresent()) {

            final var imageDocument = imageDocumentHolder.get();


        }
    }
    
    // -------------------------------------------------------------------------------------

    private void verifyTenantIdExists(final String tenantId) {

        final var tenantHolder = tenantRepository.findByTenantId(tenantId);

        if (tenantHolder.isEmpty()) {

            var exception = BusinessExceptionBuilder.notFoundException("tenant: %s was not found", tenantId);
            LOGGER.error("tenantId: {} wat not found - ", tenantId, exception);
            throw exception;
        }
    }
}
