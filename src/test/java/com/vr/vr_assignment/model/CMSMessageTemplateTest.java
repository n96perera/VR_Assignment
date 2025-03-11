package com.vr.vr_assignment.model;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class CMSMessageTemplateTest {

    @Test
    void testCMSMessageTemplate_GettersAndSetters() {
        CMSMessageTemplate template = new CMSMessageTemplate();

        template.setKey("template123");
        template.setName("Test Template");
        template.setTrafficType("email");
        template.setSubject("Test Subject");
        template.setBody("Test Body");

        assertThat(template.getKey()).isEqualTo("template123");
        assertThat(template.getName()).isEqualTo("Test Template");
        assertThat(template.getTrafficType()).isEqualTo("email");
        assertThat(template.getSubject()).isEqualTo("Test Subject");
        assertThat(template.getBody()).isEqualTo("Test Body");
    }

    @Test
    void testCMSMessageTemplate_Equality() {
        CMSMessageTemplate template1 = new CMSMessageTemplate();
        template1.setKey("template123");
        template1.setTrafficType("email");

        CMSMessageTemplate template2 = new CMSMessageTemplate();
        template2.setKey("template123");
        template2.setTrafficType("email");

        assertThat(template1).isEqualTo(template2);
        assertThat(template1.hashCode()).isEqualTo(template2.hashCode());
    }

    @Test
    void testCMSMessageTemplate_ToString() {
        CMSMessageTemplate template = new CMSMessageTemplate();
        template.setKey("template123");
        template.setName("Test Template");

        assertThat(template.toString()).contains("template123", "Test Template");
    }
}
