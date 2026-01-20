package ru.learning.java.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.learning.java.company.Department;
import ru.learning.java.model.Employee;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeJpaRepository extends JpaRepository<Employee, String> {
    List<Employee> findByDepartment(Department department);
    List<Employee> findByNameContaining(String name);
    Optional<Employee> findByName(String name);
}
