package ru.learning.java.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import ru.learning.java.company.Department;
import ru.learning.java.model.EmployeeDocument;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface EmployeeSearchRepository extends ElasticsearchRepository<EmployeeDocument, String> {

  /**
   * Поиск сотрудников по имени (полнотекстовый).
   */
  List<EmployeeDocument> findByNameContaining(String name);

  /**
   * Поиск по департаменту.
   */
  List<EmployeeDocument> findByDepartment(Department department);

  /**
   * Поиск по типу сотрудника.
   */
  List<EmployeeDocument> findByEmployeeType(String employeeType);

  /**
   * Поиск сотрудников с зарплатой больше указанной.
   */
  List<EmployeeDocument> findBySalaryGreaterThan(BigDecimal salary);
}