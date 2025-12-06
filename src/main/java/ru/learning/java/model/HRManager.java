package ru.learning.java.model;

import ru.learning.java.company.Department;

public class HRManager extends Employee {

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
