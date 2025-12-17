package ru.learning.java.kafka;

public final class Topics {
  private Topics() {}

  public static final String EMPLOYEE_RAW = "company.employee.raw";
  public static final String EMPLOYEE_VALIDATED = "company.employee.validated";
  public static final String EMPLOYEE_DLQ = "company.employee.dlq";
}
