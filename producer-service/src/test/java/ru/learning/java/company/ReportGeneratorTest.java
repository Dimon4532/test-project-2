package ru.learning.java.company;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.learning.java.exceptions.SalaryException;
import ru.learning.java.model.employees.Developer;
import ru.learning.java.model.employees.Employee;
import ru.learning.java.model.employees.Manager;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты для ReportGenerator")
class ReportGeneratorTest {

  private ReportGenerator reportGenerator;
  private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
  private final PrintStream originalOut = System.out;

  @BeforeEach
  void setUp() {
    reportGenerator = new ReportGenerator();
    System.setOut(new PrintStream(outputStreamCaptor));
  }

  @Test
  @DisplayName("Генерация отчетов по количеству сотрудников")
  void testGenerateReports() {
    reportGenerator.generateReports(3);

    String output = outputStreamCaptor.toString();
    assertTrue(output.contains("Начинаем генерацию отчетов..."));
    assertTrue(output.contains("Всего времени:"));
    assertTrue(output.contains("мс"));
  }

  @Test
  @DisplayName("Генерация отчетов выполняется многопоточно")
  void testGenerateReportsMultithreaded() {
    long start = System.currentTimeMillis();
    reportGenerator.generateReports(10);
    long duration = System.currentTimeMillis() - start;

    // С 4 потоками 10 задач по 100мс должны выполниться примерно за 300-400мс
    // (а не за 1000мс последовательно)
    assertTrue(duration < 800, "Многопоточное выполнение должно быть быстрее последовательного");
  }

  @Test
  @DisplayName("Генерация отчетов для списка сотрудников")
  void testGenerateReportsForEmployees() throws SalaryException {
    List<Employee> employees = Arrays.asList(
      createEmployee("001", "Иван Иванов", Department.IT, new BigDecimal("40000")),
      createEmployee("002", "Мария Петрова", Department.HR, new BigDecimal("35000"))
    );

    reportGenerator.generateReportsForEmployees(employees);

    String output = outputStreamCaptor.toString();
    assertTrue(output.contains("Иван Иванов"));
    assertTrue(output.contains("Мария Петрова"));
    assertTrue(output.contains("Information Technology"));
    assertTrue(output.contains("Human Resources"));
  }

  @Test
  @DisplayName("Генерация отчета по департаменту")
  void testGenerateDepartmentReport() throws SalaryException {
    List<Employee> itEmployees = Arrays.asList(
      createEmployee("001", "Иван Иванов", Department.IT, new BigDecimal("40000")),
      createEmployee("003", "Петр Сидоров", Department.IT, new BigDecimal("45000"))
    );

    reportGenerator.generateDepartmentReport(Department.IT, itEmployees);

    String output = outputStreamCaptor.toString();
    assertTrue(output.contains("Information Technology"));
    assertTrue(output.contains("Всего сотрудников: 2"));
  }

  @Test
  @DisplayName("Генерация отчетов для пустого списка сотрудников")
  void testGenerateReportsForEmptyList() {
    reportGenerator.generateReportsForEmployees(List.of());

    String output = outputStreamCaptor.toString();
    assertTrue(output.contains("Начинаем генерацию отчетов"));
    assertFalse(output.contains("готов")); // Ни один отчет не должен быть сгенерирован
  }

  @Test
  @DisplayName("Генерация нулевого количества отчетов")
  void testGenerateZeroReports() {
    reportGenerator.generateReports(0);

    String output = outputStreamCaptor.toString();
    assertTrue(output.contains("Всего времени:"));
    assertFalse(output.contains("готов"));
  }

  @org.junit.jupiter.api.AfterEach
  void tearDown() {
    System.setOut(originalOut);
  }

  private Employee createEmployee(String id, String name, Department department, BigDecimal salary) throws SalaryException {
    Employee emp;

    // Создаём Developer для IT департамента, Manager для остальных
    if (department == Department.IT) {
      emp = new Developer();
    } else {
      emp = new Manager();
    }

    emp.setId(id);
    emp.setName(name);
    emp.setDepartment(department);
    emp.setSalary(salary);

    return emp;
  }
}