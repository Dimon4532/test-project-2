package ru.learning.java.model.employees;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import ru.learning.java.interfaces.Trainable;
import ru.learning.java.interfaces.WriterDoc;

@Entity
@DiscriminatorValue("Manager")
public final class Manager extends Employee implements Trainable, WriterDoc {

  @Column(name = "training_hours")
  private int trainingHours = 0;

  @Override
  public void work() {
    System.out.println(getName() + " is managing the team.");
  }

  @Override
  public void conductTraining(String topic) {
    System.out.println(getName() + " is conducting training on: " + topic);
    trainingHours += 2;
  }

  @Override
  public int getTrainingHours() {
    return trainingHours;
  }

  @Override
  public String writeDocument() {
    return "empty_document";
  }
}