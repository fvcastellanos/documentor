package net.cavitos.documentor.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.cavitos.documentor.domain.model.ImageDocument;
import net.cavitos.documentor.repository.DocumentRepository;

@RestController
@RequestMapping("/documents")
public class DocumentController {
    
    @Autowired
    private DocumentRepository documentRepository;

    @RequestMapping("/{tenantId}")
    public ResponseEntity<Page<ImageDocument>> getDocumentsByTenant(@PathVariable("tenantId") String tenantId,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "20") int size) {

        final var pageable = PageRequest.of(page, size);

        final var documentPage = documentRepository.findByTenantId(tenantId, pageable);

        return new ResponseEntity<>(documentPage, HttpStatus.OK);        
    }

    // public ResponseEntity<
}
