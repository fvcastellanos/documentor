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
    
    @Value("#{environment.AWS_ACCESS_KEY_ID}")
    private String clientId;

    @Value("#{environment.AWS_SECRET_ACCESS_KEY}")
    private String clientSecret;

    @Value("#{environment.AWS_ENDPOINT}")
    private String endpoint;

    @Value("#{environment.AWS_REGION}")
    private String region;

    @Value("${net.cavitos.documentor.storage.baseDirectory:private/documentor/documents}")
    private String baseDirectory;

    @Value("${net.cavitos.documentor.storage.bucket:object-cavitos}")
    private String bucket;

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

        return new ObjectStorageS3Client(spacesClient, baseDirectory, bucket);
    }
}
