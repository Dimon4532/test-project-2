package ru.learning.java.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.learning.java.entity.EmployeeRecord;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeRecord, Long> {

  List<EmployeeRecord> findByEmployeeName(String employeeName);

  List<EmployeeRecord> findByStatus(String status);

  List<EmployeeRecord> findAllByOrderByReceivedAtDesc();
}