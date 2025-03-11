package com.vr.vr_assignment.model;

import lombok.Data;

@Data
public class CMSMessageTemplate {
    private String key;

    private String name;

    private String trafficType;

    private String subject;

    private String body;
}
