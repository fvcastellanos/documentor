package net.cavitos.documentor.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

import static net.cavitos.documentor.web.controller.ControllerRoute.DOCUMENTS_ROUTE;

@RestController
@RequestMapping(DOCUMENTS_ROUTE)
public class DocumentController extends BaseController {
    
    @Autowired
    private DocumentService documentService;

    @Autowired
    private NewDocumentValidator newDocumentValidator;

    @GetMapping("/{tenantId}")
    public ResponseEntity<Page<ImageDocument>> getDocumentsByTenant(@PathVariable("tenantId") String tenantId,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "20") int size) {

        final var documents = documentService.findByTenantId(tenantId, page, size);
        return new ResponseEntity<>(documents, HttpStatus.OK);        
    }

    @PostMapping("/{tenantId}")
    public ResponseEntity<ResourceResponse<ImageDocument>> addDocument(@PathVariable("tenantId") final String tenantId,
                                                                       @RequestBody final DocumentRequest request) {

        var errors = buildErrorObject(request);
        newDocumentValidator.validate(request, errors);

        if (errors.hasErrors()) {

            throw new ValidationException(errors);
        }

        var storedDocument = documentService.addDocument(tenantId, request);
        var response = new ResourceResponse<>(storedDocument, buildSelf(tenantId, storedDocument.getId()));

        return new ResponseEntity<>(response, HttpStatus.OK);
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

    // ---------------------------------------------------------------------------------------------------------

    private String buildSelf(final String tenantId, final String self) {

        return new StringBuilder()
            .append(DOCUMENTS_ROUTE)
            .append("/")
            .append(tenantId)
            .append("/")
            .append(self)
            .toString();
    }
}
