package net.cavitos.documentor.web.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import net.cavitos.documentor.domain.Upload;
import net.cavitos.documentor.repository.UploadRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import net.cavitos.documentor.client.ObjectStorageClient;

@RestController
public class UploadDocumentController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadDocumentController.class);

    @Autowired
    private UploadRepository uploadRepository;

    @Autowired
    private ObjectStorageClient objectStorageClient;

    @PostMapping("/documents/upload/{tenantId}")
    public ResponseEntity<String> uploadDocument(@PathVariable("tenantId") String tenantId,
                                                 @Valid @NotNull @RequestParam("files") List<MultipartFile> files) {

        LOGGER.info("files to be uploaded: {} for tenantId: {}", files.size(), tenantId);

        files.forEach(file -> objectStorageClient.storeDocument(file, tenantId)
                .ifPresent(fileName -> {
                        uploadRepository.save(buildUpload(tenantId, fileName));
                        LOGGER.info("store document file: {} for tenantId: {}", file, tenantId);
                }));

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    private Upload buildUpload(String tenantId, String fileName) {

        var upload = new Upload();
        upload.setStored(false);
        upload.setTenantId(tenantId);
        upload.setFile(fileName);

        return upload;
    }
}
