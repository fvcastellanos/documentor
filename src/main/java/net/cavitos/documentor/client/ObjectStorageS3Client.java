package net.cavitos.documentor.client;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;

import com.amazonaws.services.s3.model.PutObjectRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import net.cavitos.documentor.client.domain.StoredDocument;

public class ObjectStorageS3Client implements ObjectStorageClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(ObjectStorageS3Client.class);

    private final String baseDirectory;
    private final String bucket;
    private final String subdomainBaseUrl;

    private final AmazonS3 amazonS3;

    public ObjectStorageS3Client(final AmazonS3 amazonS3, 
                                 final String baseDirectory, 
                                 final String bucket,
                                 final String subdomainBaseUrl) {

        this.baseDirectory = baseDirectory;
        this.bucket = bucket;
        this.amazonS3 = amazonS3;
        this.subdomainBaseUrl = subdomainBaseUrl;
    }

    @Override
    public Optional<StoredDocument> storeDocument(final MultipartFile multipartFile, final String documentId, final String tenantId) {

        try {

            LOGGER.info("upload file: {} for tenantId: {}", multipartFile.getOriginalFilename(), tenantId);

            final var key = buildFileUrl(tenantId, documentId, multipartFile);
            final var objectMetadata = buildObjectMetadata(multipartFile);

            final var storedDocument = StoredDocument.builder()
                .fileName(key)
                .url(subdomainBaseUrl + "/" + key)
                .build();

            final var request = new PutObjectRequest(bucket, key, multipartFile.getInputStream(), objectMetadata);
            request.setCannedAcl(CannedAccessControlList.PublicRead);

            amazonS3.putObject(request);

            LOGGER.info("file: {} uploaded for tenantId: {}", key, tenantId);

            return Optional.of(storedDocument);
        } catch (final Exception ex) {

            LOGGER.error("can't upload file: {} for tenantId: {} - ", multipartFile.getOriginalFilename(), tenantId, ex);
            return Optional.empty();
        }
    }

    @Override
    public void removeDocument(final String fileName) {

        try {

            amazonS3.deleteObject(bucket, fileName);

        } catch (Exception ex) {

            LOGGER.error("can't remove file: {} - ", fileName, ex);
        }
    }

    @Override
    public List<StoredDocument> getStoredDocuments(final String documentId, final String tenantId) {

        final var objects = amazonS3.listObjects(bucket,  baseDirectory + "/" + tenantId + "/" + documentId);

        return objects.getObjectSummaries()
                .stream()
                .map(s3ObjectSummary -> StoredDocument.builder()
                        .fileName(s3ObjectSummary.getKey())
                        .url(subdomainBaseUrl + "/" + s3ObjectSummary.getKey())
                        .build()
                ).collect(Collectors.toList());
    }

    // ------------------------------------------------------------------------------------------------------------------------

    private String buildFileUrl(final String tenantId, final String documentId, final MultipartFile multipartFile) {

        final var fileName = Objects.requireNonNull(multipartFile.getOriginalFilename());

        return baseDirectory +
                "/" +
                tenantId +
                "/" +
                documentId +
                "/" +
                UUID.randomUUID() +
                buildFileExtension(fileName);
    }

    private String buildFileExtension(final String fileName) {

        return fileName.substring(fileName.lastIndexOf("."));
    }

    private ObjectMetadata buildObjectMetadata(final MultipartFile multipartFile) {

        final var objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());

        return objectMetadata;
    }    
}
