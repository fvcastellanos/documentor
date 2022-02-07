package net.cavitos.documentor.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.cavitos.documentor.domain.exception.ValidationException;
import net.cavitos.documentor.domain.model.ImageDocument;
import net.cavitos.documentor.domain.response.NewResourceResponse;
import net.cavitos.documentor.service.DocumentService;
import net.cavitos.documentor.web.validator.DocumentValidator;

@RestController
@RequestMapping("/documents")
public class DocumentController extends BaseController {
    
    @Autowired
    private DocumentService documentService;

    @Autowired
    private DocumentValidator documentValidator;

    @GetMapping("/{tenantId}")
    public ResponseEntity<Page<ImageDocument>> getDocumentsByTenant(@PathVariable("tenantId") String tenantId,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "20") int size) {

        final var documents = documentService.findByTenantId(tenantId, page, size);
        return new ResponseEntity<>(documents, HttpStatus.OK);        
    }

    @PostMapping("/{tenantId}")
    public ResponseEntity<NewResourceResponse<ImageDocument>> addDocument(@PathVariable("tenantId") String tenantId,
                                                                          @RequestBody ImageDocument imageDocument) {

        imageDocument.setTenantId(tenantId);
        
        var errors = buildErrorObject(imageDocument);
        documentValidator.validate(imageDocument, errors);

        if (errors.hasErrors()) {

            throw new ValidationException(errors);
        }

        var storedDocument = documentService.addDocument(imageDocument);
        var response = new NewResourceResponse<>(storedDocument, "/documents/" + tenantId + "/" + storedDocument.getId());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
