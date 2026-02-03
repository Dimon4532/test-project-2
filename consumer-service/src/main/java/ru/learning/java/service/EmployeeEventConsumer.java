package ru.learning.java.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import ru.learning.java.dto.EmployeeEvent;
import ru.learning.java.entity.EmployeeRecord;
import ru.learning.java.repository.EmployeeRepository;

@Service
public class EmployeeEventConsumer {

  private static final Logger log = LoggerFactory.getLogger(EmployeeEventConsumer.class);

  private final EmployeeRepository employeeRepository;

  public EmployeeEventConsumer(EmployeeRepository employeeRepository) {
    this.employeeRepository = employeeRepository;
  }

  @KafkaListener(topics = "validated-employees", groupId = "consumer-service-group")
  public void listen(@Payload EmployeeEvent event,
                     @Header(KafkaHeaders.RECEIVED_KEY) String key) {

    log.info("Получено событие из Kafka (Key: {}): {}", key, event);

    try {
      EmployeeRecord record = new EmployeeRecord(
        event.getName(),
        event.getStatus(),
        event.getTimestamp()
      );

      EmployeeRecord saved = employeeRepository.save(record);
      log.info("Событие успешно сохранено в БД с ID: {}", saved.getId());

    } catch (Exception e) {
      log.error("Ошибка при сохранении события в БД: {}", e.getMessage(), e);
    }
  }
}