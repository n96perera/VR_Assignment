package com.vr.vr_assignment.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;

@DynamoDbBean
public class MessageTemplate {

    private String pk;
    private String sk;
    private String trafficType;

    private String subject;

    private String content;

    public MessageTemplate () {}

    public MessageTemplate (CMSMessageTemplate cmsTemplate) {
        this.pk = "TEMPLATE#" + cmsTemplate.getKey();
        this.sk = "TRAFFIC_TYPE#" + cmsTemplate.getTrafficType();
        this.trafficType = cmsTemplate.getTrafficType();
        this.subject = cmsTemplate.getSubject();
        this.content = cmsTemplate.getBody();
    }

    @DynamoDbPartitionKey
    @DynamoDbAttribute("PK")
    public String getPk() { return pk; }
    public void setPk(String pk) { this.pk = pk; }

    @DynamoDbSortKey
    @DynamoDbAttribute("SK")
    public String getSk() { return sk; }
    public void setSk(String sk) { this.sk = sk; }

    @DynamoDbAttribute("trafficType")
    public String getTrafficType() { return trafficType; }
    public void setTrafficType(String trafficType) { this.trafficType = trafficType; }

    @DynamoDbAttribute("subject")
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @DynamoDbAttribute("content")
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}

