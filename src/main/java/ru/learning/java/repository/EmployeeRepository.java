package ru.learning.java.repository;

import org.springframework.stereotype.Repository;
import ru.learning.java.company.Department;
import ru.learning.java.exceptions.SalaryException;
import ru.learning.java.model.Employee;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Repository
public class EmployeeRepository {

  private static final Logger LOGGER = Logger.getLogger(EmployeeRepository.class.getName());
  private final Path filePath = Paths.get("employees.txt");

  public List<Employee> getAllEmployees() {
    if (!Files.exists(filePath)) {
      return new ArrayList<>();
    }

    try {
      return Files.readAllLines(filePath).stream()
        .filter(line -> !line.trim().isEmpty())
        .map(this::mapToEmployee)
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
    } catch (IOException e) {
      throw new RuntimeException("Ошибка при чтении файла базы данных сотрудников", e);
    }
  }

  public void save(Employee employee) {
    String line = mapToString(employee);
    try {
      Files.writeString(
        filePath,
        line + System.lineSeparator(),
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND
      );
    } catch (IOException e) {
      throw new RuntimeException("Ошибка при сохранении сотрудника в файл", e);
    }
  }

  private String mapToString(Employee employee) {
    if (employee.getDepartment() == null) {
      throw new IllegalStateException("У сотрудника " + employee.getName() + " не установлен департамент!");
    }

    return String.format("%s,%s,%s",
      employee.getName(),
      employee.getDepartment().name(),
      employee.getSalary());
  }

  private Employee mapToEmployee(String line) {
    String[] parts = line.split(",");

    if (parts.length < 3) {
      LOGGER.log(Level.WARNING, "Некорректный формат строки: {0}", line);
      return null; // Возвращаем null, чтобы потом отфильтровать
    }

    String name = parts[0].trim();
    String departmentStr = parts[1].trim();
    String salaryStr = parts[2].trim();

    try {
      double salary = Double.parseDouble(salaryStr);

      Department department = Department.valueOf(departmentStr);
      Employee employee = new Employee() {};

      employee.setName(name);
      employee.setDepartment(department);
      employee.setSalary(salary);

      return employee;

    } catch (NumberFormatException e) {
      LOGGER.log(Level.SEVERE, "Ошибка формата числа в строке: " + line, e);
    } catch (IllegalArgumentException e) {
      LOGGER.log(Level.SEVERE, "Неизвестный департамент '" + departmentStr + "' в строке: " + line, e);
    } catch (SalaryException e) {
      LOGGER.log(Level.SEVERE, "Некорректная зарплата в строке: " + line + ". " + e.getMessage());
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Неожиданная ошибка при разборе строки: " + line, e);
    }
    return null;
  }
}