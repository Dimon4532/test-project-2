package ru.learning.java.validator.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.learning.java.kafka.Topics;

@Service
public class EmployeeEventValidatorListener {

  private static final Logger log = LoggerFactory.getLogger(EmployeeEventValidatorListener.class);

  private final JsonSchemaValidator validator;
  private final DlqMessageFactory dlqMessageFactory;
  private final KafkaTemplate<String, String> kafkaTemplate;

  public EmployeeEventValidatorListener(
    JsonSchemaValidator validator,
    DlqMessageFactory dlqMessageFactory,
    KafkaTemplate<String, String> kafkaTemplate) {
    this.validator = validator;
    this.dlqMessageFactory = dlqMessageFactory;
    this.kafkaTemplate = kafkaTemplate;
  }

  @KafkaListener(
    topics = Topics.EMPLOYEE_RAW,
    groupId = "validator-service-group",
    containerFactory = "kafkaListenerContainerFactory"
  )
  public void listen(ConsumerRecord<String, String> record) {
    String message = record.value();
    String key = record.key();

    log.info("Получено сообщение из топика {}: key={}, value={}",
      record.topic(), key, message);

    JsonSchemaValidator.ValidationResult result = validator.validate(message);

    if (result.isValid()) {
      log.info("Сообщение валидно. Отправляем в топик {}", Topics.EMPLOYEE_VALIDATED);
      kafkaTemplate.send(Topics.EMPLOYEE_VALIDATED, key, message);
    } else {
      log.warn("Сообщение НЕ валидно: {}. Отправляем в DLQ", result.getErrorMessage());
      String dlqMessage = dlqMessageFactory.createDlqMessage(
        message,
        result.getErrorMessage(),
        record.topic(),
        record.partition(),
        record.offset()
      );
      kafkaTemplate.send(Topics.EMPLOYEE_DLQ, key, dlqMessage);
    }
  }
}