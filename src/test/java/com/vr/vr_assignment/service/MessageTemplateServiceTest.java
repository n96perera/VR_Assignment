package com.vr.vr_assignment.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vr.vr_assignment.model.CMSMessageTemplate;
import com.vr.vr_assignment.model.MessageTemplate;
import com.vr.vr_assignment.repository.MessageTemplateRepository;
import com.vr.vr_assignment.util.CMSMessageMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MessageTemplateServiceTest {

    @Mock
    private MessageTemplateRepository messageTemplateRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private MessageTemplateService messageTemplateService;

    private final String validJson = "{ \"items\": [ { \"fields\": { \"key\": \"template1\", \"trafficType\": { \"sys\": { \"id\": \"type1\" } }, \"subject\": { \"content\": [ { \"content\": [ { \"nodeType\": \"text\", \"value\": \"test subject\" } ] } ] }, \"body\": { \"content\": [ { \"content\": [ { \"nodeType\": \"text\", \"value\": \"test body\" } ] } ] } } } ], \"includes\": { \"Entry\": [ { \"sys\": { \"id\": \"type1\" }, \"fields\": { \"key\": \"email\" } } ] } }";

    private CMSMessageTemplate cmsMessageTemplate;
    private MessageTemplate messageTemplate;

    private MockedStatic<CMSMessageMapper> mockedStaticMapper;

    @BeforeEach
    void setUp() {
        cmsMessageTemplate = new CMSMessageTemplate();
        cmsMessageTemplate.setKey("template1");
        cmsMessageTemplate.setTrafficType("email");
        cmsMessageTemplate.setSubject("test subject");
        cmsMessageTemplate.setBody("test body");

        messageTemplate = new MessageTemplate(cmsMessageTemplate);

        mockedStaticMapper = mockStatic(CMSMessageMapper.class);
    }

    @AfterEach
    void tearDown() {
        mockedStaticMapper.close();
    }

    @Test
    void testProcessAndStore_NewEntry() {
        mockedStaticMapper.when(() -> CMSMessageMapper.mapJsonToCMSMessageTemplate(validJson))
                .thenReturn(cmsMessageTemplate);

        when(messageTemplateRepository.findById(messageTemplate.getPk(), messageTemplate.getTrafficType()))
                .thenReturn(Optional.empty());

        messageTemplateService.processAndStore(validJson);

        verify(messageTemplateRepository).save(any(MessageTemplate.class));
    }

    @Test
    void testProcessAndStore_UpdateExistingEntry() {
        mockedStaticMapper.when(() -> CMSMessageMapper.mapJsonToCMSMessageTemplate(validJson))
                .thenReturn(cmsMessageTemplate);

        when(messageTemplateRepository.findById(messageTemplate.getPk(), messageTemplate.getTrafficType()))
                .thenReturn(Optional.of(messageTemplate));

        messageTemplateService.processAndStore(validJson);

        verify(messageTemplateRepository).update(any(MessageTemplate.class));
        verify(messageTemplateRepository, never()).save(any(MessageTemplate.class));
    }

    @Test
    void testProcessAndStore_JsonProcessingException() throws JsonProcessingException {
        String invalidJson = "{invalid json}";

        mockedStaticMapper.when(() -> CMSMessageMapper.mapJsonToCMSMessageTemplate(invalidJson))
                .thenThrow(new JsonProcessingException("JSON error") {});

        assertThrows(RuntimeException.class, () -> messageTemplateService.processAndStore(invalidJson));

        verify(messageTemplateRepository, never()).save(any(MessageTemplate.class));
        verify(messageTemplateRepository, never()).update(any(MessageTemplate.class));
    }

    @Test
    void testProcessAndStore_UnexpectedException() {
        String json = "{unexpected error}";

        mockedStaticMapper.when(() -> CMSMessageMapper.mapJsonToCMSMessageTemplate(json))
                .thenThrow(new RuntimeException("Unexpected error"));

        assertThrows(RuntimeException.class, () -> messageTemplateService.processAndStore(json));

        verify(messageTemplateRepository, never()).save(any(MessageTemplate.class));
        verify(messageTemplateRepository, never()).update(any(MessageTemplate.class));
    }
}
