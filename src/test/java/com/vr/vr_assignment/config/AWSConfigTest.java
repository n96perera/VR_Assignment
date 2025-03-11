package com.vr.vr_assignment.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.sqs.SqsClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AWSConfigTest {

    @InjectMocks
    private AwsConfig awsConfig;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(awsConfig, "awsRegion", "eu-west-1");
        ReflectionTestUtils.setField(awsConfig, "localstackUrl", "http://localhost:4566");
        ReflectionTestUtils.setField(awsConfig, "accessKey", "test");
        ReflectionTestUtils.setField(awsConfig, "secretKey", "test");
    }

    @Test
    void testSqsClient() {
        SqsClient sqsClient = awsConfig.sqsClient();
        assertNotNull(sqsClient);
    }

    @Test
    void testS3Client() {
        S3Client s3Client = awsConfig.s3Client();
        assertNotNull(s3Client);
    }

    @Test
    void testDynamoDbClient() {
        DynamoDbClient dynamoDbClient = awsConfig.dynamoDbClient();
        assertNotNull(dynamoDbClient);
    }

    @Test
    void testDynamoDbEnhancedClient() {
        DynamoDbClient dynamoDbClient = mock(DynamoDbClient.class);
        DynamoDbEnhancedClient dynamoDbEnhancedClient = awsConfig.dynamoDbEnhancedClient(dynamoDbClient);

        assertNotNull(dynamoDbEnhancedClient);
    }
}
