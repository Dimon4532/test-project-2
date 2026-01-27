package ru.learning.java.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.learning.java.model.EmployeeDocument;
import ru.learning.java.service.EmployeeSearchService;
import ru.learning.java.service.EmployeeService;

import java.util.List;

/**
 * REST контроллер для поиска сотрудников через Elasticsearch
 */
@RestController
@RequestMapping("/api/v1/employees/search")
public class EmployeeSearchController {

  private final EmployeeSearchService searchService;
  private final EmployeeService employeeService;

  public EmployeeSearchController(EmployeeSearchService searchService, EmployeeService employeeService) {
    this.searchService = searchService;
    this.employeeService = employeeService;
  }

  /**
   * Поиск по имени
   */
  @GetMapping("/by-name")
  public ResponseEntity<List<EmployeeDocument>> searchByName(@RequestParam String name) {
    List<EmployeeDocument> results = searchService.searchByName(name);
    return ResponseEntity.ok(results);
  }

  /**
   * Поиск по отделу
   */
  @GetMapping("/by-department")
  public ResponseEntity<List<EmployeeDocument>> searchByDepartment(@RequestParam String department) {
    List<EmployeeDocument> results = searchService.searchByDepartment(department);
    return ResponseEntity.ok(results);
  }

  /**
   * Поиск по типу сотрудника
   */
  @GetMapping("/by-type")
  public ResponseEntity<List<EmployeeDocument>> searchByType(@RequestParam String type) {
    List<EmployeeDocument> results = searchService.searchByType(type);
    return ResponseEntity.ok(results);
  }

  /**
   * Получить все индексированные документы
   */
  @GetMapping("/all")
  public ResponseEntity<List<EmployeeDocument>> getAllIndexed() {
    List<EmployeeDocument> results = searchService.findAll();
    return ResponseEntity.ok(results);
  }

  /**
   * Переиндексировать всех сотрудников
   */
  @PostMapping("/reindex")
  public ResponseEntity<String> reindexAll() {
    employeeService.reindexAllEmployees();
    return ResponseEntity.ok("Все сотрудники успешно переиндексированы в Elasticsearch");
  }
}