package ru.learning.java.service;

import org.springframework.stereotype.Service;
import ru.learning.java.company.BonusBudget;
import ru.learning.java.exceptions.InvalidEmployeeException;
import ru.learning.java.exceptions.SalaryException;
import ru.learning.java.model.Employee;
import ru.learning.java.model.HRManager;
import ru.learning.java.repository.EmployeeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

  private final EmployeeRepository repository;

  public EmployeeService(EmployeeRepository repository) {
    this.repository = repository;
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
    List<Employee> all = repository.getAllEmployees();

    Optional<Employee> employeeOpt = all.stream()
      .filter(e -> e.getId() != null && e.getId().equals(id))
      .findFirst();

    if (employeeOpt.isEmpty()) {
      throw new RuntimeException("Сотрудник с ID " + id + " не найден");
    }

    Employee employee = employeeOpt.get();

    employee.setSalary(newSalary);

    repository.update(employee);
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
}