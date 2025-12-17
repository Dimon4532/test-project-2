package ru.learning.java.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.learning.java.kafka.Topics;
import ru.learning.java.model.Employee;
import ru.learning.java.model.EmployeeWithDepartmentEvent;

@Service
public class EmployeeEventProducer {

  private final KafkaTemplate<String, EmployeeWithDepartmentEvent> kafkaTemplate;

  public EmployeeEventProducer(KafkaTemplate<String, EmployeeWithDepartmentEvent> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  public void sendEvent(Employee employee) {
    String departmentName = employee.getDepartment() != null
      ? employee.getDepartment().name()
      : "FLOW"; // Здесь для проверки лучше задам значение по умолчанию

    EmployeeWithDepartmentEvent event = new EmployeeWithDepartmentEvent(
      employee.getName(),
      departmentName
    );

    kafkaTemplate.send(Topics.EMPLOYEE_RAW, employee.getId(), event);;
  }
}
