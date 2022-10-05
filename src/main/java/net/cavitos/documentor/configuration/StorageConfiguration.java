package net.cavitos.documentor.configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import net.cavitos.documentor.client.ObjectStorageClient;
import net.cavitos.documentor.client.ObjectStorageS3Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfiguration {
    
    @Value("${documentor.storage.client-id}")
    private String clientId;

    @Value("${documentor.storage.client-secret}")
    private String clientSecret;

    @Value("${documentor.storage.endpoint}")
    private String endpoint;

    @Value("${documentor.storage.region}")
    private String region;

    @Value("${documentor.storage.base-directory:private/documentor/documents}")
    private String baseDirectory;

    @Value("${documentor.storage.bucket:object-cavitos}")
    private String bucket;

    @Value("${documentor.storage.subdomain.base.url:https://cdn.cavitos.net}")
    private String subdomainBaseUrl;

    @Bean
    public AmazonS3 spacesClient() {

        var endpointConfiguration = new EndpointConfiguration(endpoint, region);
        var credentials = new BasicAWSCredentials(clientId, clientSecret);

        return AmazonS3ClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .withEndpointConfiguration(endpointConfiguration)
            .build();           
    }

    @Bean
    public ObjectStorageClient objectStorageClient(AmazonS3 spacesClient) {

        return new ObjectStorageS3Client(spacesClient, baseDirectory, bucket, subdomainBaseUrl);
    }
}
