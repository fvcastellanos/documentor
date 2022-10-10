package net.cavitos.documentor.web.controller;

import net.cavitos.documentor.domain.web.Document;
import net.cavitos.documentor.service.UploadService;
import net.cavitos.documentor.transformer.DocumentTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import net.cavitos.documentor.domain.exception.ValidationException;
import net.cavitos.documentor.domain.model.ImageDocument;
import net.cavitos.documentor.service.DocumentService;
import net.cavitos.documentor.web.model.request.DocumentRequest;
import net.cavitos.documentor.web.model.response.ResourceResponse;
import net.cavitos.documentor.web.validator.document.NewDocumentValidator;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.security.Principal;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/documents")
public class DocumentController extends BaseController {

    private final DocumentService documentService;
    private final UploadService uploadService;

    @Autowired
    public DocumentController(final DocumentService documentService,
                              final UploadService uploadService) {

        this.documentService = documentService;
        this.uploadService = uploadService;
    }

    @GetMapping
    public ResponseEntity<Page<Document>> search(@RequestParam(defaultValue = "0") final int page,
                                                 @RequestParam(defaultValue = "20") final int size,
                                                 @RequestParam(defaultValue = "") final String text,
                                                 final Principal principal) {

        final var tenant = getUserTenant(principal);
        final var imageDocumentPage = documentService.search(tenant, text, page, size);

        final var documents = imageDocumentPage.stream()
                .map(DocumentTransformer::toWeb)
                .collect(Collectors.toList());

        final var response = new PageImpl<>(documents, Pageable.ofSize(size),
                imageDocumentPage.getTotalElements());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Document> getById(@PathVariable("id") final String id,
                                            final Principal principal) {

        final var tenant = getUserTenant(principal);
        final var imageDocument = documentService.findById(tenant, id);

        final var response = DocumentTransformer.toWeb(imageDocument);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Document> addDocument(@RequestBody @Valid final Document document,
                                                final Principal principal) {

        final var tenant = getUserTenant(principal);

        final var imageDocument = documentService.addDocument(tenant, document);
        final var response = DocumentTransformer.toWeb(imageDocument);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Document> updateDocument(@PathVariable("id") @NotBlank final String id,
                                                   @RequestBody @Valid final Document document,
                                                   final Principal principal) {

        final var tenant = getUserTenant(principal);

        final var imageDocument = documentService.updateDocument(tenant, id, document);
        final var response = DocumentTransformer.toWeb(imageDocument);

        return  new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Document> deleteDocument(@PathVariable("id") @NotBlank final String id,
                                                   final Principal principal) {

        final var tenant = getUserTenant(principal);
//        uploadService.deleteUploadsForDocument(tenant, id);

        return null;
    }
}
