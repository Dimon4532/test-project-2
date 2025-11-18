package ru.learning.java.employees;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class EmployeeFileManager implements AutoCloseable {
  private BufferedWriter writer;

  public EmployeeFileManager(String filename) throws IOException {
    writer = new BufferedWriter(new FileWriter(filename));
  }

  public void writeEmployee(Employee emp) throws IOException {
    writer.write(emp.getName() + "," + emp.getSalary());
    writer.newLine();
  }

  @Override
  public void close() throws IOException {
    if (writer != null) {
      writer.close();
      System.out.println("Файл успешно закрыт");
    }
  }
}
