package ru.learning.java.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EmployeeEvent {

  @JsonProperty("name")
  private String name;

  @JsonProperty("status")
  private String status;

  @JsonProperty("timestamp")
  private Long timestamp;

  public EmployeeEvent() {
  }

  public EmployeeEvent(String name, String status, Long timestamp) {
    this.name = name;
    this.status = status;
    this.timestamp = timestamp;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Long timestamp) {
    this.timestamp = timestamp;
  }

  @Override
  public String toString() {
    return "EmployeeEvent{" +
      "name='" + name + '\'' +
      ", status='" + status + '\'' +
      ", timestamp=" + timestamp +
      '}';
  }
}