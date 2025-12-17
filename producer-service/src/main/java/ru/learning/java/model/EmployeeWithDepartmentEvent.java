package ru.learning.java.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.UUID;

public class EmployeeWithDepartmentEvent {

  @JsonProperty("eventId")
  private String eventId;

  @JsonProperty("createdAt")
  private String createdAt;

  @JsonProperty("payload")
  private Payload payload;

  public EmployeeWithDepartmentEvent() {
  }

  public EmployeeWithDepartmentEvent(String nameEmployee, String department) {
    this.eventId = UUID.randomUUID().toString();
    this.createdAt = Instant.now().toString();
    this.payload = new Payload(nameEmployee, department);
  }

  public String getEventId() {
    return eventId;
  }

  public void setEventId(String eventId) {
    this.eventId = eventId;
  }

  public String getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(String createdAt) {
    this.createdAt = createdAt;
  }

  public Payload getPayload() {
    return payload;
  }

  public void setPayload(Payload payload) {
    this.payload = payload;
  }

  public static class Payload {
    @JsonProperty("nameEmployee")
    private String nameEmployee;

    @JsonProperty("department")
    private String department;

    public Payload() {
    }

    public Payload(String nameEmployee, String department) {
      this.nameEmployee = nameEmployee;
      this.department = department;
    }

    public String getNameEmployee() {
      return nameEmployee;
    }

    public void setNameEmployee(String nameEmployee) {
      this.nameEmployee = nameEmployee;
    }

    public String getDepartment() {
      return department;
    }

    public void setDepartment(String department) {
      this.department = department;
    }
  }
}
