package ru.learning.java.exceptions;

public class InvalidEmployeeException extends Exception {
  public InvalidEmployeeException(String message) {
    super(message);
  }

  public InvalidEmployeeException(String message, Throwable cause) {
    super(message, cause);
  }
}
