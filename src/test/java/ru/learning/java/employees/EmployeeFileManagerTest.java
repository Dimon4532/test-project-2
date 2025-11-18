package ru.learning.java.employees;

import org.junit.jupiter.api.*;
import ru.learning.java.exceptions.SalaryException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты для EmployeeFileManager")
class EmployeeFileManagerTest {

  private static final String TEST_FILE = "test_employees.txt";
  private Developer developer;
  private Manager manager;

  @BeforeEach
  @DisplayName("Подготовка тестовых данных")
  void setUp() throws SalaryException {
    developer = new Developer();
    developer.setName("Test Dev");
    developer.setSalary(4000);

    manager = new Manager();
    manager.setName("Test Manager");
    manager.setSalary(5000);
  }

  @AfterEach
  @DisplayName("Очистка тестовых файлов")
  void tearDown() throws IOException {
    Files.deleteIfExists(Path.of(TEST_FILE));
  }

  @Test
  @DisplayName("Проверка записи одного сотрудника в файл")
  void testWriteSingleEmployee() throws IOException {
    try (EmployeeFileManager fileManager = new EmployeeFileManager(TEST_FILE)) {
      fileManager.writeEmployee(developer);
    }

    assertTrue(Files.exists(Path.of(TEST_FILE)), "Файл должен быть создан");

    try (BufferedReader reader = new BufferedReader(new FileReader(TEST_FILE))) {
      String line = reader.readLine();
      assertNotNull(line, "Файл должен содержать данные");
      assertTrue(line.contains("Test Dev"), "Файл должен содержать имя сотрудника");
      assertTrue(line.contains("4000"), "Файл должен содержать зарплату");
    }
  }

  @Test
  @DisplayName("Проверка записи нескольких сотрудников в файл")
  void testWriteMultipleEmployees() throws IOException {
    try (EmployeeFileManager fileManager = new EmployeeFileManager(TEST_FILE)) {
      fileManager.writeEmployee(developer);
      fileManager.writeEmployee(manager);
    }

    assertTrue(Files.exists(Path.of(TEST_FILE)), "Файл должен быть создан");

    try (BufferedReader reader = new BufferedReader(new FileReader(TEST_FILE))) {
      String line1 = reader.readLine();
      String line2 = reader.readLine();

      assertNotNull(line1, "Первая строка должна существовать");
      assertNotNull(line2, "Вторая строка должна существовать");

      assertTrue(line1.contains("Test Dev"), "Первая строка должна содержать данные разработчика");
      assertTrue(line2.contains("Test Manager"), "Вторая строка должна содержать данные менеджера");
    }
  }

  @Test
  @DisplayName("Проверка автоматического закрытия файла (try-with-resources)")
  void testAutoCloseable() throws IOException {
    EmployeeFileManager fileManager = new EmployeeFileManager(TEST_FILE);
    fileManager.writeEmployee(developer);

    assertDoesNotThrow(() -> fileManager.close(),
      "Закрытие файла не должно вызывать исключений");

    assertTrue(Files.exists(Path.of(TEST_FILE)), "Файл должен существовать после закрытия");
  }

  @Test
  @DisplayName("Проверка формата записи данных")
  void testDataFormat() throws IOException {
    try (EmployeeFileManager fileManager = new EmployeeFileManager(TEST_FILE)) {
      fileManager.writeEmployee(developer);
    }

    try (BufferedReader reader = new BufferedReader(new FileReader(TEST_FILE))) {
      String line = reader.readLine();
      String[] parts = line.split(",");

      assertEquals(2, parts.length, "Строка должна содержать 2 части (имя и зарплата)");
      assertEquals("Test Dev", parts[0], "Первая часть должна быть именем");
      assertEquals("4000.0", parts[1], "Вторая часть должна быть зарплатой");
    }
  }

  @Test
  @DisplayName("Проверка создания файла в несуществующей директории")
  void testFileCreationInNonExistentDirectory() {
    assertThrows(IOException.class, () -> {
      new EmployeeFileManager("/nonexistent/directory/" + TEST_FILE);
    }, "Должно быть выброшено IOException при попытке создать файл в несуществующей директории");
  }
}