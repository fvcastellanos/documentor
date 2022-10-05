package net.cavitos.documentor.service;

import net.cavitos.documentor.domain.web.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import net.cavitos.documentor.builder.BusinessExceptionBuilder;
import net.cavitos.documentor.domain.model.ImageDocument;
import net.cavitos.documentor.repository.DocumentRepository;
import net.cavitos.documentor.repository.TenantRepository;
import net.cavitos.documentor.transformer.DocumentTransformer;
import net.cavitos.documentor.web.model.request.DocumentRequest;

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
        return documentRepository.findByTenantIdAndText(tenant, text, pageable);
    }

    public Page<ImageDocument> findByTenantId(final String tenantId, final int page, final int size) {

        LOGGER.info("Find documents for tenantId: {}", tenantId);
        
        final var pageable = PageRequest.of(page, size);
        
        verifyTenantIdExists(tenantId);

        return documentRepository.findByTenantId(tenantId, pageable);
    }

    public Page<ImageDocument> findDocumentsByText(final String tenantId, 
                                                   final String text, 
                                                   final int page, 
                                                   final int size) {

        LOGGER.info("Search documents for tenantId: {} using text: {}", tenantId, text);
        
        final var pageable = PageRequest.of(page, size);

        verifyTenantIdExists(tenantId);

        return documentRepository.findByTenantIdAndText(tenantId, text, pageable);                                            
    }

    public ImageDocument findById(final String tenant, final String documentId) {

        LOGGER.info("get document: {} for tenant: {}", documentId, tenant);

        verifyTenantIdExists(tenant);

        return documentRepository.findById(documentId)
                .orElseThrow(() -> BusinessExceptionBuilder.notFoundException("Document not found"));
    }

    public ImageDocument addDocument(final String tenantId, final Document document) {
        
        final var name = document.getName();
        LOGGER.info("add new document with name: {} for tenantId: {}", name, tenantId);

        verifyTenantIdExists(tenantId);

        final var documentHolder = documentRepository.findByTenantIdAndName(tenantId, name);

        if (documentHolder.isPresent()) {

            final var exception = BusinessExceptionBuilder.unprocessableException("a document with name: %s already exists for tenantId: %s",
                name, tenantId);

            LOGGER.error("a document_name={} already exists for tenantId={} - ", name, tenantId);

            throw exception;
        }

        return documentRepository.save(DocumentTransformer.toModel(tenantId, document));
    }

    public ImageDocument updateDocument(final String tenantId, final DocumentRequest documentRequest) {

        verifyTenantIdExists(tenantId);

        return null;
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
