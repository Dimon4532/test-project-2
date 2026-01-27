package ru.learning.java.model;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.learning.java.exceptions.InvalidEmployeeException;
import ru.learning.java.exceptions.SalaryException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты для ProjectManager")
class ProjectManagerTest {

  private ProjectManager projectManager;
  private Developer developer;
  private Manager manager;
  private Designer designer;

  @BeforeEach
  @DisplayName("Инициализация ProjectManager и сотрудников перед каждым тестом")
  void setUp() throws SalaryException {
    projectManager = new ProjectManager();

    developer = new Developer();
    developer.setName("Alice");
    developer.setSalary(new BigDecimal("4000"));

    manager = new Manager();
    manager.setName("Bob");
    manager.setSalary(new BigDecimal("5000"));

    designer = new Designer();
    designer.setName("Charlie");
    designer.setSalary(new BigDecimal("3500"));
  }

  @Test
  @DisplayName("Проверка создания пустого ProjectManager")
  void testProjectManagerCreation() {
    assertNotNull(projectManager, "ProjectManager не должен быть null");
    assertEquals(0, projectManager.getTeam().size(), "Новая команда должна быть пустой");
  }

  @Test
  @DisplayName("Проверка добавления валидного сотрудника")
  void testAddValidEmployee() throws SalaryException, InvalidEmployeeException {
    projectManager.addEmployee(developer);

    assertEquals(1, projectManager.getTeam().size(), "После добавления должен быть 1 сотрудник");
    assertTrue(projectManager.getTeam().contains(developer), "Команда должна содержать добавленного сотрудника");
  }

  @Test
  @DisplayName("Проверка добавления нескольких сотрудников")
  void testAddMultipleEmployees() throws SalaryException, InvalidEmployeeException {
    projectManager.addEmployee(developer);
    projectManager.addEmployee(manager);
    projectManager.addEmployee(designer);

    assertEquals(3, projectManager.getTeam().size(), "В команде должно быть 3 сотрудника");
  }

  @Test
  @DisplayName("Проверка выброса исключения при добавлении null сотрудника")
  void testAddNullEmployee() {
    InvalidEmployeeException exception = assertThrows(InvalidEmployeeException.class, () -> projectManager.addEmployee(null), "Добавление null сотрудника должно вызывать InvalidEmployeeException");

    assertTrue(exception.getMessage().contains("null"),
            "Сообщение должно содержать информацию о null");
  }

  @Test
  @DisplayName("Проверка выброса исключения при добавлении сотрудника без имени")
  void testAddEmployeeWithEmptyName() throws SalaryException {
    Developer invalidDev = new Developer();
    invalidDev.setName("");
    invalidDev.setSalary(new BigDecimal("4000"));

    InvalidEmployeeException exception = assertThrows(InvalidEmployeeException.class, () -> projectManager.addEmployee(invalidDev), "Добавление сотрудника с пустым именем должно вызывать исключение");

    assertTrue(exception.getMessage().contains("пустым"),
            "Сообщение должно содержать информацию о пустом имени");
  }

  @Test
  @DisplayName("Проверка подсчёта общего бюджета пустой команды")
  void testCalculateBudgetEmptyTeam() {
    BigDecimal budget = projectManager.calculateTotalBudget();
    assertEquals(0, BigDecimal.ZERO.compareTo(budget), "Бюджет пустой команды должен быть 0");
  }

  @Test
  @DisplayName("Проверка подсчёта общего бюджета")
  void testCalculateTotalBudget() throws SalaryException, InvalidEmployeeException {
    projectManager.addEmployee(developer);
    projectManager.addEmployee(manager);
    projectManager.addEmployee(designer);

    BigDecimal expectedBudget = new BigDecimal("4000")
            .add(new BigDecimal("5000"))
            .add(new BigDecimal("3500"));
    BigDecimal actualBudget = projectManager.calculateTotalBudget();

    assertEquals(0, expectedBudget.compareTo(actualBudget),
            "Общий бюджет должен быть суммой всех зарплат");
  }

  @Test
  @DisplayName("Проверка подсчёта средней зарплаты пустой команды")
  void testCalculateAverageSalaryEmptyTeam() {
    BigDecimal avgSalary = projectManager.calculateAverageSalary();
    assertEquals(0, BigDecimal.ZERO.compareTo(avgSalary),
            "Средняя зарплата пустой команды должна быть 0");
  }

  @Test
  @DisplayName("Проверка подсчёта средней зарплаты")
  void testCalculateAverageSalary() throws SalaryException, InvalidEmployeeException {
    projectManager.addEmployee(developer);
    projectManager.addEmployee(manager);
    projectManager.addEmployee(designer);

    BigDecimal expectedAverage = new BigDecimal("4166.67"); // (4000 + 5000 + 3500) / 3
    BigDecimal actualAverage = projectManager.calculateAverageSalary();

    assertEquals(0, expectedAverage.compareTo(actualAverage),
            "Средняя зарплата должна быть корректно рассчитана");
  }

  @ParameterizedTest
  @DisplayName("Параметризованный тест проверки бюджета с разными зарплатами")
  @CsvSource({
          "1000, 2000, 3000, 6000",
          "5000, 5000, 5000, 15000",
          "10000, 20000, 30000, 60000"
  })
  void testCalculateBudgetWithDifferentSalaries(String sal1, String sal2, String sal3, String expected)
          throws SalaryException, InvalidEmployeeException {

    Developer dev1 = new Developer();
    dev1.setName("Dev1");
    dev1.setSalary(new BigDecimal(sal1));

    Developer dev2 = new Developer();
    dev2.setName("Dev2");
    dev2.setSalary(new BigDecimal(sal2));

    Developer dev3 = new Developer();
    dev3.setName("Dev3");
    dev3.setSalary(new BigDecimal(sal3));

    projectManager.addEmployee(dev1);
    projectManager.addEmployee(dev2);
    projectManager.addEmployee(dev3);

    BigDecimal expectedBudget = new BigDecimal(expected);
    BigDecimal actualBudget = projectManager.calculateTotalBudget();

    assertEquals(0, expectedBudget.compareTo(actualBudget),
            "Бюджет должен совпадать с ожидаемым");
  }

  @Test
  @DisplayName("Проверка распределения задач без исключений")
  void testAssignTasks() throws SalaryException, InvalidEmployeeException {
    projectManager.addEmployee(developer);
    projectManager.addEmployee(manager);

    assertDoesNotThrow(() -> projectManager.assignTasks(), "Распределение задач не должно вызывать исключений");
  }

  @Test
  @DisplayName("Проверка альтернативного распределения задач")
  void testAssignTasksAlternative() throws SalaryException, InvalidEmployeeException {
    projectManager.addEmployee(developer);
    projectManager.addEmployee(designer);

    assertDoesNotThrow(() -> projectManager.assignTasksAlternative(), "Альтернативное распределение задач не должно вызывать исключений");
  }

  @Test
  @DisplayName("Проверка доступности всех сотрудников")
  void testCheckAllEmployeesAvailability() throws SalaryException, InvalidEmployeeException {
    projectManager.addEmployee(developer);
    projectManager.addEmployee(manager);

    assertDoesNotThrow(() -> projectManager.checkAllEmployeesAvailability(), "Проверка доступности не должна вызывать исключений");
  }

  @Test
  @DisplayName("Проверка поиска существующего сотрудника")
  void testCheckEmployeeAvailabilityExists() throws SalaryException, InvalidEmployeeException {
    projectManager.addEmployee(developer);

    assertDoesNotThrow(() -> projectManager.checkEmployeeAvailability("Alice"), "Проверка существующего сотрудника не должна вызывать исключений");
  }

  @Test
  @DisplayName("Проверка поиска несуществующего сотрудника")
  void testCheckEmployeeAvailabilityNotExists() {
    assertDoesNotThrow(() -> projectManager.checkEmployeeAvailability("NonExistent"), "Проверка несуществующего сотрудника не должна вызывать исключений");
  }

  @Nested
  @DisplayName("Интеграционные тесты ProjectManager")
  class IntegrationTests {

    @Test
    @DisplayName("Полный жизненный цикл проекта")
    void testFullProjectLifecycle() throws SalaryException, InvalidEmployeeException {
      QAEngineer qa = new QAEngineer();
      qa.setName("QA Engineer");
      qa.setSalary(new BigDecimal("3800"));

      TeamLead lead = new TeamLead();
      lead.setName("Team Lead");
      lead.setSalary(new BigDecimal("6000"));

      projectManager.addEmployee(developer);
      projectManager.addEmployee(manager);
      projectManager.addEmployee(qa);
      projectManager.addEmployee(lead);

      assertEquals(4, projectManager.getTeam().size());

      BigDecimal budget = projectManager.calculateTotalBudget();
      assertTrue(budget.compareTo(BigDecimal.ZERO) > 0, "Бюджет должен быть больше нуля");

      BigDecimal avgSalary = projectManager.calculateAverageSalary();
      assertTrue(avgSalary.compareTo(BigDecimal.ZERO) > 0, "Средняя зарплата должна быть больше нуля");
      assertTrue(avgSalary.compareTo(budget) <= 0, "Средняя зарплата не может быть больше общего бюджета");

      assertAll("Все операции с командой должны выполниться успешно",
              () -> assertDoesNotThrow(() -> projectManager.assignTasks()),
              () -> assertDoesNotThrow(() -> projectManager.assignTasksAlternative()),
              () -> assertDoesNotThrow(() -> projectManager.checkAllEmployeesAvailability())
      );
    }
  }
}