package com.matheus.springwebfluxdynamodbpoc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

import java.net.URI;

@Component
public class DynamoDbConfig {

    @Bean
    public DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient() {
        DynamoDbAsyncClient dynamoDbAsyncClient = DynamoDbAsyncClient.builder()
                .endpointOverride(URI.create("http://localhost:4566"))
                .region(Region.US_EAST_1)
                .credentialsProvider(() -> AwsBasicCredentials.create("localstack", "localstack"))
                .build();
        return DynamoDbEnhancedAsyncClient.builder()
                .dynamoDbClient(dynamoDbAsyncClient)
                .build();
    }
}
