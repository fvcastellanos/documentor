package net.cavitos.documentor.configuration;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "net.cavitos.documentor.repository")
public class MongoConfiguration {
    
    @Value("${documentor.connection:mongodb://root:r00t@cloud.cavitos.net:27017}")
    private String connectionString;

    @Value("${documentor.database:Documentor}")
    private String databaseName;

    @Bean
    public MongoClient mongoClient() {

        return MongoClients.create(connectionString);
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoClient mongoClient) {

        return new MongoTemplate(mongoClient, databaseName);
    }
}
