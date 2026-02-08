package ru.learning.java.model.employees;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Developer")
public class Developer extends Employee {
  @Override
  public void work() {
    System.out.println(getName() + " is coding.");
  }

  public void drinkCoffee() {
    System.out.println(getName() + " is drink coffee.");
  }
}