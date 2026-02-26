
package ru.learning.java.company;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.learning.java.exceptions.SalaryException;
import ru.learning.java.model.employees.Developer;
import ru.learning.java.model.employees.Employee;
import ru.learning.java.model.employees.Manager;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты для CompanyDirectory")
class CompanyDirectoryTest {

  private CompanyDirectory directory;
  private Employee emp1;
  private Employee emp2;
  private Employee emp3;

  @BeforeEach
  void setUp() throws SalaryException {
    directory = new CompanyDirectory();

    emp1 = createEmployee("001", "Иван Иванов", Department.IT, new BigDecimal("40000"));
    emp2 = createEmployee("002", "Мария Петрова", Department.HR, new BigDecimal("35000"));
    emp3 = createEmployee("003", "Петр Сидоров", Department.IT, new BigDecimal("45000"));
  }

  @Test
  @DisplayName("Добавление сотрудника в справочник")
  void testAddEmployee() {
    directory.addEmployee(emp1);

    Employee found = directory.findByIdMap("001");
    assertNotNull(found, "Сотрудник должен быть найден");
    assertEquals("Иван Иванов", found.getName());
  }

  @Test
  @DisplayName("Поиск сотрудника по ID через список")
  void testFindByIdList() {
    directory.addEmployee(emp1);
    directory.addEmployee(emp2);

    Employee found = directory.findByIdList("002");
    assertNotNull(found, "Сотрудник должен быть найден");
    assertEquals("Мария Петрова", found.getName());
    assertEquals(Department.HR, found.getDepartment());
  }

  @Test
  @DisplayName("Поиск несуществующего сотрудника через список возвращает null")
  void testFindByIdListNotFound() {
    directory.addEmployee(emp1);

    Employee found = directory.findByIdList("999");
    assertNull(found, "Несуществующий сотрудник должен вернуть null");
  }

  @Test
  @DisplayName("Поиск сотрудника по ID через Map")
  void testFindByIdMap() {
    directory.addEmployee(emp1);
    directory.addEmployee(emp3);

    Employee found = directory.findByIdMap("003");
    assertNotNull(found, "Сотрудник должен быть найден");
    assertEquals("Петр Сидоров", found.getName());
  }

  @Test
  @DisplayName("Получение списка всех департаментов")
  void testGetDepartments() {
    directory.addEmployee(emp1);
    directory.addEmployee(emp2);
    directory.addEmployee(emp3);

    Set<Department> departments = directory.getDepartments();

    assertEquals(2, departments.size(), "Должно быть 2 уникальных департамента");
    assertTrue(departments.contains(Department.IT));
    assertTrue(departments.contains(Department.HR));
  }

  @Test
  @DisplayName("Получение сотрудников по департаменту")
  void testGetEmployeesByDepartment() {
    directory.addEmployee(emp1);
    directory.addEmployee(emp2);
    directory.addEmployee(emp3);

    List<Employee> itEmployees = directory.getEmployeesByDepartment(Department.IT);

    assertEquals(2, itEmployees.size(), "В IT департаменте должно быть 2 сотрудника");
    assertTrue(itEmployees.stream().anyMatch(e -> e.getName().equals("Иван Иванов")));
    assertTrue(itEmployees.stream().anyMatch(e -> e.getName().equals("Петр Сидоров")));
  }

  @Test
  @DisplayName("Получение сотрудников из пустого департамента")
  void testGetEmployeesByEmptyDepartment() {
    directory.addEmployee(emp1);

    List<Employee> salesEmployees = directory.getEmployeesByDepartment(Department.SALES);

    assertTrue(salesEmployees.isEmpty(), "Пустой департамент должен вернуть пустой список");
  }

  @Test
  @DisplayName("Департаменты обновляются при добавлении сотрудников")
  void testDepartmentsUpdatedOnAdd() {
    assertTrue(directory.getDepartments().isEmpty(), "Изначально список департаментов пуст");

    directory.addEmployee(emp1);
    assertEquals(1, directory.getDepartments().size());

    directory.addEmployee(emp2);
    assertEquals(2, directory.getDepartments().size());

    directory.addEmployee(emp3); // Тот же департамент IT
    assertEquals(2, directory.getDepartments().size(), "Дубликаты департаментов не добавляются");
  }

  private Employee createEmployee(String id, String name, Department department, BigDecimal salary) throws SalaryException {
    Employee emp;

    // Создаём Developer для IT департамента, Manager для остальных
    if (department == Department.IT) {
      emp = new Developer();
    } else {
      emp = new Manager();
    }

    emp.setId(id);
    emp.setName(name);
    emp.setDepartment(department);
    emp.setSalary(salary);

    return emp;
  }
}