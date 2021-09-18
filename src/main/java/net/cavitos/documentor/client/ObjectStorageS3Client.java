package net.cavitos.documentor.client;

import java.util.Optional;
import java.util.UUID;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public class ObjectStorageS3Client implements ObjectStorageClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(ObjectStorageS3Client.class);

    private final String baseDirectory;
    private final String bucket;

    private final AmazonS3 amazonS3;

    public ObjectStorageS3Client(AmazonS3 amazonS3, String baseDirectory, String bucket) {

        this.baseDirectory = baseDirectory;
        this.bucket = bucket;
        this.amazonS3 = amazonS3;
    }

    @Override
    public Optional<String> storeDocument(MultipartFile multipartFile, String tenantId) {

        try {

            LOGGER.info("upload file: {} for tenantId: {}", multipartFile.getOriginalFilename(), tenantId);

            var key = buildFileNane(tenantId, multipartFile);
            var objectMetadata = buildObjectMetadata(multipartFile);

            amazonS3.putObject(bucket, key, multipartFile.getInputStream(), objectMetadata);

            LOGGER.info("file: {} uploaded for tenantId: {}", key, tenantId);

            return Optional.of(key);
        } catch (Exception ex) {
            LOGGER.error("can't upload file: {} for tenantId: {} - ", multipartFile.getOriginalFilename(), tenantId, ex);
        }

        return Optional.empty();
    }

    @Override
    public void removeDocument(String fileName) {

        try {

            var key = baseDirectory + "/" + fileName;

            amazonS3.deleteObject(bucket, key);
        } catch (Exception ex) {
            LOGGER.error("can't remove file: {} - ", fileName, ex);
        }
    }

    // ------------------------------------------------------------------------------------------------------------------------

    private String buildFileNane(String tenantId, MultipartFile multipartFile) {

        var stringBuilder = new StringBuilder();
        stringBuilder.append(baseDirectory)
            .append("/")
            .append(tenantId)
            .append("/")
            .append(UUID.randomUUID().toString())
            .append(buildFileExtension(multipartFile.getOriginalFilename()));

        return stringBuilder.toString();
    }

    private String buildFileExtension(String fileName) {

        return fileName.substring(fileName.lastIndexOf("."));
    }

    private ObjectMetadata buildObjectMetadata(MultipartFile multipartFile) {

        var objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());

        return objectMetadata;
    }    
}
