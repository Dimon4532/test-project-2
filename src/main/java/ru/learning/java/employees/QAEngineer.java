package ru.learning.java.employees;

public class QAEngineer extends Employee {
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
