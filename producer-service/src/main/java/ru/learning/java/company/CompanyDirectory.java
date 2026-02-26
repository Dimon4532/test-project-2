package ru.learning.java.company;

import ru.learning.java.model.employees.Employee;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CompanyDirectory {
  private final List<Employee> allEmployees = new ArrayList<>();

  private final Map<String, Employee> employeesById = new HashMap<>();

  private final Set<Department> departments = new HashSet<>();

  public void addEmployee(Employee emp) {
    allEmployees.add(emp);
    employeesById.put(emp.getId(), emp);
    departments.add(emp.getDepartment());
  }

  public Employee findByIdList(String id) {
    for (Employee e : allEmployees) {
      if (e.getId().equals(id)) {
        System.out.println("Найден следующий сотрудник " + e.getName());
        return e;
      }
    }
    return null;
  }

  public Employee findByIdMap(String id) {
    System.out.println("Найден следующий сотрудник " + employeesById.get(id).getName());
    return employeesById.get(id);
  }

  public Set<Department> getDepartments() {
    return departments;
  }

  public List<Employee> getEmployeesByDepartment(Department department) {
    return allEmployees.stream()
      .filter(e -> e.getDepartment() == department)
      .toList();
  }
}
