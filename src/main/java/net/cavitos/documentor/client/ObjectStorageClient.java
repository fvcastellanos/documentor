package net.cavitos.documentor.client;

import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

public interface ObjectStorageClient {

    Optional<String> storeDocument(MultipartFile multipartFile, String tenantId);
    void removeDocument(String fileName);
}
