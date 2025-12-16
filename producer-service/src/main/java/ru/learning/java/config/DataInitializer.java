package ru.learning.java.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.learning.java.company.Department;
import ru.learning.java.model.Designer;
import ru.learning.java.model.Developer;
import ru.learning.java.model.HRManager;
import ru.learning.java.model.Manager;
import ru.learning.java.model.QAEngineer;
import ru.learning.java.model.TeamLead;
import ru.learning.java.service.EmployeeService;

import java.util.logging.Logger;

@Component
public class DataInitializer implements CommandLineRunner {

  private static final Logger LOGGER = Logger.getLogger(DataInitializer.class.getName());
  private final EmployeeService employeeService;

  public DataInitializer(EmployeeService employeeService) {
    this.employeeService = employeeService;
  }

  @Override
  public void run(String... args) {
    if (!employeeService.findAll().isEmpty()) {
      LOGGER.info("База данных не пуста, пропускаем инициализацию.");
      return;
    }

    LOGGER.info("=== Инициализация начальных данных ===");

    try {
      Manager manager = new Manager();
      manager.setName("Alice");
      manager.setSalary(5000);
      manager.setId("id-manager");
      manager.setDepartment(Department.FLOW);
      employeeService.hireEmployee(manager);

      Developer developer = new Developer();
      developer.setName("Bob");
      developer.setSalary(4500);
      developer.setId("id-dev");
      developer.setDepartment(Department.DESIGN);
      employeeService.hireEmployee(developer);

      Developer developerFlow = new Developer();
      developerFlow.setName("DIM");
      developerFlow.setSalary(5500);
      developerFlow.setId("id-dev");
      developerFlow.setDepartment(Department.FLOW);
      employeeService.hireEmployee(developerFlow);

      TeamLead teamLead = new TeamLead();
      teamLead.setName("Charlie");
      teamLead.setSalary(6000);
      teamLead.setTeamSize(5);
      teamLead.setId("id-team-lead");
      teamLead.setDepartment(Department.FLOW);
      employeeService.hireEmployee(teamLead);

      QAEngineer qaEngineer = new QAEngineer();
      qaEngineer.setName("Diana");
      qaEngineer.setSalary(4000);
      qaEngineer.setId("id-qa");
      qaEngineer.setDepartment(Department.ENGINE);
      employeeService.hireEmployee(qaEngineer);

      Designer designer = new Designer();
      designer.setName("Eve");
      designer.setSalary(4200);
      designer.setId("id-designer");
      designer.setDepartment(Department.DESIGN);
      employeeService.hireEmployee(designer);

      HRManager hrManager = new HRManager();
      hrManager.setName("Ivan");
      hrManager.setSalary(4800);
      hrManager.setId("id-hr");
      hrManager.setDepartment(Department.HR);
      employeeService.hireEmployee(hrManager);

      LOGGER.info("Сотрудники успешно добавлены.");

    } catch (Exception e) {
      LOGGER.severe("Ошибка при инициализации данных: " + e.getMessage());
    }
  }
}