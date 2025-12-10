package ru.learning.java.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.learning.java.exceptions.InvalidEmployeeException;
import ru.learning.java.exceptions.SalaryException;
import ru.learning.java.model.Employee;
import ru.learning.java.model.EmployeeEvent;
import ru.learning.java.service.EmployeeEventProducer;
import ru.learning.java.service.EmployeeService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

  private final EmployeeService service;
  private final EmployeeEventProducer producer;

  public EmployeeController(EmployeeService service, EmployeeEventProducer producer) {
    this.service = service;
    this.producer = producer;
  }

  @GetMapping
  public List<Employee> getAll() {
    return service.findAll();
  }

  @PostMapping
  public void create(@RequestBody Employee employee) {
    try {
      service.hireEmployee(employee);
      producer.sendEvent(new EmployeeEvent(employee.getName(), "HIRED"));
    } catch (InvalidEmployeeException e) {
      throw new RuntimeException(e);
    }
  }

  @GetMapping("/average-salary")
  public double getAverageSalary() {
    return service.calculateAverageSalary();
  }

  @PostMapping("/work-routine")
  public String triggerWorkRoutine() {
    service.performWorkRoutine();
    return "Рабочая рутина выполнена (см. логи консоли)";
  }

  @PostMapping("/demo-concurrency")
  public String triggerConcurrencyDemo() {
    service.runConcurrencyDemo();
    return "Демонстрация потоков завершена";
  }

  @PutMapping("/{id}/salary")
  public ResponseEntity<String> updateSalary(@PathVariable String id, @RequestParam double newSalary) {
    try {
      service.changeSalary(id, newSalary);
      return ResponseEntity.ok("Зарплата сотрудника " + id + " успешно обновлена на " + newSalary);
    } catch (SalaryException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка валидации зарплаты: " + e.getMessage());
    } catch (RuntimeException e) {
      if (e.getMessage().contains("не найден")) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
      }
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Внутренняя ошибка: " + e.getMessage());
    }
  }
}