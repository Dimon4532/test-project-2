package ru.learning.java.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.learning.java.entity.EmployeeRecord;
import ru.learning.java.repository.EmployeeRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class EmployeeRecordControllerTest {

  private MockMvc mockMvc;

  @Mock
  private EmployeeRepository employeeRepository;

  @InjectMocks
  private EmployeeRecordController employeeRecordController;

  private EmployeeRecord testRecord1;
  private EmployeeRecord testRecord2;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(employeeRecordController).build();

    testRecord1 = createEmployeeRecord(1L, "John Doe", "CREATED", System.currentTimeMillis());
    testRecord2 = createEmployeeRecord(2L, "Jane Smith", "UPDATED", System.currentTimeMillis());
  }

  private EmployeeRecord createEmployeeRecord(Long id, String name, String status, Long timestamp) {
    EmployeeRecord record = new EmployeeRecord(name, status, timestamp);
    record.setId(id);
    record.setReceivedAt(LocalDateTime.now());
    return record;
  }

  @Test
  void shouldGetAllEmployees() throws Exception {
    // Given
    List<EmployeeRecord> records = Arrays.asList(testRecord1, testRecord2);
    when(employeeRepository.findAllByOrderByReceivedAtDesc()).thenReturn(records);

    // When & Then
    mockMvc.perform(get("/api/employees"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$", hasSize(2)))
      .andExpect(jsonPath("$[0].employeeName").value("John Doe"))
      .andExpect(jsonPath("$[1].employeeName").value("Jane Smith"));
  }

  @Test
  void shouldGetEmployeeById() throws Exception {
    // Given
    when(employeeRepository.findById(1L)).thenReturn(Optional.of(testRecord1));

    // When & Then
    mockMvc.perform(get("/api/employees/1"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").value(1))
      .andExpect(jsonPath("$.employeeName").value("John Doe"))
      .andExpect(jsonPath("$.status").value("CREATED"));
  }

  @Test
  void shouldReturn404WhenEmployeeNotFound() throws Exception {
    // Given
    when(employeeRepository.findById(999L)).thenReturn(Optional.empty());

    // When & Then
    mockMvc.perform(get("/api/employees/999"))
      .andExpect(status().isNotFound());
  }

  @Test
  void shouldSearchByName() throws Exception {
    // Given
    when(employeeRepository.findByEmployeeName("John Doe"))
      .thenReturn(List.of(testRecord1));

    // When & Then
    mockMvc.perform(get("/api/employees/search")
        .param("name", "John Doe"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$", hasSize(1)))
      .andExpect(jsonPath("$[0].employeeName").value("John Doe"));
  }

  @Test
  void shouldGetByStatus() throws Exception {
    // Given
    when(employeeRepository.findByStatus("CREATED"))
      .thenReturn(List.of(testRecord1));

    // When & Then
    mockMvc.perform(get("/api/employees/status/CREATED"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$", hasSize(1)))
      .andExpect(jsonPath("$[0].status").value("CREATED"));
  }

  @Test
  void shouldGetCount() throws Exception {
    // Given
    when(employeeRepository.count()).thenReturn(2L);

    // When & Then
    mockMvc.perform(get("/api/employees/count"))
      .andExpect(status().isOk())
      .andExpect(content().string("2"));
  }
}