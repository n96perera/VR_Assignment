package com.vr.vr_assignment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class S3Service {
    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    /**
     * Retrieve Json Content in the file uploaded to S3.
     * @param fileName Json File name.
     */
    public String getFileContent(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("S3 Key cannot be null or empty");
        }

        log.info("Fetching file from S3: bucket={}, key={}", bucketName, fileName);

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        try (InputStream inputStream = s3Client.getObject(getObjectRequest);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (software.amazon.awssdk.services.s3.model.NoSuchKeyException e) {
            log.error("S3 file not found: {}", fileName);
            throw new RuntimeException("S3 file not found: " + fileName, e);
        } catch (software.amazon.awssdk.services.s3.model.NoSuchBucketException e) {
            log.error("S3 bucket  not found: {}", bucketName);
            throw new RuntimeException("S3 bucket not found: " + bucketName, e);
        } catch (Exception e) {
            log.error("Error fetching file from S3: {}", e.getMessage());
            throw new RuntimeException("Failed to read S3 file: " + fileName, e);
        }

    }
}
