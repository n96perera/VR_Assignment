package com.vr.vr_assignment.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vr.vr_assignment.model.CMSMessageTemplate;
import com.vr.vr_assignment.model.MessageTemplate;
import com.vr.vr_assignment.repository.MessageTemplateRepository;
import com.vr.vr_assignment.util.CMSMessageMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageTemplateService {

    private final MessageTemplateRepository messageTemplateRepository;

    /**
     * Parse JSON to CMS Template and save in DB.
     * @param jsonContent The raw JSON content received from the SQS queue.
     */
    public void processAndStore(String jsonContent) {
        try {
            log.info("Processing incoming message template...");

            // Parse CMS JSON using mapper
            CMSMessageTemplate cmsTemplate = CMSMessageMapper.mapJsonToCMSMessageTemplate(jsonContent);

            // map data into messageTemplate model
            MessageTemplate messageTemplate = new MessageTemplate(cmsTemplate);

            // Check for existing entry
            Optional<MessageTemplate> existingTemplate = messageTemplateRepository.findById(messageTemplate.getPk(),messageTemplate.getTrafficType());
            if (existingTemplate.isPresent()) {
                log.info(" Updating Message template with ID: {}.", messageTemplate.getPk());
                messageTemplateRepository.update(messageTemplate);
                return;
            }

            log.debug("Saving MessageTemplate to DynamoDB: PK={}, SK={}, Content={}",
                    messageTemplate.getPk(), messageTemplate.getSk(), messageTemplate.getContent());

            messageTemplateRepository.save(messageTemplate);
            log.info("Message template successfully stored with ID: {}", messageTemplate.getPk());

        } catch (JsonProcessingException e) {
            log.error("JSON parsing error: {}", e.getMessage(), e);
            throw new RuntimeException("Error parsing CMS JSON content", e);
        } catch (Exception e) {
            log.error("Unexpected error during template processing: {}", e.getMessage(), e);
            throw new RuntimeException("Error processing template", e);
        }
    }
}
