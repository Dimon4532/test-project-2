package ru.learning.java.model.employees;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import ru.learning.java.exceptions.TrainingHoursException;
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
  public void conductTraining(String topic) throws TrainingHoursException {
    int hoursToAdd = 2;
    validateTrainingHours(hoursToAdd);

    System.out.println(getName() + " is conducting training on: " + topic);
    trainingHours += hoursToAdd;
  }

  private void validateTrainingHours(int hours) throws TrainingHoursException {
    if (hours < 0) {
      throw new TrainingHoursException("Невозможно начислить отрицательное количество часов: " + hours);
    }
    if (hours > 200) {
      throw new TrainingHoursException("Невозможно начислить более 200 часов за одно начисление. Попытка начислить: " + hours);
    }
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