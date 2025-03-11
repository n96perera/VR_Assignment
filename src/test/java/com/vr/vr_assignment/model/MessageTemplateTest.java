package com.vr.vr_assignment.model;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class MessageTemplateTest {

    @Test
    void testMessageTemplate_CreationFromCMSMessageTemplate() {
        CMSMessageTemplate cmsTemplate = new CMSMessageTemplate();
        cmsTemplate.setKey("template123");
        cmsTemplate.setTrafficType("email");
        cmsTemplate.setSubject("test subject");
        cmsTemplate.setBody("test body");

        MessageTemplate messageTemplate = new MessageTemplate(cmsTemplate);

        assertThat(messageTemplate.getPk()).isEqualTo("TEMPLATE#template123");
        assertThat(messageTemplate.getSk()).isEqualTo("TRAFFIC_TYPE#email");
        assertThat(messageTemplate.getTrafficType()).isEqualTo("email");
        assertThat(messageTemplate.getSubject()).isEqualTo("test subject");
        assertThat(messageTemplate.getContent()).isEqualTo("test body");
    }

    @Test
    void testMessageTemplate_GettersAndSetters() {
        MessageTemplate messageTemplate = new MessageTemplate();

        messageTemplate.setPk("TEMPLATE#test123");
        messageTemplate.setSk("TRAFFIC_TYPE#sms");
        messageTemplate.setTrafficType("sms");
        messageTemplate.setSubject("Test Subject");
        messageTemplate.setContent("Test Content");

        assertThat(messageTemplate.getPk()).isEqualTo("TEMPLATE#test123");
        assertThat(messageTemplate.getSk()).isEqualTo("TRAFFIC_TYPE#sms");
        assertThat(messageTemplate.getTrafficType()).isEqualTo("sms");
        assertThat(messageTemplate.getSubject()).isEqualTo("Test Subject");
        assertThat(messageTemplate.getContent()).isEqualTo("Test Content");
    }
}
