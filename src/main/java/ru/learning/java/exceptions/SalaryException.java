package ru.learning.java.exceptions;

public class SalaryException extends Exception {
  public SalaryException(String message) {
    super(message);
  }

  public SalaryException(String message, Throwable cause) {
    super(message, cause);
  }
}
