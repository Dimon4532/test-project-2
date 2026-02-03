package ru.learning.java.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.learning.java.entity.EmployeeRecord;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
class EmployeeRepositoryTest {

  @Container
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
    .withDatabaseName("testdb")
    .withUsername("test")
    .withPassword("test");

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
  }

  @Autowired
  private EmployeeRepository employeeRepository;

  @Test
  void shouldSaveAndFindEmployeeRecord() {
    // Given
    EmployeeRecord record = new EmployeeRecord("John Doe", "CREATED", System.currentTimeMillis());

    // When
    EmployeeRecord saved = employeeRepository.save(record);

    // Then
    assertThat(saved.getId()).isNotNull();
    assertThat(saved.getEmployeeName()).isEqualTo("John Doe");
    assertThat(saved.getStatus()).isEqualTo("CREATED");
  }

  @Test
  void shouldFindByEmployeeName() {
    // Given
    employeeRepository.save(new EmployeeRecord("Alice", "CREATED", System.currentTimeMillis()));
    employeeRepository.save(new EmployeeRecord("Bob", "UPDATED", System.currentTimeMillis()));
    employeeRepository.save(new EmployeeRecord("Alice", "DELETED", System.currentTimeMillis()));

    // When
    List<EmployeeRecord> results = employeeRepository.findByEmployeeName("Alice");

    // Then
    assertThat(results).hasSize(2);
    assertThat(results).allMatch(r -> r.getEmployeeName().equals("Alice"));
  }

  @Test
  void shouldFindByStatus() {
    // Given
    employeeRepository.save(new EmployeeRecord("Alice", "CREATED", System.currentTimeMillis()));
    employeeRepository.save(new EmployeeRecord("Bob", "CREATED", System.currentTimeMillis()));
    employeeRepository.save(new EmployeeRecord("Charlie", "UPDATED", System.currentTimeMillis()));

    // When
    List<EmployeeRecord> results = employeeRepository.findByStatus("CREATED");

    // Then
    assertThat(results).hasSize(2);
    assertThat(results).allMatch(r -> r.getStatus().equals("CREATED"));
  }

  @Test
  void shouldFindAllOrderedByReceivedAtDesc() throws InterruptedException {
    // Given
    EmployeeRecord first = new EmployeeRecord("First", "CREATED", System.currentTimeMillis());
    employeeRepository.save(first);

    Thread.sleep(10); // Небольшая задержка для гарантии разного времени

    EmployeeRecord second = new EmployeeRecord("Second", "CREATED", System.currentTimeMillis());
    employeeRepository.save(second);

    // When
    List<EmployeeRecord> results = employeeRepository.findAllByOrderByReceivedAtDesc();

    // Then
    assertThat(results).hasSize(2);
    assertThat(results.get(0).getEmployeeName()).isEqualTo("Second"); // Последний сохранённый первым
    assertThat(results.get(1).getEmployeeName()).isEqualTo("First");
  }

  @Test
  void shouldCountRecords() {
    // Given
    employeeRepository.save(new EmployeeRecord("Alice", "CREATED", System.currentTimeMillis()));
    employeeRepository.save(new EmployeeRecord("Bob", "UPDATED", System.currentTimeMillis()));

    // When
    long count = employeeRepository.count();

    // Then
    assertThat(count).isEqualTo(2);
  }
}