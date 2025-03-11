package com.vr.vr_assignment.listener;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vr.vr_assignment.service.S3Service;
import com.vr.vr_assignment.service.MessageTemplateService;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class SqsMessageListener {
    private final S3Service s3Service;
    private final MessageTemplateService messageTemplateService;
    private final ObjectMapper objectMapper;

    /**
     * Processes messages receive from the SQS queue.
     * @param message The raw JSON message received from the SQS queue.
     */
    @SqsListener("${cloud.aws.sqs.queue-name}")
    public void processMessage(String message) {
        try {
            log.info("Received SQS message: {}", message);

            JsonNode jsonNode = objectMapper.readTree(message);
            JsonNode record = jsonNode.get("Records").get(0);

            String bucketName = record.get("s3").get("bucket").get("name").asText();
            String objectKey = record.get("s3").get("object").get("key").asText();

            log.info("Processing S3 file: bucket={}, key={}", bucketName, objectKey);

            String fileContent = s3Service.getFileContent(objectKey);
            log.info("File content retrieved: {}", fileContent);

            messageTemplateService.processAndStore(fileContent);
            log.info("Message template processed and stored successfully");

        } catch (Exception e) {
            log.error("Error processing SQS message", e);
        }
    }
}
