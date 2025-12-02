package ru.learning.java;

import ru.learning.java.company.BonusBudget;
import ru.learning.java.company.CompanyDirectory;
import ru.learning.java.company.ReportGenerator;
import ru.learning.java.company.Team;
import ru.learning.java.employees.Designer;
import ru.learning.java.employees.Developer;
import ru.learning.java.employees.Employee;
import ru.learning.java.employees.EmployeeFileManager;
import ru.learning.java.employees.Manager;
import ru.learning.java.employees.ProjectManager;
import ru.learning.java.employees.QAEngineer;
import ru.learning.java.employees.TeamLead;
import ru.learning.java.exceptions.InvalidEmployeeException;
import ru.learning.java.exceptions.SalaryException;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

  private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

  public static void main(String[] args) {
    System.out.println("=== ОБНОВЛЕННЫЙ ПРОЕКТ С НОВЫМИ СОТРУДНИКАМИ ПРОЕКТА ===\n");

    ProjectManager projectManager = new ProjectManager();

    try {
      System.out.println("=== Создание и найм сотрудников в команду ===");

      Manager manager = new Manager();
      manager.setName("Alice");
      manager.setSalary(5000);
      manager.setId("id-manager");
      manager.setDepartment("Flow");
      projectManager.addEmployee(manager);

      Developer developer = new Developer();
      developer.setName("Bob");
      developer.setSalary(4500);
      developer.setId("id-dev");
      developer.setDepartment("Designer");
      projectManager.addEmployee(developer);

      TeamLead teamLead = new TeamLead();
      teamLead.setName("Charlie");
      teamLead.setSalary(6000);
      teamLead.setTeamSize(5);
      teamLead.setId("id-team-lead");
      manager.setDepartment("Flow");
      projectManager.addEmployee(teamLead);

      QAEngineer qaEngineer = new QAEngineer();
      qaEngineer.setName("Diana");
      qaEngineer.setSalary(4000);
      qaEngineer.setId("id-qa");
      qaEngineer.setDepartment("Engine");
      projectManager.addEmployee(qaEngineer);

      Designer designer = new Designer();
      designer.setName("Eve");
      designer.setSalary(4200);
      designer.setId("id-designer");
      designer.setDepartment("Designer");
      projectManager.addEmployee(designer);

      System.out.println("\n=== Полиморфизм в действии ===");
      for (Employee emp : projectManager.getTeam()) {
        emp.work();
      }

      projectManager.assignTasks();
      projectManager.assignTasksAlternative();

      projectManager.calculateAverageSalary();

      projectManager.checkEmployeeAvailability("Charlie");
      projectManager.checkEmployeeAvailability("John"); // Не существует
      projectManager.checkAllEmployeesAvailability();

      CompanyDirectory companyDirectory = new CompanyDirectory();
      companyDirectory.addEmployee(manager);
      companyDirectory.addEmployee(designer);
      companyDirectory.addEmployee(developer);

      companyDirectory.findByIdMap("id-manager");


      new ReportGenerator().generateReports(20);

      System.out.println("\n=== Запись данных в файл (try-with-resources) ===");
      try (EmployeeFileManager fileManager = new EmployeeFileManager("employees.txt")) {
        for (Employee emp : projectManager.getTeam()) {
          fileManager.writeEmployee(emp);
        }
        System.out.println("Данные всех сотрудников записаны в файл.");
      } catch (Exception e) {
        System.err.println("Ошибка при работе с файлом: " + e.getMessage());
      }

      System.out.println("\n=== Демонстрация обработки исключений ===");

      try {
        Employee invalidEmployee1 = new Developer();
        invalidEmployee1.setName("Invalid Dev");
        invalidEmployee1.setSalary(-1000);
        projectManager.addEmployee(invalidEmployee1);
      } catch (SalaryException e) {
        System.out.println("❌ Поймано исключение SalaryException: " + e.getMessage());
      } catch (InvalidEmployeeException e) {
        System.out.println("❌ Поймано исключение InvalidEmployeeException: " + e.getMessage());
      }

      try {
        Employee invalidEmployee2 = new Manager();
        invalidEmployee2.setName("Rich Manager");
        invalidEmployee2.setSalary(100000);
        projectManager.addEmployee(invalidEmployee2);
      } catch (SalaryException e) {
        System.out.println("❌ Поймано исключение SalaryException: " + e.getMessage());
      } catch (InvalidEmployeeException e) {
        System.out.println("❌ Поймано исключение InvalidEmployeeException: " + e.getMessage());
      }

      try {
        Employee invalidEmployee3 = new Designer();
        invalidEmployee3.setName(""); // Пустое имя
        invalidEmployee3.setSalary(4000);
        projectManager.addEmployee(invalidEmployee3);
      } catch (SalaryException e) {
        System.out.println("❌ Поймано исключение SalaryException: " + e.getMessage());
      } catch (InvalidEmployeeException e) {
        System.out.println("❌ Поймано исключение InvalidEmployeeException: " + e.getMessage());
      }

      System.out.println("\n=== Демонстрация Generics ===");

      Team<Developer> devTeam = new Team<>("Super Developers");

      Developer javaDev = new Developer();
      javaDev.setName("Java Guru");
      javaDev.setSalary(7000);

      Developer pythonDev = new Developer();
      pythonDev.setName("Python Expert");
      pythonDev.setSalary(6800);

      devTeam.addParticipant(javaDev);
      devTeam.addParticipant(pythonDev);

      //А эту строку лучше прикопать для демонстрации защиты команды от "неподходящих" типов
      // devTeam.addParticipant(manager);

      devTeam.playTeam();

      Team<Employee> mixedTeam = new Team<>("Mixed Squad");
      mixedTeam.addParticipant(manager);
      mixedTeam.addParticipant(designer);
      mixedTeam.playTeam();

      System.out.println("\n=== Демонстрация потокобезопасных типов ===");
      try {
        new BonusBudget().runRaceConditionDemo();
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      System.out.println("\n=== Программа завершена успешно ===");

    } catch (SalaryException e) {
      LOGGER.log(Level.SEVERE, "Ошибка зарплаты: " + e.getMessage(), e);
    } catch (InvalidEmployeeException e) {
      LOGGER.log(Level.SEVERE, "Ошибка данных сотрудника: " + e.getMessage(), e);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Неожиданная ошибка: " + e.getMessage(), e);
    }
  }
}