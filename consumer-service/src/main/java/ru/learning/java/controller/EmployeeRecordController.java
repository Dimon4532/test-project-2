package ru.learning.java.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.learning.java.entity.EmployeeRecord;
import ru.learning.java.repository.EmployeeRepository;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeRecordController {

  private final EmployeeRepository employeeRepository;

  public EmployeeRecordController(EmployeeRepository employeeRepository) {
    this.employeeRepository = employeeRepository;
  }

  /**
   * Получить все записи о сотрудниках
   */
  @GetMapping
  public ResponseEntity<List<EmployeeRecord>> getAllEmployees() {
    List<EmployeeRecord> records = employeeRepository.findAllByOrderByReceivedAtDesc();
    return ResponseEntity.ok(records);
  }

  /**
   * Получить запись по ID
   */
  @GetMapping("/{id}")
  public ResponseEntity<EmployeeRecord> getEmployeeById(@PathVariable Long id) {
    return employeeRepository.findById(id)
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.notFound().build());
  }

  /**
   * Поиск по имени сотрудника
   */
  @GetMapping("/search")
  public ResponseEntity<List<EmployeeRecord>> searchByName(@RequestParam String name) {
    List<EmployeeRecord> records = employeeRepository.findByEmployeeName(name);
    return ResponseEntity.ok(records);
  }

  /**
   * Фильтр по статусу
   */
  @GetMapping("/status/{status}")
  public ResponseEntity<List<EmployeeRecord>> getByStatus(@PathVariable String status) {
    List<EmployeeRecord> records = employeeRepository.findByStatus(status);
    return ResponseEntity.ok(records);
  }

  /**
   * Получить количество записей
   */
  @GetMapping("/count")
  public ResponseEntity<Long> getCount() {
    long count = employeeRepository.count();
    return ResponseEntity.ok(count);
  }
}