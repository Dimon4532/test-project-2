package ru.learning.java.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import ru.learning.java.company.Department;

@Entity
@DiscriminatorValue("HRManager")
public final class HRManager extends Employee {

  @Column(name = "candidates_interviewed")
  private int candidatesInterviewed = 0;

  public HRManager() {
    this.setDepartment(Department.HR);
  }

  @Override
  public void work() {
    System.out.println(getName() + " (HR) просматривает резюме и обновляет базу данных сотрудников.");
  }

  public void conductInterview(String candidateName) {
    System.out.println(getName() + " проводит собеседование с кандидатом: " + candidateName);
    candidatesInterviewed++;
  }

  public void organizeTeamBuilding() {
    System.out.println(getName() + " организует тимбилдинг для поднятия боевого духа!");
  }

  public int getCandidatesInterviewed() {
    return candidatesInterviewed;
  }
}
