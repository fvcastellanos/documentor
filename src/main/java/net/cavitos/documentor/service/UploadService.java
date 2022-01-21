package net.cavitos.documentor.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

import net.cavitos.documentor.domain.Upload;
import net.cavitos.documentor.client.ObjectStorageClient;
import net.cavitos.documentor.repository.UploadRepository;

@Service
public class UploadService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UploadService.class);

    private final ObjectStorageClient objectStorageClient;
    private final UploadRepository uploadRepository;

    @Autowired
    public UploadService(final ObjectStorageClient objectStorageClient,
                         final UploadRepository uploadRepository) {

        this.objectStorageClient = objectStorageClient;
        this.uploadRepository = uploadRepository;
    }

    public void storeDocument(final String tenantId, final List<MultipartFile> files) {

        for (final MultipartFile multipartFile : files) {
            
            final var documentHolder = objectStorageClient.storeDocument(multipartFile, tenantId);

            if (documentHolder.isPresent()) {

                final var fileName = documentHolder.get();

                uploadRepository.save(buildUpload(tenantId, fileName, true));
                LOGGER.info("store document file: {} for tenantId: {}", multipartFile, tenantId);
                break;
            }
        }
    }

    // ------------------------------------------------------------------------------------------

    private Upload buildUpload(final String tenantId, 
                               final String fileName, 
                               final boolean stored) {

        var upload = new Upload();
        upload.setStored(stored);
        upload.setTenantId(tenantId);
        upload.setFile(fileName);

        return upload;
    }
}
