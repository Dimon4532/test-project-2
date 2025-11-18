package ru.learning.java.employees;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.learning.java.Trainable;
import ru.learning.java.exceptions.SalaryException;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты для Employee и его наследников")
class EmployeeTest {

  private Developer developer;
  private Manager manager;
  private Designer designer;
  private QAEngineer qaEngineer;
  private TeamLead teamLead;

  @BeforeEach
  @DisplayName("Подготовка тестовых данных перед каждым тестом")
  void setUp() throws SalaryException {
    developer = new Developer();
    developer.setName("Test Developer");
    developer.setSalary(4000);

    manager = new Manager();
    manager.setName("Test Manager");
    manager.setSalary(5000);

    designer = new Designer();
    designer.setName("Test Designer");
    designer.setSalary(3500);

    qaEngineer = new QAEngineer();
    qaEngineer.setName("Test QA");
    qaEngineer.setSalary(3800);

    teamLead = new TeamLead();
    teamLead.setName("Test TeamLead");
    teamLead.setSalary(6000);
  }

  @AfterEach
  @DisplayName("Очистка после каждого теста")
  void tearDown() {
    developer = null;
    manager = null;
    designer = null;
    qaEngineer = null;
    teamLead = null;
  }

  @Test
  @DisplayName("Проверка установки и получения имени сотрудника")
  void testSetAndGetName() {
    developer.setName("New Name");
    assertEquals("New Name", developer.getName(), "Имя должно быть установлено корректно");
  }

  @Test
  @DisplayName("Проверка установки и получения зарплаты")
  void testSetAndGetSalary() throws SalaryException {
    developer.setSalary(5000);
    assertEquals(5000, developer.getSalary(), 0.01, "Зарплата должна быть установлена корректно");
  }

  @Test
  @DisplayName("Проверка выброса исключения при отрицательной зарплате")
  void testNegativeSalaryThrowsException() {
    SalaryException exception = assertThrows(SalaryException.class, () -> {
      developer.setSalary(-1000);
    }, "Должно быть выброшено исключение для отрицательной зарплаты");

    assertTrue(exception.getMessage().contains("отрицательной"),
      "Сообщение исключения должно содержать информацию об отрицательной зарплате");
  }

  @Test
  @DisplayName("Проверка выброса исключения при слишком большой зарплате")
  void testExcessiveSalaryThrowsException() {
    SalaryException exception = assertThrows(SalaryException.class, () -> {
      developer.setSalary(100000);
    }, "Должно быть выброшено исключение для слишком большой зарплаты");

    assertTrue(exception.getMessage().contains("большая"),
      "Сообщение исключения должно содержать информацию о превышении лимита");
  }

  @ParameterizedTest
  @DisplayName("Параметризованный тест валидных зарплат")
  @ValueSource(doubles = {0.0, 1000.0, 25000.0, 49999.99, 50000.0})
  void testValidSalaries(double salary) {
    assertDoesNotThrow(() -> {
      developer.setSalary(salary);
      assertEquals(salary, developer.getSalary(), 0.01);
    }, "Валидная зарплата не должна вызывать исключения");
  }

  @ParameterizedTest
  @DisplayName("Параметризованный тест невалидных зарплат")
  @ValueSource(doubles = {-1.0, -1000.0, 50000.01, 100000.0})
  void testInvalidSalaries(double salary) {
    assertThrows(SalaryException.class, () -> {
      developer.setSalary(salary);
    }, "Невалидная зарплата должна вызывать исключение");
  }

  @Test
  @DisplayName("Проверка полиморфизма - Developer является Employee")
  void testDeveloperIsEmployee() {
    assertTrue(developer instanceof Employee, "Developer должен быть экземпляром Employee");
  }

  @Test
  @DisplayName("Проверка полиморфизма - TeamLead является Developer")
  void testTeamLeadIsDeveloper() {
    assertTrue(teamLead instanceof Developer, "TeamLead должен быть экземпляром Developer");
    assertTrue(teamLead instanceof Employee, "TeamLead должен быть экземпляром Employee");
  }

  @Test
  @DisplayName("Проверка работы QAEngineer - подсчёт найденных багов")
  void testQAEngineerFindBugs() {
    assertEquals(0, qaEngineer.getBugsFound(), "Изначально багов должно быть 0");

    qaEngineer.findBug();
    assertEquals(1, qaEngineer.getBugsFound(), "После поиска должен быть 1 баг");

    qaEngineer.findBug();
    qaEngineer.findBug();
    assertEquals(3, qaEngineer.getBugsFound(), "После трёх поисков должно быть 3 бага");
  }

  @Test
  @DisplayName("Проверка работы TeamLead - управление командой")
  void testTeamLeadManagement() throws SalaryException {
    teamLead.setTeamSize(5);
    assertEquals(5, teamLead.getTeamSize(), "Размер команды должен быть установлен корректно");

    Developer juniorDev = new Developer();
    juniorDev.setName("Junior Dev");
    juniorDev.setSalary(3000);

    assertDoesNotThrow(() -> {
      teamLead.assignTask(juniorDev, "Implement feature");
    }, "Назначение задачи не должно вызывать исключений");
  }

  @Nested
  @DisplayName("Тесты для Manager")
  class ManagerTests {

    @Test
    @DisplayName("Проверка проведения тренингов")
    void testConductTraining() {
      assertEquals(0, manager.getTrainingHours(), "Изначально часов тренинга должно быть 0");

      manager.conductTraining("Java Basics");
      assertEquals(2, manager.getTrainingHours(), "После одного тренинга должно быть 2 часа");

      manager.conductTraining("Design Patterns");
      assertEquals(4, manager.getTrainingHours(), "После двух тренингов должно быть 4 часа");
    }

    @Test
    @DisplayName("Проверка интерфейса Trainable")
    void testTrainableInterface() {
      assertTrue(manager instanceof Trainable, "Manager должен реализовывать интерфейс Trainable");
    }

    @Test
    @DisplayName("Проверка записи документов")
    void testWriteDocument() {
      String document = manager.writeDocument();
      assertNotNull(document, "Документ не должен быть null");
      assertFalse(document.isEmpty(), "Документ не должен быть пустым");
    }
  }
}