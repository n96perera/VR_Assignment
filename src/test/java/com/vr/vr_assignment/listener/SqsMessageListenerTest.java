package com.vr.vr_assignment.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vr.vr_assignment.service.S3Service;
import com.vr.vr_assignment.service.MessageTemplateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SqsMessageListenerTest {

    @Mock
    private S3Service s3Service;

    @Mock
    private MessageTemplateService messageTemplateService;

    @InjectMocks
    private SqsMessageListener sqsMessageListener;

    private final String sampleSqsMessage = """
        {
            "Records": [
                {
                   "s3": {
                        "bucket": { "name": "test-bucket" },
                        "object": { "key": "test-key" }
                    }
                }
            ]
        }
    """;

    @BeforeEach
    void setUp() throws Exception {
        ReflectionTestUtils.setField(sqsMessageListener, "objectMapper", new ObjectMapper());
    }

    @Test
    void testProcessMessage_Success() throws Exception {
        when(s3Service.getFileContent("test-key")).thenReturn("test file content");

        sqsMessageListener.processMessage(sampleSqsMessage);

        verify(s3Service, times(1)).getFileContent("test-key");
        verify(messageTemplateService, times(1)).processAndStore("test file content");
    }

    @Test
    void testProcessMessage_ExceptionHandling() throws Exception {
        doThrow(new RuntimeException("S3 retrieval failed")).when(s3Service).getFileContent(anyString());

        sqsMessageListener.processMessage(sampleSqsMessage);

        verify(s3Service, times(1)).getFileContent("test-key");
        verify(messageTemplateService, never()).processAndStore(anyString());
    }
}