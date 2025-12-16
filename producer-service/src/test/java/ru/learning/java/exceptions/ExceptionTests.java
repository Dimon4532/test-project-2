package ru.learning.java.exceptions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты для пользовательских исключений")
class ExceptionTests {

  @Test
  @DisplayName("Проверка создания SalaryException с сообщением")
  void testSalaryExceptionWithMessage() {
    String message = "Некорректная зарплата";
    SalaryException exception = new SalaryException(message);

    assertNotNull(exception, "Исключение не должно быть null");
    assertEquals(message, exception.getMessage(), "Сообщение должно совпадать");
  }

  @Test
  @DisplayName("Проверка создания SalaryException с причиной")
  void testSalaryExceptionWithCause() {
    String message = "Ошибка зарплаты";
    Throwable cause = new IllegalArgumentException("Исходная ошибка");
    SalaryException exception = new SalaryException(message, cause);

    assertNotNull(exception, "Исключение не должно быть null");
    assertEquals(message, exception.getMessage(), "Сообщение должно совпадать");
    assertEquals(cause, exception.getCause(), "Причина должна быть сохранена");
  }

  @Test
  @DisplayName("Проверка создания InvalidEmployeeException с сообщением")
  void testInvalidEmployeeExceptionWithMessage() {
    String message = "Некорректные данные сотрудника";
    InvalidEmployeeException exception = new InvalidEmployeeException(message);

    assertNotNull(exception, "Исключение не должно быть null");
    assertEquals(message, exception.getMessage(), "Сообщение должно совпадать");
  }

  @Test
  @DisplayName("Проверка создания InvalidEmployeeException с причиной")
  void testInvalidEmployeeExceptionWithCause() {
    String message = "Ошибка данных";
    Throwable cause = new NullPointerException("Null значение");
    InvalidEmployeeException exception = new InvalidEmployeeException(message, cause);

    assertNotNull(exception, "Исключение не должно быть null");
    assertEquals(message, exception.getMessage(), "Сообщение должно совпадать");
    assertEquals(cause, exception.getCause(), "Причина должна быть сохранена");
  }

  @Test
  @DisplayName("Проверка, что исключения наследуются от Exception")
  void testExceptionsInheritance() {
    SalaryException salaryException = new SalaryException("Test");
    InvalidEmployeeException employeeException = new InvalidEmployeeException("Test");

    assertTrue(salaryException instanceof Exception, "SalaryException должно наследоваться от Exception");
    assertTrue(employeeException instanceof Exception, "InvalidEmployeeException должно наследоваться от Exception");
  }

  @ParameterizedTest
  @DisplayName("Параметризованный тест различных сообщений об ошибках зарплаты")
  @ValueSource(strings = {
    "Зарплата отрицательная",
    "Зарплата слишком большая",
    "Некорректное значение зарплаты"
  })
  void testSalaryExceptionMessages(String message) {
    SalaryException exception = new SalaryException(message);
    assertEquals(message, exception.getMessage());
    assertNotNull(exception.toString());
  }
}