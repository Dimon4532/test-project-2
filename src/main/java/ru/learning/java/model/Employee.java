package ru.learning.java.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import ru.learning.java.company.Department;
import ru.learning.java.exceptions.SalaryException;


@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.PROPERTY,
  property = "type"
)
@JsonSubTypes({
  @JsonSubTypes.Type(value = Developer.class, name = "developer"),
  @JsonSubTypes.Type(value = Manager.class, name = "manager"),
  @JsonSubTypes.Type(value = HRManager.class, name = "hr"),
  @JsonSubTypes.Type(value = ProjectManager.class, name = "projectManager"),
  @JsonSubTypes.Type(value = QAEngineer.class, name = "qa"),
  @JsonSubTypes.Type(value = TeamLead.class, name = "teamLead"),

})
public abstract class Employee {
  private String name;
  private double salary;
  private String id;
  private Department department;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Department getDepartment() {
    return department;
  }

  public void setDepartment(Department department) {
    this.department = department;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public double getSalary() {
    return salary;
  }

  public void setSalary(double salary) throws SalaryException {
    if (salary < 0) {
      throw new SalaryException("Зарплата не может быть отрицательной: " + salary);
    }
    if (salary > 50000) {
      throw new SalaryException("Зарплата слишком большая: " + salary);
    }
    this.salary = salary;
  }

  public void work() {
    System.out.println(name + " is working.");
  }
}