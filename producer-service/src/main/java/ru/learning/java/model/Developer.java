package ru.learning.java.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("DEVELOPER")
public class Developer extends Employee {
  @Override
  public void work() {
    System.out.println(getName() + " is coding.");
  }

  public void drinkCoffee() {
    System.out.println(getName() + " is drink coffee.");
  }
}