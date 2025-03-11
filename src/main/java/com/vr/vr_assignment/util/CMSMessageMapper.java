package com.vr.vr_assignment.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vr.vr_assignment.model.CMSMessageTemplate;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CMSMessageMapper {

    /**
     * Map JSON Content to CMS Template.
     * @param json Json content.
     */
    public static CMSMessageTemplate mapJsonToCMSMessageTemplate(String json) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(json);

        JsonNode item = rootNode.path("items").get(0);
        CMSMessageTemplate cmsTemplate = new CMSMessageTemplate();

        cmsTemplate.setKey(item.path("fields").path("key").asText(null));
        cmsTemplate.setName(item.path("fields").path("name").asText(null));

        // Resolve TrafficType from includes
        Map<String, String> includedEntries = new HashMap<>();
        for (JsonNode entry : rootNode.path("includes").path("Entry")) {
            includedEntries.put(entry.path("sys").path("id").asText(), entry.path("fields").path("key").asText());
        }

        // Assign trafficType after resolving
        String trafficTypeId = item.path("fields").path("trafficType").path("sys").path("id").asText(null);
        cmsTemplate.setTrafficType(includedEntries.getOrDefault(trafficTypeId, null));

        // Extract subject and body content
        cmsTemplate.setSubject(extractText(item.path("fields").path("subject"), includedEntries));
        cmsTemplate.setBody(extractText(item.path("fields").path("body"), includedEntries));

        return cmsTemplate;
    }

    /**
     * Extracts and formats text content from a JSON structure
     @param contentNode The JSON node containing text content.
     @param includedEntries A map of included entry IDs to their resolved values.
     * */
    private static String extractText(JsonNode contentNode, Map<String, String> includedEntries) {
        StringBuilder text = new StringBuilder();

        for (JsonNode paragraph : contentNode.path("content")) {
            for (JsonNode node : paragraph.path("content")) {
                if ("text".equals(node.path("nodeType").asText())) {
                    text.append(node.path("value").asText());
                } else if ("embedded-entry-inline".equals(node.path("nodeType").asText())) {
                    String entryId = node.path("data").path("target").path("sys").path("id").asText();
                    String paramValue = includedEntries.getOrDefault(entryId, "UNKNOWN");
                    text.append("{").append(paramValue).append("}");
                }
            }
            text.append("\n");
        }
        return text.toString().trim();
    }
}
