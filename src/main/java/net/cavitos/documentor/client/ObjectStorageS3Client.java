package net.cavitos.documentor.client;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

public class ObjectStorageS3Client implements ObjectStorageClient {

    @Override
    public String storeDocument(byte[] content) {

        final AmazonS3 s3 = AmazonS3ClientBuilder.standard()
            .withRegion(Regions.DEFAULT_REGION)
            .build();

        // TODO Auto-generated method stub
        return null;
    }
    
}
