package ru.learning.java.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("TeamLead")
public class TeamLead extends Developer {

  @Column(name = "team_size")
  private int teamSize = 0;

  @Override
  public void work() {
    System.out.println(getName() + " is managing the development team and coding.");
  }

  @Override
  public void drinkCoffee() {
    System.out.println(getName() + " is drinking coffee with the development team.");
  }

  public void assignTask(Employee employee, String task) {
    System.out.println(getName() + " assigned task '" + task + "' to " + employee.getName());
  }

  public int getTeamSize() {
    return teamSize;
  }

  public void setTeamSize(int teamSize) {
    this.teamSize = teamSize;
  }
}
