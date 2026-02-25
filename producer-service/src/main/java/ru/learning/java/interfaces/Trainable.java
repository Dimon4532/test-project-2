package ru.learning.java.interfaces;

import ru.learning.java.exceptions.TrainingHoursException;

public interface Trainable {
  void conductTraining(String topic, int trainingHours) throws TrainingHoursException;

  void setTrainingHours(int trainingHours) throws TrainingHoursException;

  int getTrainingHours();
}