package ru.learning.java.interfaces;

import ru.learning.java.exceptions.TrainingHoursException;

public interface Trainable {
  void conductTraining(String topic) throws TrainingHoursException;

  int getTrainingHours();
}