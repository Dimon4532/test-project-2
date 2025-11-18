package ru.learning.java.employees;

public class Developer extends Employee {
  @Override
  public void work() {
    System.out.println(getName() + " is coding.");
  }

  public void drinkCoffee() {
    System.out.println(getName() + " is drink coffee.");
  }
}