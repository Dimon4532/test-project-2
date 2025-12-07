package ru.learning.java.controller;

import org.springframework.web.bind.annotation.*;
import ru.learning.java.exceptions.InvalidEmployeeException;
import ru.learning.java.exceptions.SalaryException;
import ru.learning.java.model.Employee;
import ru.learning.java.service.EmployeeService;
import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

  private final EmployeeService service;

  public EmployeeController(EmployeeService service) {
    this.service = service;
  }

  @GetMapping
  public List<Employee> getAll() {
    return service.findAll();
  }

  @PostMapping
  public void create(@RequestBody Employee employee) {
    try {
      service.hireEmployee(employee);
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
    public String updateSalary(@PathVariable String id, @RequestParam double newSalary) {
        try {
            service.changeSalary(id, newSalary);
            return "Зарплата сотрудника " + id + " успешно обновлена на " + newSalary;
        } catch (SalaryException e) {
            throw new RuntimeException("Ошибка валидации зарплаты: " + e.getMessage());
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}