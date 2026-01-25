package ru.learning.java.model;

import ru.learning.java.exceptions.InvalidEmployeeException;
import ru.learning.java.exceptions.SalaryException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class ProjectManager {
  private static final BigDecimal MAX_SALARY = new BigDecimal("50000.0");
  private static final BigDecimal MIN_SALARY = BigDecimal.ZERO;
  private final List<Employee> team;

  public ProjectManager() {
    this.team = new ArrayList<>();
  }

  public void addEmployee(Employee employee) throws SalaryException, InvalidEmployeeException {
    validateEmployee(employee);
    team.add(employee);
    System.out.println("Сотрудник " + employee.getName() + " добавлен в команду.");
  }

  private void validateEmployee(Employee employee) throws SalaryException, InvalidEmployeeException {
    if (employee == null) {
      throw new InvalidEmployeeException("Сотрудник не может быть null");
    }

    if (employee.getName() == null || employee.getName().trim().isEmpty()) {
      throw new InvalidEmployeeException("Имя сотрудника не может быть пустым");
    }

    BigDecimal salary = employee.getSalary();
    if (salary.compareTo(MIN_SALARY) < 0) {
      throw new SalaryException("Зарплата не может быть отрицательной: " + salary);
    }

    if (salary.compareTo(MAX_SALARY) > 0) {
      throw new SalaryException("Зарплата слишком большая: " + salary + ". Максимум: " + MAX_SALARY);
    }
  }

  public void assignTasks() {
    System.out.println("\n=== Распределение задач ===");

    for (Employee emp : team) {
      String taskType = getEmployeeType(emp);

      switch (taskType) {
        case "Manager":
          System.out.println("Назначена задача для менеджера " + emp.getName() + ": Управление проектом");
          break;
        case "Developer":
          System.out.println("Назначена задача для разработчика " + emp.getName() + ": Разработка функционала");
          break;
        case "TeamLead":
          System.out.println("Назначена задача для TeamLead'а " + emp.getName() + ": Код-ревью и управление командой");
          break;
        case "QAEngineer":
          System.out.println("Назначена задача для тестировщика " + emp.getName() + ": Тестирование приложения");
          break;
        case "Designer":
          System.out.println("Назначена задача для дизайнера " + emp.getName() + ": Создание макетов");
          break;
        default:
          System.out.println("Назначена задача для сотрудника " + emp.getName() + ": Общая задача");
          break;
      }
    }
  }

  public void assignTasksAlternative() {
    System.out.println("\n=== Альтернативное распределение задач (if-else) ===");

    for (Employee emp : team) {
      if (emp instanceof TeamLead) {
        System.out.println(emp.getName() + " (TeamLead): Код-ревью и координация команды");
      } else if (emp instanceof Developer) {
        System.out.println(emp.getName() + " (Developer): Реализация новых фич");
      } else if (emp instanceof QAEngineer) {
        System.out.println(emp.getName() + " (QA): Написание и выполнение тестов");
      } else if (emp instanceof Designer) {
        System.out.println(emp.getName() + " (Designer): Разработка UI/UX");
      } else if (emp instanceof Manager) {
        System.out.println(emp.getName() + " (Manager): Планирование спринта");
      } else {
        System.out.println(emp.getName() + ": Стандартная рабочая задача");
      }
    }
  }

  public BigDecimal calculateTotalBudget() {
    System.out.println("\n=== Расчёт бюджета проекта ===");

    BigDecimal totalBudget = BigDecimal.ZERO;
    int employeeCount = 0;

    for (Employee emp : team) {
      BigDecimal salary = emp.getSalary();
      totalBudget = totalBudget.add(salary);
      employeeCount++;
      System.out.println(emp.getName() + " - зарплата: " + salary);
    }

    System.out.println("Общее количество сотрудников: " + employeeCount);
    System.out.println("Общий бюджет проекта: " + totalBudget);

    return totalBudget;
  }

  public BigDecimal calculateAverageSalary() {
    if (team.isEmpty()) {
      return BigDecimal.ZERO;
    }

    BigDecimal totalSalary = BigDecimal.ZERO;
    int index = 0;

    while (index < team.size()) {
      totalSalary = totalSalary.add(team.get(index).getSalary());
      index++;
    }

    BigDecimal average = totalSalary.divide(
            BigDecimal.valueOf(team.size()),
            2,
            RoundingMode.HALF_UP
    );
    System.out.println("Средняя зарплата в команде: " + average);
    return average;
  }

  public void checkEmployeeAvailability(String employeeName) {
    System.out.println("\n=== Проверка доступности сотрудника ===");

    boolean found = false;
    Employee foundEmployee = null;

    for (Employee emp : team) {
      if (emp.getName().equalsIgnoreCase(employeeName)) {
        found = true;
        foundEmployee = emp;
        break;
      }
    }

    if (found) {
      System.out.println("Сотрудник " + employeeName + " найден в команде.");

      if (foundEmployee.getSalary().compareTo(BigDecimal.ZERO) > 0) {
        System.out.println("Статус: Активный сотрудник");

        if (foundEmployee instanceof TeamLead) {
          System.out.println("Роль: Тимлид - может быть занят управлением командой");
        } else if (foundEmployee instanceof Manager) {
          System.out.println("Роль: Менеджер - может быть на встречах");
        } else {
          System.out.println("Роль: Исполнитель - доступен для задач");
        }
      } else {
        System.out.println("Статус: Неактивный сотрудник (нулевая зарплата)");
      }
    } else {
      System.out.println("Сотрудник " + employeeName + " не найден в команде.");
    }
  }

  public void checkAllEmployeesAvailability() {
    System.out.println("\n=== Проверка доступности всех сотрудников ===");

    int availableCount = 0;
    int unavailableCount = 0;

    for (Employee emp : team) {
      if (emp.getSalary().compareTo(BigDecimal.ZERO) > 0 && emp.getName() != null && !emp.getName().isEmpty()) {
        System.out.println("✓ " + emp.getName() + " - Доступен");
        availableCount++;
      } else {
        System.out.println("✗ " + emp.getName() + " - Недоступен");
        unavailableCount++;
      }
    }

    System.out.println("\nДоступно: " + availableCount + ", Недоступно: " + unavailableCount);
  }

  private String getEmployeeType(Employee emp) {
    if (emp instanceof TeamLead) {
      return "TeamLead";
    } else if (emp instanceof Developer) {
      return "Developer";
    } else if (emp instanceof QAEngineer) {
      return "QAEngineer";
    } else if (emp instanceof Designer) {
      return "Designer";
    } else if (emp instanceof Manager) {
      return "Manager";
    }
    return "Unknown";
  }

  public List<Employee> getTeam() {
    return team;
  }
}