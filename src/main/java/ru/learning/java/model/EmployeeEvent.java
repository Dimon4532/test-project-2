package ru.learning.java.model;

public class EmployeeEvent {
  private String name;
  private String status;
  private long timestamp;

  public EmployeeEvent() {
  }

  public EmployeeEvent(String name, String status) {
    this.name = name;
    this.status = status;
    this.timestamp = System.currentTimeMillis();
  }

  public String getName() {
    return name;
  }

  public String getStatus() {
    return status;
  }

  public long getTimestamp() {
    return timestamp;
  }
}