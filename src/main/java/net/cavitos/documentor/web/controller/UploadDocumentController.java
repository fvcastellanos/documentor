package net.cavitos.documentor.web.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import net.cavitos.documentor.domain.model.ImageDocument;
import net.cavitos.documentor.service.UploadService;

@RestController
@RequestMapping("/documents")
public class UploadDocumentController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadDocumentController.class);

    @Autowired
    private UploadService uploadService;

    @PostMapping("/{tenantId}/uploads")
    public ResponseEntity<ImageDocument> uploadDocument(@PathVariable("tenantId") String tenantId,
                                                 @Valid @NotNull @Size(max = 150) @RequestParam String documentId,
                                                 @Valid @NotNull @RequestParam("files") List<MultipartFile> files) {

        LOGGER.info("files to be uploaded: {} for tenantId: {}", files.size(), tenantId);

        final var result = uploadService.storeDocument(tenantId, documentId, files);
        return new ResponseEntity<ImageDocument>(result, HttpStatus.CREATED);                                        
    }

    @DeleteMapping("/{tenantId}/uploads/{uploadId}")
    public ResponseEntity deleteDocument(@PathVariable("tenantId") String tenantId, 
                                         @PathVariable("uploadId") String uploadId) {

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
