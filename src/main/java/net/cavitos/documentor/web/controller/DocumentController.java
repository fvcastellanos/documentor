package net.cavitos.documentor.web.controller;

import net.cavitos.documentor.domain.web.Document;
import net.cavitos.documentor.transformer.DocumentTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.cavitos.documentor.domain.exception.ValidationException;
import net.cavitos.documentor.domain.model.ImageDocument;
import net.cavitos.documentor.service.DocumentService;
import net.cavitos.documentor.web.model.request.DocumentRequest;
import net.cavitos.documentor.web.model.response.ResourceResponse;
import net.cavitos.documentor.web.validator.document.NewDocumentValidator;

import javax.validation.Valid;
import java.security.Principal;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/documents")
public class DocumentController extends BaseController {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private NewDocumentValidator newDocumentValidator;

//    @GetMapping
//    public ResponseEntity<Page<ImageDocument>> getDocumentsByTenant(@RequestParam(defaultValue = "0") final int page,
//                                                                    @RequestParam(defaultValue = "20") final int size,
//                                                                    final Principal principal) {
//
//        final var tenant = getUserTenant(principal);
//        final var documents = documentService.findByTenantId(tenant, page, size);
//        return new ResponseEntity<>(documents, HttpStatus.OK);
//    }

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

//    @PostMapping
//    public ResponseEntity<ResourceResponse<ImageDocument>> addDocument(@RequestBody @Valid final DocumentRequest request,
//                                                                       final Principal principal) {
//
//        final var tenant = getUserTenant(principal);
//        var errors = buildErrorObject(request);
//        newDocumentValidator.validate(request, errors);
//
//        if (errors.hasErrors()) {
//
//            throw new ValidationException(errors);
//        }
//
//        var storedDocument = documentService.addDocument(tenant, request);
//        var response = new ResourceResponse<>(storedDocument, buildSelf(tenant, storedDocument.getId()));
//
//        return new ResponseEntity<>(response, HttpStatus.OK);
//
//        return null;
//    }

    @PostMapping
    public ResponseEntity<Document> addDocument(@RequestBody @Valid final Document document,
                                                final Principal principal) {

        final var tenant = getUserTenant(principal);

        var imageDocument = documentService.addDocument(tenant, document);
        var response = DocumentTransformer.toWeb(imageDocument);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    public ResponseEntity<ResourceResponse<ImageDocument>> updateDocument(@PathVariable final String tenantId,
                                                                          @RequestBody final DocumentRequest request) {
        
        final var errors = buildErrorObject(request);
        newDocumentValidator.validate(request, errors);

        if (errors.hasErrors()) {

            throw new ValidationException(errors);
        }

        return null;

    }
}
