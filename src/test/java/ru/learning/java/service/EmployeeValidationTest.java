package ru.learning.java.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.learning.java.company.Department;
import ru.learning.java.exceptions.InvalidEmployeeException;
import ru.learning.java.exceptions.SalaryException;
import ru.learning.java.model.Developer;
import ru.learning.java.model.Employee;
import ru.learning.java.model.Manager;
import ru.learning.java.repository.EmployeeRepository;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeValidationTest {

  @Test
  @DisplayName("setSalary должен выбрасывать SalaryException при отрицательном значении")
  void shouldThrowSalaryException_WhenSalaryIsNegative() {
    Employee employee = new Developer();
    employee.setName("Test Dev");

    SalaryException exception = assertThrows(SalaryException.class, () -> {
      employee.setSalary(-1000);
    });

    assertTrue(exception.getMessage().contains("отрицательной"),
      "Сообщение ошибки должно говорить об отрицательной зарплате");
  }

  @Test
  @DisplayName("setSalary должен выбрасывать SalaryException при превышении лимита")
  void shouldThrowSalaryException_WhenSalaryIsTooHigh() {
    Employee employee = new Manager();
    employee.setName("Rich Manager");

    SalaryException exception = assertThrows(SalaryException.class, () -> {
      employee.setSalary(100000);
    });

    assertTrue(exception.getMessage().contains("слишком большая"),
      "Сообщение ошибки должно говорить о превышении лимита");
  }

  @Test
  @DisplayName("hireEmployee должен выбрасывать InvalidEmployeeException, если имя пустое")
  void shouldThrowInvalidEmployeeException_WhenNameIsEmpty() {
    EmployeeRepository stubRepo = new EmployeeRepository() {
      @Override
      public void save(Employee employee) {
      }
    };

    EmployeeService service = new EmployeeService(stubRepo);

    Employee unnamedEmployee = new Developer();
    unnamedEmployee.setName("");
    assertDoesNotThrow(() -> unnamedEmployee.setSalary(1000));
    unnamedEmployee.setDepartment(Department.IT);

    InvalidEmployeeException exception = assertThrows(InvalidEmployeeException.class, () -> {
      service.hireEmployee(unnamedEmployee);
    });

    assertEquals("Имя сотрудника не может быть пустым", exception.getMessage());
  }
}