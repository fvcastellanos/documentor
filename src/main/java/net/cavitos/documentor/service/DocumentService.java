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
        
        final var tenantHolder = tenantRepository.findById(tenantId);

        if (tenantHolder.isEmpty()) {

            var exception = BusinessExceptionBuilder.notFoundException("Tenant: %s was not found", tenantId);
            LOGGER.error("TenantId: {} wat not found - ", tenantId, exception);
            throw exception;
        }

        return documentRepository.findByTenantId(tenantId, pageable);
    }

    public Page<ImageDocument> findDocumentsByTags(final String tenantId, 
                                                   final String text, 
                                                   final int page, 
                                                   final int size) {

        LOGGER.info("Search documents for tenantId: {} using text: {}", tenantId, text);
        
        final var pageable = PageRequest.of(page, size);

        final var tenantHolder = tenantRepository.findById(tenantId);

        if (tenantHolder.isEmpty()) {

            var exception = BusinessExceptionBuilder.notFoundException("Tenant: %s was not found", tenantId);
            LOGGER.error("TenantId: {} wat not found - ", tenantId, exception);
            throw exception;
        }

        return documentRepository.findByTenantIdAndText(tenantId, text, pageable);                                            
    }

    public ImageDocument addDocument(final ImageDocument imageDocument) {

        LOGGER.info("Add new document with name: {} for tenantId: {}", imageDocument.getName(), imageDocument.getTenantId());

        return documentRepository.save(imageDocument);
    }
    
}
