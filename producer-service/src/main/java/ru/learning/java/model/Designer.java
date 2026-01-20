package ru.learning.java.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("DESIGNER")
public class Designer extends Employee {
  @Override
  public void work() {
    System.out.println(getName() + " is working on the design.");
  }

  public void createMockup(String projectName) {
    System.out.println(getName() + " created a mockup for: " + projectName);
  }
}
