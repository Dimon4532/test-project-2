package ru.learning.java.company;

import ru.learning.java.model.Employee;
import java.util.ArrayList;
import java.util.List;

public class Team<T extends Employee> {
  private final String teamName;
  private final List<T> participants = new ArrayList<>();

  public Team(String teamName) {
    this.teamName = teamName;
  }

  public void addParticipant(T participant) {
    participants.add(participant);
    System.out.println("В команду " + teamName + " добавлен сотрудник: " + participant.getName());
  }

  public void playTeam() {
    System.out.println("Команда " + teamName + " начинает работу:");
    for (T participant : participants) {
      participant.work();
    }
  }
}