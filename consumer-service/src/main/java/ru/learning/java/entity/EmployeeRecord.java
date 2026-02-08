package ru.learning.java.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "employee_records")
public class EmployeeRecord {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "employee_name", nullable = false)
  private String employeeName;

  @Column(name = "status", nullable = false)
  private String status;

  @Column(name = "event_timestamp")
  private Long eventTimestamp;

  @Column(name = "received_at", nullable = false)
  private LocalDateTime receivedAt;

  public EmployeeRecord() {
  }

  public EmployeeRecord(String employeeName, String status, Long eventTimestamp) {
    this.employeeName = employeeName;
    this.status = status;
    this.eventTimestamp = eventTimestamp;
    this.receivedAt = LocalDateTime.now();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getEmployeeName() {
    return employeeName;
  }

  public void setEmployeeName(String employeeName) {
    this.employeeName = employeeName;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Long getEventTimestamp() {
    return eventTimestamp;
  }

  public void setEventTimestamp(Long eventTimestamp) {
    this.eventTimestamp = eventTimestamp;
  }

  public LocalDateTime getReceivedAt() {
    return receivedAt;
  }

  public void setReceivedAt(LocalDateTime receivedAt) {
    this.receivedAt = receivedAt;
  }

  @Override
  public String toString() {
    return "EmployeeRecord{" +
      "id=" + id +
      ", employeeName='" + employeeName + '\'' +
      ", status='" + status + '\'' +
      ", eventTimestamp=" + eventTimestamp +
      ", receivedAt=" + receivedAt +
      '}';
  }
}