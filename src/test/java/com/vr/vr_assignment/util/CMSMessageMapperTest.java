package com.vr.vr_assignment.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vr.vr_assignment.model.CMSMessageTemplate;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class CMSMessageMapperTest {

    @Test
    void testMapJsonToCMSMessageTemplate_Success() throws IOException {
        String json = """
        {
            "items": [
                {
                    "fields": {
                        "key": "template1",
                        "name": "Test Template",
                        "trafficType": { "sys": { "id": "traffic1" } },
                        "subject": { "content": [{ "content": [{ "nodeType": "text", "value": "test subject" }] }] },
                        "body": { "content": [{ "content": [{ "nodeType": "text", "value": "test body" }] }] }
                    }
                }
            ],
            "includes": {
                "Entry": [
                    {
                        "sys": { "id": "traffic1" },
                        "fields": { "key": "email" }
                    }
                ]
            }
        }
        """;

        CMSMessageTemplate template = CMSMessageMapper.mapJsonToCMSMessageTemplate(json);

        assertNotNull(template);
        assertEquals("template1", template.getKey());
        assertEquals("Test Template", template.getName());
        assertEquals("email", template.getTrafficType());
        assertEquals("test subject", template.getSubject());
        assertEquals("test body", template.getBody());
    }

    @Test
    void testMapJsonToCMSMessageTemplate_MissingFields() throws IOException {
        String json = """
        {
            "items": [
                {
                    "fields": {}
                }
            ],
            "includes": { "Entry": [] }
        }
        """;

        CMSMessageTemplate template = CMSMessageMapper.mapJsonToCMSMessageTemplate(json);

        assertTrue(template.getKey() == null || template.getKey().isEmpty());

    }

    @Test
    void testMapJsonToCMSMessageTemplate_InvalidJson() {
        String invalidJson = "{invalid json}";
        assertThrows(JsonProcessingException.class, () ->
                CMSMessageMapper.mapJsonToCMSMessageTemplate(invalidJson));
    }
}
