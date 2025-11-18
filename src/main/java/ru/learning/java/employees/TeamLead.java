package ru.learning.java.employees;

public class TeamLead extends Developer {
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

  public void setTeamSize(int teamSize) {
    this.teamSize = teamSize;
  }

  public int getTeamSize() {
    return teamSize;
  }
}
