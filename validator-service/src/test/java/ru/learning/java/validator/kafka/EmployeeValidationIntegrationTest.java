package ru.learning.java.validator.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import ru.learning.java.kafka.Topics;

import java.time.Instant;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(
        partitions = 1,
        topics = {Topics.EMPLOYEE_RAW, Topics.EMPLOYEE_VALIDATED, Topics.EMPLOYEE_DLQ},
        brokerProperties = {
                "listeners=PLAINTEXT://localhost:9092",
                "port=9092"
        }
)
@TestPropertySource(properties = {
        "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}",
        "spring.kafka.consumer.auto-offset-reset=earliest",
        "spring.kafka.consumer.group-id=test-consumer-group"
})
class EmployeeValidationIntegrationTest {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Test
    void shouldSendInvalidMessageToDlq() throws Exception {
        BlockingQueue<ConsumerRecord<String, String>> dlqRecords = new LinkedBlockingQueue<>();

        var consumerProps = new HashMap<String, Object>();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "test-dlq-consumer");
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        var consumerFactory = new DefaultKafkaConsumerFactory<String, String>(consumerProps);
        var containerProperties = new ContainerProperties(Topics.EMPLOYEE_DLQ);
        containerProperties.setMessageListener((MessageListener<String, String>) dlqRecords::add);

        var container = new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);
        container.start();
        ContainerTestUtils.waitForAssignment(container, 1);

        Thread.sleep(2000);

        String invalidMessage = """
            {
              "eventId": "%s",
              "createdAt": "%s",
              "payload": {
                "nameEmployee": "John"
              }
            }
            """.formatted(UUID.randomUUID(), Instant.now().toString());

        kafkaTemplate.send(Topics.EMPLOYEE_RAW, "test-key", invalidMessage).get();

        // Wait for message to arrive in DLQ
        ConsumerRecord<String, String> dlqRecord = dlqRecords.poll(15, TimeUnit.SECONDS);

        assertNotNull(dlqRecord, "Message should be sent to DLQ");
        assertTrue(dlqRecord.value().contains("John") ||
                        dlqRecord.value().contains("validation") ||
                        dlqRecord.value().contains("department"),
                "DLQ message should contain error information");

        container.stop();
    }

    @Test
    void shouldSendValidMessageToValidatedTopic() throws Exception {
        BlockingQueue<ConsumerRecord<String, String>> validatedRecords = new LinkedBlockingQueue<>();

        var consumerProps = new HashMap<String, Object>();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "test-validated-consumer");
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        var consumerFactory = new DefaultKafkaConsumerFactory<String, String>(consumerProps);
        var containerProperties = new ContainerProperties(Topics.EMPLOYEE_VALIDATED);
        containerProperties.setMessageListener((MessageListener<String, String>) validatedRecords::add);

        var container = new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);
        container.start();
        ContainerTestUtils.waitForAssignment(container, 1);

        Thread.sleep(2000);

        String validMessage = """
            {
              "eventId": "%s",
              "createdAt": "%s",
              "payload": {
                "nameEmployee": "Alice",
                "department": "IT"
              }
            }
            """.formatted(UUID.randomUUID(), Instant.now().toString());

        kafkaTemplate.send(Topics.EMPLOYEE_RAW, "test-key", validMessage).get();

        ConsumerRecord<String, String> validatedRecord = validatedRecords.poll(15, TimeUnit.SECONDS);

        assertNotNull(validatedRecord, "Valid message should be sent to VALIDATED topic");
        assertTrue(validatedRecord.value().contains("Alice"));
        assertTrue(validatedRecord.value().contains("IT"));

        container.stop();
    }
}