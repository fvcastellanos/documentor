package net.cavitos.documentor.web.controller;

import net.cavitos.documentor.domain.web.Document;
import net.cavitos.documentor.domain.web.FileUpload;
import net.cavitos.documentor.security.service.UserService;
import net.cavitos.documentor.service.DocumentService;
import net.cavitos.documentor.service.UploadService;
import net.cavitos.documentor.transformer.DocumentTransformer;
import net.cavitos.documentor.transformer.FileUploadTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/documents")
public class DocumentController extends BaseController {

    private final DocumentService documentService;
    private final UploadService uploadService;

    @Autowired
    public DocumentController(final DocumentService documentService,
                              final UploadService uploadService,
                              final UserService userService) {

        super(userService);
        this.documentService = documentService;
        this.uploadService = uploadService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('user')")
    public ResponseEntity<Page<Document>> search(@RequestParam(defaultValue = "") @NotEmpty @Size(max = 50) final String text,
                                                 @RequestParam(defaultValue = "0") final int page,
                                                 @RequestParam(defaultValue = "20") final int size,
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
    @PreAuthorize("hasAuthority('user')")
    public ResponseEntity<Document> getById(@PathVariable @NotEmpty @Size(max = 50) final String id,
                                            final Principal principal) {

        final var tenant = getUserTenant(principal);
        final var imageDocument = documentService.findById(tenant, id);

        final var response = DocumentTransformer.toWeb(imageDocument);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('user')")
    public ResponseEntity<Document> addDocument(@RequestBody @Valid final Document document,
                                                final Principal principal) {

        final var tenant = getUserTenant(principal);

        final var imageDocument = documentService.addDocument(tenant, document);
        final var response = DocumentTransformer.toWeb(imageDocument);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('user')")
    public ResponseEntity<Document> updateDocument(@PathVariable @NotEmpty @Size(max = 50) final String id,
                                                   @RequestBody @Valid final Document document,
                                                   final Principal principal) {

        final var tenant = getUserTenant(principal);

        final var imageDocument = documentService.updateDocument(tenant, id, document);
        final var response = DocumentTransformer.toWeb(imageDocument);

        return  new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('user')")
    public ResponseEntity<Void> deleteDocument(@PathVariable @NotEmpty @Size(max = 50) final String id,
                                               final Principal principal) {

        final var tenant = getUserTenant(principal);
        documentService.deleteDocument(tenant, id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // --------------------------------------------------------------------------------------------------------

    @PostMapping("/{id}/uploads")
    @PreAuthorize("hasAuthority('user')")
    public ResponseEntity<Document> uploadDocument(@PathVariable @NotEmpty @Size(max = 50) final String id,
                                                        @RequestParam("files") @Valid @NotNull final List<MultipartFile> files,
                                                        final Principal principal) {

        final var tenant = getUserTenant(principal);

        final var imageDocument = uploadService.storeDocument(tenant, id, files);
        final var response = DocumentTransformer.toWeb(imageDocument);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}/uploads")
    @PreAuthorize("hasAuthority('user')")
    public ResponseEntity<Page<FileUpload>> getUploads(@PathVariable @NotEmpty @Size(max = 50) final String id,
                                                   final Principal principal) {

        final var tenant = getUserTenant(principal);

        final var uploads = uploadService.getUploads(tenant, id)
                .stream()
                .map(upload -> FileUploadTransformer.toWeb(id, upload))
                .collect(Collectors.toList());

        final var response = new PageImpl<>(uploads, Pageable.ofSize(50), uploads.size());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}/uploads/{uploadId}")
    @PreAuthorize("hasAuthority('user')")
    public ResponseEntity<FileUpload> getUploadById(@PathVariable @NotEmpty @Size(max = 50) final String id,
                                                    @PathVariable("uploadId") @NotEmpty @Size(max = 50) final String uploadId,
                                                    final Principal principal) {

        final var tenant = getUserTenant(principal);
        final var upload = uploadService.getUploadById(tenant, id, uploadId);

        final var response = FileUploadTransformer.toWeb(id, upload);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}/uploads/{uploadId}")
    @PreAuthorize("hasAuthority('user')")
    public ResponseEntity<Void> deleteDocument(@PathVariable @NotEmpty @Size(max = 50) final String id,
                                               @PathVariable("uploadId") @NotEmpty @Size(max = 50) final String uploadId,
                                               final Principal principal) {

        final var tenant = getUserTenant(principal);

        uploadService.deleteUpload(tenant, id, uploadId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
