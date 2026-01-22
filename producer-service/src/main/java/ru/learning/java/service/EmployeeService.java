package ru.learning.java.service;

import org.springframework.stereotype.Service;
import ru.learning.java.company.BonusBudget;
import ru.learning.java.exceptions.InvalidEmployeeException;
import ru.learning.java.exceptions.SalaryException;
import ru.learning.java.model.Employee;
import ru.learning.java.model.HRManager;
import ru.learning.java.repository.EmployeeRepository;

import java.util.List;

@Service
public class EmployeeService {

  private final EmployeeRepository repository;
  private final EmployeeSearchService searchService;

  public EmployeeService(EmployeeRepository repository, EmployeeSearchService searchService) {
    this.repository = repository;
    this.searchService = searchService;
  }

  public List<Employee> findAll() {
    return repository.getAllEmployees();
  }

  /**
   * Изменяет зарплату сотрудника по его ID.
   *
   * @param id        ID сотрудника
   * @param newSalary новая зарплата
   */
  public void changeSalary(String id, double newSalary) throws SalaryException {
    Employee employee = repository.findById(id);
    employee.setSalary(newSalary);
    repository.update(employee);

    // Обновляем в Elasticsearch
    searchService.indexEmployee(employee);
  }

  /**
   * Нанимает сотрудника.
   *
   * @throws InvalidEmployeeException если данные сотрудника некорректны (например, пустое имя)
   * @throws IllegalArgumentException если зарплата отрицательная (проверка дублируется в setSalary, но может быть и тут)
   */
  public void hireEmployee(Employee employee) throws InvalidEmployeeException {
    // Валидация имени (бывшая логика ProjectManager)
    if (employee.getName() == null || employee.getName().trim().isEmpty()) {
      throw new InvalidEmployeeException("Имя сотрудника не может быть пустым");
    }

    // Валидация зарплаты (на всякий случай, хотя сеттер модели тоже проверяет)
    if (employee.getSalary() < 0) {
      throw new IllegalArgumentException("Зарплата не может быть отрицательной");
    }

    repository.save(employee);

    // Индексируем в Elasticsearch
    searchService.indexEmployee(employee);
  }

  // Логика "Полиморфизм в действии"
  public void performWorkRoutine() {
    List<Employee> team = repository.getAllEmployees();
    for (Employee emp : team) {
      emp.work(); // Вывод в консоль останется в логах сервера
      if (emp instanceof HRManager) {
        ((HRManager) emp).conductInterview("New Junior Developer");
      }
    }
  }

  // Логика calculateAverageSalary из ProjectManager
  public double calculateAverageSalary() {
    List<Employee> team = repository.getAllEmployees();
    if (team.isEmpty()) return 0;

    double total = team.stream().mapToDouble(Employee::getSalary).sum();
    return total / team.size();
  }

  // Демонстрация потокобезопасности
  public void runConcurrencyDemo() {
    try {
      new BonusBudget().runRaceConditionDemo();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(e);
    }
  }

  /**
   * Переиндексация всех сотрудников в Elasticsearch
   */
  public void reindexAllEmployees() {
    List<Employee> allEmployees = repository.getAllEmployees();
    searchService.reindexAll(allEmployees);
  }
}