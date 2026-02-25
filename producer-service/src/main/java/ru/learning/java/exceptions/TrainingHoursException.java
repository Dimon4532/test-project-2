package ru.learning.java.exceptions;

public class TrainingHoursException extends Exception {
  public TrainingHoursException(String message) {
    super(message);
  }

  public TrainingHoursException(String message, Throwable cause) {
    super(message, cause);
  }
}
