package ru.learning.java.repository;

import org.springframework.stereotype.Repository;
import ru.learning.java.company.Department;
import ru.learning.java.model.employees.Employee;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
public class EmployeeRepository {

  private static final Logger LOGGER = Logger.getLogger(EmployeeRepository.class.getName());
  private final EmployeeJpaRepository jpaRepository;

  public EmployeeRepository(EmployeeJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  /**
   * Получить всех сотрудников (старый метод getAllEmployees)
   */
  public List<Employee> getAllEmployees() {
    return jpaRepository.findAll();
  }

  /**
   * Сохранить сотрудника с валидацией департамента
   */
  public void save(Employee employee) {
    validateEmployee(employee);

    try {
      jpaRepository.save(employee);
      LOGGER.info("Сотрудник " + employee.getName() + " успешно сохранен в БД");
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Ошибка при сохранении сотрудника в БД: " + employee.getName(), e);
      throw new RuntimeException("Ошибка при сохранении сотрудника в БД", e);
    }
  }

  /**
   * Обновить сотрудника с валидацией и проверкой существования
   */
  public void update(Employee updatedEmployee) {
    validateEmployee(updatedEmployee);

    boolean exists = false;

    if (updatedEmployee.getId() != null) {
      exists = jpaRepository.existsById(updatedEmployee.getId());
    }

    if (!exists && updatedEmployee.getName() != null) {
      exists = jpaRepository.findByName(updatedEmployee.getName()).isPresent();
      if (exists) {
        LOGGER.warning("Сотрудник найден по имени, а не по ID. Рекомендуется использовать ID.");
      }
    }

    if (!exists) {
      throw new RuntimeException("Сотрудник с ID " + updatedEmployee.getId() + " не найден");
    }

    try {
      jpaRepository.save(updatedEmployee);
      LOGGER.info("Сотрудник " + updatedEmployee.getName() + " успешно обновлен");
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Ошибка при обновлении сотрудника: " + updatedEmployee.getName(), e);
      throw new RuntimeException("Ошибка при обновлении базы данных", e);
    }
  }

  /**
   * Валидация сотрудника (из старой логики mapToString)
   */
  private void validateEmployee(Employee employee) {
    if (employee == null) {
      throw new IllegalArgumentException("Сотрудник не может быть null");
    }

    if (employee.getDepartment() == null) {
      throw new IllegalStateException("У сотрудника " + employee.getName() + " не установлен департамент!");
    }

    if (employee.getName() == null || employee.getName().trim().isEmpty()) {
      throw new IllegalArgumentException("Имя сотрудника не может быть пустым");
    }
  }

  /**
   * Дополнительные методы для удобства
   */
  public List<Employee> findByDepartment(Department department) {
    return jpaRepository.findByDepartment(department);
  }

  public Employee findById(String id) {
    return jpaRepository.findById(id).orElseThrow(() -> new RuntimeException("Сотрудник с ID " + id + " не найден"));
  }
}