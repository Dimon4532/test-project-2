package ru.learning.java.company;

public enum Department {
  IT("Information Technology"),
  HR("Human Resources"),
  SALES("Sales Department"),
  DESIGN("Creative Design"),
  FLOW("Flow Department"),
  ENGINE("Engine Department");

  private final String fullName;

  Department(String fullName) {
    this.fullName = fullName;
  }

  public String getFullName() {
    return fullName;
  }
}