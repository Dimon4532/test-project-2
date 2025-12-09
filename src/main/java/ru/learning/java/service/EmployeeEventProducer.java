package ru.learning.java.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.learning.java.model.EmployeeEvent;

@Service
public class EmployeeEventProducer {

  private final KafkaTemplate<String, EmployeeEvent> kafkaTemplate;

  public EmployeeEventProducer(KafkaTemplate<String, EmployeeEvent> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  public void sendEvent(EmployeeEvent event) {
    kafkaTemplate.send("test-project-input", event.getName(), event);
  }
}
