package ru.learning.java.employees;

import ru.learning.java.exceptions.SalaryException;

public abstract class Employee {
  private String name;
  private double salary;

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
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

  public double getSalary() {
    return salary;
  }

  public void work() {
    System.out.println(name + " is working.");
  }
}