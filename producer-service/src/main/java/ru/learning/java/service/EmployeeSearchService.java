package ru.learning.java.service;

import org.springframework.stereotype.Service;
import ru.learning.java.company.Department;
import ru.learning.java.model.employees.Employee;
import ru.learning.java.model.EmployeeDocument;
import ru.learning.java.repository.EmployeeSearchRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Сервис для работы с поиском сотрудников через Elasticsearch
 */
@Service
public class EmployeeSearchService {

    private final EmployeeSearchRepository searchRepository;

    public EmployeeSearchService(EmployeeSearchRepository searchRepository) {
        this.searchRepository = searchRepository;
    }

    /**
     * Индексирует сотрудника в Elasticsearch
     */
    public void indexEmployee(Employee employee) {
        EmployeeDocument document = convertToDocument(employee);
        searchRepository.save(document);
    }

    /**
     * Удаляет сотрудника из индекса
     */
    public void removeFromIndex(String employeeId) {
        searchRepository.deleteById(employeeId);
    }

    /**
     * Поиск по имени
     */
    public List<EmployeeDocument> searchByName(String name) {
        return searchRepository.findByNameContaining(name);
    }

    /**
     * Поиск по отделу
     */
    public List<EmployeeDocument> searchByDepartment(Department department) {
        return searchRepository.findByDepartment(department);
    }

    /**
     * Поиск по типу сотрудника
     */
    public List<EmployeeDocument> searchByType(String type) {
        return searchRepository.findByEmployeeType(type);
    }

    /**
     * Получить все документы
     */
    public List<EmployeeDocument> findAll() {
        return StreamSupport.stream(searchRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    /**
     * Переиндексировать всех сотрудников
     */
    public void reindexAll(List<Employee> employees) {
        searchRepository.deleteAll();
        List<EmployeeDocument> documents = employees.stream()
                .map(this::convertToDocument)
                .collect(Collectors.toList());
        searchRepository.saveAll(documents);
    }

    /**
     * Конвертация Employee в EmployeeDocument
     */
    private EmployeeDocument convertToDocument(Employee employee) {
      return EmployeeDocument.fromEntity(employee);
    }
}