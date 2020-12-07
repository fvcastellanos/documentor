package net.cavitos.documentor.web.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadDocumentController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadDocumentController.class);

    @PostMapping("/documents/upload")
    public ResponseEntity<String> uploadDocument(@Valid @NotNull @RequestParam("files") List<MultipartFile> files) throws Exception {

        LOGGER.info("file: {}", files.size());

        return new ResponseEntity<>("file", HttpStatus.OK);
    }
}
