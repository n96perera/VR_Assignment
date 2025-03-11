package com.vr.vr_assignment.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class S3ServiceTest {

    @Mock
    private S3Client s3Client;

    @InjectMocks
    private S3Service s3Service;

    private final String bucketName = "test-bucket";
    private final String fileName = "test.json";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(s3Service, "bucketName", bucketName);
    }

    @Test
    void testGetFileContent_Success() {
        String fileContent = "{\"key\":\"value\"}";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes());
        ResponseInputStream<GetObjectResponse> response =
                new ResponseInputStream<>(GetObjectResponse.builder().build(), inputStream);

        when(s3Client.getObject(any(GetObjectRequest.class))).thenReturn(response);

        String result = s3Service.getFileContent(fileName);
        assertEquals(fileContent, result);
        verify(s3Client).getObject(any(GetObjectRequest.class));
    }

    @Test
    void testGetFileContent_FileNotFound() {
        when(s3Client.getObject(any(GetObjectRequest.class)))
                .thenThrow(NoSuchKeyException.builder().build());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> s3Service.getFileContent(fileName));
        assertTrue(exception.getMessage().contains("S3 file not found"));
    }

    @Test
    void testGetFileContent_BucketNotFound() {
        when(s3Client.getObject(any(GetObjectRequest.class)))
                .thenThrow(NoSuchBucketException.builder().build());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> s3Service.getFileContent(fileName));
        assertTrue(exception.getMessage().contains("S3 bucket not found"));
    }

    @Test
    void testGetFileContent_EmptyFileName() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> s3Service.getFileContent(""));
        assertEquals("S3 Key cannot be null or empty", exception.getMessage());
    }

    @Test
    void testGetFileContent_GeneralException() {
        when(s3Client.getObject(any(GetObjectRequest.class))).thenThrow(new RuntimeException("Some AWS error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> s3Service.getFileContent(fileName));
        assertTrue(exception.getMessage().contains("Failed to read S3 file"));
    }
}
