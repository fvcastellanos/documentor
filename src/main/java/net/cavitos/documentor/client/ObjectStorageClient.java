package net.cavitos.documentor.client;

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import net.cavitos.documentor.client.domain.StoredDocument;

public interface ObjectStorageClient {

    Optional<StoredDocument> storeDocument(MultipartFile multipartFile, String documentId, String tenantId);
    void removeDocument(String fileName);
    List<StoredDocument> getStoredDocuments(String documentId, String tenantId);
}
