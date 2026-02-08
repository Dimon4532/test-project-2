package ru.learning.java.model.employees;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import ru.learning.java.company.Department;
import ru.learning.java.exceptions.SalaryException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.PROPERTY,
  property = "type"
)
@JsonSubTypes({
  @JsonSubTypes.Type(value = Developer.class, name = "Developer"),
  @JsonSubTypes.Type(value = Manager.class, name = "Manager"),
  @JsonSubTypes.Type(value = HRManager.class, name = "Hr"),
  @JsonSubTypes.Type(value = ProjectManager.class, name = "ProjectManager"),
  @JsonSubTypes.Type(value = QAEngineer.class, name = "QAEngineer"),
  @JsonSubTypes.Type(value = TeamLead.class, name = "TeamLead"),
  @JsonSubTypes.Type(value = Designer.class, name = "Designer")
})
@Entity
@Table(name = "employees")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "employee_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Employee {

  @Id
  @Column(name = "id", nullable = false)
  private String id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "salary", nullable = false)
  private BigDecimal salary;

  @Enumerated(EnumType.STRING)
  @Column(name = "department", nullable = false)
  private Department department;

  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @PrePersist
  protected void onCreate() {
    createdAt = LocalDateTime.now();
    updatedAt = LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
    updatedAt = LocalDateTime.now();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BigDecimal getSalary() {
    return salary;
  }

  public void setSalary(BigDecimal salary) throws SalaryException {
    if (salary.compareTo(BigDecimal.ZERO) < 0) {
      throw new SalaryException("Зарплата не может быть отрицательной: " + salary);
    }
    if (salary.compareTo(new BigDecimal("50000")) > 0) {
      throw new SalaryException("Зарплата слишком большая: " + salary);
    }
    this.salary = salary;
  }

  public Department getDepartment() {
    return department;
  }

  public void setDepartment(Department department) {
    this.department = department;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public void work() {
    System.out.println(name + " is working.");
  }
}