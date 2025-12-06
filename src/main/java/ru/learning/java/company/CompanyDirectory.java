package ru.learning.java.company;

import ru.learning.java.model.Employee;

import java.util.*;

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
}
