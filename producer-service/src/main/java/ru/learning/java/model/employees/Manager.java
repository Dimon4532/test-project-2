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
  public void conductTraining(String topic, int hoursToAdd) throws TrainingHoursException {
    checkTrainingHours(hoursToAdd);

    System.out.println(getName() + " is conducting training on: " + topic);
    trainingHours += hoursToAdd;
  }

  @Override
  public void setTrainingHours(int trainingHours) throws TrainingHoursException {
    checkTrainingHours(trainingHours);
    this.trainingHours = trainingHours;
  }

  private static void checkTrainingHours(int hoursToAdd) throws TrainingHoursException {
    if (hoursToAdd < 0) {
      throw new TrainingHoursException(
        "Невозможно начислить отрицательное количество часов: " + hoursToAdd
      );
    }
    if (hoursToAdd > 200) {
      throw new TrainingHoursException(
        "Невозможно начислить более 200 часов за одно начисление. Попытка начислить: " + hoursToAdd
      );
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