package net.cavitos.documentor.web.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import net.cavitos.documentor.domain.model.ImageDocument;
import net.cavitos.documentor.domain.model.Upload;
import net.cavitos.documentor.service.UploadService;

@RestController
@RequestMapping("/documents")
public class UploadDocumentController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadDocumentController.class);

    @Autowired
    private UploadService uploadService;

    @PostMapping("/{tenantId}/{documentId}/uploads")
    public ResponseEntity<ImageDocument> uploadDocument(@PathVariable("tenantId") String tenantId,
                                                 @Valid @NotNull @Size(max = 150) @PathVariable String documentId,
                                                 @Valid @NotNull @RequestParam("files") List<MultipartFile> files) {

        LOGGER.info("files to be uploaded: {} for tenantId: {}", files.size(), tenantId);

        final var result = uploadService.storeDocument(tenantId, documentId, files);
        return new ResponseEntity<>(result, HttpStatus.CREATED);                                        
    }

    @GetMapping("/{tenantId}/{documentId}/uploads")
    // public ResponseEntity<List<Upload>> getUploads(@PathVariable("tenantId") String tenantId, 
    //                                                @PathVariable("documentId") String documentId) {
    public ResponseEntity<Page<Upload>> getUploads(@PathVariable("tenantId") String tenantId, 
                                                   @PathVariable("documentId") String documentId,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size) {
 
        LOGGER.info("get uploads for tenant: {} - document: {}", tenantId, documentId);

        final var uploads = uploadService.getUploads(tenantId, documentId);

        var pageObject = new PageImpl<>(uploads, PageRequest.of(page, size), uploads.size());
        return new ResponseEntity<>(pageObject, HttpStatus.OK);

        // return new ResponseEntity<>(uploads, HttpStatus.OK);
    }

    @DeleteMapping("/{tenantId}/{documentId}/uploads/{uploadId}")
    public ResponseEntity<Void> deleteDocument(@PathVariable("tenantId") String tenantId, 
                                         @PathVariable("documentId") String documentId,
                                         @PathVariable("uploadId") String uploadId) {

        LOGGER.info("delete request for upload: {} received for document: {} - tenant: {}", uploadId, documentId, tenantId);

        uploadService.deleteUpload(tenantId, documentId, uploadId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
