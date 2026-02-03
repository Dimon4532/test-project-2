package ru.learning.java.service;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.learning.java.dto.EmployeeEvent;
import ru.learning.java.entity.EmployeeRecord;
import ru.learning.java.repository.EmployeeRepository;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@Testcontainers
class EmployeeEventConsumerIntegrationTest {

  @Container
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
    .withDatabaseName("testdb")
    .withUsername("test")
    .withPassword("test");

  @Container
  static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.4.0"));

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
    registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
  }

  @Autowired
  private EmployeeRepository employeeRepository;

  private KafkaTemplate<String, EmployeeEvent> kafkaTemplate;

  @BeforeEach
  void setUp() {
    employeeRepository.deleteAll();

    Map<String, Object> producerProps = new HashMap<>();
    producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
    producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

    kafkaTemplate = new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(producerProps));
  }

  @Test
  void shouldConsumeMessageAndSaveToDatabase() {
    // Given
    EmployeeEvent event = new EmployeeEvent("Alice Johnson", "CREATED", System.currentTimeMillis());

    // When
    kafkaTemplate.send("validated-employees", "emp-123", event);

    // Then
    await()
      .atMost(Duration.ofSeconds(10))
      .pollInterval(Duration.ofMillis(500))
      .untilAsserted(() -> {
        List<EmployeeRecord> records = employeeRepository.findAll();
        assertThat(records).hasSize(1);
        assertThat(records.get(0).getEmployeeName()).isEqualTo("Alice Johnson");
        assertThat(records.get(0).getStatus()).isEqualTo("CREATED");
      });
  }

  @Test
  void shouldConsumeMultipleMessages() {
    // Given
    EmployeeEvent event1 = new EmployeeEvent("Bob Brown", "CREATED", System.currentTimeMillis());
    EmployeeEvent event2 = new EmployeeEvent("Charlie Davis", "UPDATED", System.currentTimeMillis());
    EmployeeEvent event3 = new EmployeeEvent("Diana Evans", "DELETED", System.currentTimeMillis());

    // When
    kafkaTemplate.send("validated-employees", "emp-1", event1);
    kafkaTemplate.send("validated-employees", "emp-2", event2);
    kafkaTemplate.send("validated-employees", "emp-3", event3);

    // Then
    await()
      .atMost(Duration.ofSeconds(10))
      .untilAsserted(() -> {
        List<EmployeeRecord> records = employeeRepository.findAll();
        assertThat(records).hasSize(3);

        assertThat(records)
          .extracting(EmployeeRecord::getEmployeeName)
          .containsExactlyInAnyOrder("Bob Brown", "Charlie Davis", "Diana Evans");
      });
  }

  @Test
  void shouldHandleDuplicateEmployeeNames() {
    // Given
    EmployeeEvent event1 = new EmployeeEvent("John Doe", "CREATED", System.currentTimeMillis());
    EmployeeEvent event2 = new EmployeeEvent("John Doe", "UPDATED", System.currentTimeMillis());

    // When
    kafkaTemplate.send("validated-employees", "emp-1", event1);
    kafkaTemplate.send("validated-employees", "emp-2", event2);

    // Then
    await()
      .atMost(Duration.ofSeconds(10))
      .untilAsserted(() -> {
        List<EmployeeRecord> records = employeeRepository.findByEmployeeName("John Doe");
        assertThat(records).hasSize(2);
        assertThat(records).extracting(EmployeeRecord::getStatus)
          .containsExactlyInAnyOrder("CREATED", "UPDATED");
      });
  }
}