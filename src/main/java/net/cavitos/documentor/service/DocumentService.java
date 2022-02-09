package net.cavitos.documentor.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    public ImageDocument addDocument(final String tenantId, final DocumentRequest documentRequest) {
        
        final var name = documentRequest.getName();
        LOGGER.info("Add new document with name: {} for tenantId: {}", name, tenantId);

        verifyTenantIdExists(tenantId);

        final var documentHolder = documentRepository.findByTenantIdAndName(tenantId, name);

        if (documentHolder.isPresent()) {

            final var exception = BusinessExceptionBuilder.unprocessableException("A document with name: %s already exists for tenantId: %s", 
                name, tenantId);

            LOGGER.error("A document_name={} already exists for tenantId={} - ", name, tenantId);

            throw exception;
        }

        return documentRepository.save(DocumentTransformer.toModel(tenantId, documentRequest));
    }

    public ImageDocument updateDocument(final String tenantId, final DocumentRequest documentRequest) {

        verifyTenantIdExists(tenantId);

        return null;
    }
    
    // -------------------------------------------------------------------------------------

    private void verifyTenantIdExists(final String tenantId) {

        final var tenantHolder = tenantRepository.findByTenantId(tenantId);

        if (tenantHolder.isEmpty()) {

            var exception = BusinessExceptionBuilder.notFoundException("Tenant: %s was not found", tenantId);
            LOGGER.error("TenantId: {} wat not found - ", tenantId, exception);
            throw exception;
        }
    }
}
