package ru.learning.java.model.employees;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("QAEngineer")
public final class QAEngineer extends Employee {

  @Column(name = "bugs_found")
  private int bugsFound = 0;

  @Override
  public void work() {
    System.out.println(getName() + " is testing the application.");
  }

  public void findBug() {
    bugsFound++;
    System.out.println(getName() + " found a bug! Total bugs found: " + bugsFound);
  }

  public int getBugsFound() {
    return bugsFound;
  }
}
