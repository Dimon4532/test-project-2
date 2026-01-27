package ru.learning.java.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import ru.learning.java.company.Department;

import java.math.BigDecimal;

@Document(indexName = "employees")
public class EmployeeDocument {

    @Id
    private String id;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String name;

    @Field(type = FieldType.Double)
    private BigDecimal salary;

    @Field(type = FieldType.Keyword)
    private Department department;

    @Field(type = FieldType.Keyword)
    private String employeeType;

    // Конструкторы
    public EmployeeDocument() {}

    public EmployeeDocument(String id, String name, BigDecimal salary, Department department, String employeeType) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.department = department;
        this.employeeType = employeeType;
    }

    public static EmployeeDocument fromEntity(Employee employee) {
        String employeeType = employee.getClass().getSimpleName();

        return new EmployeeDocument(
                employee.getId(),
                employee.getName(),
                employee.getSalary(),
                employee.getDepartment(),
                employeeType
        );
    }

    // Геттеры и сеттеры
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(String employeeType) {
        this.employeeType = employeeType;
    }
}