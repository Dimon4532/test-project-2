package ru.learning.java.company;

import ru.learning.java.model.employees.Employee;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ReportGenerator {

  public void generateReports(int employeeCount) {
    long start;
    try (ExecutorService executor = Executors.newFixedThreadPool(4)) {

      System.out.println("Начинаем генерацию отчетов...");
      start = System.currentTimeMillis();

      for (int i = 0; i < employeeCount; i++) {
        final int empId = i;
        executor.submit(() -> {
          try {
            Thread.sleep(100);
            System.out.println("Отчет для сотрудника " + empId + " готов. Поток: " + Thread.currentThread().getName());
          } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
          }
        });
      }
      executorShutdown(executor);
    }

    long end = System.currentTimeMillis();
    System.out.println("Всего времени: " + (end - start) + " мс");
  }

  public void generateReportsForEmployees(List<Employee> employees) {
    long start;
    try (ExecutorService executor = Executors.newFixedThreadPool(4)) {

      System.out.println("Начинаем генерацию отчетов для сотрудников...");
      start = System.currentTimeMillis();

      for (Employee employee : employees) {
        executor.submit(() -> {
          try {
            Thread.sleep(100);
            System.out.println("Отчет для " + employee.getName() + " (ID: " + employee.getId() +
              ", " + employee.getDepartment().getFullName() + ") готов. Поток: " +
              Thread.currentThread().getName());
          } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
          }
        });
      }
      executorShutdown(executor);
    }

    long end = System.currentTimeMillis();
    System.out.println("Всего времени: " + (end - start) + " мс");
  }

  public void generateDepartmentReport(Department department, List<Employee> employees) {
    System.out.println("\n=== Отчет по департаменту: " + department.getFullName() + " ===");
    System.out.println("Всего сотрудников: " + employees.size());
    generateReportsForEmployees(employees);
  }

  private void executorShutdown(ExecutorService executor) {
    executor.shutdown();
    try {
      boolean terminated = executor.awaitTermination(1, TimeUnit.MINUTES);
      if (!terminated) {
        System.err.println("Предупреждение: не все задачи завершились за отведенное время");
        executor.shutdownNow();
      }
    } catch (InterruptedException e) {
      System.err.println("Ожидание завершения задач было прервано");
      executor.shutdownNow();
      Thread.currentThread().interrupt();
    }
  }
}