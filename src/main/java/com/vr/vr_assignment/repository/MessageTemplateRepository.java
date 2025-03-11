package com.vr.vr_assignment.repository;

import com.vr.vr_assignment.model.MessageTemplate;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import java.util.Optional;

@Repository
@Slf4j
@RequiredArgsConstructor
public class MessageTemplateRepository {

    private final DynamoDbEnhancedClient dynamoDbEnhancedClient;
    private DynamoDbTable<MessageTemplate> table;

    @Value("${dynamodb.table.name}")
    private String tableName;

    @PostConstruct
    private void init() {
        this.table = dynamoDbEnhancedClient.table(tableName, TableSchema.fromBean(MessageTemplate.class));
    }

    public void save(MessageTemplate messageTemplate) {
        table.putItem(messageTemplate);
    }

    public Optional<MessageTemplate> findById(String id, String trafficType) {
        String partitionKey = id.startsWith("TEMPLATE#") ? id : "TEMPLATE#" + id;
        String sortKey = "TRAFFIC_TYPE#" + trafficType;

        log.info("Querying DynamoDB with PK: '{}', SK: '{}'", partitionKey, sortKey);

        if (partitionKey.isEmpty() || sortKey.isEmpty()) {
            log.error("DynamoDB query aborted: One or both keys are empty/null.");
            return Optional.empty();
        }

        return Optional.ofNullable(
                table.getItem(r -> r.key(k -> k.partitionValue(partitionKey).sortValue(sortKey)))
        );
    }

    public void update(MessageTemplate messageTemplate) {
        String partitionKey = messageTemplate.getPk().startsWith("TEMPLATE#")
                ? messageTemplate.getPk()
                : "TEMPLATE#" + messageTemplate.getPk();

        String sortKey = "TRAFFIC_TYPE#" + messageTemplate.getTrafficType();

        log.info("Updating DynamoDB entry with PK: '{}', SK: '{}'", partitionKey, sortKey);

        table.updateItem(item -> item.item(messageTemplate));

        log.info("Successfully updated MessageTemplate with ID: {}", messageTemplate.getPk());
    }

}
